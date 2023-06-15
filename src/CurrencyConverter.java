import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CurrencyConverter {
    private Map<String, Double> rates;

    public CurrencyConverter(String configFile) {
        rates = loadRates(configFile);
    }

    private Map<String, Double> loadRates(String configFile) {
        Map<String, Double> rates = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String currency = parts[0].trim();
                    double rate = Double.parseDouble(parts[1].trim());
                    rates.put(currency, rate);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rates;
    }

    public double convertTo(String currency, double amount) {
        if (rates.containsKey(currency)) {
            double rate = rates.get(currency);
            return amount / rate;
        }
        return 0;
    }

    public double convertFrom(String currency, double amount) {
        if (rates.containsKey(currency)) {
            double rate = rates.get(currency);
            return amount * rate;
        }
        return 0;
    }
}
