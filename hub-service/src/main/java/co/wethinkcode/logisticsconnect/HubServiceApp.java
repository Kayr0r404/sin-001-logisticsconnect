package co.wethinkcode.logisticsconnect;

import co.wethinkcode.logisticsconnect.controller.HubController;
import co.wethinkcode.logisticsconnect.repository.HubRepository;
import co.wethinkcode.logisticsconnect.service.HubService;
import io.javalin.Javalin;

import java.io.IOException;

public class HubServiceApp {

    public static void main(String[] args) throws IOException, InterruptedException {
        Javalin app = JavalinConfig.create();
        app.start(7051);
    }
}

class JavalinConfig {

    public static Javalin create() throws IOException, InterruptedException {
        Javalin app = Javalin.create(config -> {
            // configuration
        });

        HubRepository repository = new HubRepository();
        HubService service = new HubService(repository);
        HubController controller = new HubController(service);

        registerRoutes(app, controller);

        return app;
    }

    private static void registerRoutes(Javalin app, HubController controller) {
        controller.registerRoutes(app);
    }
}
