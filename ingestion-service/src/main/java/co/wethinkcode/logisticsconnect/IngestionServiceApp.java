package co.wethinkcode.logisticsconnect;

import co.wethinkcode.logisticsconnect.controller.IngestionController;
import co.wethinkcode.logisticsconnect.repository.IngestionRepository;
import co.wethinkcode.logisticsconnect.service.IngestionService;
import io.javalin.Javalin;

public class IngestionServiceApp {

    public static void main(String[] args) {
        Javalin app = JavalinConfig.create();

        app.start(7050);
    }
}

class JavalinConfig {

    public static Javalin create() {
        Javalin app = Javalin.create(config -> {
            // configuration
        });

        IngestionRepository repository = new IngestionRepository();
        IngestionService service = new IngestionService(repository);
        IngestionController controller = new IngestionController(service);

        registerRoutes(app, controller);

        return app;
    }

    private static void registerRoutes(Javalin app, IngestionController controller) {
        app.get("/health", ctx -> ctx.result("OK"));
        controller.registerRoutes(app);
    }
}