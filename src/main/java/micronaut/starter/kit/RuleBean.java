package micronaut.starter.kit;

import org.dom4j.Element;
import org.dom4j.Node;

import java.util.ArrayList;
import java.util.List;

public class RuleBean {
    public String name;
    public String label;
    public String conditionLogic;
    public String nextDecision;
    public List<String> conditions;

    public String toString() {
        List<String> buf = new ArrayList<>();
        buf.add(String.format("name:%s label:%s",this.name, this.label));
        buf.add(String.format("nextDecision:%s",this.nextDecision));
        buf.add(String.format("conditionLogic:%s",this.conditionLogic));
        this.conditions.forEach(c -> {
            buf.add(String.format("conditions:%s",c));
        });
        return String.join("\n", buf);
    }

    public RuleBean(Element el){
        this.conditions = new ArrayList<>();
        NodeHelper.getElementNodes(el)
            .forEach(nd -> {
                final String tag = nd.getName().toLowerCase();
                switch(tag){
                    case "name":
                        this.name = nd.getStringValue();
                        break;
                    case "label":
                        this.label = nd.getStringValue();
                        break;
                    case "conditionlogic":
                        this.conditionLogic = nd.getStringValue();
                        break;
                    case "conditions":
                        parseConditions((Element) nd);
                        break;
                    case "connector":
                        this.nextDecision = NodeHelper.parseValue((Element) nd);
                        break;
                }
            });
    }

    void parseConditions(Element el){
        String left = "";
        String operation = "";
        String right = "";

        List<Node> nodes = NodeHelper.getElementNodes(el);
        for(Node n: nodes) {
            final String tag = n.getName().toLowerCase();
            switch (tag) {
                case "leftvaluereference":
                    left = n.getStringValue();
                    break;
                case "operator":
                    operation = n.getStringValue();
                    break;
                case "rightvalue":
                    right = NodeHelper.parseValue((Element) n);
                    break;
            }
        }
        if (left.isEmpty()) return;
        this.conditions.add(String.format("%s:%s:%s", left, operation, right));
    }
}
