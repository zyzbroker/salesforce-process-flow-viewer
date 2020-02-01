package micronaut.starter.kit;

import javax.inject.Inject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class MarkdownProcessor {
    private String outputFile;
    private Pattern pattern;
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
        this.pattern = Pattern.compile("(Rule)*[_0-9]*_myRule_[0-9_]*");
        this.outputFile = args.outFile;
    }

    public String toMarkdownFile() {
        String markdown = "";
        try(BufferedWriter writer = Files.newBufferedWriter(Paths.get(this.outputFile))){
            markdown = toMarkdown();
            writer.write(markdown);
        }
        catch(ClassNotFoundException notFound){
            System.out.println(notFound.getMessage());
        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
        }
        return markdown;
    }

    public String toMarkdown() throws ClassNotFoundException{
        markdownEditor.addTitle(this.processDefinition.name + " Process Flow Guide", 0);
        markdownEditor.addTable(genPageSummary());
        markdownEditor.addTitle("Process Variable Definition", 2);
        markdownEditor.addPage("These variables represents the old and current instance of the record and process level accessible instances.");
        markdownEditor.addTable(genProcessVariables());
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

    TableBean genProcessVariables() {
        TableBean table = new TableBean();
        table.addHeader(Arrays.asList("Variable","DataType","IsInput","IsOutput","ObjectType","IsCollect"));
            for (VariableBean var : this.variablesNode.values()) {
                table.addRow(Arrays.asList(var.name,
                        var.dataType,
                        String.valueOf(var.isInput),
                        String.valueOf(var.isOutput),
                        var.objectType,
                        String.valueOf(var.isCollection)));
            }
        return table;
    }

    TableBean genSteps() throws ClassNotFoundException {
        int step = 1;
        TableBean table = new TableBean();
        table.addHeader(Arrays.asList("Step#","Step","Condition","Action","Then"));
        String decision = this.processDefinition.startDecision;
        while(decision != null && !decision.isEmpty()){
            final DecisionRowBean rowBean = genDecisionRow(step, decision);
            table.addRow(rowBean.row);
            decision = rowBean.nextDecision;
            step++;
        }
        return table;
    }

    DecisionRowBean genDecisionRow(int stepSeq, String decisionName) throws ClassNotFoundException{
        DecisionBean decision = this.decisionNode.findDecision(decisionName);
        String stepCol = getStepCol(decision);
        String conditionCol = getConditionCol(decision);
        List<String> actionCol = getActionCol(stepSeq, decision);
        String thenCol = actionCol.get(1);

        List<String> row = Arrays.asList(
                String.valueOf(stepSeq),
                stepCol,
                conditionCol,
                actionCol.get(0),
                thenCol);
        return DecisionRowBean.create(row, decision.defaultDecisionName);
    }

    boolean isEmpty(String obj) {
        return obj == null || obj.isEmpty();
    }

    List<String> getActionCol(int currentStep, DecisionBean decision) throws ClassNotFoundException {
        if (decision.rules == null) {
            return Arrays.asList("n/a", isEmpty(decision.defaultDecisionName)
                    ? "Exit Process"
                    : String.format("Goto Step %d", currentStep + 1));
        }
        String connector = decision.rules.nextDecision;

        if (this.decisionNode.keySet().contains(connector)){
            return Arrays.asList("n/a", String.format("Goto Step %s", currentStep + 1));
        }
        int actionIndex = 1;
        String thenWhat = "Exit Of Process";
        List<String> actions = new ArrayList<>();
        while(hasNextAction(connector)){
            if (this.assignmentsNode.keySet().contains(connector)){
                AssignmentBean assignment = this.assignmentsNode.findBean(connector);
                actions.add(String.format("**Action%d:** *%s* -> *%s* -> *%s*", actionIndex++,assignment.toReference, assignment.operator, getFormulaExpression(assignment.value)));
                connector = assignment.connector;
            } else if (this.decisionNode.keySet().contains(connector)){
                thenWhat = String.format("Goto Step %s", currentStep + 1);
                break;
            } else if (this.actionCallsNode.keySet().contains(connector)){
                ActionCallBean actionCall = this.actionCallsNode.findBean(connector);
                actions.add(String.format("**Action%d:** *%s* -> *%s* -> *%s*",actionIndex++,
                        actionCall.actionType, actionCall.actionName, actionCall.label));
                connector = actionCall.connector;
            } else {
                thenWhat = "End of Process";
                connector = "";
            }
        }

        //find "then"

        return Arrays.asList(String.join(" ", actions), thenWhat);
    }

    boolean hasNextAction(String connector){
        if (connector == null || connector.isEmpty()) return false;
        return this.actionCallsNode.keySet().contains(connector)
            || this.recordUpdatesNode.keySet().contains(connector)
            || this.assignmentsNode.keySet().contains(connector);
    }

    String getStepCol(DecisionBean decision){
        String name = decision.rules == null
                ? decision.name
                : decision.rules.label;
        Matcher match = this.pattern.matcher(name);
        name = match.replaceAll(": ");

        if (!name.contains(" ")) {
            name = String.join(" ", name.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])"));
        }
        return name;
    }

    String getConditionCol(DecisionBean decision) {
        if (decision.rules == null) {
            return "";
        }
        return String.format("**Condition Logic:** *%s* **Conditions:** %s",
                decision.rules.conditionLogic.toUpperCase(),
                IntStream.range(0, decision.rules.conditions.size())
                   .mapToObj(i -> String.format("**[%d]:** *%s*",i + 1, parseCondition(decision.rules.conditions.get(i))))
                    .reduce("", (c1, c2) -> String.format("%s %s", c1, c2))
                );
    }

    String parseCondition(String condition) {
        String matchWord = "formula";
        String[] parts = condition.split(":");
        if (parts.length != 3){
            return String.join(" ", parts).replaceAll("[.]", " . ");
        }
        if (!condition.contains(matchWord)) {
            Matcher match = this.pattern.matcher(parts[0]);
            parts[0] = match.replaceAll(": ").replaceAll("[.]"," . ");
            if (parts[1].contains("EqualTo")) {
                parts[1] = "=";
            } else if (parts[1].contains("IsNull")) {
                parts[1]= "IsNull =";
            }
            return String.join(" ", parts);
        }

        if (parts[0].contains(matchWord)) {
            parts[0] = getFormulaExpression(parts[0]
                    .replaceAll("[.]", " . "))
                    .replaceAll(",", ", ");
            Matcher match = this.pattern.matcher(parts[0]);
            parts[0] = match.replaceAll(": ");
        }
        if (parts[1].contains("EqualTo")) {
            parts[1] = "=";
        }
        if (parts[2].contains(matchWord)) {
            parts[2] = getFormulaExpression(parts[2]).replaceAll(",",", ");
        }
        return String.join(" ", parts);
    }

    String getFormulaExpression(String name) {
        try {
            if (this.formulasNode.keySet().contains(name)) {
                FormulasBean formula = this.formulasNode.findFormulas(name);
                name = formula.expression;
            }
        } catch(ClassNotFoundException ex){
        }
        return name.replaceAll("[.]"," . ");
    }

}
