import com.google.gson.*;

import memory.MemoryStore;
import sahre._java.agents.MonitorAgent;
import sahre._java.agents.PlannerAgent;
import sahre._java.agents.RecommenderAgent;
import session.SessionService;
import utils.LoggerUtil;

import java.nio.file.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Starting Sahre demo...");

        String memoryFile = "data/memory.json";
        MemoryStore memory = new MemoryStore(memoryFile);
        SessionService session = new SessionService();

        // Load sample user from resources
        String examplePath = "src/main/resources/examples/user_shalini.json";
        String userJson = Files.readString(Path.of(examplePath));
        JsonObject userProfile = JsonParser.parseString(userJson).getAsJsonObject();

        LoggerUtil.info("Main", "User profile loaded: " + userProfile.get("name").getAsString());

        PlannerAgent planner = new PlannerAgent(memory, session);
        RecommenderAgent recommender = new RecommenderAgent(memory, session);
        MonitorAgent monitor = new MonitorAgent(memory, session);

        // Run planner and recommender in parallel
        ExecutorService ex = Executors.newFixedThreadPool(2);
        Future<Object> planFuture = ex.submit(() -> {
            LoggerUtil.info("PlannerAgent", "Starting plan generation");
            Object plan = planner.run(Map.of("profile", userProfile));
            LoggerUtil.info("PlannerAgent", "Plan generated");
            return plan;
        });

        Future<Object> recFuture = ex.submit(() -> {
            // Wait small bit to let planner set plan in session (in real system you might orchestrate better)
            Thread.sleep(200);
            LoggerUtil.info("RecommenderAgent", "Starting recommendations");
            Object rec = recommender.run(null);
            LoggerUtil.info("RecommenderAgent", "Recommendations ready");
            return rec;
        });

        Object plan = planFuture.get();
        Object rec = recFuture.get();

        LoggerUtil.info("Main", "Plan => " + new GsonBuilder().setPrettyPrinting().create().toJson(plan));
        LoggerUtil.info("Main", "Recommendations => " + new GsonBuilder().setPrettyPrinting().create().toJson(rec));

        // Start monitor simulation for 3 days (no real time wait; iterations)
        LoggerUtil.info("MonitorAgent", "Starting monitor simulation (3 days)");
        Object monitorResult = monitor.run(3);
        LoggerUtil.info("MonitorAgent", "Monitor actions => " + monitorResult);

        LoggerUtil.info("Main", "Calendar events => " + monitor.getCalendarEvents());
        ex.shutdown();
        System.out.println("Demo finished.");
    }
}
