package micronaut.starter.kit;

import org.dom4j.Element;
import org.dom4j.Node;

import java.util.ArrayList;
import java.util.List;

public class DecisionBean {
    private List<String> conditions;

    public String name;
    public String label;
    public String defaultDecisionName;
    public String nextDecisionName;
    public RuleBean rules;

    public String toString() {
        List<String> buf = new ArrayList<>();
        buf.add(String.format("name:%s label:%s",this.name, this.label));
        buf.add(String.format("defaultDecision:%s",
                this.defaultDecisionName));
        buf.add(String.format("nextDecision:%s",this.nextDecisionName));
        buf.add(String.format("[rule]:%s",this.rules.toString()));
        return String.join("\n", buf);
    }

    public DecisionBean(Element decision) {
        this.conditions = new ArrayList<String>();
        for(int i =0; i < decision.nodeCount(); i++) {
            Node n = decision.node(i);
            if (n instanceof Element) {
                switch (n.getName()) {
                    case "name":
                        this.name = n.getStringValue();
                        break;
                    case "label":
                        this.label = n.getStringValue();
                        break;
                    case "defaultConnector":
                        this.defaultDecisionName = NodeHelper.parseValue((Element) n);
                        break;
                    case "rules":
                        parseRule((Element) n);
                        break;
                }
            }
        }
    }

    private void parseRule(Element rules){
        this.rules = new RuleBean(rules);
    }
}
