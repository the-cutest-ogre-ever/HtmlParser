import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class RecordTest {

    private static Record expectedRecord;

    @BeforeAll
    public static void setUp() {
        expectedRecord = new Record("word");
    }

    @Test
    public void constructorShouldCreate() {
        Record record = new Record("word");
        Assertions.assertNotNull(record);
    }

    @Test
    public void equalsShouldReturnTrue() {
        Record record = new Record("word");

        Assertions.assertTrue(record.equals("word"));
        Assertions.assertTrue(record.equals(new Record("word")));
    }

    @Test
    public void equalsShouldReturnFalse() {
        Record record = new Record("another word");
        record.incrementCounter();
        Assertions.assertFalse(expectedRecord.equals(record));
    }

    @Test
    public void incrementCounterShouldIncrement() {
        expectedRecord.incrementCounter();
        Assertions.assertEquals(2, expectedRecord.getCounter());
    }

    @Test
    public void testToString() {
        Assertions.assertEquals(expectedRecord.toString(), "word - 1");
    }
}