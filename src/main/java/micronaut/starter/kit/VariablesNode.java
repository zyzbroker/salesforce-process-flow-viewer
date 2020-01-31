package micronaut.starter.kit;

import org.dom4j.Element;
import org.dom4j.Node;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Singleton
public class VariablesNode {
    HashMap<String, VariableBean> map;

    public VariablesNode() {
        this.map = new HashMap<String, VariableBean>();
    }

    public void parse(Element el) {
        VariableBean bn = new VariableBean(el);
        this.map.put(bn.name, bn);
    }
    public Set<String> keySet() {
        return (Set<String>) this.map.keySet();
    }

    public List<VariableBean> values() {
        return new ArrayList<VariableBean>(this.map.values());
    }
    public VariableBean findBean(String key) throws ClassNotFoundException {
        if (this.map.containsKey(key)){
            return this.map.get(key);
        }
        throw new ClassNotFoundException("Variables with name: " + key + " not found");
    }
}