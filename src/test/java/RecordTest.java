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
    public void incrementCounterShouldIncrement() {
        expectedRecord.incrementCounter();
        Assert.assertEquals(2, expectedRecord.getCounter());
    }

    @Test
    public void equalsShouldReturnTrue() {
        Assert.assertTrue(expectedRecord.equals("word"));
    }

    @Test
    public void equalsShouldReturnFalse() {
        Assert.assertFalse(expectedRecord.equals("another word"));
    }

    @Test
    public void testToString() {
        Assert.assertTrue(expectedRecord.toString().equals("word - 1"));
    }
}