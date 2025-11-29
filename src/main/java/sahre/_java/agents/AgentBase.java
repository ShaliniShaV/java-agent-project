package sahre._java.agents;

import memory.MemoryStore;
import session.SessionService;

public abstract class AgentBase {
    protected final MemoryStore memory;
    protected final SessionService session;

    public AgentBase(MemoryStore memory, SessionService session) {
        this.memory = memory;
        this.session = session;
    }

    // Setup if needed
    public void setup() {}

    // run the agent job and return a result object (string/json)
    public abstract Object run(Object input);
}
