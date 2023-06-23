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
        input = input.replaceAll("\\s", ""); // Удаление пробелов

        if (input.startsWith("$") && input.contains("to")) {
            // Операция со сложением/вычитанием и конвертацией
            String[] parts = input.split("[+\\-]");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Некорректная операция");
            }

            Currency operand1 = parseCurrency(parts[0]);
            Currency operand2 = parseCurrency(parts[1]);

            // Проверка, является ли операнд конвертацией
            if (operand1.getCurrencyType().equals("$") && parts[1].startsWith("to")) {
                operand2 = parseCurrency(evaluateExpression(parts[1])); // Выполнение конвертации
            } else if (operand2.getCurrencyType().equals("$") && parts[0].startsWith("to")) {
                operand1 = parseCurrency(evaluateExpression(parts[0])); // Выполнение конвертации
            }

            Currency result;
            if (input.contains("+")) {
                result = operand1.add(operand2);
            } else {
                result = operand1.subtract(operand2);
            }

            return result.toString();
        } else if (input.contains("toRubles")) {
            // Операция конвертации в рубли
            String value = input.substring(input.indexOf('(') + 1, input.indexOf(')'));
            return convertToRubles(value);
        } else if (input.contains("toDollars")) {
            // Операция конвертации в доллары
            String value = input.substring(input.indexOf('(') + 1, input.indexOf(')'));
            return convertToDollars(value);
        } else {
            throw new IllegalArgumentException("Некорректная операция");
        }
    }

    private String convertToRubles(String value) {
        try {
            Currency currency = parseCurrency(value);
            double rubles;
            if (currency.getCurrencyType().equals("$")) {
                rubles = currencyConverter.toRubles(currency.getAmount());
            } else {
                throw new IllegalArgumentException("Некорректный формат валюты");
            }
            return new Currency(rubles, "p").toString();
        } catch (IllegalArgumentException e) {
            return "Ошибка: " + e.getMessage();
        }
    }

    private String convertToDollars(String value) {
        try {
            Currency currency = parseCurrency(value);
            double dollars;
            if (currency.getCurrencyType().equals("p")) {
                dollars = currencyConverter.toDollars(currency.getAmount());
            } else {
                throw new IllegalArgumentException("Некорректный формат валюты");
            }
            return new Currency(dollars, "$").toString();
        } catch (IllegalArgumentException e) {
            return "Ошибка: " + e.getMessage();
        }
    }

    private Currency parseCurrency(String input) {
        input = input.trim();

        if (input.startsWith("$")) {
            double amount = Double.parseDouble(input.substring(1).replace(",", "."));
            return new Currency(amount, "$");
        } else if (input.endsWith("p")) {
            double amount = Double.parseDouble(input.substring(0, input.length() - 1).replace(",", "."));
            return new Currency(amount, "p");
        } else if (input.contains("toDollars")) {
            String value = input.substring(input.indexOf('(') + 1, input.indexOf(')'));
            double amount = currencyConverter.toDollars(parseCurrency(value).getAmount());
            return new Currency(amount, "$");
        } else if (input.contains("toRubles")) {
            String value = input.substring(input.indexOf('(') + 1, input.indexOf(')'));
            double amount = currencyConverter.toRubles(parseCurrency(value).getAmount());
            return new Currency(amount, "p");
        } else {
            throw new IllegalArgumentException("Некорректный формат валюты");
        }
    }
}
