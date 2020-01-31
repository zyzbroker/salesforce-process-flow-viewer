package micronaut.starter.kit;

import javax.inject.Singleton;
import java.security.InvalidParameterException;
import java.util.Arrays;

@Singleton
public class ArgumentBean {
    public String inFile;
    public String outFile;
    public String inPath;

    public ArgumentBean build(String[] args){
        if (args == null || args.length < 1) {
            throw new InvalidParameterException("The args is required. please issue command \"-f:{inputFile} -o:{outputFile}\"");
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
