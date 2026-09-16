package co.wethinkcode.logisticsconnect;

import co.wethinkcode.logisticsconnect.controller.IngestionController;
import co.wethinkcode.logisticsconnect.repository.IngestionRepository;
import co.wethinkcode.logisticsconnect.service.CsvCleaningService;
import co.wethinkcode.logisticsconnect.service.IngestionService;
import io.javalin.Javalin;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;

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
        ingestCsv(repository);
        IngestionService service = new IngestionService(repository);
        IngestionController controller = new IngestionController(service);

        registerRoutes(app, controller);

        return app;
    }

    private static void ingestCsv(IngestionRepository repository) {
        try {
            new CsvCleaningService().cleanAndDeduplicate().forEach(repository::save);
        } catch (IOException | CsvException e) {
            throw new IllegalStateException("Failed to ingest hubs-global.csv", e);
        }
    }

    private static void registerRoutes(Javalin app, IngestionController controller) {
        app.get("/health", ctx -> ctx.result("OK"));
        controller.registerRoutes(app);
    }
}