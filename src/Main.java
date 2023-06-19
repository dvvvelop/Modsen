import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CurrencyConverter converter = new CurrencyConverter("rates.txt");
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите выражение: ");
        String input = scanner.nextLine();

        CurrencyExpression expression = new CurrencyExpression(converter, input);
        CurrencyValue result = expression.evaluate();

        System.out.println("Результат: " + result.format());
    }
}