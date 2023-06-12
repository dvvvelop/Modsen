import java.util.Scanner;

public class CurrencyConverter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Добро пожаловать в калькулятор валют!");
        System.out.println("Поддерживаемые операции: + (сложение) и - (вычитание)");
        System.out.println("Введите значения валюты в формате: $57.75 или 57.75р");

        System.out.print("Введите первое значение: ");
        String value1 = scanner.nextLine();

        System.out.print("Введите операцию (+ или -): ");
        String operation = scanner.nextLine();

        System.out.print("Введите второе значение: ");
        String value2 = scanner.nextLine();

        boolean isDollar = isDollar(value1);
        boolean isSameCurrency = (isDollar && isDollar(value2)) || (!isDollar && !isDollar(value2));

        if (!isSameCurrency) {
            System.out.println("Ошибка: операции над разными валютами недопустимы");
            return;
        }

        double amount1 = extractAmount(value1);
        double amount2 = extractAmount(value2);
        double result;

        if (operation.equals("+")) {
            result = amount1 + amount2;
        } else if (operation.equals("-")) {
            result = amount1 - amount2;
        } else {
            System.out.println("Неподдерживаемая операция");
            return;
        }

        String currencySymbol = isDollar ? "$" : "р";
        System.out.println("Результат: " + currencySymbol + result);
    }

    private static boolean isDollar(String value) {
        return value.charAt(0) == '$';
    }

    private static double extractAmount(String value) {
        if (isDollar(value)) {
            return Double.parseDouble(value.substring(1));
        } else {
            return Double.parseDouble(value.substring(0, value.length() - 1));
        }
    }
}
