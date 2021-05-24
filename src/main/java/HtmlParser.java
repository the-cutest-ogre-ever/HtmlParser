import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class HtmlParser {

    private static Logger LOGGER;

   /* static {
        try (FileInputStream inputStream = new FileInputStream("/logging.properties")) {
            LogManager.getLogManager().readConfiguration(inputStream);
            LOGGER = Logger.getLogger(HtmlParser.class.getName());
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }*/

    static {
        try {
            LogManager.getLogManager().readConfiguration(
                    HtmlParser.class.getResourceAsStream("/logging.properties")
            );
            LOGGER = Logger.getLogger(HtmlParser.class.getName());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static int findRecord(ArrayList<Record> records, String word)
    {
        int index = 0;
        for (Record record : records)
        {
            if (record.equals(word))
                return index;
            index++;
        }
        return 0;
    }

    public static void main(String[] args) {

        boolean isDelimiterFirst = true;
        List <String> delimiters = Arrays.asList(" ", ",", ".", "! ", "?", "\"", ":", ";", "[", "]", "(", ")", "\n", "\r", "\t");
        String splitPattern = "";
        for (String delimiter : delimiters)
        {
            if (isDelimiterFirst)
            {
                splitPattern = splitPattern.concat(delimiter);
                isDelimiterFirst = false;
            }
            else
                splitPattern = splitPattern.concat("|\\" + delimiter);
        }
        System.out.println(splitPattern);

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter web-page address:");
        String url = scanner.nextLine();
        Document htmlDocument;
        try {
            htmlDocument = Jsoup.connect(url).userAgent("Chrome/4.0.249.0").get();
            ArrayList<Record> records = new ArrayList<Record>();
            Elements elements = htmlDocument.getAllElements();
            String[] words;
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
            for (Record record : records)
                System.out.println(record);
        }
        catch (IOException exception) {
            LOGGER.log(Level.WARNING, "IOException occurred", exception);
        }
        catch (Exception exception) {
            LOGGER.log(Level.WARNING, "Exception occurred", exception);
        }
    }
}
