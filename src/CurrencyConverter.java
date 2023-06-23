import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class CurrencyConverter {
    private double exchangeRate;

    public CurrencyConverter() {
        loadExchangeRate();
    }

    private void loadExchangeRate() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
            String exchangeRateStr = properties.getProperty("exchangeRate");
            exchangeRate = Double.parseDouble(exchangeRateStr);
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке файла конфигурации: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Ошибка при чтении курса валюты: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public double toRubles(double dollars) {
        return dollars * exchangeRate;
    }

    public double toDollars(double rubles) {
        return rubles / exchangeRate;
    }
}
