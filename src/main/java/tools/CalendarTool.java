package tools;

import java.util.*;

public class CalendarTool {
    // Simulated calendar stored in-memory for demo
    private final List<String> events = new ArrayList<>();

    public void addEvent(String event) {
        events.add(event);
    }

    public List<String> listEvents() {
        return List.copyOf(events);
    }
}
