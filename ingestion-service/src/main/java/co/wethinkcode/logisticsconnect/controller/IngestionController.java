package co.wethinkcode.logisticsconnect.controller;

import co.wethinkcode.logisticsconnect.service.IngestionService;
import io.javalin.Javalin;

public class IngestionController {

    private final IngestionService service;

    public IngestionController(IngestionService service) {
        this.service = service;
    }

    public void registerRoutes(Javalin app) {
        app.get("/hubs", ctx -> ctx.json(service.getAll()));
    }

}