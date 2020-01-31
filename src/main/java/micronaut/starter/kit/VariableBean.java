package micronaut.starter.kit;

import org.dom4j.Element;

public class VariableBean {
    public String name;
    public String dataType;
    public boolean isCollection;
    public boolean isInput;
    public boolean isOutput;
    public String objectType;

    public String toString() {
        return String.format("object:%s name:%s dataType:%s collection:%b input:%b output:%b",
                this.objectType,
                this.name,
                this.dataType,
                this.isCollection,
                this.isInput,
                this.isOutput);
    }

    public VariableBean(Element el){
        NodeHelper.getElementNodes(el)
                .forEach(n -> {
                    final String tag = n.getName().toLowerCase();
                    switch(tag){
                        case "name":
                            this.name = n.getStringValue();
                            break;
                        case "datatype":
                            this.dataType = n.getStringValue();
                            break;
                        case "iscollection":
                            this.isCollection = Boolean.valueOf(n.getStringValue());
                            break;
                        case "isinput":
                            this.isInput = Boolean.valueOf(n.getStringValue());
                            break;
                        case "isoutput":
                            this.isOutput = Boolean.valueOf(n.getStringValue());
                            break;
                        case "objecttype":
                            this.objectType = n.getStringValue();
                            break;
                    }
                });
    }
}
