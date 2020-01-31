package micronaut.starter.kit;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


@Singleton
public class FlowParser {
    @Inject
    private MarkdownEditor markdownEditor;
    @Inject
    private DecisionsNode decisionNode;
    @Inject
    private VariablesNode variablesNode;
    @Inject
    private FormulasNode formulasNode;
    @Inject
    private ProcessBean processDefinition;
    @Inject
    private AssignmentsNode assignmentsNode;
    @Inject
    private ActionCallsNode actionCallsNode;
    @Inject
    private RecordUpatesNode recordUpdatesNode;

    public void parse(String metaXmlFile) throws DocumentException
    {
        System.out.println("---" + metaXmlFile + "---");
        SAXReader reader = new SAXReader();
        Document doc = reader.read(new File(metaXmlFile));
        Element root = doc.getRootElement();
        processDefinition.parse(root);

        for(Iterator<Element> it = root.elementIterator(); it.hasNext();){
            Element n = it.next();
            String tag = n.getName().toLowerCase();
            switch (tag) {
                case "decisions":
                    decisionNode.parse(n);
                    break;
                case "formulas":
                    formulasNode.parse(n);
                    break;
                case "variables":
                    variablesNode.parse(n);
                    break;
                case "assignments":
                    assignmentsNode.parse(n);
                    break;
                case "actioncalls":
                    actionCallsNode.parse(n);
                    break;
                case "recordupdates":
                    this.recordUpdatesNode.parse(n);
                    break;
            }
        }

        System.out.println("----------process def------------");
        System.out.println(processDefinition.toString());

        System.out.println("\n--------decision nodes---------");
        decisionNode.values()
                .forEach(nm -> {
                    System.out.println(nm.toString());
                    System.out.println("-----------------------------------");
                });
        System.out.println("\n--------- formulas---------");
        formulasNode.values()
                .forEach(f -> {
                    System.out.println(f.toString());
                    System.out.println("-----------------------------------");
                });

        System.out.println("\n------- variables----------");
        variablesNode.values().forEach(v -> {
            System.out.println(v.toString());
            System.out.println("-----------------------------------");
        });

        System.out.println("\n-------- assignments ---------");
        assignmentsNode.values().forEach(a -> {
            System.out.println(a.toString());
            System.out.println("-----------------------------------");
        });

        System.out.println("\n-------- actionCalls ---------");
        actionCallsNode.values().forEach(a -> {
            System.out.println(a.toString());
            System.out.println("-----------------------------------");
        });

        System.out.println("\n----------recordupdates-------------");
        recordUpdatesNode.values().forEach(r -> {
            System.out.println(r.toString());
            System.out.println("-----------------------------------");
        });
    }
}
