package micronaut.starter.kit;

import org.dom4j.Element;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Singleton
public class ActionCallsNode {
    HashMap<String, ActionCallBean> actionCalls;

    public ActionCallsNode() {
        this.actionCalls = new HashMap<>();
    }

    public void parse(Element el) {
        ActionCallBean bn = new ActionCallBean(el);
        this.actionCalls.put(bn.name, bn);
    }

    public Set<String> keySet() {
        return (Set<String>) this.actionCalls.keySet();
    }

    public List<ActionCallBean> values() {
        return new ArrayList<ActionCallBean>(this.actionCalls.values());
    }

    public ActionCallBean findBean(String key) throws ClassNotFoundException {
        if (this.actionCalls.containsKey(key)){
            return this.actionCalls.get(key);
        }
        throw new ClassNotFoundException("The Actioncall with name:" + key + " not found.");
    }
}
