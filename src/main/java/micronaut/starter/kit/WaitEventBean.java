package micronaut.starter.kit;

import org.dom4j.Element;
import org.dom4j.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WaitEventBean {
    public String name;
    public String label;
    public String eventName;
    public String eventLabel;
    public String conditionLogic;
    public List<String> conditions;
    public String connector;
    public String eventType;
    public HashMap<String,String> inputsMeta;

    public String toString() {
        List<String> buf = new ArrayList<>();

        buf.add(String.format("name: %s label: %s", this.name, this.label));
        buf.add(String.format("eventName: %s eventLabel: %s", this.eventName, this.eventLabel));
        buf.add(String.format("EventType: %s", this.eventType));
        buf.add(String.format("connector: %s", this.connector));
        buf.add(String.format("conditionLogic: %s", this.conditionLogic));
        buf.add(String.format("conditions: %s", String.join("\n", this.conditions)));

        buf.add("--inputParameters--");
        for(String key: this.inputsMeta.keySet()){
            buf.add(String.format("%s -> %s", key, this.inputsMeta.get(key)));
        }

        return String.join("\n", buf);
    }

    public WaitEventBean(Element el){
        this.conditions = new ArrayList<>();
        this.inputsMeta = new HashMap<>();
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
                    case "waitevents":
                        parseWaitEvents((Element) nd);
                        break;
                }
            });
    }

    void parseWaitEvents(Element el){
        NodeHelper.getElementNodes(el)
            .forEach(nd -> {
                final String tag = nd.getName().toLowerCase();
                switch(tag){
                    case "name":
                        this.eventName= nd.getStringValue();
                        break;
                    case "label":
                        this.eventLabel = nd.getStringValue();
                        break;
                    case "conditionlogic":
                        this.conditionLogic = nd.getStringValue().toUpperCase();
                        break;
                    case "conditions":
                        parseConditions((Element) nd);
                        break;
                    case "connector":
                        this.connector = NodeHelper.parseValue((Element) nd);
                        break;
                    case "eventtype":
                        this.eventType = nd.getStringValue();
                        break;
                    case "inputparameters":
                        parseInputMeta((Element) nd);
                        break;
                }
            });
    }
    void parseConditions(Element el){
        String left = "";
        String operator = "";
        String right = "";
        List<Node> nodes = NodeHelper.getElementNodes(el);
        for(Node nd: nodes){
            final String tag = nd.getName().toLowerCase();
            switch(tag){
                case "leftvaluereference":
                    left = nd.getStringValue();
                    break;
                case "operator":
                    operator = nd.getStringValue();
                    break;
                case "rightvalue":
                    right = NodeHelper.parseValue((Element) nd);
                    break;
            }
        }
        if (left.isEmpty()) return;
        if (operator.equalsIgnoreCase("EqualTo")) {
            operator ="=";
        }
        this.conditions.add(String.format("%s %s %s", left, operator, right));
    }

    void parseInputMeta(Element el){
        NodeHelper.getElementNodes(el)
            .forEach(nd -> {
                final List<String> nvp = NodeHelper.parseNameValueNodes(el);
                if(!nvp.isEmpty()) {
                    this.inputsMeta.put(nvp.get(0), nvp.get(1));
                }
            });
    }
}
