package co.wethinkcode.logisticsconnect.mq;

import co.wethinkcode.logisticsconnect.mapper.HubMapper;
import co.wethinkcode.logisticsconnect.model.dto.HubResponse;
import co.wethinkcode.logisticsconnect.service.HubService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.IOException;
import java.util.Optional;

import static javax.jms.Session.AUTO_ACKNOWLEDGE;

public class Consumer implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    private final HubService hubService;
    private final Producer producer;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String topicName;
    private Connection connection;
    private Session session;
    private Topic topic;
    private MessageConsumer consumer;

    public Consumer(String topicName, HubService hubService, Producer producer) throws JMSException {
        this.topicName = topicName;
        this.hubService = hubService;
        this.producer = producer;

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(MqConfig.BROKER_URL);
        connection = connectionFactory.createConnection("admin", "admin");
        connection.setClientID("LogisticsConnect-Transit-Consumer-" + System.currentTimeMillis());
        connection.start();
        session = connection.createSession(false, AUTO_ACKNOWLEDGE);
        topic = session.createTopic(topicName);
        consumer = session.createConsumer(topic);
        consumer.setMessageListener(this);
    }

    @Override
    public void onMessage(Message message) {
        try {
            processMessage(message);
        } catch (Exception e) {
            logger.error("Failed to process JMS message", e);
        }
    }

    private void processMessage(Message message) throws JMSException, IOException {
        String payload = extractPayload(message);
        if (payload == null || payload.isBlank()) {
            logger.warn("Received empty message");
            return;
        }

        JsonNode node = objectMapper.readTree(payload);

        // Only delay-stage updates carry a "stage"; ETA messages we (or another
        // producer) publish back to the same topic are ignored so we don't react
        // to our own calculations.
        if (!node.isObject() || !node.has("hubId") || !node.has("stage") || node.has("estimatedTimeArrival")) {
            logger.debug("Ignoring non stage-update message: {}", payload);
            return;
        }

        String hubId = node.get("hubId").asText();
        int stage = node.get("stage").asInt();

        hubService.updateStage(hubId, stage);

        Optional<HubResponse> eta = hubService.calculateEta(hubId, stage);
        if (eta.isEmpty()) {
            logger.warn("Hub {} not found in repository", hubId);
            return;
        }

        String etaJson = HubMapper.jsonStringResponse(eta.get());
        producer.send(etaJson);
        logger.info("Published ETA for hub {}: {}", hubId, etaJson);
    }

    private String extractPayload(Message message) throws JMSException {
        if (message instanceof TextMessage textMessage) {
            return textMessage.getText();
        }
        if (message instanceof ObjectMessage objectMessage) {
            Object obj = objectMessage.getObject();
            return obj != null ? obj.toString() : null;
        }
        logger.warn("Received unsupported message type: {}", message.getClass().getName());
        return null;
    }

    public void close() throws JMSException {
        try {
            if (consumer != null) {
                consumer.close();
                consumer = null;
            }
            if (session != null) {
                session.close();
                session = null;
            }
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } finally {
            logger.info("Consumer resources closed");
        }
    }

    public String getTopicName() {
        return topicName;
    }

    public Connection getConnection() {
        return connection;
    }

    public Session getSession() {
        return session;
    }

    public Topic getTopic() {
        return topic;
    }

    public MessageConsumer getConsumer() {
        return consumer;
    }
}
