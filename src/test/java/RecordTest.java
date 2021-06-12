import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTest {

    private Record expectedRecord;

    @Before
    public void setUp() {
        expectedRecord = new Record("word");
    }

    @Test
    public void constructorShouldCreate() {
        Record record = new Record("word");
        Assert.assertNotNull(record);
    }

    @Test
    public void equalsShouldReturnTrue() {
        Assert.assertTrue(expectedRecord.equals("word"));
        Assert.assertTrue(expectedRecord.equals(new Record("word")));
    }

    @Test
    public void equalsShouldReturnFalse() {
        Record record = new Record("another word");
        record.incrementCounter();
        Assert.assertFalse(expectedRecord.equals(record));
    }

    @Test
    public void incrementCounterShouldIncrement() {
        expectedRecord.incrementCounter();
        Assert.assertEquals(2, expectedRecord.getCounter());
    }

    @Test
    public void testToString() {
        Assert.assertTrue(expectedRecord.toString().equals("word - 1"));
    }
}