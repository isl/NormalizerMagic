package gr.forth.ics.isl.normalizationmagic;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** ConsoleRunner for Magic Normalizer
 *
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
public class ConsoleRunner {
    private static final Logger log=LogManager.getLogger(ConsoleRunner.class);
    static final CommandLineParser PARSER = new DefaultParser();
    static final HelpFormatter HELP = new HelpFormatter();
    static Options options = new Options();
    public static String MagicNormalizerVersion;
    
    private static void runFromConsole(String[] arguments){
        loadInfoAboutMagicNormalizer();
        createOptionsList();
        try{
            if(checkForVersionReporting(arguments)){
                log.info(MagicNormalizerVersion);
                System.exit(0);
            }
            if(checkForChangelogReporting(arguments)){
                log.info(Changelog.getFullVersionHistory());
                System.exit(0);
            }
            CommandLine cli = PARSER.parse(options, arguments);
        }catch(ParseException ex){
            log.error("Unable to parse parameters."+ex);
        }catch(IOException ex){
            log.error("Unable to retrieve version history"+ex);
        }
    }
    
    private static void createOptionsList() {
        Option inputFileOption = new Option(Resources.INPUT_FILE_SHORT_CLI, Resources.INPUT_FILE_CLI, true, "The original XML file to be normalized");
        inputFileOption.setRequired(true);
        Option rulesFileOption = new Option(Resources.RULES_FILE_SHORT_CLI, Resources.RULES_FILE_CLI, true, "The TXT file with the magic normalizer rules");
        rulesFileOption.setRequired(true);
        Option outputFileOption = new Option(Resources.OUTPUT_FILE_SHORT_CLI, Resources.OUTPUT_FILE_CLI, true, "The normalized XML file");
        Option versionOption = new Option(Resources.VERSION_SHORT_CLI, Resources.VERSION_CLI, false, "Reports the MagicNormalizer version");
        Option historyOption = new Option(Resources.HISTORY_SHORT_CLI, Resources.HISTORY_CLI, false, "Reports the full version history of MagicNormalizer");

        options.addOption(inputFileOption)
                .addOption(rulesFileOption)
                .addOption(outputFileOption)
                .addOption(versionOption)
                .addOption(historyOption);
    }
    
    private static void error(String message) {
        HELP.setDescPadding(5);
        HELP.setLeftPadding(5);
        HELP.printHelp(
                200,
                "java -jar MagicNormalizer.jar -i <input_file> -r <rules_file> -o <output_file>",
                "Options",
                options,
                message
        );
    }
    
    private static boolean checkForVersionReporting(String[] args) throws org.apache.commons.cli.ParseException{
        List<String> arguments=Arrays.asList(args);
        return arguments.contains("-"+Resources.VERSION_SHORT_CLI) || arguments.contains("--"+Resources.VERSION_CLI);
    }
    
    private static boolean checkForChangelogReporting(String[] args) throws org.apache.commons.cli.ParseException{
        List<String> arguments=Arrays.asList(args);
        return arguments.contains("-"+Resources.HISTORY_SHORT_CLI) || arguments.contains("--"+Resources.HISTORY_CLI);
    }
    
    private static void loadInfoAboutMagicNormalizer(){
        try{
            Properties propertiesFile=new Properties();
            propertiesFile.load(ConsoleRunner.class.getClassLoader().getResourceAsStream(Resources.MAGIC_NORMALIZER_PROPERTIES_FILE));
//            String artifactId=propertiesFile.getProperty(Resources.MAGIC_NORMALIZER_NAME_PROPERTY);
//            String version=propertiesFile.getProperty(Resources.MAGIC_NORMALIZER_VERSION_PROPERTY);
            MagicNormalizerVersion=propertiesFile.getProperty(Resources.MAGIC_NORMALIZER_NAME_PROPERTY)+" Version: "+propertiesFile.getProperty(Resources.MAGIC_NORMALIZER_VERSION_PROPERTY);
        }catch(IOException ex){
            log.error("An error occured while retrieving project-related information",ex);
        }
    }
    
    public static void main(String[] args){
        runFromConsole(args);
    }
}