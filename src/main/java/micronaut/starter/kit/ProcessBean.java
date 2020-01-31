package micronaut.starter.kit;

import org.dom4j.Element;
import org.dom4j.Node;

import javax.inject.Singleton;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ProcessBean {
    public String name;
    public String label;
    public String description;
    public String status;
    public String startDecision;
    public String objectType;
    public String triggerType;
    public String processType;
    public String objectVariable;
    public String oldObjectVariable;

    public String toString() {
        List<String> msgs = new ArrayList<String>();
        msgs.add(String.format("name:%s label:%s",this.name, this.label));
        msgs.add(String.format("description:%s", this.description));
        msgs.add(String.format("status:%s startDecision:%s", String.valueOf(this.status), this.startDecision));
        msgs.add(String.format("objectType:%s triggerType:%s", this.objectType, this.triggerType));
        msgs.add(String.format("processType:%s", this.processType));
        msgs.add(String.format("objVar:%s oldObjVar:%s", this.objectVariable, this.oldObjectVariable));
        return String.join("\n", msgs);
    }

    public ProcessBean() {}

    public void parse(Element root){
        NodeHelper.getElementNodes(root)
                .forEach(n -> {
                    final String tagName = n.getName().toLowerCase();
                    switch(tagName){
                        case "name":
                            this.name = n.getStringValue();
                            break;
                        case "label":
                            this.label = n.getStringValue();
                            this.name = this.label;
                            break;
                        case "description":
                            this.description = n.getStringValue();
                            break;
                        case "status":
                            this.status = n.getStringValue();
                            break;
                        case "processtype":
                            this.processType = n.getStringValue();
                            break;
                        case "startelementreference":
                            this.startDecision = n.getStringValue();
                            break;
                        case "processmetadatavalues":
                            this.parseMetadata((Element) n);
                            break;
                    }
                });
    }

    public void parseMetadata(Element el){
        List<String> nvp = NodeHelper.parseNameValueNodes(el);
        if (nvp.size() != 2) {return;}

        final String type = nvp.get(0).toLowerCase();
        switch(type){
            case "objectvariable":
                this.objectVariable = nvp.get(1);
                break;
            case "oldobjectvariable":
                this.oldObjectVariable = nvp.get(1);
                break;
            case "objecttype":
                this.objectType = nvp.get(1);
                break;
            case "triggertype":
                this.triggerType = nvp.get(1);
                break;
        }
    }
}
