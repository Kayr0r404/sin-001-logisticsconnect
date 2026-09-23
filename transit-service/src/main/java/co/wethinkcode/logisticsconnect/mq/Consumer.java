package co.wethinkcode.logisticsconnect.mq;

import co.wethinkcode.logisticsconnect.model.entity.Hub;
import co.wethinkcode.logisticsconnect.repository.HubRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.IOException;

import static javax.jms.Session.AUTO_ACKNOWLEDGE;

public class Consumer implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);
    private static final String CLIENTID_PREFIX = "LogisticsConnect-Consumer-";

    private final HubRepository hubRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String topicName;
    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private Topic topic;
    private MessageConsumer consumer;

    public Consumer(String topicName, HubRepository hubRepository) throws JMSException {
        this.topicName = topicName;
        this.hubRepository = hubRepository;
        connectionFactory = new ActiveMQConnectionFactory(MqConfig.BROKER_URL);
        connection = connectionFactory.createConnection("admin", "admin");
        connection.setClientID(CLIENTID_PREFIX + System.currentTimeMillis());
        connection.start();
        session = connection.createSession(false, AUTO_ACKNOWLEDGE);
        topic = session.createTopic(this.topicName);
        consumer = session.createConsumer(topic);
        consumer.setMessageListener(this);
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

        HubUpdate update = objectMapper.readValue(payload, HubUpdate.class);
        String hubId = update.hubId();
        int stage = update.stage();

        Hub hub = hubRepository.getByHubId(hubId);
        if (hub != null) {
            hub.setStage(stage);
            logger.info("Updated hub {} to stage {}", hubId, stage);
        } else {
            logger.warn("Hub {} not found in repository", hubId);
        }
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

    private record HubUpdate(String hubId, int stage) {}
}
