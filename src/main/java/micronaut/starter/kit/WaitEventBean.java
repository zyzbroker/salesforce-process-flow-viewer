package micronaut.starter.kit;

import org.dom4j.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WaitEventBean {
    public String name;
    public String label;
    public String conditionLogic;
    public List<String> conditions;
    public String connector;
    public String eventType;
    public HashMap<String,String> inputsMeta;

    public WaitEventBean(Element el){
        this.conditions = new ArrayList<>();
        this.inputsMeta = new HashMap<>();
        NodeHelper.getElementNodes(el)
            .forEach(nd -> {
                
            });
    }
}
