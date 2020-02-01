package micronaut.starter.kit;

import java.util.List;

public class DecisionRowBean {
    public List<String> row;
    public String nextDecision;

    private DecisionRowBean(List<String> row, String nextStep) {
        this.row = row;
        this.nextDecision = nextStep;
    }

    public static DecisionRowBean create(List<String> row, String nextStep){
        return new DecisionRowBean(row, nextStep);
    }
}
