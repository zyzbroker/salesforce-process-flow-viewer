package micronaut.starter.kit;

import org.dom4j.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActionCallBean {
    public String name;
    public String label;
    public String processName;
    public String processValue;
    public String connector;
    public String actionName;
    public String actionType;
    public HashMap<String, String> arguments;

    public ActionCallBean(Element el) {
        this.arguments = new HashMap<>();
        parse(el);
    }

    public String toString() {
        List<String> buf = new ArrayList<>();
        buf.add(String.format("name:%s", this.name));
        buf.add(String.format("label:%s", this.label));
        buf.add(String.format("connector:%s", this.connector));
        buf.add(String.format("procName:%s", this.processName));
        buf.add(String.format("procValue:%s", this.processValue));
        buf.add(String.format("actName:%s", this.actionName));
        buf.add(String.format("actType:%s", this.actionType));
        this.arguments.keySet().forEach(key-> {
            buf.add(String.format("inputParams:%s:%s", key, this.arguments.get(key)));
        });
        return String.join("\n", buf);
    }

    void parse(Element el){
        NodeHelper.getElementNodes(el)
                .forEach(n -> {
                   final String tag = n.getName().toLowerCase();
                   switch (tag){
                       case "actionname":
                           this.actionName = n.getStringValue();
                           break;
                       case "actiontype":
                           this.actionType = n.getStringValue();
                           break;
                       case "name":
                           this.name = n.getStringValue();
                           break;
                       case "label":
                           this.label = n.getStringValue();
                           break;
                       case "connector":
                           this.connector = NodeHelper.parseValue((Element) n);
                           break;
                       case "inputparameters":
                           parseInputParams((Element) n);
                           break;
                       case "processmetadatavalues":
                           parseProcessMeta((Element) n);
                           break;
                   }
                });
    }

    void parseProcessMeta(Element el){
        List<String> nvp = NodeHelper.parseNameValueNodes(el);
        if (nvp.size() != 2) return;
        this.processName = nvp.get(0);
        this.processValue = nvp.get(1);
    }

    void parseInputParams(Element el){
        List<String> nvp = NodeHelper.parseNameValueNodes(el);
        if (nvp.size() != 2) return;
        this.arguments.put(nvp.get(0), nvp.get(1));
    }
}
