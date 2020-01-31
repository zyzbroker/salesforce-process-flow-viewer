package micronaut.starter.kit;

import javax.inject.Inject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class MarkdownProcessor {
    private String outputFile;

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

    public MarkdownProcessor(ArgumentBean args){
        this.outputFile = args.outFile;
    }

    public String toMarkdownFile() {
        String markdown = toMarkdown();
        try(BufferedWriter writer = Files.newBufferedWriter(Paths.get(this.outputFile))){
            writer.write(markdown);
        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
        }
        return markdown;
    }

    public String toMarkdown() {
        markdownEditor.addTitle(this.processDefinition.name + " Process Flow Guide", 0);
        markdownEditor.addTable(genPageSummary());
        markdownEditor.addTitle("Process Variable Definition", 2);
        markdownEditor.addPage("These variables represents the old and current instance of the record.");
        markdownEditor.addTable(genProcessVaribles());
        markdownEditor.addTitle("Process Flow Execution Detail",2);
        markdownEditor.addPage("The process flow is executed based on the following step sequence.");
        markdownEditor.addTable(genSteps());
        return markdownEditor.toString();
    }

    TableBean genPageSummary() {
        TableBean table = new TableBean();
        table.addHeader(Arrays.asList("Process Name","Description", "SObject", "TriggerType", "Status"));
        table.addRow(Arrays.asList(
                processDefinition.name,
                processDefinition.description,
                processDefinition.objectType,
                processDefinition.triggerType,
                processDefinition.status));
        return table;
    }

    TableBean genProcessVaribles() {
        TableBean table = new TableBean();
        table.addHeader(Arrays.asList("objectVariable", "oldObjectVariable"));
        table.addRow(Arrays.asList(processDefinition.objectVariable, processDefinition.oldObjectVariable));
        return table;
    }

    TableBean genSteps() {
        int step = 1;
        TableBean table = new TableBean();
        table.addHeader(Arrays.asList("Step#","Step","Condition","Action","Then"));

        return table;
    }

}
