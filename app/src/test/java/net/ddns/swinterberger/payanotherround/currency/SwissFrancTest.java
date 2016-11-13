package net.ddns.swinterberger.payanotherround.currency;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the SwissFranc Currency.
 */
public class SwissFrancTest {

    private SwissFranc testee;

    @Before
    public void setUp() {
        testee = new SwissFranc();
        testee.setFranc(10);
        testee.setCentime(50);
    }

    @After
    public void tearDown() {
        this.testee = null;
    }

    @Test
    public void addFranc() throws Exception {
        testee.addFranc(5);
        assertEquals(15, testee.getFranc());
    }

    @Test
    public void subtractFranc() throws Exception {
        testee.subtractFranc(8);
        assertEquals(2, testee.getFranc());
    }

    @Test
    public void addCentimeWithoutOverflow() throws Exception {
        testee.addCentime(40);
        assertEquals(90, testee.getCentime());
    }

    @Test
    public void addCentimeWithOverflow() throws Exception {
        testee.addCentime(210);
        assertEquals(60, testee.getCentime());
        assertEquals(12, testee.getFranc());
    }


    @Test
    public void subtractCentimeWithoutOverflow() throws Exception {
        testee.subtractCentime(40);
        assertEquals(10, testee.getCentime());
    }

    @Test
    public void subtractCentimeWithOverflow() throws Exception {
        testee.subtractCentime(180);
        assertEquals(70, testee.getCentime());
        assertEquals(8, testee.getFranc());
    }

    @Test
    public void setAmount() throws Exception {
        testee.setAmount(5, 20);
        assertEquals(20, testee.getCentime());
        assertEquals(5, testee.getFranc());
    }

    @Test
    public void addAmount() throws Exception {
        testee.addAmount(5, 20);
        assertEquals(70, testee.getCentime());
        assertEquals(15, testee.getFranc());
    }

    @Test
    public void subtractAmount() throws Exception {
        testee.subtractAmount(4, 80);
        assertEquals(70, testee.getCentime());
        assertEquals(5, testee.getFranc());
    }

}