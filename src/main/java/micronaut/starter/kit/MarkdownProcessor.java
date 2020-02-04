package micronaut.starter.kit;

import io.micronaut.discovery.$DefaultServiceInstanceIdGeneratorDefinitionClass;

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
    @Inject
    private WaitEventNode waitsNode;

    private HashMap<String, Integer> parsedWaits;

    public MarkdownProcessor(ArgumentBean args){
        this.pattern = Pattern.compile("(Rule)*[_0-9]*_myRule_[0-9_]*");
        this.outputFile = args.getOutFile();
        this.parsedWaits = new HashMap<String, Integer>();
    }

    public String toMarkdownFile() {
        String markdown = "";
        try(BufferedWriter writer = Files.newBufferedWriter(Paths.get(this.outputFile))){
            markdown = toMarkdown();
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
        markdownEditor.addPage("These variables represents the old and current instance of the record and process level accessible instances.");
        markdownEditor.addTable(genProcessVariables());
        markdownEditor.addTitle("Process Flow Execution Detail",2);
        markdownEditor.addPage("The process flow is executed based on the following step sequence.");
        this.addSteps();

        return markdownEditor.toString();
    }

    TableBean genPageSummary() {
        TableBean table = new TableBean();
        table.addHeader(Arrays.asList("Process Name","Description", "SObject", "TriggerType", "Status"));
        table.addRow(Arrays.asList(
                processDefinition.name,
                processDefinition.description,
                keepUnderscoreMark(processDefinition.objectType),
                processDefinition.triggerType,
                processDefinition.status));
        return table;
    }

    TableBean genProcessVariables() {
        TableBean table = new TableBean();
        table.addHeader(Arrays.asList("Variable","DataType","IsInput","IsOutput","ObjectType","IsCollect"));
            for (VariableBean var : this.variablesNode.values()) {
                table.addRow(Arrays.asList(keepUnderscoreMark(var.name),
                        var.dataType,
                        String.valueOf(var.isInput),
                        String.valueOf(var.isOutput),
                        keepUnderscoreMark(var.objectType),
                        String.valueOf(var.isCollection)));
            }
        return table;
    }

    // step mean "decision"
    void addSteps() {
        int stepSeq = 1;
        String connector = this.processDefinition.startDecision;
        while(!this.isEmpty(connector)) {
            if (this.parsedWaits.containsKey(connector)) break;
            final StepRowBean step = genStepRow(stepSeq, connector);
            this.addStepHeader(stepSeq, keepUnderscoreMark(step.description));
            this.addStepDetail(step);
            connector = step.nextDecision;
            stepSeq++;
        }
    }

    void addStepHeader(int step, String description) {
        markdownEditor.addTitle(String.format("Step %d: *%s*", step, description), 3);
    }

    void addStepDetail(StepRowBean stepRow) {
        final TableBean table = new TableBean();
        table.addHeader(Arrays.asList("Condition","Action", "Then"));
        table.addRow(stepRow.row);
        this.markdownEditor.addTable(table);
    }

    StepRowBean genStepRowForWaits(int stepSeq,  WaitEventBean waitEvent) {
        boolean negateFlag = true;
        this.parsedWaits.put(waitEvent.name, stepSeq);
        String conditionCol = genConditionColForWait(waitEvent, negateFlag);
        String actionCol = "n/a";
        String thenCol = "Exit Of Process";
        List<String> row = Arrays.asList(
                conditionCol,
                actionCol,
                thenCol
        );
        return StepRowBean.create(row, getStepDetail(waitEvent.label) ,waitEvent.connector);
    }

    StepRowBean genStepRowForAssignments(int stepSeq, AssignmentBean assign) {
        String conditionCol = "n/a";
        String actionCol = String.format("**Action1** *%s %s %s*",
                assign.toReference,
                assign.operator,
                assign.value);
        String thenCol = String.format("Goto Step %s",
        this.parsedWaits.containsKey(assign.connector)
                ? this.parsedWaits.get(assign.connector)
                : stepSeq + 1);
        List<String> row = Arrays.asList(
                conditionCol,
                actionCol,
                thenCol
        );
        return StepRowBean.create(row, getStepDetail(assign.label) ,assign.connector);
    }

    StepRowBean genStepRow(int stepSeq, String connector) {

        if (this.waitsNode.hasWait(connector)){
            return genStepRowForWaits(stepSeq, this.waitsNode.findBean(connector));
        }

        if (this.assignmentsNode.hasAssignment(connector)){
            return genStepRowForAssignments(stepSeq, this.assignmentsNode.findBean(connector));
        }

        DecisionBean decision = this.decisionNode.findDecision(connector);
        boolean negateFlag = shouldNegateConditionActionLogic(decision);
        System.out.println("-----Negate:" + String.valueOf(negateFlag) + "-----");
        String conditionCol = genConditionCol(decision, negateFlag);
        List<String> actionCol = getActionCol(stepSeq, decision, negateFlag);
        String thenCol = actionCol.get(1);

        List<String> row = Arrays.asList(
                conditionCol,
                actionCol.get(0),
                thenCol);
        return StepRowBean.create(row, getStepDetail(decision), negateFlag ? decision.rules.nextDecision : decision.defaultDecisionName);
    }

    boolean isEmpty(String obj) {
        return obj == null || obj.isEmpty();
    }

    List<String> getActionCol(int currentStep, DecisionBean decision, boolean negateFlag){
        if (decision.rules == null) {
            return Arrays.asList("n/a", isEmpty(decision.defaultDecisionName)
                    ? "Exit Process"
                    : String.format("Goto Step %d", currentStep + 1));
        }
        String connector = negateFlag ? decision.defaultDecisionName : decision.rules.nextDecision;

        if (this.decisionNode.hasDecision(connector)){
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
            } else if (this.actionCallsNode.keySet().contains(connector)){
                ActionCallBean actionCall = this.actionCallsNode.findBean(connector);
                actions.add(String.format("**Action%d:** *%s* -> *%s* -> *%s*",actionIndex++,
                        actionCall.actionType, keepUnderscoreMark(actionCall.actionName), actionCall.label));
                connector = actionCall.connector;
            } else if (this.recordUpdatesNode.keySet().contains(connector)){
                RecordUpdatesBean recordUpdate = this.recordUpdatesNode.findBean(connector);
                actions.add(String.format("**UpdateRecord** *%s*", keepUnderscoreMark(recordUpdate.object)));
                actions.add(String.format("**Action%d**: *%s*", actionIndex++, genRecordUpdateExpression(recordUpdate)));
                connector = recordUpdate.connector;
            } else if (this.waitsNode.hasWait(connector)) {
                WaitEventBean waitEvent = this.waitsNode.findBean(connector);
                actions.add(String.format("**Action%d** *%s*", actionIndex++, keepUnderscoreMark(waitEvent.eventName)));
                connector = "";
            }
            else if (this.decisionNode.keySet().contains(connector)){
                thenWhat = String.format("Goto Step %s", currentStep + 1);
                break;
            } else {
                thenWhat = "End of Process";
                connector = "";
            }
        }
        return Arrays.asList(String.join(" ", actions), thenWhat);
    }

    String genRecordUpdateExpression(RecordUpdatesBean recordUpdate) {
        return keepUnderscoreMark(recordUpdate.inputAssignments.keySet()
                .stream()
                .map(key -> {
                    final String val = recordUpdate.inputAssignments.get(key);
                    if (val.contains("formula")){
                        return String.format("%s = %s", key, this.getFormulaExpression(val));
                    }
                    return String.format("%s = %s", key, val);
                })
                .reduce("",(s1,s2) -> s1.isEmpty() ? s2 : String.format("%s %s", s1, s2)));
    }

    boolean hasNextAction(String connector){
        if (connector == null || connector.isEmpty()) return false;
        return this.actionCallsNode.keySet().contains(connector)
            || this.recordUpdatesNode.keySet().contains(connector)
            || this.assignmentsNode.keySet().contains(connector)
            || this.decisionNode.keySet().contains(connector);
    }

    String getStepDetail(String title){
        Matcher match = this.pattern.matcher(title);
        title = match.replaceAll(": ");

        if (!title.contains(" ")) {
            title = String.join(" ", title.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])"));
        }
        return title.trim();
    }


    String getStepDetail(DecisionBean decision){
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

    String genConditionColForWait(WaitEventBean waits, boolean negateFlag){
        if (waits.conditions == null || waits.conditions.isEmpty()) return "daivd zhao";
        String logic = negateFlag ? "NOT " + waits.conditionLogic.toUpperCase()
                : waits.conditionLogic.toUpperCase();
        return String.format("**Condition Logic:** *%s* **Conditions:** %s",
                logic,
                IntStream.range(0, waits.conditions.size())
                .mapToObj(i -> String.format("**[%d]:** *%s*",i + 1, parseCondition(waits.conditions.get(i))))
                .reduce("",(a1,a2) -> String.format("%s %s",a1,a2))
                );
    }

    String genConditionCol(DecisionBean decision, boolean negateFlag) {
        if (decision.rules == null) {
            return "";
        }
        String logic = decision.rules.conditionLogic.toUpperCase();
        return String.format("**Condition Logic:** *%s* **Conditions:** %s",
                negateFlag ? String.format("NOT %s", logic) : logic,
                IntStream.range(0, decision.rules.conditions.size())
                   .mapToObj(i -> String.format("**[%d]:** *%s*",i + 1, parseCondition(decision.rules.conditions.get(i))))
                    .reduce("", (c1, c2) -> c1.isEmpty() ? c2 : String.format("%s %s", c1, c2))
                );
    }

    boolean shouldNegateConditionActionLogic(DecisionBean decision) {
        if (decision.defaultDecisionName == null) {
            if (decision.rules != null
                && decision.rules.nextDecision != null
                && this.decisionNode.hasDecision(decision.rules.nextDecision)){
                return true;
            }
            return false;
        }
        return decision.defaultDecisionName != null
                    && decision.rules != null
                    && decision.rules.nextDecision != null
                    && !this.decisionNode.hasDecision(decision.defaultDecisionName)
                    && this.decisionNode.hasDecision(decision.rules.nextDecision);
    }

    String keepUnderscoreMark(String val){
        return val.replaceAll("[_]","-");
    }

    String parseCondition(String condition) {
        String matchWord = "formula";
        String[] parts = condition.split(":");
        if (parts.length != 3){
            return keepUnderscoreMark(String.join(" ", parts).replaceAll("[.]", " . "));
        }
        if (!condition.contains(matchWord)) {
            Matcher match = this.pattern.matcher(parts[0]);
            parts[0] = match.replaceAll(": ").replaceAll("[.]"," . ");
            if (parts[1].contains("EqualTo")) {
                parts[1] = "=";
            } else if (parts[1].contains("IsNull")) {
                parts[1]= "IsNull =";
            }
            return keepUnderscoreMark(String.join(" ", parts));
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
        return keepUnderscoreMark(String.join(" ", parts));
    }

    String getFormulaExpression(String name) {
        try {
            if (this.formulasNode.keySet().contains(name)) {
                FormulasBean formula = this.formulasNode.findFormulas(name);
                name = formula.expression;
            }
        } catch(ClassNotFoundException ex){
        }
        return name.replaceAll("[.]"," . ").replaceAll("\n","");
    }

}
