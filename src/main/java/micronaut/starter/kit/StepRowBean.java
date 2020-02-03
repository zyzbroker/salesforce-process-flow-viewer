package micronaut.starter.kit;

import java.util.List;

public class StepRowBean {
    public List<String> row;
    public String description;
    public String nextDecision;

    private StepRowBean(List<String> row, String description, String nextStep) {
        this.row = row;
        this.description = description;
        this.nextDecision = nextStep;
    }

    public static StepRowBean create(List<String> row, String description, String nextStep){
        return new StepRowBean(row, description, nextStep);
    }
}
