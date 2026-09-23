package co.wethinkcode.logisticsconnect.controller;

import co.wethinkcode.logisticsconnect.service.HubService;
import io.javalin.Javalin;

public class HubController {

    private final HubService service;

    public HubController(HubService service) {
        this.service = service;
    }

    public void registerRoutes(Javalin app) {
        app.get("/health", ctx -> ctx.result("OK"));
    }

}