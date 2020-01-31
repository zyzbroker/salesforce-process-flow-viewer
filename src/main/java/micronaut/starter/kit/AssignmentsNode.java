package micronaut.starter.kit;

import org.dom4j.Element;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Singleton
public class AssignmentsNode {
    HashMap<String, AssignmentBean> map;

    public AssignmentsNode() {
        this.map = new HashMap<>();
    }

    public Set<String> keySet() {
        return (Set<String>) this.map.keySet();
    }

    public List<AssignmentBean> values() {
        return new ArrayList<AssignmentBean>(this.map.values());
    }

    public void parse(Element el){
        AssignmentBean bn = new AssignmentBean(el);
        this.map.put(bn.name, bn);
    }
}
