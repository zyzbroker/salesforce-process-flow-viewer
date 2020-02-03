package micronaut.starter.kit;

import org.dom4j.Element;
import org.dom4j.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

public class RecordUpdatesBean {
    public String name;
    public String label;
    public String object;
    public String connector;
    public HashMap<String,String> inputAssignments;
    public HashMap<String,String> metaData;

    public String toMarkdownString() {
        return this.inputAssignments.keySet()
            .stream()
            .map(key -> String.format("%s = %s", key, this.inputAssignments.get(key)))
            .reduce("",(s1,s2) -> String.format("%s %s", s1, s2));
    }

    public String toString() {
        List<String> buf = new ArrayList<>();
        buf.add(String.format("name:%s label:%s",this.name, this.label));
        buf.add(String.format("object:%s",this.object));
        buf.add(String.format("connector:%s",this.connector));
        buf.add("-----------meta---------------");
        this.metaData.keySet()
            .forEach(key -> {
                    buf.add(String.format("%s:%s",key, this.metaData.get(key)));
            });
        buf.add("----------input------------");
        buf.add(toMarkdownString());
        return String.join("\n", buf);
    }

    public RecordUpdatesBean(Element el) {
        this.inputAssignments = new HashMap<>();
        this.metaData = new HashMap<>();

        NodeHelper.getElementNodes(el)
        .forEach(n -> {
            final String tag = n.getName().toLowerCase();
            switch(tag){
                case "object":
                    this.object = n.getStringValue();
                    break;
                case "name":
                    this.name = n.getStringValue();
                    break;
                case "label":
                    this.label = n.getStringValue();
                    break;
                case "processmetadatavalues":
                    this.parseMetaValues((Element) n, this.metaData);
                    break;
                case "inputassignments":
                    this.parseInputAssignments((Element) n);
                    break;
                case "connector":
                    this.connector = NodeHelper.parseValue((Element) n);
                    break;
            }
        });
    }

    void parseMetaValues(Element el, HashMap<String,String> target){
        List<String> nvp = NodeHelper.parseNameValueNodes(el);
        if (nvp.size() != 2) {return;}
        target.put(nvp.get(0), nvp.get(1));
    }

    void parseInputAssignments(Element el){
        String name = "";
        String value = "";
        HashMap<String,String> inputMeta = new HashMap<>();
        List<Node> nodes = NodeHelper.getElementNodes(el);
        for(Node nd: nodes)
        {
                final String tag = nd.getName().toLowerCase();
                switch(tag) {
                    case "field":
                        name = nd.getStringValue();
                        break;
                    case "value":
                        value = NodeHelper.parseValue((Element) nd);
                        break;
                    case "processmetadatavalues":
                        this.parseMetaValues((Element) nd, inputMeta);
                        break;
                }
            };
        if (name.isEmpty()) {return;}
        if (value.isEmpty() && inputMeta.keySet().contains("rightHandSideType")){
            value = inputMeta.get("rightHandSideType");
            if (value.equalsIgnoreCase("GlobalConstant")) {
                value = "Null";
            }
        }
        this.inputAssignments.put(name,value);
    }
}
