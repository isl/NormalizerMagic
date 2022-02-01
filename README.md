# NormalizerMagic

This tool is responsible for performing several normalizations in an XML file.
The normalization aims to transform an XML resource, so that it becomes less ambiguous, it does not contain special characters or unused text segments, 
and facilitate the mappings definition process by creating a simpler form of the resources. 
Such normalization can be (a) removal of characters or short segments of text from particular elements,
(b) splitting particular elements into more fine-grained elements, etc.

## How it works

The tool requires two input files to start the normalization process;
(a) a source file in XML format, and 
(b) a set of rules describing the normalization that should take place.

Based on the aforementioned reosurces, it applies all the given normalization rules and produce the new normalized XML resource.

## The available rules

The normalizations are given in a simple TXT file, where each rule is described in a single line. 
The first word in the line indicates the normalization process to be executed. 
The valid verbs (and their corresponding syntax) that can be used are described below. 
Notice that each verb can have alternative representations (e.g. remove is the same with delete), 
and they are case insensitive (e.g. REMOVE is the same with remove). 
Case insensitivity is valid though only for the verbs, and not for the actual element names (from the XML input)
Furthermore, empty lines are allowwed between rules, as well as commented rules (e.g. using a '#' preceding character)

### Remove (or delete)

This rule is used for expressing the removal of a string from a particular element. Its syntax is the following: 
```
remove some_string from some_element
```
The preserved words are **remove** (or delete) and **from**. some_string describes the string segment that should be removed from the element with name **some_element**

### Remove Between 
This rule is used for expressing the removal of a string that appears between two characters from a particular element. Its syntax is the following: 
```
remove all between ( ) some_element
```
The preserved words are **remove** (or delete) **all between**.  **some_element** is the name of the element in the XML input where removal will take place

### Replace

This rule is used for expressing the replacement of a string with another in  particular element. Its syntax is the following: 
```
Replace some_text with some_new_text from some_element
```
The preserved words are **replace** (or change) **with** and **from**. **some_text** is the string that should be replaced with **some_new_text** in every occurrence of the element with name **some_element**

### Dissect

This rule is used for expressing the dissection of the textual contents of an element into more fine-grained elements. In particular it will dissect the contents from the text-node of a given element, and it will create children elements, each one having the dissected text nodes.  Its syntax is the following: 
```
dissect some_text from some_element
```
The preserved words are **dissect** (or split) and **from**. **some_text** is the string to be used as a separator of the text node of the  element with name **some_element**. The result is that it will create chlidren elements with name **some_element_child** each one having the dissected contents.

## How to run it

The tool can be either be used as a JAVA API, or as a command-line executable. 
As regards the latter, you are requested to download the latest release from https://github.com/isl/NormalizerMagic/releases 
and execute it using JAVA runtime. 
```bash
java -jar MagicNormalizer-exejar.jar
```

The above execution reveals the parameters that are accepted from the tool. They are shown in the following table. 

| Param short | Param long  |  Mandatory | Description |
|-----------|---------------|-------|-------|
| -i        | --input       | yes   | defines the XML file to be normalized |
| -r        | --rules       | yes   | defines the txt file containing the normalization rules |
| -o        | --output      | yes   | defines the XML file to export the normalized contents |
| -v        | --version     | no    | reports the current version of the tool |
| -h        | --history     | no    | reports the full version history of the tool |

Assuming that the file to be normalized is input.xml, the rules are defined in a file with name rules.txt and that the normalized resources should be exported in a file with name input_normalized.xml, the tool can be executed as follows 
```bash
java -jar MagicNormalizer-exejar.jar -i input.xml -r rules.txt -o input_normalized.xml
```
