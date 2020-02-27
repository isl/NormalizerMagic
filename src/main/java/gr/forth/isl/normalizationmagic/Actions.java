/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.forth.isl.normalizationmagic;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javafx.util.Pair;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author minadakn
 */
public class Actions {

    public static void remove(List inputs, StreamSource source, StreamResult result, XMLInputFactory ifactory, XMLOutputFactory ofactory) {

        Map<String, List<String>> testmap = new HashMap();

        ArrayList<String> values = new ArrayList();

        Iterator i = inputs.iterator();

        String elementName = null;
        String elementValue = null;

        while (i.hasNext()) {

            String manme = i.next().toString();

            if ((manme.toLowerCase().contains("remove") || manme.toLowerCase().contains("delete")) && !(manme.toLowerCase().contains("between"))) {
                String[] allofme = manme.split(" ");
                int counter = 0;

                for (String allofmeCont : allofme) {
                    counter++;
                    if (allofmeCont.toLowerCase().equals("remove") || allofmeCont.toLowerCase().equals("delete")) {
                        elementValue = allofme[counter];
                        values.add(elementValue);
                    }

                    if (allofmeCont.toLowerCase().equals("from")) {
                        elementName = allofme[counter].replace("<", "").replace(">", "");
                    }
                }
                testmap.put(elementName, values);
            }
        }
        try {
            XMLEventReader in = ifactory.createXMLEventReader(source);
            XMLEventWriter out = ofactory.createXMLEventWriter(result);

            XMLEventFactory ef = XMLEventFactory.newInstance();

            while (in.hasNext()) {

                XMLEvent e = in.nextEvent();

                if (e.isStartElement() && testmap.containsKey(((StartElement) e).getName().getLocalPart().toLowerCase())) {

                    String tocheck = e.asStartElement().getName().getLocalPart().toLowerCase();

                    XMLEvent ef2 = (XMLEvent) in.next();

                    Iterator it = testmap.get(tocheck).iterator();

                    while (it.hasNext()) {
                        String toreplace = it.next().toString().replace("_", " ");

                        if (ef2.isCharacters() && ((Characters) ef2).getData().contains(toreplace)) {

                            ef2 = ef.createCharacters(ef2.asCharacters().getData().replace(toreplace, "").trim());  
                        }

                    }

                    out.add(e);
                    out.add(ef2);

                } else {

                    out.add(e);
                }
            }
            in.close();
            out.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

    }

    public static void remove2(List inputs, StreamSource source, StreamResult result, XMLInputFactory ifactory, XMLOutputFactory ofactory) {

        Map<String, List<String>> testmap = new HashMap();

        // ArrayList<String> values = new ArrayList(); 
        Iterator i = inputs.iterator();

        String elementName = null;

        Set s = new TreeSet();

        while (i.hasNext()) {

            //  String elementValue = null;
            String manme = i.next().toString();

            if ((manme.toLowerCase().contains("remove") || manme.toLowerCase().contains("delete")) && !(manme.toLowerCase().contains("between"))) {
                String[] allofme = manme.split(" ");
                int counter = 0;

                for (String allofmeCont : allofme) {
                    counter++;

                    if (allofmeCont.toLowerCase().equals("from")) {
                        elementName = allofme[counter].replace("<", "").replace(">", "");
                        s.add(elementName);
                    }

                }

            }

        }

        Iterator setIt = s.iterator();

        while (setIt.hasNext()) {
            String edw = setIt.next().toString();

            Iterator i2 = inputs.iterator();

            ArrayList<String> values = new ArrayList();

            while (i2.hasNext()) {

                String manme = i2.next().toString();
                String elementValue = null;

                if ((manme.toLowerCase().contains("remove") || manme.toLowerCase().contains("delete")) && !(manme.toLowerCase().contains("between")) && (manme.contains(edw))) {
                    String[] allofme = manme.split(" ");
                    int counter = 0;

                    for (String allofmeCont : allofme) {
                        counter++;

                        if (allofmeCont.toLowerCase().equals("remove") || allofmeCont.equals("delete")) {
                            elementValue = allofme[counter];
                        }

                        if (allofmeCont.equals("from")) {
                            elementName = allofme[counter].replace("<", "").replace(">", "");
                        }
                    }

                    values.add(elementValue);

                }

            }

            testmap.put(elementName, values);
        }
        try {
            XMLEventReader in = ifactory.createXMLEventReader(source);
            XMLEventWriter out = ofactory.createXMLEventWriter(result);

            XMLEventFactory ef = XMLEventFactory.newInstance();

            while (in.hasNext()) {

                XMLEvent e = in.nextEvent();

                if (e.isStartElement() && testmap.containsKey(((StartElement) e).getName().getLocalPart().toLowerCase())) {

                    String tocheck = e.asStartElement().getName().getLocalPart().toLowerCase();

                    XMLEvent ef2 = (XMLEvent) in.next();

                    Iterator it = testmap.get(tocheck).iterator();

                    while (it.hasNext()) {
                        String toreplace = it.next().toString().replace("_", " ");

                        if (ef2.isCharacters() && ((Characters) ef2).getData().contains(toreplace)) {

                            ef2 = ef.createCharacters(ef2.asCharacters().getData().replace(toreplace, ""));
                        }

                    }

                    out.add(e);
                    out.add(ef2);

                } else {

                    out.add(e);
                }
            }
            in.close();
            out.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

    }

    public static void add(List inputs, StreamSource source, StreamResult result, XMLInputFactory ifactory, XMLOutputFactory ofactory) {

        Map<String, List<String>> testmap = new HashMap();

        ArrayList<String> values = new ArrayList();

        Iterator i = inputs.iterator();

        String elementName = null;
        String elementValue = null;

        while (i.hasNext()) {

            String manme = i.next().toString();

            if ((manme.contains("ADD"))) {
                String[] allofme = manme.split(" ");
                int counter = 0;

                for (String allofmeCont : allofme) {
                    counter++;
                    if (allofmeCont.equals("ADD")) {
                        elementValue = allofme[counter];
                        values.add(elementValue);
                    }

                    if (allofmeCont.toLowerCase().equals("from")) {
                        elementName = allofme[counter].replace("<", "").replace(">", "");
                    }
                }
                testmap.put(elementName, values);
            }
        }

        try {
            XMLEventReader in = ifactory.createXMLEventReader(source);
            XMLEventWriter out = ofactory.createXMLEventWriter(result);

            XMLEventFactory ef = XMLEventFactory.newInstance();

            while (in.hasNext()) {

                XMLEvent e = in.nextEvent();

                if (e.isStartElement() && testmap.containsKey(((StartElement) e).getName().getLocalPart().toLowerCase())) {

                    String tocheck = e.asStartElement().getName().getLocalPart().toLowerCase();

                    XMLEvent ef2 = (XMLEvent) in.next();


                    Iterator it = testmap.get(tocheck).iterator();

                    while (it.hasNext()) {
                        String toreplace = it.next().toString().replace("_", " ");

                        if (ef2.isCharacters()) {

                            ef2 = ef.createCharacters(toreplace + ef2.asCharacters().getData());
                        }

                    }

                    out.add(e);
                    out.add(ef2);

                } else {

                    out.add(e);
                }
            }
            in.close();
            out.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

    }

    public static void add2(List inputs, StreamSource source, StreamResult result, XMLInputFactory ifactory, XMLOutputFactory ofactory) {

        Map<String, List<String>> testmap = new HashMap();


        Iterator i = inputs.iterator();
        String elementName = null;

        
        Set s = new TreeSet();
        while (i.hasNext()) {

            String manme = i.next().toString();

            if ((manme.contains("ADD"))) {
                String[] allofme = manme.split(" ");
                int counter = 0;

                for (String allofmeCont : allofme) {
                    counter++;

                    if (allofmeCont.toLowerCase().equals("from")) {
                        elementName = allofme[counter].replace("<", "").replace(">", "");
                        s.add(elementName);
                    }
                }
            }

        }

        Iterator setIt = s.iterator();

        while (setIt.hasNext()) {
            String edw = setIt.next().toString();

            Iterator i2 = inputs.iterator();

            ArrayList<String> values = new ArrayList();

            while (i2.hasNext()) {

                String manme = i2.next().toString();
                
                String elementValue = null;

                if ((manme.contains("ADD")) && (manme.contains(edw))) {
                    String[] allofme = manme.split(" ");
                    int counter = 0;

                    for (String allofmeCont : allofme) {
                        counter++;

                        if (allofmeCont.equals("ADD")) {
                            elementValue = allofme[counter];
                            
                        }

                        if (allofmeCont.equals("from")) {
                            elementName = allofme[counter].replace("<", "").replace(">", "");

                            
                        }

                    }

                    values.add(elementValue);

                }

            }

            testmap.put(elementName, values);
        }

        try {
            XMLEventReader in = ifactory.createXMLEventReader(source);
            XMLEventWriter out = ofactory.createXMLEventWriter(result);

            XMLEventFactory ef = XMLEventFactory.newInstance();

            while (in.hasNext()) {

                XMLEvent e = in.nextEvent();

                
                if (e.isStartElement() && testmap.containsKey(((StartElement) e).getName().getLocalPart().toLowerCase())) {

                    String tocheck = e.asStartElement().getName().getLocalPart().toLowerCase();

                    XMLEvent ef2 = (XMLEvent) in.next();


                    Iterator it = testmap.get(tocheck).iterator();

                    while (it.hasNext()) {
                        String toreplace = it.next().toString().replace("_", " ");

                        if (ef2.isCharacters()) {
                            ef2 = ef.createCharacters(toreplace + ef2.asCharacters().getData());
                        }

                    }

                    out.add(e);
                    out.add(ef2);

                } else {

                    out.add(e);
                }
            }
            in.close();
            out.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

    }

    public static void replace(List inputs, StreamSource source, StreamResult result, XMLInputFactory ifactory, XMLOutputFactory ofactory) {

        String elementName = null;
        Pair<String, String> elementValue = new Pair(null, null);

        Map<String, List<Pair>> testmap = new HashMap();

        ArrayList<Pair> values = new ArrayList();

        Iterator i = inputs.iterator();

        while (i.hasNext()) {

            String manme = i.next().toString();

            if (manme.toLowerCase().contains("replace") || manme.contains("change")) {
                String[] allofme = manme.split(" ");
                int counter = 0;
                String elementValue1 = null;
                String elementValue2 = null;

                for (String allofmeCont : allofme) {
                    counter++;

                    if (allofmeCont.toLowerCase().equals("replace") || allofmeCont.equals("change")) {
                        elementValue1 = allofme[counter];
                        //      System.out.println("Value" + elementValue1);
                    }

                    if (allofmeCont.equals("with")) {
                        elementValue2 = allofme[counter];
                        //  System.out.println("Value" + elementValue2);
                    }

                    if (allofmeCont.equals("from")) {
                        elementName = allofme[counter].replace("<", "").replace(">", "");
                        // System.out.println("Name" + elementName);
                    }

                }

                values.add(new Pair(elementValue1, elementValue2));

            }
            testmap.put(elementName, values);
//            if(manme.contains("Remove") || manme.contains("Delete") )
//           {
//               String[] allofme = manme.split(" ");
//               
//               //     System.out.println(allofme[counter]);
//           }
        }
//
//        String elementName2 = "t2";
//        String elementValue2 = "()";
//
//        testmap.put(elementName1, elementValue1);
//        testmap.put(elementName2, elementValue2);
       

        try {
            XMLEventReader in = ifactory.createXMLEventReader(source);
            XMLEventWriter out = ofactory.createXMLEventWriter(result);

            XMLEventFactory ef = XMLEventFactory.newInstance();

            while (in.hasNext()) {

                XMLEvent e = in.nextEvent();

                if (e.isStartElement() && testmap.containsKey(((StartElement) e).getName().getLocalPart().toLowerCase())) {

                    String tocheck = e.asStartElement().getName().getLocalPart().toLowerCase();

                    XMLEvent ef2 = (XMLEvent) in.next();

                    Iterator it = testmap.get(tocheck).iterator();

                    while (it.hasNext()) {
                        Pair tor = (Pair) it.next();
                        String toreplace = tor.getKey().toString().replace("_", " ");
                        String newvalue = tor.getValue().toString().replace("_", " ");

                        if (ef2.isCharacters() && ((Characters) ef2).getData().contains(toreplace)) {
                            ef2 = ef.createCharacters(ef2.asCharacters().getData().replace(toreplace, newvalue));
                            //         System.out.println("    1  "+ef2.asCharacters().getData().replace(toreplace,newvalue)); 
                        }

                    }
                    out.add(e);
                    out.add(ef2);
                } else {
                    out.add(e);
                }
            }
            in.close();
            out.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

    }

    public static void replace2(List inputs, StreamSource source, StreamResult result, XMLInputFactory ifactory, XMLOutputFactory ofactory) {

        String elementName = null;
        //  Pair<String,String> elementValue= new Pair(null,null);

        Map<String, List<Pair>> testmap = new HashMap();

        Set s = new TreeSet();

        Iterator i = inputs.iterator();

        while (i.hasNext()) {

            String manme = i.next().toString();
        //     System.out.println(manme);

            if (manme.toLowerCase().contains("replace") || manme.contains("change")) {
                String[] allofme = manme.split(" ");
                int counter = 0;

                for (String allofmeCont : allofme) {
                    counter++;

                    if (allofmeCont.equals("from")) {
                        elementName = allofme[counter].replace("<", "").replace(">", "");
                        s.add(elementName);
                        // System.out.println("Name" + elementName);
                    }

                }

            }

        }

        Iterator setIt = s.iterator();

        while (setIt.hasNext()) {
            String edw = setIt.next().toString();

            Iterator i2 = inputs.iterator();

            ArrayList<Pair> values = new ArrayList();
            while (i2.hasNext()) {

                String manme = i2.next().toString();
                // System.out.println(manme);
                String elementValue1 = null;
                String elementValue2 = null;

                if (manme.toLowerCase().contains("replace") && manme.contains(edw)) {
                    String[] allofme = manme.split(" ");
                    int counter = 0;

                    for (String allofmeCont : allofme) {
                        counter++;

                        if (allofmeCont.toLowerCase().equals("replace") || allofmeCont.equals("change")) {
                            elementValue1 = allofme[counter];
                            //System.out.println("Value" + elementValue1);
                        }

                        if (allofmeCont.equals("with")) {
                            elementValue2 = allofme[counter];
                            // System.out.println("Value" + elementValue2);
                        }

                        if (allofmeCont.equals("from")) {
                            elementName = allofme[counter].replace("<", "").replace(">", "");
                            //  s.add(elementName);
                            //System.out.println("Name" + elementName);
                        }

                    }

                    values.add(new Pair(elementValue1, elementValue2));

                }

            }

            testmap.put(elementName, values);
        }

        //      System.out.println("STET"+s);
//               System.out.println(values);
        //     System.out.println(values2);
//
//        String elementName2 = "t2";
//        String elementValue2 = "()";
//
//        testmap.put(elementName1, elementValue1);


        try {
            XMLEventReader in = ifactory.createXMLEventReader(source);
            XMLEventWriter out = ofactory.createXMLEventWriter(result);

            XMLEventFactory ef = XMLEventFactory.newInstance();

            while (in.hasNext()) {

                XMLEvent e = in.nextEvent();

                if (e.isStartElement() && testmap.containsKey(((StartElement) e).getName().getLocalPart().toLowerCase())) {

                    String tocheck = e.asStartElement().getName().getLocalPart().toLowerCase();

                    XMLEvent ef2 = (XMLEvent) in.next();

                    Iterator it = testmap.get(tocheck).iterator();

                    while (it.hasNext()) {
                        Pair tor = (Pair) it.next();
                        String toreplace = tor.getKey().toString().replace("_", " ");
                        String newvalue = tor.getValue().toString().replace("_", " ");

                        if (ef2.isCharacters() && ((Characters) ef2).getData().contains(toreplace)) {
                            ef2 = ef.createCharacters(ef2.asCharacters().getData().replace(toreplace, newvalue));
                            
                        }

                    }
                    out.add(e);
                    out.add(ef2);
                } else {
                    out.add(e);
                }
            }
            in.close();
            out.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

    }

    public static void remove_between(List inputs, StreamSource source, StreamResult result, XMLInputFactory ifactory, XMLOutputFactory ofactory) {

//        StreamSource source = new StreamSource("contexts.xml");
//        StreamResult result = new StreamResult("connection2.xml");     
//        
//        XMLInputFactory ifactory = XMLInputFactory.newFactory();
//        XMLOutputFactory ofactory = XMLOutputFactory.newFactory();
        String elementName = null;
        // Pair<String, String> elementValue = new Pair(null, null);

        Map<String, List<Pair>> testmap = new HashMap();

       // ArrayList<Pair> values = new ArrayList();
        Set s = new TreeSet();

        Iterator i = inputs.iterator();

        while (i.hasNext()) {

            String manme = i.next().toString();

            if (manme.toLowerCase().contains("remove") && manme.toLowerCase().contains("between")) {
                String[] allofme = manme.split(" ");
                int counter = 0;

                for (String allofmeCont : allofme) {
                    counter++;

                    if (allofmeCont.equals("from")) {
                        elementName = allofme[counter].replace("<", "").replace(">", "");
                        s.add(elementName);
                    }

                }
                //  values.add(new Pair(elementValue1, elementValue2));

            }
        //    testmap.put(elementName, values);
            //    System.out.println(" " + testmap);

//            if(manme.contains("Remove") || manme.contains("Delete") )
//           {
//               String[] allofme = manme.split(" ");
//               
//               //     System.out.println(allofme[counter]);
//           }
        }
//
//        String elementName2 = "t2";
//        String elementValue2 = "()";
//
//        testmap.put(elementName1, elementValue1);
//        testmap.put(elementName2, elementValue2);
        Iterator setIt = s.iterator();

        while (setIt.hasNext()) {
            String edw = setIt.next().toString();

            Iterator i2 = inputs.iterator();

            ArrayList<Pair> values = new ArrayList();
            while (i2.hasNext()) {

                String manme = i2.next().toString();
                // System.out.println(manme);

                if (manme.toLowerCase().contains("remove") && manme.contains("between") && manme.contains(edw)) {
                    String elementValue1 = null;
                    String elementValue2 = null;

                    String[] allofme = manme.split(" ");
                    int counter = 0;

                    for (String allofmeCont : allofme) {
                        counter++;

                        if (allofmeCont.equals("between")) {
                            elementValue1 = allofme[counter];
                            elementValue2 = allofme[counter + 1];
                            
                        }

                        if (allofmeCont.equals("from")) {
                            elementName = allofme[counter].replace("<", "").replace(">", "");
                            // s.add(elementName);
                            //System.out.println("Name" + elementName);
                        }
                    }
                    values.add(new Pair(elementValue1, elementValue2));
                }
            }
            testmap.put(elementName, values);
        }

        
        try {
            XMLEventReader in = ifactory.createXMLEventReader(source);
            XMLEventWriter out = ofactory.createXMLEventWriter(result);

            XMLEventFactory ef = XMLEventFactory.newInstance();

            while (in.hasNext()) {

                XMLEvent e = in.nextEvent();

                if (e.isStartElement() && testmap.containsKey(((StartElement) e).getName().getLocalPart().toLowerCase())) {

                    String tocheck = e.asStartElement().getName().getLocalPart().toLowerCase();

                    XMLEvent ef2 = (XMLEvent) in.next();

                    Iterator it = testmap.get(tocheck).iterator();

                    while (it.hasNext()) {
                        Pair tor = (Pair) it.next();
                        String firststring = tor.getKey().toString();
                        String secondstring = tor.getValue().toString();

                        if (ef2.isCharacters() && ((Characters) ef2).getData().contains(firststring) && ((Characters) ef2).getData().contains(secondstring)) {
                            String substringme = null;

                            substringme = ef2.asCharacters().getData().substring(ef2.asCharacters().getData().indexOf(firststring), ef2.asCharacters().getData().indexOf(secondstring) + secondstring.length());
                            ef2 = ef.createCharacters(ef2.asCharacters().getData().replace(" " + substringme, "").trim());
                            ef2 = ef.createCharacters(ef2.asCharacters().getData().replace(substringme, "").trim());
                        }
                    }
                    out.add(e);
                    out.add(ef2);

                } else {
                    out.add(e);
                }
            }
            in.close();
            out.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

    }

    public static void remove_between2(List inputs, StreamSource source, StreamResult result, XMLInputFactory ifactory, XMLOutputFactory ofactory) {

//        StreamSource source = new StreamSource("contexts.xml");
//        StreamResult result = new StreamResult("connection2.xml");     
//        
//        XMLInputFactory ifactory = XMLInputFactory.newFactory();
//        XMLOutputFactory ofactory = XMLOutputFactory.newFactory();
        String elementName = null;
        Pair<String, String> elementValue = new Pair(null, null);

        Map<String, List<Pair>> testmap = new HashMap();

        ArrayList<Pair> values = new ArrayList();

        Iterator i = inputs.iterator();

        while (i.hasNext()) {

            String manme = i.next().toString();

            if (manme.contains("remove") && manme.contains("between")) {
                String[] allofme = manme.split(" ");
                int counter = 0;
                String elementValue1 = null;
                String elementValue2 = null;

                for (String allofmeCont : allofme) {
                    counter++;

                    if (allofmeCont.equals("between")) {
                        elementValue1 = allofme[counter];
                        elementValue2 = allofme[counter + 1];
                    }

                    if (allofmeCont.equals("from")) {
                        elementName = allofme[counter].replace("<", "").replace(">", "");
                    }

                }
                values.add(new Pair(elementValue1, elementValue2));

            }
            testmap.put(elementName, values);

//            if(manme.contains("Remove") || manme.contains("Delete") )
//           {
//               String[] allofme = manme.split(" ");
//               
//               //     System.out.println(allofme[counter]);
//           }
        }
//
//        String elementName2 = "t2";
//        String elementValue2 = "()";
//
//        testmap.put(elementName1, elementValue1);
//        testmap.put(elementName2, elementValue2);

        try {
            XMLEventReader in = ifactory.createXMLEventReader(source);
            XMLEventWriter out = ofactory.createXMLEventWriter(result);

            XMLEventFactory ef = XMLEventFactory.newInstance();

            while (in.hasNext()) {

                XMLEvent e = in.nextEvent();

                if (e.isStartElement() && testmap.containsKey(((StartElement) e).getName().getLocalPart().toLowerCase())) {

                    String tocheck = e.asStartElement().getName().getLocalPart().toLowerCase();

                    XMLEvent ef2 = (XMLEvent) in.next();

                    Iterator it = testmap.get(tocheck).iterator();

                    while (it.hasNext()) {
                        Pair tor = (Pair) it.next();
                        String firststring = tor.getKey().toString();
                        String secondstring = tor.getValue().toString();

                        if (ef2.isCharacters() && ((Characters) ef2).getData().contains(firststring) && ((Characters) ef2).getData().contains(secondstring)) {
                            String substringme = null;

                            substringme = ef2.asCharacters().getData().substring(ef2.asCharacters().getData().indexOf(firststring), ef2.asCharacters().getData().indexOf(secondstring) + secondstring.length());

                            ef2 = ef.createCharacters(ef2.asCharacters().getData().replace(" " + substringme, ""));
                            ef2 = ef.createCharacters(ef2.asCharacters().getData().replace(substringme, ""));
                        }
                    }
                    out.add(e);
                    out.add(ef2);

                } else {
                    out.add(e);
                }
            }
            in.close();
            out.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

    }

    public static void dissect(String elementName, String delimeter, StreamSource source, StreamResult result, XMLInputFactory ifactory, XMLOutputFactory ofactory) {
//        StreamSource source = new StreamSource("contexts.xml");
//        StreamResult result = new StreamResult("connection2.xml");     
//        
//        XMLInputFactory ifactory = XMLInputFactory.newFactory();
//        XMLOutputFactory ofactory = XMLOutputFactory.newFactory();
//        String elementName = null;
//        Pair<String, String> elementValue = new Pair(null, null);
//
//        Map<String, List<Pair>> testmap = new HashMap();
//
//        ArrayList<Pair> values = new ArrayList();
//
//        Iterator i = inputs.iterator();
//
//        while (i.hasNext()) {
//
//            String manme = i.next().toString();
//
//            if (manme.contains("remove") && manme.contains("between")) {
//                String[] allofme = manme.split(" ");
//                int counter = 0;
//                String elementValue1 = null;
//                String elementValue2 = null;
//
//                for (String allofmeCont : allofme) {
//                    counter++;
//
//                    if (allofmeCont.equals("between")) {
//                        elementValue1 = allofme[counter];
//                        elementValue2 = allofme[counter + 1];
//                        System.out.println("Value" + elementValue1 + " " + elementValue2);
//                    }
//
//                    if (allofmeCont.equals("from")) {
//                        elementName = allofme[counter].replace("<", "").replace(">", "");
//                        System.out.println("Name" + elementName);
//                    }
//
//                }
//                values.add(new Pair(elementValue1, elementValue2));
//
//            }
//            testmap.put(elementName, values);
//            System.out.println(" " + testmap);
//            if(manme.contains("Remove") || manme.contains("Delete") )
//           {
//               String[] allofme = manme.split(" ");
//               
//               //     System.out.println(allofme[counter]);
//           }
        //     }
//
//        String elementName2 = "t2";
//        String elementValue2 = "()";
//
//        testmap.put(elementName1, elementValue1);
//        testmap.put(elementName2, elementValue2);
        try {
            XMLEventReader in = ifactory.createXMLEventReader(source);
            XMLEventWriter out = ofactory.createXMLEventWriter(result);

            XMLEventFactory ef = XMLEventFactory.newInstance();

            while (in.hasNext()) {

                XMLEvent e = in.nextEvent();

                if (e.isStartElement() && (((StartElement) e).getName().getLocalPart().toLowerCase().equals(elementName))) {

                  //  String tocheck = e.asStartElement().getName().getLocalPart().toLowerCase();
                    XMLEvent ef2 = (XMLEvent) in.next();
                    if(ef2.isEndElement()){
                        out.add(e);
                        out.add(ef2);
                        continue;
                    }else if(ef2.isCharacters()){
                    
                    String[] texts = ((Characters) ef2).getData().replace(" ", "").split(delimeter);

                    int count = 0;
                    out.add(e);
                    for (String text : texts) {

                        ef2 = ef.createStartElement("", "", elementName);

                        // ef2=ef.
                        out.add(ef2);

                        ef2 = ef.createCharacters(texts[count]);
                        out.add(ef2);

                        ef2 = ef.createEndElement("", "", elementName);
                        out.add(ef2);

//                     ef2= ef.createStartElement("object_broken","","");
//                    out.add(ef2);
//                     ef2 = ef.createCharacters("OLE2");
//
//                    out.add(ef2);
//                     ef2= ef.createEndElement("object_broken","","");
//                    out.add(ef2);
                        count++;
                    } }

                } else {
                    out.add(e);
                }
            }
            in.close();
            out.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

    }

    
    
    
    
    //added my MaTh
    
    public static void dissect2(String elementName, String delimeter, List inputs, StreamSource source, StreamResult result, XMLInputFactory ifactory, XMLOutputFactory ofactory) {

//        
        try {
            XMLEventReader in = ifactory.createXMLEventReader(source);
            XMLEventWriter out = ofactory.createXMLEventWriter(result);

            XMLEventFactory ef = XMLEventFactory.newInstance();

            while (in.hasNext()) {

                XMLEvent e = in.nextEvent();

                if (e.isStartElement() && (((StartElement) e).getName().getLocalPart().toLowerCase().equals(elementName))) {

                  //  String tocheck = e.asStartElement().getName().getLocalPart().toLowerCase();
                    XMLEvent ef2 = (XMLEvent) in.next();

                    if(ef2.isCharacters()){
                    
                    String[] texts = ((Characters) ef2).getData().split(delimeter);

                    int count = 0;
                    out.add(e);
                    for (String text : texts) {

                        ef2 = ef.createStartElement("", "", elementName);
                
                        out.add(ef2);

                        ef2 = ef.createCharacters(texts[count]);
                        out.add(ef2);

                        ef2 = ef.createEndElement("", "", elementName);
                        out.add(ef2);
                    
                        count++;
                    } }
                   

                } else {
                    out.add(e);
                }
            }
            in.close();
            out.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

    }
}
