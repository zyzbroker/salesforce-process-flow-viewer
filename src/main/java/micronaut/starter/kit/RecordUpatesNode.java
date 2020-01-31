package micronaut.starter.kit;

import org.dom4j.Element;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Singleton
public class RecordUpatesNode {
    HashMap<String, RecordUpdatesBean> recordUpdates;

    public RecordUpatesNode() {
        this.recordUpdates = new HashMap<>();
    }

    public void parse(Element el){
        RecordUpdatesBean bn = new RecordUpdatesBean(el);
        this.recordUpdates.put(bn.name, bn);
    }

    public Set<String> keySet() {
        return (Set<String>) this.recordUpdates.keySet();
    }

    public List<RecordUpdatesBean> values() {
        return new ArrayList<>(this.recordUpdates.values());
    }

    public RecordUpdatesBean findBean(String key) throws ClassNotFoundException{
        if (this.recordUpdates.containsKey(key)){
            return this.recordUpdates.get(key);
        }
        throw new ClassNotFoundException("The RecordUdpates with name:" + key + " not found");
    }
}
