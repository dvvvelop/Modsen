import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class CurrencyConverter {
    private static final String CONFIG_FILE_PATH = "config.properties";
    private static final String EXCHANGE_RATE_KEY = "exchangeRate";

    public static double toRubles(double dollars) {
        double exchangeRate = getExchangeRate();
        return round(dollars * exchangeRate);
    }

    public static double toDollars(double rubles) {
        double exchangeRate = getExchangeRate();
        return round(rubles / exchangeRate);
    }

    private static double getExchangeRate() {
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(fileInputStream);
            String exchangeRateString = properties.getProperty(EXCHANGE_RATE_KEY);
            return Double.parseDouble(exchangeRateString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
