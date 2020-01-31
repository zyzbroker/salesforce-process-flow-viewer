package micronaut.starter.kit;

import org.dom4j.Node;

public class InputAssignmentsBean {
    public String dataType;
    public boolean isRequired;
    public String leftHandLabel;
    public String rightHandType;
    public String field;
    public String value;
    public boolean isFormula;

    public InputAssignmentsBean(Node el) {

    }
}
