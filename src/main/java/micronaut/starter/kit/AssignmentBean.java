package micronaut.starter.kit;

import org.dom4j.Element;
import org.dom4j.Node;

public class AssignmentBean {
    public String name;
    public String label;

    public String toReference;
    public String operator;
    public String value;

    public String toString() {
        return String.format("name:%s label:%s toReference:%s operator:%s value:%s",
                this.name, this.label, this.toReference, this.operator, this.value);
    }

    public AssignmentBean(Element el){
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
                        case "assignmentitems":
                            parseAssignItems((Element) nd);
                            break;
                    }
                });
    }

    void parseAssignItems(Element el){
        NodeHelper.getElementNodes(el)
                .forEach(n -> {
                    final String tag = n.getName().toLowerCase();
                    switch(tag){
                        case "assigntoreference":
                            this.toReference = n.getStringValue();
                            break;
                        case "operator":
                            this.operator = n.getStringValue();
                            break;
                        case "value":
                            this.value = NodeHelper.parseValue((Element) n);
                            break;
                    }
                });
    }
}
