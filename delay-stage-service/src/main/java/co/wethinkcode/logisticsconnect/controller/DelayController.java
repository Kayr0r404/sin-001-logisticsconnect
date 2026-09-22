package co.wethinkcode.logisticsconnect.controller;

import co.wethinkcode.logisticsconnect.mq.mytopic.Producer;
import io.javalin.Javalin;

import co.wethinkcode.logisticsconnect.service.DelayService;
import co.wethinkcode.logisticsconnect.model.dto.*;

public class DelayController {

    private final DelayService service;

    public DelayController(DelayService service) {this.service=service;}

    public void registerRoutes(Javalin app) throws Exception {
        // Initialize the JMS infrastructure ONCE during application startup
        Producer delayTopic = new Producer("package-status-topic");

        app.get("delay-stage/{hubId}", ctx -> {
            try {
                String jsonResponse = service.getByHubId(ctx.pathParam("hubId"));
                delayTopic.sendToTopic(delayTopic.getProducer(), jsonResponse);
                ctx.status(200).result("Message dispatched to delay stage");
            } catch (Exception e) {
                // Catch JMS exceptions so the client doesn't get an empty response on failure
                System.err.println("Failed to publish to ActiveMQ: " + e.getMessage());
                ctx.status(500).result("Internal Server Error: Message dispatch failed");
            }
        });

        app.post("/delay-stage/{hubId}", ctx -> {
            String hubId = ctx.pathParam("hubId");

            // Parse the incoming request body
            HubRequest request = ctx.bodyAsClass(HubRequest.class);

            // Validate the stage range (0–8)
            if (request.getStage() < 0 || request.getStage() > 8) {
                ctx.status(400).result("Invalid delay stage. Must be between 0 and 8.");
                return;
            }

            // TODO: Update state in your in-memory map or service
            // delayStageService.updateStage(hubId, request.getStage());

            // TODO Stage 3: Publish event to ActiveMQ 'package-status-topic'

            // Return the updated delay stage object
            ctx.status(200).json(new HubResponse(hubId, request.getStage()));
        });
    }
}
