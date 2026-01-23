package integration.context;

import io.cucumber.spring.ScenarioScope;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Contexto compartilhado entre steps do Cucumber.
 * Usa @ScenarioScope para garantir isolamento entre cenários.
 */
@ScenarioScope
public class TestContext {

    private ResponseEntity<?> lastResponse;
    private Object lastCreatedEntity;
    private final Map<String, Object> contextData = new HashMap<>();

    public ResponseEntity<?> getLastResponse() {
        return lastResponse;
    }

    public void setLastResponse(ResponseEntity<?> lastResponse) {
        this.lastResponse = lastResponse;
    }

    public Object getLastCreatedEntity() {
        return lastCreatedEntity;
    }

    public void setLastCreatedEntity(Object lastCreatedEntity) {
        this.lastCreatedEntity = lastCreatedEntity;
    }

    public void put(String key, Object value) {
        contextData.put(key, value);
    }

    public Object get(String key) {
        return contextData.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        return (T) contextData.get(key);
    }

    public void clear() {
        lastResponse = null;
        lastCreatedEntity = null;
        contextData.clear();
    }
}

