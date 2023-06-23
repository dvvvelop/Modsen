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
        } else if (input.startsWith("toRubles(") && input.endsWith(")")) {
            // Операция конвертации в рубли
            String value = input.substring(9, input.length() - 1);
            return convertToRubles(value);
        } else if (input.startsWith("toDollars(") && input.endsWith(")")) {
            // Операция конвертации в доллары
            String value = input.substring(10, input.length() - 1);
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





    private String performOperation(String input) {
        String[] parts = input.split("[+-]");
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
        } else if (input.contains("-")) {
            result = operand1.subtract(operand2);
        } else {
            throw new IllegalArgumentException("Некорректный оператор");
        }

        return result.toString();
    }



    private Currency parseCurrency(String input) {
        input = input.trim();

        // Replace commas with periods
        input = input.replace(",", ".");

        if (input.startsWith("$")) {
            double amount = Double.parseDouble(input.substring(1));
            return new Currency(amount, "$");
        } else if (input.endsWith("p")) {
            double amount = Double.parseDouble(input.substring(0, input.length() - 1));
            return new Currency(amount, "p");
        } else if (input.startsWith("toDollars(") && input.endsWith(")")) {
            String value = input.substring(10, input.length() - 1);
            double amount = currencyConverter.toDollars(parseCurrency(value).getAmount());
            return new Currency(amount, "$");
        } else if (input.startsWith("toRubles(") && input.endsWith(")")) {
            String value = input.substring(9, input.length() - 1);
            double amount = currencyConverter.toRubles(parseCurrency(value).getAmount());
            return new Currency(amount, "p");
        } else {
            throw new IllegalArgumentException("Некорректный формат валюты");
        }
    }



    private double parseAmount(String amountStr) {
        try {
            return Double.parseDouble(amountStr.replace(",", ".")); // Заменяем запятую на точку в числе
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Некорректный формат числа");
        }
    }




    private Currency addCurrencies(Currency operand1, Currency operand2) {
        if (!operand1.getCurrencyType().equals(operand2.getCurrencyType())) {
            throw new IllegalArgumentException("Некорректная операция сложения валютных значений: неподдерживаемые типы валют");
        }

        double resultAmount = operand1.getAmount() + operand2.getAmount();
        return new Currency(resultAmount, operand1.getCurrencyType());
    }

    private Currency subtractCurrencies(Currency operand1, Currency operand2) {
        if (!operand1.getCurrencyType().equals(operand2.getCurrencyType())) {
            throw new IllegalArgumentException("Некорректная операция вычитания валютных значений: неподдерживаемые типы валют");
        }

        double resultAmount = operand1.getAmount() - operand2.getAmount();
        return new Currency(resultAmount, operand1.getCurrencyType());
    }



}


