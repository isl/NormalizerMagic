/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.forth.isl.normalizationmagic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author minadakn
 */
public class StAXModifyDemo {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        XMLInputFactory ifactory = XMLInputFactory.newFactory();
        XMLOutputFactory ofactory = XMLOutputFactory.newFactory();
        StreamSource source = new StreamSource("normalized_contexts-17.xml");
        StreamResult result = new StreamResult("normalized_contexts-18.xml");

        List<String> inputs = new ArrayList();

        BufferedReader reader = new BufferedReader(new FileReader("tellMeWhatYouWantWhatYouReallyReallyWant.txt"));

        String line = reader.readLine();
        while (line != null) {
            inputs.add(line);
            line = reader.readLine();
        }
      //  System.out.println(inputs);
        reader.close();

   
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
     
          Actions.dissect("contexts_notebook",",",inputs,source,result,ifactory,ofactory);
          
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
