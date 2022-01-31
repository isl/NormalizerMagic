package gr.forth.isl.normalizationmagic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/** Simple parsing and accessing changelog
 *
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
public class Changelog {
    private static final String CHANGELOG_FILE_RESOURCE_LOCATION="Changelog.md";
    
    public static String getFullVersionHistory() throws IOException{
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(Changelog.class.getClassLoader().getResourceAsStream(CHANGELOG_FILE_RESOURCE_LOCATION)));
        StringBuilder stringBuilder=new StringBuilder();
        String line;
        while((line=bufferedReader.readLine())!=null){
            stringBuilder.append(line).append("\n");
        }
        return stringBuilder.toString();
    }
    
    public static String getLastVersionChanges() throws IOException{
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(Changelog.class.getClassLoader().getResourceAsStream(CHANGELOG_FILE_RESOURCE_LOCATION)));
        StringBuilder stringBuilder=new StringBuilder();
        String line;
        int headerCounter=0;
        while((line=bufferedReader.readLine())!=null){
            if(line.startsWith("##")){
                headerCounter+=1;
            }
            if(headerCounter>1){
                break;
            }
            stringBuilder.append(line).append("\n");
        }
        return stringBuilder.toString();
    }
}
