import java.util.Scanner;

public class CurrencyCalculator {
    private CurrencyConverter currencyConverter;

    public CurrencyCalculator() {
        currencyConverter = new CurrencyConverter();
    }

    public void run() {
        System.out.println("Добро пожаловать в Currency Calculator!");
        System.out.println("Введите операцию:");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        String result = evaluateExpression(input);
        System.out.println("Результат: " + result);
    }

    private String evaluateExpression(String input) {
        input = input.replaceAll("\\s", ""); // Remove whitespace

        if (input.startsWith("toRubles(") && input.endsWith(")")) {
            String value = input.substring(9, input.length() - 1);
            return convertToRubles(value);
        } else if (input.startsWith("toDollars(") && input.endsWith(")")) {
            String value = input.substring(10, input.length() - 1);
            return convertToDollars(value);
        } else {
            return performOperation(input);
        }
    }

    private String convertToRubles(String value) {
        Currency dollars = parseCurrency(value);
        double rubles = currencyConverter.toRubles(dollars.getAmount());
        return new Currency(rubles, "p").toString();
    }

    private String convertToDollars(String value) {
        Currency rubles = parseCurrency(value);
        double dollars = currencyConverter.toDollars(rubles.getAmount());
        return new Currency(dollars, "$").toString();
    }


    private String performOperation(String input) {
        String[] parts = input.split("[+-]");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Некорректная операция");
        }

        Currency operand1 = parseCurrency(parts[0]);
        Currency operand2 = parseCurrency(parts[1]);

        Currency result;
        if (input.contains("+")) {
            result = operand1.add(operand2);
        } else if (input.contains("-")) {
            result = operand1.subtract(operand2);
        } else {
            throw new IllegalArgumentException("Некорректный оператор");
        }

        return result.toString();
    }

    private Currency parseCurrency(String input) {
        input = input.trim();

        if (input.startsWith("$")) {
            double amount = Double.parseDouble(input.substring(1));
            return new Currency(amount, "$");
        } else if (input.endsWith("p")) {
            double amount = Double.parseDouble(input.substring(0, input.length() - 1));
            return new Currency(amount, "p");
        } else {
            throw new IllegalArgumentException("Некорректный формат валюты");
        }
    }


    public static void main(String[] args) {
        CurrencyCalculator calculator = new CurrencyCalculator();
        calculator.run();
    }
}

