package sahre._java.agents;



import java.util.*;

import memory.MemoryStore;
import session.SessionService;
import tools.CalendarTool;

public class MonitorAgent extends AgentBase {
    private final CalendarTool calendar = new CalendarTool();

    public MonitorAgent(MemoryStore memory, SessionService session) {
        super(memory, session);
    }

    // simulate a daily monitoring loop with iterations (no real-time waits)
    @Override
    public Object run(Object input) {
        int days = (input instanceof Integer) ? (Integer) input : 3;
        List<String> actions = new ArrayList<>();
        for (int day = 1; day <= days; day++) {
            Map<String, Object> today = session.popNextTask(); // simulated
            if (today == null) {
                actions.add("Day " + day + ": No tasks (idle).");
                continue;
            }
            // Simulate random "miss" event to force adaptation
            boolean missed = new Random().nextDouble() < 0.3;
            if (missed) {
                String adapt = "Day " + day + ": Missed " + today.get("task") + ". Adapting plan.";
                actions.add(adapt);
                memory.addHistory(Map.of("event","missed_task","task",today,"day",day));
                // Simple adaptation: move to later (just a log here)
                calendar.addEvent("Rescheduled " + today.get("task") + " to evening of day " + day);
            } else {
                String done = "Day " + day + ": Completed " + today.get("task") + ".";
                actions.add(done);
                memory.addHistory(Map.of("event","completed_task","task",today,"day",day));
            }
        }
        return actions;
    }

    public List<String> getCalendarEvents() {
        return calendar.listEvents();
    }
}
