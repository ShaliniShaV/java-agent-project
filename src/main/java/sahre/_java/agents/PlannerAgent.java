package sahre._java.agents;


import com.google.gson.*;

import adapters.LLMAdapter;
import memory.MemoryStore;
import session.SessionService;

import java.util.Map;

public class PlannerAgent extends AgentBase {
    private final LLMAdapter llm = new LLMAdapter();
    private final Gson gson = new Gson();

    public PlannerAgent(MemoryStore memory, SessionService session) {
        super(memory, session);
    }

    @Override
    public Object run(Object input) {
        // input expected: Map with user profile & availability
        String system = "You are a helpful planner agent.";
        String userPrompt = "Please generate weekly plan for the user based on profile: " + gson.toJson(input);
        String llmResp = llm.generate(system, "Generate weekly plan: " + userPrompt);

        // parse mock structured response
        try {
            JsonElement je = JsonParser.parseString(llmResp);
            Map plan = gson.fromJson(je, Map.class);
            session.setCurrentPlan(plan);
            memory.addHistory(Map.of("event", "plan_generated", "plan", plan));
            return plan;
        } catch (Exception e) {
            return Map.of("error", "could not parse plan", "raw", llmResp);
        }
    }
}
