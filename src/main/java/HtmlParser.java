import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.sql.*;

public class HtmlParser {

    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/parser";

    private static final String USER = "postgres";
    private static final String PASSWORD = "shrekislove";

    private static Connection connection;
    private static Statement statement;

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
    }

    static {
        SCANNER = new Scanner(System.in);
    }

    private static int findRecord(ArrayList<Record> records, String word) {
        int index = 0;

        for (Record record : records) {
            if (record.equals(word))
                return index;
            index++;
        }
        return 0;
    }

    private static String getUrl(String[] args) {
        String url;

        if (args.length == 1)
            url = args[0];
        else {
            System.out.println("Enter web-page address:");
            url = SCANNER.nextLine();
        }
        return url;
    }

    private static String getSplitPattern() {
        List <String> delimiters = Arrays.asList(" ", ",", ".", "! ", "?", "\"", ":", ";", "[", "]", "(", ")", "\n", "\r", "\t");
        String splitPattern = "";
        boolean isDelimiterFirst = true;

        System.out.println("Current delimiters: " + delimiters);
        for (String delimiter : delimiters) {
            if (isDelimiterFirst) {
                splitPattern = splitPattern.concat(delimiter);
                isDelimiterFirst = false;
            } else
                splitPattern = splitPattern.concat("|\\" + delimiter);
        }
        return splitPattern;
    }

    private static void connectDatabase() {
        try {
            Class.forName("org.postgresql.Driver");

            connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            statement = connection.createStatement();
        }
        catch (ClassNotFoundException exception) {
            LOGGER.log(Level.WARNING, "ClassNotFoundException occurred", exception);
        }
        catch (SQLException exception) {
            LOGGER.log(Level.WARNING, "SQLException occurred", exception);
        }
        catch (Exception exception) {
            LOGGER.log(Level.WARNING, "Exception occurred", exception);
        }
    }

    private static void setUpDataTable() {
        connectDatabase();

        String sqlRequest = "CREATE TABLE records (" +
                "               word VARCHAR(100) NOT NULL," +
                "               counter INT NOT NULL," +
                "               PRIMARY KEY (word)" +
                "            );";
        try  {
            statement.execute(sqlRequest);
        }
        catch (SQLException exception) {
            LOGGER.log(Level.WARNING, "SQLException occurred", exception);
        }
        catch (Exception exception) {
            LOGGER.log(Level.WARNING, "Exception occurred", exception);
        }
    }

    private static void closeDatabaseConnection() {
        String sqlRequest = "DROP TABLE records;";
        try  {
            statement.execute(sqlRequest);

            statement.close();
            connection.close();
        }
        catch (SQLException exception) {
            LOGGER.log(Level.WARNING, "SQLException occurred", exception);
        }
        catch (Exception exception) {
            LOGGER.log(Level.WARNING, "Exception occurred", exception);
        }
    }

    private static void doSomeStuff() {
        ResultSet resultSet;
        String sql1 = "INSERT INTO records (word, counter) VALUES ('word', '100');";
        String sql2 = "INSERT INTO records (word, counter) VALUES ('word2', '1002');";
        String sql3 = "SELECT * FROM records";

        try {
            resultSet = statement.executeQuery(sql1);
            resultSet = statement.executeQuery(sql2);
            resultSet = statement.executeQuery(sql3);

            System.out.println("SQL RESULT:");
            while (resultSet.next()) {
                String word = resultSet.getString("word");
                int counter = resultSet.getInt("counter");

                System.out.println(word + " " + counter);
            }

            resultSet.close();
        }
        catch (SQLException exception) {
            LOGGER.log(Level.WARNING, "SQLException occurred", exception);
        }
        catch (Exception exception) {
            LOGGER.log(Level.WARNING, "Exception occurred", exception);
        }

    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException{

        setUpDataTable();
        doSomeStuff();
        closeDatabaseConnection();

        String url = getUrl(args);

        Document htmlDocument = null;
        String splitPattern = getSplitPattern();

        try {
            ArrayList<Record> records = new ArrayList<Record>();
            String[] words;
            htmlDocument = Jsoup.connect(url).userAgent("Chrome/4.0.249.0").get();
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
