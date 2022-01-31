package gr.forth.ics.isl.normalizationmagic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.FactoryConfigurationError;
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
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * @author minadakn
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
public class Normalizer {
    private static final Logger log=LogManager.getLogger(Normalizer.class);
    private final File inputFile;
    private final File outputFile;
    private final File rulesFile;
    
    public Normalizer(String inputFilePath, String rulesFilePath, String outputFilePath){
        log.debug("Initializing Normalizer with input file path: "+inputFilePath+" output file path: "+outputFilePath+" and rules file path: "+rulesFilePath);
        this.inputFile=new File(inputFilePath);
        this.outputFile=new File(outputFilePath);
        this.rulesFile=new File(rulesFilePath);
        if(!Files.exists(Paths.get(inputFile.getAbsolutePath())) || !Files.isRegularFile(Paths.get(inputFile.getAbsolutePath()))){
            log.error("Cannot find the XML input file with path: "+inputFilePath);
        }
        if(!Files.exists(Paths.get(rulesFile.getAbsolutePath())) || !Files.isRegularFile(Paths.get(rulesFile.getAbsolutePath()))){
            log.error("Cannot find the TXT rules file with path: "+rulesFilePath);
        }
    }

    public void normalize(){
        try{
            XMLInputFactory ifactory = XMLInputFactory.newFactory();
            XMLOutputFactory ofactory = XMLOutputFactory.newFactory();
            StreamSource source=new StreamSource(new InputStreamReader(new FileInputStream(this.inputFile),"UTF-8"));
            StreamResult result=new StreamResult(new OutputStreamWriter(new FileOutputStream(this.outputFile, false),"UTF-8"));

            StreamSource intermediateInput=null;

            List<String> replaceInputs = new ArrayList();
            List<String> removeInputs = new ArrayList();
            List<String> removeBetweenInputs = new ArrayList();
            List<String> dissectInputs = new ArrayList();

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.rulesFile), "UTF-8"));

            String line = reader.readLine();
            while (line != null) {
                if(line.toLowerCase().startsWith(Resources.REPLACE) || line.toLowerCase().startsWith(Resources.CHANGE)){
                    replaceInputs.add(line);
                }else if(line.toLowerCase().startsWith(Resources.REMOVE) || line.toLowerCase().startsWith(Resources.DELETE)){
                    if(line.toLowerCase().contains(" "+Resources.BETWEEN+" ")){
                        removeBetweenInputs.add(line);
                    }else{
                        removeInputs.add(line);
                    }

                }else if(line.toLowerCase().startsWith(Resources.DISSECT) || line.toLowerCase().startsWith(Resources.SPLIT)){
                    dissectInputs.add(line);
                }else if(line.trim().isEmpty()){
                    log.debug("skipping empty line");
                }else if(line.trim().startsWith("#")){
                    log.debug("skipping commented rule "+line);
                }else{
                    log.warn("skipping unknown rule");
                }
                line = reader.readLine();
            }
            reader.close();
            int i=0;
            for(String replaceInput : replaceInputs){
                log.info("Applying REPLACE rule : "+replaceInput);
                result=new StreamResult(new OutputStreamWriter(new FileOutputStream(this.outputFile, false),"UTF-8"));
                if(intermediateInput==null){
                    Actions.replace(Arrays.asList(replaceInput), source, result, ifactory, ofactory);
                }else{
                    Actions.replace(Arrays.asList(replaceInput), intermediateInput, result, ifactory, ofactory);
                }
                ifactory= XMLInputFactory.newFactory();
                ofactory = XMLOutputFactory.newFactory();
                Files.copy(Paths.get(this.outputFile.getAbsolutePath()), Paths.get(Resources.INTERMEDIATE_FILE_NAME), StandardCopyOption.REPLACE_EXISTING);
                intermediateInput=new StreamSource(new InputStreamReader(new FileInputStream(Resources.INTERMEDIATE_FILE_NAME),"UTF-8"));           
            }

            for(String removeInput : removeBetweenInputs){
                log.info("Applying REMOVE BETWEEN rule : "+removeInput);
                result=new StreamResult(new OutputStreamWriter(new FileOutputStream(this.outputFile, false),"UTF-8"));
                if(intermediateInput==null){
                    Actions.remove_between(Arrays.asList(removeInput), source, result, ifactory, ofactory);
                }else{
                    Actions.remove_between(Arrays.asList(removeInput), intermediateInput, result, ifactory, ofactory);
                }
                ifactory= XMLInputFactory.newFactory();
                ofactory = XMLOutputFactory.newFactory();
                Files.copy(Paths.get(this.outputFile.getAbsolutePath()), Paths.get(Resources.INTERMEDIATE_FILE_NAME), StandardCopyOption.REPLACE_EXISTING);
                intermediateInput=new StreamSource(new InputStreamReader(new FileInputStream(Resources.INTERMEDIATE_FILE_NAME),"UTF-8"));
            }

            for(String removeInput : removeInputs){
                log.info("Applying REMOVE rule : "+removeInput);
                result=new StreamResult(new OutputStreamWriter(new FileOutputStream(this.outputFile, false),"UTF-8"));
                if(intermediateInput==null){
                    Actions.remove(Arrays.asList(removeInput), source, result, ifactory, ofactory);
                }else{
                    Actions.remove(Arrays.asList(removeInput), intermediateInput, result, ifactory, ofactory);
                }
                ifactory= XMLInputFactory.newFactory();
                ofactory = XMLOutputFactory.newFactory();
                Files.copy(Paths.get(this.outputFile.getAbsolutePath()), Paths.get(Resources.INTERMEDIATE_FILE_NAME), StandardCopyOption.REPLACE_EXISTING);
                intermediateInput=new StreamSource(new InputStreamReader(new FileInputStream(Resources.INTERMEDIATE_FILE_NAME),"UTF-8"));
            }

            for(String dissectLine : dissectInputs){
                log.info("Applying DISSECT rule : "+dissectLine);
                result=new StreamResult(new OutputStreamWriter(new FileOutputStream(this.outputFile, false),"UTF-8"));
                Pair<String,String> dissectPair=Utils.getDissectionInfo(dissectLine.trim());
                if(intermediateInput==null){
                    Actions.dissect(dissectPair.getKey(), dissectPair.getValue(), source, result, ifactory, ofactory);
                }else{
                    Actions.dissect(dissectPair.getKey(), dissectPair.getValue(), intermediateInput, result, ifactory, ofactory);
                }
                ifactory= XMLInputFactory.newFactory();
                ofactory = XMLOutputFactory.newFactory();
                Files.copy(Paths.get(this.outputFile.getAbsolutePath()), Paths.get(Resources.INTERMEDIATE_FILE_NAME), StandardCopyOption.REPLACE_EXISTING);
                intermediateInput=new StreamSource(new InputStreamReader(new FileInputStream(Resources.INTERMEDIATE_FILE_NAME),"UTF-8"));   
            }
            result.getWriter().flush();
            result.getWriter().close();
            intermediateInput.getReader().close();
            
            beautifyOutputResult(this.outputFile);
            Files.delete(Paths.get(Resources.INTERMEDIATE_FILE_NAME));
        }catch(FactoryConfigurationError ex){
            log.error("An error occured while initiating the normalizer",ex);
        }catch(IOException ex){
            log.error("An error occured while normalizing resources",ex);
        }catch(ParserConfigurationException | SAXException | TransformerException ex){
            log.error("An error occured during the final formatting of the normalized resources",ex);
        }
    }
    
    private void beautifyOutputResult(File file) throws ParserConfigurationException, SAXException, FileNotFoundException, IOException, TransformerConfigurationException, TransformerException{
        Document doc=DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new FileInputStream(file), "UTF-8");
        
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        
        StreamResult finalResult = new StreamResult(file);
        DOMSource source = new DOMSource(doc);
        transformer.transform(source, finalResult);
    }
    
    public static void main(String[] args){
        String sampleInput="examples/input.xml";
        String sampleRules="examples/rules.txt";
        String producedOutput="examples/output.xml";
        new Normalizer(sampleInput, sampleRules, producedOutput).normalize();
    }
}
