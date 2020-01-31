package micronaut.starter.kit;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class MarkdownEditor {
    private List<String> content;

    public MarkdownEditor() {
        this.content = new ArrayList<>();
    }

    public String toString() {
        return String.join("\n\n", content);
    }

    public MarkdownEditor addTitle(String title, int level) {

        this.content.add(String.format("%s %s", this.getPoundSign(level), title));
        return this;
    }

    public MarkdownEditor addPage(String page){
        this.content.add(page);
        return this;
    }

    public MarkdownEditor addList(List<String> points) {
        points.forEach(point->{
            this.content.add(String.format("- %s", point));
        });
        return this;
    }

    String getPoundSign(int level){
        switch(level){
            case 2: return "##";
            case 3: return "###";
            case 4: return "####";
            default: return "#";
        }
    }

    public MarkdownEditor addTable(TableBean table) {
        this.content.add(table.toMarkdown());
        return this;
    }
}
