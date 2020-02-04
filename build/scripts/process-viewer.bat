@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  process-viewer startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and PROCESS_VIEWER_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\process-viewer.jar;%APP_HOME%\lib\guava-28.1-jre.jar;%APP_HOME%\lib\micronaut-runtime-1.3.0.RC1.jar;%APP_HOME%\lib\micronaut-http-1.3.0.RC1.jar;%APP_HOME%\lib\micronaut-aop-1.3.0.RC1.jar;%APP_HOME%\lib\micronaut-inject-1.3.0.RC1.jar;%APP_HOME%\lib\rxjava-3.0.0-RC8.jar;%APP_HOME%\lib\dom4j-1.6.1.jar;%APP_HOME%\lib\flexmark-all-0.50.44.jar;%APP_HOME%\lib\logback-classic-1.2.3.jar;%APP_HOME%\lib\failureaccess-1.0.1.jar;%APP_HOME%\lib\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;%APP_HOME%\lib\micronaut-core-1.3.0.RC1.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\checker-qual-2.8.1.jar;%APP_HOME%\lib\error_prone_annotations-2.3.2.jar;%APP_HOME%\lib\j2objc-annotations-1.3.jar;%APP_HOME%\lib\animal-sniffer-annotations-1.18.jar;%APP_HOME%\lib\slf4j-api-1.7.26.jar;%APP_HOME%\lib\javax.annotation-api-1.3.2.jar;%APP_HOME%\lib\javax.inject-1.jar;%APP_HOME%\lib\snakeyaml-1.24.jar;%APP_HOME%\lib\validation-api-2.0.1.Final.jar;%APP_HOME%\lib\jackson-datatype-jdk8-2.10.1.jar;%APP_HOME%\lib\jackson-datatype-jsr310-2.10.1.jar;%APP_HOME%\lib\jackson-databind-2.10.1.jar;%APP_HOME%\lib\rxjava-2.2.10.jar;%APP_HOME%\lib\reactive-streams-1.0.3.jar;%APP_HOME%\lib\xml-apis-1.0.b2.jar;%APP_HOME%\lib\flexmark-profile-pegdown-0.50.44.jar;%APP_HOME%\lib\flexmark-ext-abbreviation-0.50.44.jar;%APP_HOME%\lib\flexmark-ext-admonition-0.50.44.jar;%APP_HOME%\lib\flexmark-ext-anchorlink-0.50.44.jar;%APP_HOME%\lib\flexmark-ext-aside-0.50.44.jar;%APP_HOME%\lib\flexmark-ext-enumerated-reference-0.50.44.jar;%APP_HOME%\lib\flexmark-ext-attributes-0.50.44.jar;%APP_HOME%\lib\flexmark-ext-autolink-0.50.44.jar;%APP_HOME%\lib\flexmark-ext-definition-0.50.44.jar;%APP_HOME%\lib\flexmark-html-parser-0.50.44.jar;%APP_HOME%\lib\flexmark-html2md-converter-0.50.44.jar;%APP_HOME%\lib\flexmark-ext-emoji-0.50.44.jar;%APP_HOME%\lib\flexmark-ext-escaped-character-0.50.44.jar;%APP_HOME%\lib\flexmark-ext-footnotes-0.50.44.jar;%APP_HOME%\lib\flexmark-ext-gfm-issues-0.50.44.jar;%APP_HOME%\lib\flexmark-jira-converter-0.50.44.jar;%APP_HOME%\lib\flexmark-youtrack-converter-0.50.44.jar;%APP_HOME%\lib\flexmark-ext-gfm-strikethrough-0.50.44.jar;%APP_HOME%\lib\flexmark-ext-gfm-tables-0.50.44.jar;%APP_HOME%\lib\flexmark-ext-gfm-tasklist-0.50.44.jar;%APP_HOME%\lib\flexmark-ext-gfm-users-0.50.44.jar;%APP_HOME%\lib\flexmark-ext-macros-0.50.44.jar;%APP_HOME%\lib\flexmark-ext-gitlab-0.50.44.jar;%APP_HOME%\lib\flexmark-ext-jekyll-front-matter-0.50.44.jar;%APP_HOME%\lib\flexmark-ext-jekyll-tag-0.50.44.jar;%APP_HOME%\lib\flexmark-ext-media-tags-0.50.44.jar;%APP_HOME%\lib\flexmark-ext-ins-0.50.44.jar;%APP_HOME%\lib\flexmark-ext-xwiki-macros-0.50.44.jar;%APP_HOME%\lib\flexmark-ext-superscript-0.50.44.jar;%APP_HOME%\lib\flexmark-ext-tables-0.50.44.jar;%APP_HOME%\lib\flexmark-ext-toc-0.50.44.jar;%APP_HOME%\lib\flexmark-ext-typographic-0.50.44.jar;%APP_HOME%\lib\flexmark-ext-wikilink-0.50.44.jar;%APP_HOME%\lib\flexmark-ext-yaml-front-matter-0.50.44.jar;%APP_HOME%\lib\flexmark-ext-youtube-embedded-0.50.44.jar;%APP_HOME%\lib\flexmark-formatter-0.50.44.jar;%APP_HOME%\lib\flexmark-pdf-converter-0.50.44.jar;%APP_HOME%\lib\flexmark-0.50.44.jar;%APP_HOME%\lib\flexmark-util-0.50.44.jar;%APP_HOME%\lib\logback-core-1.2.3.jar;%APP_HOME%\lib\jackson-annotations-2.10.1.jar;%APP_HOME%\lib\jackson-core-2.10.1.jar;%APP_HOME%\lib\autolink-0.6.0.jar;%APP_HOME%\lib\openhtmltopdf-jsoup-dom-converter-1.0.0.jar;%APP_HOME%\lib\jsoup-1.11.3.jar;%APP_HOME%\lib\openhtmltopdf-pdfbox-1.0.0.jar;%APP_HOME%\lib\openhtmltopdf-rtl-support-1.0.0.jar;%APP_HOME%\lib\openhtmltopdf-core-1.0.0.jar;%APP_HOME%\lib\graphics2d-0.24.jar;%APP_HOME%\lib\pdfbox-2.0.16.jar;%APP_HOME%\lib\xmpbox-2.0.16.jar;%APP_HOME%\lib\icu4j-59.1.jar;%APP_HOME%\lib\fontbox-2.0.16.jar;%APP_HOME%\lib\commons-logging-1.2.jar

@rem Execute process-viewer
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %PROCESS_VIEWER_OPTS%  -classpath "%CLASSPATH%" micronaut.starter.kit.App %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable PROCESS_VIEWER_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%PROCESS_VIEWER_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
