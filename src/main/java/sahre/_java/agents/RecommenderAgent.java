package sahre._java.agents;

import com.google.gson.*;

import adapters.LLMAdapter;
import memory.MemoryStore;
import session.SessionService;
import tools.LocalStoreTool;

import java.util.*;
import java.util.stream.Collectors;

public class RecommenderAgent extends AgentBase {
    private final LLMAdapter llm = new LLMAdapter();
    private final LocalStoreTool storeTool = new LocalStoreTool();
    private final Gson gson = new Gson();

    public RecommenderAgent(MemoryStore memory, SessionService session) {
        super(memory, session);
    }

    @Override
    public Object run(Object input) {
        // Input: current plan (Map)
        Object plan = session.getCurrentPlan();
        if (plan == null) return Map.of("error", "no plan available");

        // Ask LLM for recipe suggestions
        String userPrompt = "Suggest recipes for plan: " + gson.toJson(plan);
        String llmResp = llm.generate("Recommender", "suggest recipes " + userPrompt);

        List<Map<String, Object>> recipes;
        try {
            recipes = gson.fromJson(llmResp, List.class);
        } catch (Exception e) {
            recipes = Collections.emptyList();
        }

        // Build grocery list from recipes
        List<String> grocery = new ArrayList<>();
        for (Map<String, Object> r : recipes) {
            List<String> ingr = (List<String>) r.getOrDefault("ingredients", List.of());
            grocery.addAll(ingr);
        }

        grocery = grocery.stream().distinct().collect(Collectors.toList());
        var storeResults = storeTool.searchItems(grocery);
        var cheapest = storeTool.cheapestPerItem(grocery);

        Map<String, Object> out = new HashMap<>();
        out.put("recipes", recipes);
        out.put("grocery", grocery);
        out.put("prices", storeResults);
        out.put("cheapest", cheapest);
        memory.addHistory(Map.of("event", "recommendations", "grocery", grocery));
        return out;
    }
}
