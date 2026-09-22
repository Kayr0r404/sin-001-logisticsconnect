package co.wethinkcode.logisticsconnect.mq.mytopic;

import static javax.jms.Session.AUTO_ACKNOWLEDGE;
import static org.apache.activemq.ActiveMQConnection.DEFAULT_BROKER_URL;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Producer {

	private static final String CLIENTID = "LogisticsConnect";
	private String topicName;
	private ConnectionFactory connectionFactory;
	private Connection connection;
	private Session session;
	private Topic topic;
	private MessageProducer producer;

	public Producer(String topicName) throws Exception {
		super();
		// The name of the topic.
		this.topicName = topicName;
		// URL of the JMS server is required to create connection factory.
		// DEFAULT_BROKER_URL is : tcp://localhost:61616 and is indicates that JMS
		// server is running on localhost
		connectionFactory = new ActiveMQConnectionFactory(DEFAULT_BROKER_URL);
		// Getting JMS connection from the server and starting it
		connection = connectionFactory.createConnection("admin", "admin");
		connection.setClientID(CLIENTID);
		connection.start();
		// Creating a non-transactional session to send/receive JMS message.
		session = connection.createSession(false, AUTO_ACKNOWLEDGE);
		// Topic represents here our Topic ’BankAccountProcessingTopic’ on the JMS
		// server.
		// The Topic will be created automatically on the JSM server if its not already
		// created.
		topic = session.createTopic(this.topicName);
		// MessageProducer is used for sending (producing) messages to the Topic.
		producer = session.createProducer(topic);
	}

	public void sendToTopic(MessageProducer producer, String hub) throws Exception {
		ObjectMessage message = session.createObjectMessage(hub);
		// push the message into Topic
		producer.send(message);
	}

	public void close() throws JMSException {
		producer.close();
		producer = null;
		session.close();
		session = null;
		connection.close();
		connection = null;
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