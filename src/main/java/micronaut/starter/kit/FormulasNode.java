package micronaut.starter.kit;

import org.dom4j.Element;
import org.dom4j.Node;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Singleton
public class FormulasNode {
    HashMap<String, FormulasBean> map;

    public FormulasNode() {
        this.map = new HashMap<String, FormulasBean>();
    }

    public void parse(Element el) {
        FormulasBean bn = new FormulasBean(el);
        this.map.put(bn.name, bn);
    }

    public Set<String> keySet() {
        return (Set<String>) this.map.keySet();
    }

    public List<FormulasBean> values() {
        return new ArrayList<FormulasBean>(this.map.values());
    }

    public FormulasBean findFormulas(String name) throws ClassNotFoundException {
        if (this.map.containsKey(name)){
            return this.map.get(name);
        }
        throw new ClassNotFoundException("The formulas:" + name + " not found");
    }
}
