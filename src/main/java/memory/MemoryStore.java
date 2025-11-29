package memory;

import com.google.gson.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class MemoryStore {
    private final Path path;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private Map<String, Object> data;

    public MemoryStore(String filePath) {
        this.path = Paths.get(filePath);
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                this.data = defaultData();
                write();
            } else {
                read();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> defaultData() {
        Map<String, Object> m = new HashMap<>();
        m.put("preferences", Map.of("diet","vegetarian", "allergies", List.of()));
        m.put("medical", Map.of("medications", List.of()));
        m.put("history", new ArrayList<>());
        return m;
    }

    private void read() throws IOException {
        try (Reader r = Files.newBufferedReader(path)) {
            this.data = gson.fromJson(r, Map.class);
            if (this.data == null) this.data = defaultData();
        }
    }

    private void write() throws IOException {
        try (Writer w = Files.newBufferedWriter(path)) {
            gson.toJson(this.data, w);
        }
    }

    @SuppressWarnings("unchecked")
    public synchronized <T> T get(String key, Class<T> clazz) {
        Object v = data.get(key);
        return v == null ? null : gson.fromJson(gson.toJson(v), clazz);
    }

    public synchronized void set(String key, Object value) {
        data.put(key, value);
        try { write(); } catch (IOException e) { throw new RuntimeException(e); }
    }

    public synchronized void addHistory(Object item) {
        List<Object> history = (List<Object>) data.get("history");
        history.add(item);
        data.put("history", history);
        try { write(); } catch (IOException e) { throw new RuntimeException(e); }
    }
}
