# A salesforce process flow definition Viewer

This utility enable the user to convert process flow XML definition file into the markdown file and easily view it in 
the markdown viewer

## Technology stack
- JDK 1.8
- Micronaut Framework (include Oracle GraalVM native image build support).

## How to run it
- setup JAVA_HOME
- ./gradlew run "-f:{your process flow xml file} -o:{full path of output markdown file} -p:{absolute path where you store your process flow xml file}"

## Next To do
- use Oracle GraalVM to build executable file to run everywhere without setup JDK1.8