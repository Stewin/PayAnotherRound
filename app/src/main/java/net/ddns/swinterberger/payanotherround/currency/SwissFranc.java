package net.ddns.swinterberger.payanotherround.currency;

/**
 * Represents a Amount of SwissFrancs.
 *
 * @author Stefan Winterberger
 * @version 1.0.0
 */
public final class SwissFranc implements Currency {

    public static final String CURRENCY_ABREVIATION = "CHF";
    private int franc; //Franken
    private int centime; //Rappen

    public final int getFranc() {
        return franc;
    }

    final void setFranc(final int franc) {
        this.franc = franc;
    }

    final void addFranc(final int franc) {
        this.franc += franc;
    }

    final void subtractFranc(final int franc) {
        this.franc -= franc;
    }

    public int getCentime() {
        return centime;
    }

    void setCentime(int centime) {
        this.centime = centime;
    }

    final void addCentime(final int centime) {
        this.centime += centime;
        normalizeCentime();
    }

    final void subtractCentime(final int centime) {
        this.centime -= centime;
        normalizeCentime();
    }

    @Override
    public final void setAmount(final int franc, final int centime) {
        setFranc(franc);
        setCentime(centime);
    }

    @Override
    public final void addAmount(final int franc, final int centime) {
        addFranc(franc);
        addCentime(centime);
    }

    @Override
    public final void subtractAmount(final int franc, final int centime) {
        subtractFranc(franc);
        subtractCentime(centime);
    }

    @Override
    public void divideByUsers(int numberOsers) {

    }

    private void normalizeCentime() {

        //Zehner Stelle
        int seconddigit = (this.centime / 10) % 10;

        //Einer Stelle
        int firstDigit = this.centime % 10;

        if (this.centime >= 100) {
            this.franc += this.centime / 100;
            this.centime = seconddigit * 10 + firstDigit;
        } else if (this.centime < 0) {
            int i = (this.centime / 100) % 10;
            this.franc += i - 1;
            this.centime = 100 + (seconddigit * 10 + firstDigit);
        }
    }

    private void roundCentime() {
        //TODO: Implement Round Centime.
    }
}
