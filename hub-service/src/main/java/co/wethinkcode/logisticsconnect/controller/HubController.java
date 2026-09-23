package co.wethinkcode.logisticsconnect.controller;

import co.wethinkcode.logisticsconnect.service.HubService;
import io.javalin.Javalin;

public class HubController {

    private final HubService service;

    public HubController(HubService service) {
        this.service = service;
    }

    public void registerRoutes(Javalin app) {
        app.get("/hubs/province/{province}", ctx -> ctx.json(
                service.getByProvince(
                        ctx.pathParam("province")
                )
        ));

        app.get("/hubs/{hubId}", ctx -> ctx.json(
                service.getByHubId(
                        ctx.pathParam("hubId")
                )
        ));

        app.get("/sorting-center/{sortingCenter}",ctx-> ctx.json(
                service.getBySortingCenter(
                        ctx.pathParam("sortingCenter")
                )
        ));

        app.get("/hub/active", ctx -> ctx.json(
                service.getAllActive()
        ));

        app.get("/hubs", ctx -> ctx.json(service.getAll()));

        app.get("/health", ctx -> ctx.result("OK"));
    }

}