package adapters;

import java.util.*;

public class LLMAdapter {
    // Mocked LLM responses. Replace with real LLM SDK calls later.
    public String generate(String systemPrompt, String userPrompt) {
        String prompt = (systemPrompt == null ? "" : systemPrompt + " ") + userPrompt;
        prompt = prompt.toLowerCase();

        if (prompt.contains("make weekly plan") || prompt.contains("generate weekly plan") || prompt.contains("plan for the week")) {
            // return a compact structured string (simple)
            return "{ \"monday\": {\"breakfast\":\"oats\",\"lunch\":\"dal-rice\",\"dinner\":\"veg-pulao\"},"
                 + "\"tuesday\": {\"breakfast\":\"idli\",\"lunch\":\"sambar-rice\",\"dinner\":\"mix-veg\"} }";
        } else if (prompt.contains("suggest recipes") || prompt.contains("recipes for")) {
            return "[{\"name\":\"Oats porridge\",\"ingredients\":[\"oats\",\"milk\",\"banana\"]},"
                 + "{\"name\":\"Vegetable pulao\",\"ingredients\":[\"rice\",\"mixed-veg\",\"spices\"]}]";
        } else if (prompt.contains("why change") || prompt.contains("adapt plan")) {
            return "User missed morning exercise; rescheduling exercise to evening and swapping heavy lunch to lighter salad.";
        }
        return "OK";
    }
}
