import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class HtmlParser {

    private static Logger LOGGER;
    private static Scanner SCANNER;

    static {
        try {
            LogManager.getLogManager().readConfiguration(
                    HtmlParser.class.getResourceAsStream("/logging.properties"));
            LOGGER = Logger.getLogger(HtmlParser.class.getName());
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        SCANNER = new Scanner(System.in);
    }

    public static int findRecord(ArrayList<Record> records, String word) {
        int index = 0;

        for (Record record : records) {
            if (record.equals(word))
                return index;
            index++;
        }
        return 0;
    }

    public static String getUrl(String[] args) {
        String url;

        if (args.length >= 1)
            url = args[0];
        else {
            System.out.println("Enter web-page address:");
            url = SCANNER.nextLine();
        }
        return url;
    }

    public static String getSplitPattern() {
        List <String> delimiters = Arrays.asList(" ", ",", ".", "! ", "?", "\"", ":", ";", "[", "]", "(", ")", "\n", "\r", "\t");
        String splitPattern = "";
        boolean isDelimiterFirst = true;

        for (String delimiter : delimiters) {
            if (isDelimiterFirst) {
                splitPattern = splitPattern.concat(delimiter);
                isDelimiterFirst = false;
            } else
                splitPattern = splitPattern.concat("|\\" + delimiter);
        }
        return splitPattern;
    }

    public static Document getDocument(String url) {
        Document htmlDocument = null;

        try {
            htmlDocument = Jsoup.connect(url).userAgent("Chrome/4.0.249.0").get();
        } catch (IllegalArgumentException exception) {
            LOGGER.log(Level.WARNING, "IllegalArgumentException occurred", exception);
        } catch (IOException exception) {
            LOGGER.log(Level.WARNING, "IOException occurred", exception);
        }
        return htmlDocument;
    }

    public static ArrayList<Record> getRecords(Document htmlDocument) {
        String splitPattern = getSplitPattern();
        String[] words;
        ArrayList<Record> records = new ArrayList<Record>();
        Elements elements = htmlDocument.getAllElements();

        for (Element element : elements) {
            if (element.childNodeSize() == 1 && !element.siblingElements().isEmpty()) {
                words = element.text().split(splitPattern);
                for (String word : words) {
                    word = word.toUpperCase();
                    if (findRecord(records, word) == 0)
                        records.add(new Record(word));
                    else
                        records.get(findRecord(records, word)).incrementCounter();
                }
            }
        }
        return records;
    }

    public static void printRecords(ArrayList<Record> records) {
        for (Record record : records)
            System.out.println(record);
    }

    public static void main(String[] args) {
        String url = getUrl(args);
        Document htmlDocument = getDocument(url);
        ArrayList<Record> records = getRecords(htmlDocument);

        printRecords(records);
    }
}
