package co.wethinkcode.logisticsconnect.mq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import static javax.jms.Session.AUTO_ACKNOWLEDGE;

public class Producer {

    private static final Logger logger = LoggerFactory.getLogger(Producer.class);
    private static final String CLIENTID_PREFIX = "LogisticsConnect-Transit-Producer-";

    private final String topicName;
    private final Connection connection;
    private final Session session;
    private final Topic topic;
    private final MessageProducer producer;

    public Producer(String topicName) throws JMSException {
        this.topicName = topicName;
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(MqConfig.BROKER_URL);
        connection = connectionFactory.createConnection("admin", "admin");
        connection.setClientID(CLIENTID_PREFIX + System.currentTimeMillis());
        connection.start();
        session = connection.createSession(false, AUTO_ACKNOWLEDGE);
        topic = session.createTopic(topicName);
        producer = session.createProducer(topic);
    }

    public void send(String payload) throws JMSException {
        TextMessage message = session.createTextMessage(payload);
        producer.send(message);
    }

    public void close() throws JMSException {
        try {
            if (producer != null) {
                producer.close();
            }
            if (session != null) {
                session.close();
            }
            if (connection != null) {
                connection.close();
            }
        } finally {
            logger.info("Producer resources closed");
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

    public MessageProducer getProducer() {
        return producer;
    }
}
