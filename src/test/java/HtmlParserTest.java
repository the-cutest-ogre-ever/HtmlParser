import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class HtmlParserTest {

    private static String htmlText;
    private static ArrayList<Record> expectedRecords;

    @BeforeAll
    public static void setUp() {
        htmlText =  "<html>                                     " +
                    "   <head>                                  " +
                    "       <title href=\"hh.ru/vacancy?home\"> " +
                    "           Поиск вакансий                  " +
                    "       </title>                            " +
                    "   </head>                                 " +
                    "   <body>                                  " +
                    "       <div class=\"content\">             " +
                    "           <h1>Поиск вакансий</h1>         " +
                    "           <div>Вакансии дня</div>         " +
                    "           <div>Компании дня</div>         " +
                    "       </div>                              " +
                    "   </body>                                 " +
                    "</html>                                    ";
        expectedRecords = new ArrayList<>();
        expectedRecords.add(new Record("ПОИСК"));
        expectedRecords.add(new Record("ВАКАНСИЙ"));
        expectedRecords.add(new Record("ВАКАНСИИ"));
        expectedRecords.add(new Record("ДНЯ"));
        expectedRecords.get(3).incrementCounter();
        expectedRecords.add(new Record("КОМПАНИИ"));
    }

    @Test
    public void findRecordShouldReturnCorrectNumber() {
        Assertions.assertEquals(0, HtmlParser.findRecord(expectedRecords, "ПОИСК"));
        Assertions.assertEquals(4, HtmlParser.findRecord(expectedRecords, "КОМПАНИИ"));
        Assertions.assertEquals(0, HtmlParser.findRecord(expectedRecords, "СЛОВО"));
    }

    @Test
    public void getUrlShouldGetStringFromArgs() {
        String[] args = {"word1", "word2"};

        Assertions.assertEquals("word1", HtmlParser.getUrl(args));
    }

    @Test
    public void getSplitPatternShouldReturnCorrectPattern() {
        Assertions.assertEquals(" |\\,|\\.|\\! |\\?|\\\"|\\:|\\;|\\[|\\]|\\(|\\)|\\\n" +
                "|\\\r|\\\t", HtmlParser.getSplitPattern());
    }

    @Test
    public void getRecordShouldReturnCorrectArrayList() {
        Document htmlDocument = Jsoup.parse(htmlText, "hh.ru");
        ArrayList<Record> records = HtmlParser.getRecords(htmlDocument);

        if (expectedRecords.size() == records.size()) {
            for (int i = 0; i < records.size(); i++) {
                Assertions.assertTrue((expectedRecords.get(i)).equals(records.get(i)));
            }
        }
    }

    @Test
    public void getDocumentShouldReturnDocument() throws IOException {
        String url = "https://kazan.hh.ru";

        Assertions.assertNotNull(HtmlParser.getDocument(url));
    }

}