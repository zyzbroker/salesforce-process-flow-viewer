package micronaut.starter.kit;

import javax.inject.Singleton;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Singleton
public class PDFParser {
    private String outputFile;

    public PDFParser(ArgumentBean args){
        this.outputFile = args.outFile;
    }
    public void toPDF(String markdown) {
        System.out.println("------------toPDF------------");
        System.out.println(this.outputFile);
        System.out.println(markdown);
    }
}
