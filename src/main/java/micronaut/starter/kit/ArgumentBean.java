package micronaut.starter.kit;

import javax.inject.Singleton;
import java.security.InvalidParameterException;
import java.util.Arrays;

@Singleton
public class ArgumentBean {
    public String inFile;
    public String outFile;
    public String inPath;

    public ArgumentBean() {
        this.inPath = "";
        this.inFile = "";
        this.outFile = "";
    }

    public String getInputFile() {
        return this.inPath.isEmpty()
            ? this.inFile
            : String.format("%s/%s", this.inPath, this.inFile);
    }

    public String getOutFile() {
        return this.outFile.isEmpty()
            ? String.format("%s.md", this.getInputFile())
            : this.outFile;
    }

    public ArgumentBean build(String[] args){
        if (args == null || args.length < 1) {
            throw new InvalidParameterException("The args is required. please issue command \"-f:{inputFile} -o:{outputFile} -p:{inputFile path}\"");
        }
        Arrays.asList(args).forEach(arg -> {
            if (arg.indexOf("-f:") != -1) {
                this.inFile = arg.substring(3);
            } else if (arg.indexOf("-o:") != -1) {
                this.outFile = arg.substring(3);
            } else if (arg.indexOf("-p:") != -1){
                this.inPath = arg.substring(3);
            }
        });

        return this;
    }
}
