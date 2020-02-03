package micronaut.starter.kit;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TableBean {
    private List<String> header;
    private List<List<String>> content;

    public TableBean() {
        this.header = new ArrayList<>();
        this.content = new ArrayList<>();
    }

    public void addHeader(List<String> headers){
        this.header = headers;
    }

    public void addRow(List<String> row){
        this.content.add(row);
    }

    public String toMarkdown() {
        List<String> markdowns = new ArrayList<>();
        if (this.header.isEmpty()) {
            return "**No table header specified**";
        }

        markdowns.add(String.join(" | ", this.header));
        markdowns.add(String.join(" | ", this.header.stream()
                    .map(r -> this.transform(r))
                    .collect(Collectors.toList())));

        for(List<String> row: this.content){
            markdowns.add(String.join(" | ",
                    row.stream()
                       .map(r-> r != null ? r.replace("|", " ") : "")
                       .collect(Collectors.toList())));
        }

        return String.join("\n", markdowns);
    }

    String transform(String source) {
        List<String> buf = new ArrayList<>();
        for(int i=0; i < source.length(); i++){
            buf.add("-");
        }
        return String.join("", buf);
    }
}
