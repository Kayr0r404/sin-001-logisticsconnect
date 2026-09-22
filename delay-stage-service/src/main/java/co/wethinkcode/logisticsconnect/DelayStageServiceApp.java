package co.wethinkcode.logisticsconnect;

import java.io.IOException;


import co.wethinkcode.logisticsconnect.controller.DelayController;
import co.wethinkcode.logisticsconnect.repository.HubRepository;
import co.wethinkcode.logisticsconnect.service.DelayService;
import io.javalin.Javalin;

public class DelayStageServiceApp {

    private static final String TOPIC_NAME = "package-status-topic";

		public static void main(String[] args) throws IOException, InterruptedException, Exception {
			Javalin app = JavalinConfig.create();
			app.start(7052);
		}
	}

	class JavalinConfig {

		public static Javalin create() throws IOException, InterruptedException, Exception {
			Javalin app = Javalin.create(config -> {
				// configuration
			});

			HubRepository repository = new HubRepository();
			DelayService service = new DelayService(repository);
			DelayController controller = new DelayController(service);

			registerRoutes(app, controller);

			return app;
		}

		private static void registerRoutes(Javalin app, DelayController controller) throws Exception {
			controller.registerRoutes(app);
		}
}

// MQ TODO: publishes to ActiveMQ topic MqConfig.TOPIC at MqConfig.BROKER_URL (see co.wethinkcode.logisticsconnect.mq.MqConfig)
