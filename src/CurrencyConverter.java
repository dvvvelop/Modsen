import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Properties;

public class CurrencyConverter {
    private static final String CONFIG_FILE = "config.properties";
    public static final String DOLLAR_SYMBOL = "$";
    public static final String RUBLE_SYMBOL = "р";

    private BigDecimal exchangeRate;

    public CurrencyConverter() {
        loadExchangeRate();
    }

    private void loadExchangeRate() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            properties.load(fis);
            exchangeRate = new BigDecimal(properties.getProperty("exchange_rate"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BigDecimal toDollars(BigDecimal rubles) {
        return rubles.divide(exchangeRate, 2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal toRubles(BigDecimal dollars) {
        return dollars.multiply(exchangeRate).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal parseAmount(String amountString) {
        if (amountString.contains(DOLLAR_SYMBOL)) {
            return new BigDecimal(amountString.replace(DOLLAR_SYMBOL, "").replace(",", "."));
        } else if (amountString.contains(RUBLE_SYMBOL)) {
            return new BigDecimal(amountString.replace(RUBLE_SYMBOL, "").replace(",", "."));
        } else {
            throw new IllegalArgumentException("Invalid amount format.");
        }
    }
}
