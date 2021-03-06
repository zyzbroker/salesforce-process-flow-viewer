package micronaut.starter.kit;

import org.dom4j.Element;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Optional;

@Singleton
public class DecisionsNode {
    private HashMap<String, DecisionBean> decisions;

    public DecisionsNode() {
        this.decisions = new HashMap<String, DecisionBean>();
    }

    public void parse(Element nd) {
        DecisionBean bn = new DecisionBean(nd);
        this.decisions.put(bn.name, bn);
    }

    public boolean hasDecision(String name) {
        return this.decisions.containsKey(name);
    }

    public DecisionBean findDecision(String name) {
        return this.decisions.get(name);
    }

    public Set<String> keySet() {
        return (Set<String>) this.decisions.keySet();
    }

    public List<DecisionBean> values() {
        return new ArrayList<DecisionBean>(this.decisions.values());
    }
}
