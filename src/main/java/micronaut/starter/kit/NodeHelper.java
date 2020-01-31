package micronaut.starter.kit;

import org.dom4j.Element;
import org.dom4j.Node;

import java.util.*;

public class NodeHelper {
    public static List<Node> getElementNodes(Element el) {
        List<Node> nodes = new ArrayList<Node>();
        for(int i=0; i < el.nodeCount(); i++){
            if (el.node(i) instanceof Element) {
                nodes.add(el.node(i));
            }
        }
        return nodes;
    }

    public static List<String> parseNameValueNodes(Element el){
        return parseNameValueNodes(el, Optional.of("name"));
    }

    public static List<String> parseNameValueNodes(Element el, Optional<String> keyTag){
        String tag;
        String nm = "";
        String val = "";

        List<Node> nodes = NodeHelper.getElementNodes(el);
        final String nameTag = keyTag.isPresent() ? keyTag.get(): "name";

        for(Node n:nodes){
            tag = n.getName().toLowerCase();
            if (tag == nameTag){
                nm = n.getStringValue();
            } else if(tag == "value") {
                val = parseValue((Element) n);
            }
        }
        if (nm.isEmpty()) {return new ArrayList<>();}
        return Arrays.asList(nm, val);
    }

    public static String parseValue(Element el){
        List<Node> nodes = NodeHelper.getElementNodes(el);
        if (nodes.isEmpty()){return "";}
        return nodes.get(0).getStringValue();
    }
}
