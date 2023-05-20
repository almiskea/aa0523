package rentaltools;

import java.util.HashMap;
import java.util.Map;

public class ToolRepository {
    private Map<String, Tool> toolMap;

    public ToolRepository() {
        toolMap = new HashMap<>();
        populateToolMap();
    }

    private void populateToolMap() {
        toolMap.put("CHNS", new Tool("CHNS", "Chainsaw", "Stihl", 1.49, true, false, true));
        toolMap.put("LADW", new Tool("LADW", "Ladder", "Werner", 1.99, true, true, false));
        toolMap.put("JAKD", new Tool("JAKD", "Jackhammer", "DeWalt", 2.99, true, false, false));
        toolMap.put("JAKR", new Tool("JAKR", "Jackhammer", "Ridgid", 2.99, true, false, false));
    }

    public Tool findByToolCode(String toolCode) {
        Tool tool = toolMap.get(toolCode);
        if (tool == null) {
            throw new IllegalArgumentException("Invalid tool code: " + toolCode);
        }
        return tool;
    }
}
