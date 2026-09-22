package co.wethinkcode.logisticsconnect.producer;


import org.apache.qpid.jms.JmsConnectionFactory;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

public class Producer {

    public static void main(String [] main) {
        JMSConnectionFactory factory = new JMSConnectionFactory("amqp://localhost:5672");
        Connection connecton = factory.createConnection("admin", "password");
        connection.start();
    }
}