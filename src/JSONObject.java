import java.util.HashMap;
import java.util.Map;

class JSONObject {
    private Map<String, Object> map;

    public JSONObject() {
        map = new HashMap<>();
    }

    public void put(String key, Object value) {
        map.put(key, value);
    }

    public Object get(String key) {
        return map.get(key);
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
