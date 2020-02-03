package micronaut.starter.kit;

import org.dom4j.Element;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Singleton
public class WaitEventNode {
    private HashMap<String, WaitEventBean> waitEvents;

    public WaitEventNode(){
        this.waitEvents = new HashMap<>();
    }

    public void parse(Element waits) {
        WaitEventBean bn = new WaitEventBean(waits);
        this.waitEvents.put(bn.name, bn);
    }

    public boolean hasWait(String key){
        return this.waitEvents.containsKey(key);
    }

    public boolean hasWaitEvent(String name) {
        return this.waitEvents.values()
                .stream()
                .anyMatch(w -> w.eventName == name);
    }

    public List<WaitEventBean> values() {
        return new ArrayList<WaitEventBean>(this.waitEvents.values());
    }

    public WaitEventBean findBean(String key)  throws ClassNotFoundException {
        if (this.waitEvents.containsKey(key)){
            return this.waitEvents.get(key);
        }
        throw new ClassNotFoundException("The WaitEvent with name:" + key + " not found");
    }
}
