package micronaut.starter.kit;

import org.dom4j.Element;
import org.dom4j.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class RecordUpdatesBean {
    public String name;
    public String label;
    public String object;
    public String nextDecisionName;
    public HashMap<String,String> inputAssignments;
    public HashMap<String,String> metaData;

    public String toString() {
        List<String> buf = new ArrayList<>();
        buf.add(String.format("name:%s label:%s",this.name, this.label));
        buf.add(String.format("object:%s",this.object));
        buf.add(String.format("nextDecision:%s",this.nextDecisionName));
        this.metaData.keySet()
            .forEach(key -> {
                    buf.add(String.format("%s:%s",key, this.metaData.get(key)));
            });
        this.inputAssignments.keySet()
            .forEach(key->{
                buf.add(String.format("%s:%s",key, this.inputAssignments.get(key)));
            });
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
                    this.parseMetaValues((Element) n);
                    break;
                case "inputassignments":
                    this.parseInputAssignments((Element) n);
                    break;
                case "connector":
                    this.nextDecisionName = NodeHelper.parseValue((Element) n);
                    break;
            }
        });
    }

    void parseMetaValues(Element el){
        List<String> nvp = NodeHelper.parseNameValueNodes(el);
        if (nvp.size() != 2) {return;}
        this.metaData.put(nvp.get(0), nvp.get(1));
    }

    void parseInputAssignments(Element el){
       List<String>  nvp = NodeHelper.parseNameValueNodes(el, Optional.of("field"));
       if (nvp.size() !=2) return;
       this.inputAssignments.put(nvp.get(0), nvp.get(1));
    }
}
