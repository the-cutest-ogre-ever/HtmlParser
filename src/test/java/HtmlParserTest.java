import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import static org.junit.Assert.*;

public class HtmlParserTest {

    private String htmlText;
    private ArrayList<Record> expectedRecords;

    @Before
    public void setUp() {
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
        expectedRecords = new ArrayList<Record>();
        expectedRecords.add(new Record("ПОИСК"));
        expectedRecords.add(new Record("ВАКАНСИЙ"));
        expectedRecords.add(new Record("ВАКАНСИИ"));
        expectedRecords.add(new Record("ДНЯ"));
        expectedRecords.get(3).incrementCounter();
        expectedRecords.add(new Record("КОМПАНИИ"));
    }

    @Test
    public void findRecordShouldReturnCorrectNumber() {
        Assert.assertEquals(0, HtmlParser.findRecord(expectedRecords, "ПОИСК"));
        Assert.assertEquals(4, HtmlParser.findRecord(expectedRecords, "КОМПАНИИ"));
        Assert.assertEquals(0, HtmlParser.findRecord(expectedRecords, "СЛОВО"));
    }

    @Test
    public void getUrlShouldGetStringFromArgs() {
        String[] args = {"word1", "word2"};

        Assert.assertEquals("word1", HtmlParser.getUrl(args));
    }

    @Test
    public void getSplitPatternShouldReturnCorrectPattern() {
        Assert.assertEquals(" |\\,|\\.|\\! |\\?|\\\"|\\:|\\;|\\[|\\]|\\(|\\)|\\\n" +
                "|\\\r|\\\t", HtmlParser.getSplitPattern());
    }

    @Test
    public void getRecordShouldReturnCorrectArrayList() {
        Document htmlDocument = Jsoup.parse(htmlText, "hh.ru");
        ArrayList<Record> records = HtmlParser.getRecords(htmlDocument);

        if (expectedRecords.size() == records.size()) {
            for (int i = 0; i < records.size(); i++) {
                Assert.assertTrue((expectedRecords.get(i)).equals(records.get(i)));
            }
        }
    }

    @Test
    public void getDocumentShouldReturnDocument() {
        String url = "https://kazan.hh.ru";

        Assert.assertNotNull(HtmlParser.getDocument(url));
    }

}