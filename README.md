# A salesforce process flow definition Viewer

This utility enable the user to convert process flow XML definition file into the markdown file and easily view it in
the markdown viewer

## Technology stack

- JDK 1.8
- Micronaut Framework (include Oracle GraalVM native image build support).

## How to run it

- setup JAVA_HOME
- ./gradlew run "-f:{your process flow xml file} -o:{full path of output markdown file} -p:{absolute path where you store your process flow xml file}"

```zsh
    //open terminal and issue the following command
    ./r.sh "Account_Process.flow"
```

### r.sh

```zsh

    #!/bin/zsh
# Author: @david.zhao
# Date: 02/04/2020

export FLOW_PATH="/home/davidzhao/resentek/opensource/micronaut-starter-kit/src/main/resources"
export OUT_PATH="/home/davidzhao/resentek/opensource/micronaut-starter-kit/build"

if (test "$2" != ""); then
  echo -- ./gradlew run $1 $2 --
  ./gradlew run --args="-p:$FLOW_PATH -f:$1 -o:$OUT_PATH/$1.md" $2
else
  echo -- ./gradlew run $1 -q --
  ./gradlew run --args="-p:$FLOW_PATH -f:$1 -o:$OUT_PATH/$1.md" -q
fi
echo -- end of run --

```

**Note**: due to the fact that "\_\_" is reserved for emphasis in markdown, thus we have to use "--" to replace it.

### Account_Process.flow sample

```xml

<?xml version="1.0" encoding="UTF-8"?>
<Flow xmlns="http://soap.sforce.com/2006/04/metadata">
    <actionCalls>
        <processMetadataValues>
            <name>ActionCallType</name>
            <value>
                <stringValue>flow</stringValue>
            </value>
        </processMetadataValues>
        <processMetadataValues>
            <name>flowSelection</name>
            <value>
                <stringValue>DH Client Community Create ChatterGroup for Approved Accounts</stringValue>
            </value>
        </processMetadataValues>
        <name>myRule_11_A1</name>
        <label>Launch Flow</label>
        <locationX>1100</locationX>
        <locationY>200</locationY>
        ...
```

Here is the markdown result

![MarkdownResult](account_process_md.png)

## Next To do

- use Oracle GraalVM to build executable file to run everywhere without setup JDK1.8
- need to add feature to convert markdown to PDF so that the user can view PDF instead of using markdown viewer.
