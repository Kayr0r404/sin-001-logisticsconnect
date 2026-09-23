package co.wethinkcode.logisticsconnect.mq.mytopic;

import static org.apache.activemq.ActiveMQConnection.DEFAULT_BROKER_URL;
import static javax.jms.Session.AUTO_ACKNOWLEDGE;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Producer {

	private static final String CLIENTID_PREFIX = "LogisticsConnect-Producer-";
	private String topicName;
	private ConnectionFactory connectionFactory;
	private Connection connection;
	private Session session;
	private Topic topic;
	private MessageProducer producer;

	public Producer(String topicName) throws Exception {
		super();
		this.topicName = topicName;
		connectionFactory = new ActiveMQConnectionFactory(DEFAULT_BROKER_URL);
		connection = connectionFactory.createConnection("admin", "admin");
		connection.setClientID(CLIENTID_PREFIX + System.currentTimeMillis());
		connection.start();
		session = connection.createSession(false, AUTO_ACKNOWLEDGE);
		topic = session.createTopic(this.topicName);
		producer = session.createProducer(topic);
	}

	public void sendToTopic(MessageProducer producer, String hub) throws Exception {
		TextMessage message = session.createTextMessage(hub);
		producer.send(message);
	}

	public void close() throws JMSException {
		try {
			if (producer != null) { producer.close(); producer = null; }
			if (session != null) { session.close(); session = null; }
			if (connection != null) { connection.close(); connection = null; }
		} finally {
			System.out.println("Producer resources closed");
		}
	}

	public String getTopicName() { return topicName; }
	public Connection getConnection() { return connection; }
	public Session getSession() { return session; }
	public Topic getTopic() { return topic; }
	public MessageProducer getProducer() { return producer; }
}
