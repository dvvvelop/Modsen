import java.util.Scanner;

public class CurrencyConverterApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите выражение для конвертации: ");
        String input = scanner.nextLine();
        scanner.close();

        String result = evaluateExpression(input);
        System.out.println("Результат: " + result);
    }

    private static String evaluateExpression(String input) {
        // Удаляем все пробелы из ввода
        input = input.replaceAll(" ", "");

        // Заменяем "р" на "R" для удобства парсинга
        input = input.replaceAll("р", "R");

        // Выполняем конвертацию долларов и рублей
        while (input.contains("toDollars(") || input.contains("toRubles(")) {
            int startIndex = input.lastIndexOf("toDollars(");
            if (startIndex == -1) {
                startIndex = input.lastIndexOf("toRubles(");
            }

            int endIndex = input.indexOf(")", startIndex);
            String expression = input.substring(startIndex, endIndex + 1);
            String convertedValue = evaluateSingleExpression(expression);
            input = input.replace(expression, convertedValue);
        }

        // Удаляем все символы, кроме цифр, точек и запятых
        input = input.replaceAll("[^\\d.,]", "");

        // Преобразуем запятые в точки для парсинга вещественных чисел
        input = input.replace(",", ".");

        // Парсим результат и округляем до сотых
        double result = Double.parseDouble(input);
        result = Math.round(result * 100.0) / 100.0;

        // Форматируем результат в виде строки
        return String.format("$%.2f", result);
    }

    private static String evaluateSingleExpression(String expression) {
        if (expression.startsWith("toDollars(")) {
            double rubles = parseValue(expression);
            double dollars = CurrencyConverter.toDollars(rubles);
            return String.format("$%.2f", dollars);
        } else if (expression.startsWith("toRubles(")) {
            double dollars = parseValue(expression);
            double rubles = CurrencyConverter.toRubles(dollars);
            return String.format("%.2fR", rubles);
        }
        return "";
    }

    private static double parseValue(String expression) {
        int startIndex = expression.indexOf("(");
        int endIndex = expression.indexOf(")");
        String valueString = expression.substring(startIndex + 1, endIndex);
        return Double.parseDouble(valueString);
    }
}
