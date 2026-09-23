package co.wethinkcode.logisticsconnect;

import co.wethinkcode.logisticsconnect.controller.HubController;
import co.wethinkcode.logisticsconnect.mq.Consumer;
import co.wethinkcode.logisticsconnect.mq.MqConfig;
import co.wethinkcode.logisticsconnect.repository.HubRepository;
import co.wethinkcode.logisticsconnect.service.HubService;
import io.javalin.Javalin;

import java.io.IOException;
import javax.jms.JMSException;

public class TransitServiceApp {

    public static void main(String[] args) throws IOException, InterruptedException, JMSException {
        HubRepository repository = new HubRepository();
        HubService service = new HubService(repository);
        HubController controller = new HubController(service);

        Consumer consumer = new Consumer(MqConfig.TOPIC, repository);

        Javalin app = Javalin.create(config -> {
        });
        app.start(7053);

        controller.registerRoutes(app);
    }
}
