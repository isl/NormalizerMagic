/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.forth.isl.normalizationmagic;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.util.Pair;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * @author minadakn
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
public class StAXModifyDemo {

    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {

        XMLInputFactory ifactory = XMLInputFactory.newFactory();
        XMLOutputFactory ofactory = XMLOutputFactory.newFactory();
        StreamSource source=new StreamSource(new InputStreamReader(new FileInputStream("input.xml"),"UTF-8"));
        StreamResult result=new StreamResult(new OutputStreamWriter(new FileOutputStream("output.xml", false),"UTF-8"));

        StreamSource intermediateInput=null;

        List<String> replaceInputs = new ArrayList();
        List<String> removeInputs = new ArrayList();
        List<String> removeBetweenInputs = new ArrayList();
        List<String> dissectInputs = new ArrayList();

        BufferedReader reader = new BufferedReader(new FileReader("rules.txt"));

        String line = reader.readLine();
        while (line != null) {
            if(line.toLowerCase().startsWith("replace")){
                replaceInputs.add(line);
            }else if(line.toLowerCase().startsWith("remove") || line.toLowerCase().startsWith("delete")){
                if(line.toLowerCase().contains(" between ")){
                    removeBetweenInputs.add(line);
                }else{
                    removeInputs.add(line);
                }
                
            }else if(line.toLowerCase().startsWith("dissect") || line.toLowerCase().startsWith("split")){
                dissectInputs.add(line);
            }else{
                //to log warning here
            }
            line = reader.readLine();
        }
        reader.close();
        int i=0;
        for(String replaceInput : replaceInputs){
            System.out.println("Applying REPLACE rule : "+replaceInput);
            result=new StreamResult(new OutputStreamWriter(new FileOutputStream("output.xml", false),"UTF-8"));
            if(intermediateInput==null){
                Actions.replace(Arrays.asList(replaceInput), source, result, ifactory, ofactory);
            }else{
                Actions.replace(Arrays.asList(replaceInput), intermediateInput, result, ifactory, ofactory);
            }
            ifactory= XMLInputFactory.newFactory();
            ofactory = XMLOutputFactory.newFactory();
            Files.copy(Paths.get("output.xml"), Paths.get("input_intermediate.xml"), StandardCopyOption.REPLACE_EXISTING);
            intermediateInput=new StreamSource(new InputStreamReader(new FileInputStream("input_intermediate.xml"),"UTF-8"));           
        }

        for(String removeInput : removeBetweenInputs){
            System.out.println("Applying REMOVE BETWEEN rule : "+removeInput);
            result=new StreamResult(new OutputStreamWriter(new FileOutputStream("output.xml", false),"UTF-8"));
            if(intermediateInput==null){
                Actions.remove_between(Arrays.asList(removeInput), source, result, ifactory, ofactory);
            }else{
                Actions.remove_between(Arrays.asList(removeInput), intermediateInput, result, ifactory, ofactory);
            }
            ifactory= XMLInputFactory.newFactory();
            ofactory = XMLOutputFactory.newFactory();
            Files.copy(Paths.get("output.xml"), Paths.get("input_intermediate.xml"), StandardCopyOption.REPLACE_EXISTING);
            intermediateInput=new StreamSource(new InputStreamReader(new FileInputStream("input_intermediate.xml"),"UTF-8"));
        }

        for(String removeInput : removeInputs){
            System.out.println("Applying REMOVE rule : "+removeInput);
            result=new StreamResult(new OutputStreamWriter(new FileOutputStream("output.xml", false),"UTF-8"));
            if(intermediateInput==null){
                Actions.remove(Arrays.asList(removeInput), source, result, ifactory, ofactory);
            }else{
                Actions.remove(Arrays.asList(removeInput), intermediateInput, result, ifactory, ofactory);
            }
            ifactory= XMLInputFactory.newFactory();
            ofactory = XMLOutputFactory.newFactory();
            Files.copy(Paths.get("output.xml"), Paths.get("input_intermediate.xml"), StandardCopyOption.REPLACE_EXISTING);
            intermediateInput=new StreamSource(new InputStreamReader(new FileInputStream("input_intermediate.xml"),"UTF-8"));
        }

        for(String dissectLine : dissectInputs){
            System.out.println("Applying DISSECT rule : "+dissectLine);
            result=new StreamResult(new OutputStreamWriter(new FileOutputStream("output.xml", false),"UTF-8"));
            Pair<String,String> dissectPair=Utils.getDissectionInfo(dissectLine.trim());
            if(intermediateInput==null){
                Actions.dissect(dissectPair.getKey(), dissectPair.getValue(), source, result, ifactory, ofactory);
            }else{
                Actions.dissect(dissectPair.getKey(), dissectPair.getValue(), intermediateInput, result, ifactory, ofactory);
            }
            ifactory= XMLInputFactory.newFactory();
            ofactory = XMLOutputFactory.newFactory();
            Files.copy(Paths.get("output.xml"), Paths.get("input_intermediate.xml"), StandardCopyOption.REPLACE_EXISTING);
            intermediateInput=new StreamSource(new InputStreamReader(new FileInputStream("input_intermediate.xml"),"UTF-8"));   
        }
//        Files.delete(Paths.get("input_intermediate.xml"));
        
      //  Map<String, String> testmap = new HashMap();
//
//        Iterator i = inputs.iterator();
//
//        String elementName = null;
//        String elementValue = null;
//
//        while (i.hasNext()) {
//
//            String manme = i.next().toString();
//
//            if (manme.contains("remove") || manme.contains("delete")) {
//                String[] allofme = manme.split(" ");
//                int counter = 0;
//
//                for (String allofmeCont : allofme) {
//                    counter++;
//                    if (allofmeCont.equals("remove") || allofmeCont.equals("delete")) {
//                        elementValue = allofme[counter];
//                        System.out.println("Value" + elementValue);
//                    }
//
//                    if (allofmeCont.equals("from")) {
//                        elementName = allofme[counter].replace("<", "").replace(">", "");
//                        System.out.println("Name" + elementName);
//                    }
//
//                }
//
//                testmap.put(elementName, elementValue);
//
//            }
//
////            if(manme.contains("Remove") || manme.contains("Delete") )
////           {
////               String[] allofme = manme.split(" ");
////               
////               //     System.out.println(allofme[counter]);
////           }
     //   }       
//
//        String elementName2 = "t2";
//        String elementValue2 = "()";
//
//        testmap.put(elementName1, elementValue1);
//        testmap.put(elementName2, elementValue2);
       
  //      System.out.println(testmap.entrySet().toString());
        
     //  Actions.add2(inputs,source,result,ifactory,ofactory);
// Actions.remove2(inputs,source,result,ifactory,ofactory);
//         
 
//Actions.remove2(inputs,source,result,ifactory,ofactory);
//Actions.replace2(inputs,source,result,ifactory,ofactory);
        
   //  Actions.remove_between(inputs,source,result,ifactory,ofactory);
     
//          Actions.dissect("contexts_notebook",",",inputs,source,result,ifactory,ofactory);
          
// preserves the blanks 
          //Actions.dissect2("consequences",",",inputs,source,result,ifactory,ofactory);
     // Actions.add2(inputs,source,result,ifactory,ofactory);
//        
        
        
        
        
        
        
        

//        try {
//            XMLEventReader in = ifactory.createXMLEventReader(source);
//            XMLEventWriter out = ofactory.createXMLEventWriter(result);
//
//            XMLEventFactory ef = XMLEventFactory.newInstance();
//
//            while (in.hasNext()) {
//
//                XMLEvent e = in.nextEvent();
//
//                if (e.isStartElement() && testmap.containsKey(((StartElement) e).getName().getLocalPart().toLowerCase())) {
//
//                    String tocheck = e.asStartElement().getName().getLocalPart().toLowerCase();
//
//                    XMLEvent ef2 = (XMLEvent) in.next();
//
//                    if (ef2.isCharacters() && ((Characters) ef2).getData().contains(testmap.get(tocheck))) {
//
//                        ef2 = ef.createCharacters(ef2.asCharacters().getData().replace(testmap.get(tocheck), ""));
//                        out.add(e);
//                        out.add(ef2);
//                    } else {
//                        out.add(e);
//                        out.add(ef2);
//                    }
//
//                } else {
//                    out.add(e);
//                }
//            }
//            in.close();
//            out.close();
//        } catch (XMLStreamException e) {
//            e.printStackTrace();
//        }
        }
}
