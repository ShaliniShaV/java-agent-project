package tools;

import java.util.*;

public class LocalStoreTool {
    // Mock local store DB: store -> item -> price (INR)
    private static final Map<String, Map<String, Integer>> DB = Map.of(
        "LocalMart", Map.of("oats", 120, "rice", 50, "milk", 60, "banana", 40, "mixed-veg", 80),
        "CornerBazaar", Map.of("oats", 110, "rice", 55, "milk", 62, "banana", 38, "mixed-veg", 85)
    );

    public Map<String, Map<String, Integer>> searchItems(List<String> items) {
        Map<String, Map<String, Integer>> res = new HashMap<>();
        for (var store : DB.entrySet()) {
            Map<String, Integer> prices = new HashMap<>();
            for (String item : items) {
                prices.put(item, store.getValue().getOrDefault(item, -1)); // -1 means not available
            }
            res.put(store.getKey(), prices);
        }
        return res;
    }

    // Choose cheapest store per item
    public Map<String, String> cheapestPerItem(List<String> items) {
        Map<String, String> cheapest = new HashMap<>();
        for (String item : items) {
            String bestStore = null;
            int bestPrice = Integer.MAX_VALUE;
            for (var store : DB.entrySet()) {
                int p = store.getValue().getOrDefault(item, -1);
                if (p > 0 && p < bestPrice) {
                    bestPrice = p;
                    bestStore = store.getKey();
                }
            }
            cheapest.put(item, bestStore == null ? "NotAvailable" : bestStore);
        }
        return cheapest;
    }
}
