import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class HtmlParser {

    public static void main(String[] args) {

        boolean isDelimiterFirst = true;
        List <String> delimiters = Arrays.asList(" ", ",", ".", "! ", "?", "\"", ":", ";", "[", "]", "(", ")", "\n", "\r", "\t");
        String splitPattern = "";
        for (String delimiter : delimiters)
        {
            if (isDelimiterFirst)
            {
                splitPattern = splitPattern + delimiter;
                isDelimiterFirst = false;
            }
            else
                splitPattern = splitPattern + "|\\" + delimiter;
        }
        System.out.println(splitPattern);

        /*
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter web-page address:");
        String url = scanner.nextLine();

        try {
            Document document = Jsoup.connect(url).userAgent("Chrome/4.0.249.0").get();
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
         */
        String HTMLString = "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<title>JSoup Example</title>"
                + "</head>"
                + "<body>"
                + "<table><tr><td><h1>HelloWorld</h1></tr>"
                + "</table>"
                + "<p>some text idk</p>"
                + "<div><p>more text</p>"
                + "<div><p>and more</p></div>"
                + "</div>"
                + "<a href=\"URL\">текст ссылки</a>"
                + "<button name=\"nada\">button</button>"
                + "</body>"
                + "</html>";



        Document htmlDocument = Jsoup.parse(HTMLString);
        Elements elements = htmlDocument.getAllElements();
        String[] words;
        for (Element element : elements)
        {
            if (element.childNodeSize() == 1 && !element.siblingElements().isEmpty())
            {
                System.out.println(element.text());
                words = element.text().split(splitPattern);
                for (String word : words)
                    System.out.print(word + "+");
                System.out.println("\n");
            }
        }

    }
}
