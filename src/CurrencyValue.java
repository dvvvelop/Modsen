import java.text.DecimalFormat;

public class CurrencyValue {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.00");

    private String currency;
    private double value;

    public CurrencyValue(String currency, double value) {
        this.currency = currency;
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String format() {
        return currency + DECIMAL_FORMAT.format(value);
    }
}
