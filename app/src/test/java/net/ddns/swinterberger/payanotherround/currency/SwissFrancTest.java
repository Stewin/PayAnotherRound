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
        testee.setIntegerPart(10);
        testee.setDecimalPart(50);
    }

    @After
    public void tearDown() {
        this.testee = null;
    }

    @Test
    public void setAmount() throws Exception {
        testee.setAmount(5, 20);
        assertEquals(20, testee.getDecimalPart());
        assertEquals(5, testee.getIntPart());
    }

    @Test
    public void addAmount() throws Exception {
        testee.addAmount(5, 20);
        assertEquals(70, testee.getDecimalPart());
        assertEquals(15, testee.getIntPart());
    }

    @Test
    public void subtractAmount() throws Exception {
        testee.subtractAmount(4, 80);
        assertEquals(70, testee.getDecimalPart());
        assertEquals(5, testee.getIntPart());
    }

    @Test
    public void checkCurrencyAbreviation() {
        assertEquals("CHF", testee.getCurrencyAbbreviation());
    }

}