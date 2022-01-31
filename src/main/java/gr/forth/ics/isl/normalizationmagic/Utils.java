package gr.forth.ics.isl.normalizationmagic;

import javafx.util.Pair;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
public class Utils {
    public static Pair<String,String> getDissectionInfo(String dissectionRule){
        String elementName=dissectionRule.substring(dissectionRule.lastIndexOf(" ")+" ".length());
        String dissectText=dissectionRule.replace(elementName, "")
                                         .toLowerCase().replace("dissect", "")
                                                       .replace("split", "")
                                                       .replace("from", "").trim();
        Pair<String,String> dissectionPair=new Pair(elementName, dissectText);
        return dissectionPair;
    }
}
