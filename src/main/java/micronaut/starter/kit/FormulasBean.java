package micronaut.starter.kit;

import org.dom4j.Element;
import org.dom4j.Node;

public class FormulasBean {
    public String name;
    public String dataType;
    public String expression;
    public String metaName;
    public String metaValue;

    public String toString() {
        return String.format("name:%s dataType:%s expression:%s metaName:%s metaValue:%s",
                this.name, this.dataType, this.expression, this.metaName, this.metaValue
                );
    }

    public FormulasBean(Element el) {
        NodeHelper.getElementNodes(el)
                .forEach(nd -> {
                    String ndName = nd.getName().toLowerCase();
                    switch(ndName){
                        case "name":
                            this.name = nd.getStringValue();
                            break;
                        case "datatype":
                            this.dataType = nd.getStringValue();
                            break;
                        case "expression":
                            this.expression = nd.getStringValue();
                            break;
                        case "processmetadatavalues":
                            parseMetadata((Element) nd);
                            break;
                    }
                });
    }
    void parseMetadata(Element el) {
        NodeHelper.getElementNodes(el)
                .forEach(nd -> {
                    final String ndName = nd.getName().toLowerCase();
                    switch(ndName){
                        case "name":
                            this.metaName = nd.getStringValue();
                            break;
                        case "stringvalue":
                            this.metaValue = nd.getStringValue();
                            break;
                    }
                });
    }
}
