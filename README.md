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

### Remove

### Remove Between 

### Replace

### Dissect

### Add


## How to run it
