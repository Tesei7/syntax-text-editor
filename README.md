# Syntax Text Editor

Syntax Text Editor is a text editor with syntax highlighting feature written on java swing without using standard text components.
Syntax highlighting is based on lexical analysis which is realized using [JFlex](http://jflex.de/) lexical analysers generator. 

## Main features
* Loading and saving files;
* Text entering with delete, backspace and insert keys support;
* Text navigation using arrow keys, home, end, page up and page down;
* Placing caret using mouse;
* Scrolling using mouse wheel and scroll bars;
* Test selection using shift key and mouse;
* Copy and paste actions with hot keys: Ctrl+C to copy and Ctrl+V or Shift+Ins to paste;
* Java and JavaScript syntax highlighting;
* Big text files support;

## View modes
Syntax Text editor has too view modes: default and fixed width. In default view mode horizontal scroll bar resizes to fit longest line in document. In fixed width mode editor by default shows only first 256 columns of text. This value could be changed using editor component API.

Default view mode is how text editors usually work. Fixed width mode is useful when big file (dozens of MBs) is edited, because in this mode total width of document is not recalculated.

## Building and running
Syntax Text Editor requires Java 8 and uses [Maven](https://maven.apache.org/) to build. 
To compile, run all unit tests, and create the jar, change directory to Syntax Text Editor folder and run

    mvn install

To run Syntax Text Editor run

	java -jar target/syntaxTextEditor-1.0.0-jar-with-dependencies.jar	
	
## Syntax highlighting
Syntax Text Editor supports Java and JavaScript syntax highlighting, you also can disable highlighting using "Plain text" option. 
Syntax highlighting is based on lexical analysers generated from .jflex files witch contains regular expressions and rules.
To regenerate lexical analysers classes `JavaTokenizer` and `JavaScriptTokenizer` in package `ru.tesei7.textEditor.editor.syntax` run

	mvn generate-sources -Pflex
