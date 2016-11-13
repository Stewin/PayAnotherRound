package net.ddns.swinterberger.payanotherround.currency;

/**
 * Interface representing a Currency.
 *
 * @author Stefan Winterberger
 * @version 1.0.0
 */
public interface Currency {

    void setAmount(int franc, int centime);

    void addAmount(int franc, int centime);

    void subtractAmount(int franc, int centime);
}
