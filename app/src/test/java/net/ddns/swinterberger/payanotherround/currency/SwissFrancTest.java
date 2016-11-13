package net.ddns.swinterberger.payanotherround.currency;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the SwissFranc Currency.
 */
public class SwissFrancTest {

    private Currency testee;

    @Before
    public void setUp() {
        testee = new SwissFranc();
    }

    @After
    public void tearDown() {
        this.testee = null;
    }

    @Test
    public void setAmount() throws Exception {
        testee.setAmount(520);
        assertEquals(520, testee.getAmountInCent());
    }

    @Test
    public void addAmountPositive() throws Exception {
        testee.addAmount(560);
        assertEquals(560, testee.getAmountInCent());
    }

    @Test
    public void addAmountNegative() throws Exception {
        testee.setAmount(-550);
        testee.addAmount(620);
        assertEquals(70, testee.getAmountInCent());
    }

    @Test
    public void subtractAmountPositive() throws Exception {
        testee.setAmount(550);
        testee.subtractAmount(480);
        assertEquals(70, testee.getAmountInCent());
    }

    @Test
    public void subtractAmountNegativ() throws Exception {
        testee.setAmount(-430);
        testee.subtractAmount(280);
        assertEquals(-710, testee.getAmountInCent());
    }

    @Test
    public void checkCurrencyAbreviation() {
        assertEquals("CHF", testee.getCurrencyAbbreviation());
    }

    @Test
    public void testDivideByUsersWith2Users() {
        testee.setAmount(500);
        testee.divideByUsers(2);
        assertEquals(250, testee.getAmountInCent());
    }

    @Test
    public void testDivideByUsersWith3Users() {
        testee.setAmount(1255);
        testee.divideByUsers(4);
        assertEquals(313, testee.getAmountInCent());
    }
}