/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.forth.isl.normalizationmagic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.util.Pair;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * @author minadakn
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
public class StAXModifyDemo {
    private static final Logger logger=Logger.getLogger(StAXModifyDemo.class);

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
                System.out.println("Skipping rule "+line);
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
        
        try{
            result.getWriter().flush();
            result.getWriter().close();
            intermediateInput.getReader().close();
            beautifyOutputResult(result);
            Files.delete(Paths.get("input_intermediate.xml"));
        }catch(IOException | ParserConfigurationException | SAXException | TransformerException ex){
            ex.printStackTrace();
        }
        
    }
    
    private static void beautifyOutputResult(StreamResult resulta) throws TransformerConfigurationException, ParserConfigurationException, IOException, SAXException, TransformerException{
        Document doc=DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new FileInputStream(new File("output.xml")), "UTF-8");
        
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        
        StreamResult finalResult = new StreamResult(new File("output.xml"));
        DOMSource source = new DOMSource(doc);
        transformer.transform(source, finalResult);
    }
}
