import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

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
        expectedRecords.get(0).incrementCounter();
        expectedRecords.add(new Record("ВАКАНСИЙ"));
        expectedRecords.get(1).incrementCounter();
        expectedRecords.add(new Record("ВАКАНСИИ"));
        expectedRecords.add(new Record("КОМПАНИИ"));
        expectedRecords.add(new Record("ДНЯ"));
        expectedRecords.get(4).incrementCounter();

        for (Record record : expectedRecords)
            System.out.println(record);
    }

    @Test
    public void findRecordShouldReturnCorrectNumber() {
        Assert.assertEquals(0, HtmlParser.findRecord(expectedRecords, "ПОИСК"));
        Assert.assertEquals(3, HtmlParser.findRecord(expectedRecords, "КОМПАНИИ"));
        Assert.assertEquals(0, HtmlParser.findRecord(expectedRecords, "СЛОВО"));
    }
}