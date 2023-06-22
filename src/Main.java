import java.math.BigDecimal;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите выражение: ");
        String expression = scanner.nextLine();

        CurrencyConverter converter = new CurrencyConverter();
        Calculator calculator = new Calculator(converter);

        try {
            BigDecimal result = calculator.evaluateExpression(expression);
            System.out.println("Результат: $" + result);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}

class CurrencyConverter {
    private static final String CONFIG_FILE = "config.properties";
    public static final String DOLLAR_SYMBOL = "$";
    public static final String RUBLE_SYMBOL = "р";

    private BigDecimal exchangeRate;

    public CurrencyConverter() {
        loadExchangeRate();
    }

    private void loadExchangeRate() {
        // Здесь код загрузки обменного курса из файла конфигурации
        // Оставим заглушку для примера
        exchangeRate = new BigDecimal("0.85");
    }

    public BigDecimal convertToDollars(BigDecimal rubles) {
        return rubles.divide(exchangeRate, 2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal convertToRubles(BigDecimal dollars) {
        return dollars.multiply(exchangeRate).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}

class Calculator {
    private CurrencyConverter converter;

    public Calculator(CurrencyConverter converter) {
        this.converter = converter;
    }

    public BigDecimal evaluateExpression(String expression) {
        expression = expression.replaceAll("\\s", ""); // Удаление пробелов

        if (expression.startsWith("toDollars(") && expression.endsWith(")")) {
            String amountStr = expression.substring(10, expression.length() - 1);
            BigDecimal amount = parseCurrencyValue(amountStr);
            return converter.convertToDollars(amount);
        } else if (expression.startsWith("toRubles(") && expression.endsWith(")")) {
            String amountStr = expression.substring(9, expression.length() - 1);
            BigDecimal amount = parseCurrencyValue(amountStr);
            return converter.convertToRubles(amount);
        } else {
            throw new IllegalArgumentException("Invalid expression format.");
        }
    }

    private BigDecimal parseCurrencyValue(String valueStr) {
        if (valueStr.startsWith(CurrencyConverter.DOLLAR_SYMBOL)) {
            valueStr = valueStr.substring(1);
            return new BigDecimal(valueStr);
        } else if (valueStr.endsWith(CurrencyConverter.RUBLE_SYMBOL)) {
            valueStr = valueStr.substring(0, valueStr.length() - 1);
            return new BigDecimal(valueStr);
        } else {
            throw new IllegalArgumentException("Invalid currency value format.");
        }
    }
}
