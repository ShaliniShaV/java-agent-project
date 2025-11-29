package session;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SessionService {
    private Map<String, Object> currentPlan;
    private final Queue<Map<String,Object>> todayTasks = new ConcurrentLinkedQueue<>();

    public void setCurrentPlan(Map<String,Object> plan) {
        this.currentPlan = plan;
        // convert plan to simple tasks for demo
        generateTasksFromPlan(plan);
    }

    public Map<String,Object> getCurrentPlan() {
        return currentPlan;
    }

    private void generateTasksFromPlan(Map plan) {
        todayTasks.clear();
        // take first day in plan map for demo
        if (plan == null || plan.isEmpty()) return;
        Object firstDay = plan.values().stream().findFirst().orElse(null);
        if (firstDay instanceof Map) {
            Map day = (Map) firstDay;
            // create simple tasks: breakfast, exercise, medication if any
            if (day.get("breakfast") != null) todayTasks.add(Map.of("task","breakfast: "+day.get("breakfast")));
            if (day.get("exercise") != null) todayTasks.add(Map.of("task","exercise: "+day.get("exercise")));
            if (day.get("dinner") != null) todayTasks.add(Map.of("task","dinner: "+day.get("dinner")));
        }
    }

    public Map<String,Object> popNextTask() {
        return todayTasks.poll();
    }
}
