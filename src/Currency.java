import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Currency {

    private static StackOfString stack = new StackOfString();

    public static double sum = 0;
    public static char func = ' ';
    public static char cur = ' ';

    public double calculate(String input) {
        Pattern pattern = null;
        int dol = input.indexOf("$");
        int rub = input.indexOf("р");
        input = input.replace(",", ".");
        if (dol > -1 && rub > -1) {
            System.out.println("Invalid input (addition or subtraction of different currencies)");
            return -1;
        } else if (dol > -1) {
            cur = '$';
            pattern = Pattern.compile("\\$(\\d+(?:\\.\\d+)?)|([-+])\\s\\$(\\d+(?:\\.\\d+)?)");
        } else if (rub > -1) {
            cur = 'р';
            pattern = Pattern.compile("(?:\\s*([-+])\\s*)?(\\d+(?:\\.\\d+)?)(р)");
        }
        String match = "";
        String operator = "";
        String match2 = "";
        Matcher matcher = pattern.matcher(input);
        double total = 0.0;
        while (matcher.find()) {
            if (rub > -1) {
                match = matcher.group(2);
                operator = matcher.group(1);
                match2 = matcher.group(3);
            } else {
                match = matcher.group(1);
                operator = matcher.group(2);
                match2 = matcher.group(3);
            }
            double amount;
            if (match != null) {
                amount = Double.parseDouble(match);
            } else if (match2 != null) {
                amount = Double.parseDouble(match2);
            } else {
                continue;
            }
            if ("-".equals(operator)) {
                total -= amount;
            } else {
                total += amount;
            }
        }
        return total;
    }

    public String simplifyString(String input) {

        input = " + " + input;
        int start = input.indexOf("toDollars(");
        int start1 = input.indexOf("toRubles(");
        int count1 = 0;
        int count2 = 0;
        int count3 = 0;
        int count4 = 0;
        while ((start != -1) || (start1 != -1)) {

            if (start1 < start && start1 != -1) {
                start = start1;
                count1++;
            } else if (start == -1) {
                start = start1;
                count1++;
            }
            if (start != -1) {
                int openBrackets = 0;
                int end = -1;
                count2++;
                for (int i = start + 10; i < input.length(); i++) {
                    char c = input.charAt(i);

                    if (c == '(') {
                        openBrackets++;
                    } else if (c == ')') {
                        if (openBrackets == 0) {
                            end = i;
                            break;
                        } else {
                            openBrackets--;
                        }
                    }
                }

                if (end != -1) {

                    String substring = input.substring(start - 3, end + 1);
                    if (cur == ' ' && substring.charAt(5) == 'D') cur = '$';

                    else if (cur == ' ' && substring.charAt(5) == 'R') cur = 'р';
                    stack.push(substring);
                    input = input.substring(0, start - 3) + input.substring(end + 1);
                    start = input.indexOf("toDollars(");
                    start1 = input.indexOf("toRubles(");
                } else {
                    System.out.println("Wrong string format");
                }
            } else {
                System.out.println("Substring not found");
            }
        }
        count4 = input.indexOf('$');
        count3 = input.indexOf('р');
        if ((count3 != -1 && count4 != -1) || (count1 > 0 && count2 > 0) || (count1 > 0 && count4 != -1) || (count2 > 0 && count3 != -1))
            return null;
        return input;
    }

    public double calculateStringsInStack() {

        double amount = 0;

        while (!stack.isEmpty()) {
            String str = stack.pop();
            int start = findInnerFunction(str);
            while (start != -1) {
                int end = str.indexOf(')');


                String substring = "";
                if (func == 'd') substring = str.substring(start + 10, end);
                else if (func == 'r') substring = str.substring(start + 9, end);

                substring = " + " + substring;
                substring = substring.replace(",", ".");
                amount = calculate(substring);
                if (func == 'd') {
                    if (cur == '$') return -1;
                    String newstr = toDollars(amount);
                    str = str.substring(0, start) + newstr + str.substring(end + 1);
                } else if (func == 'r') {
                    if (cur == 'р') return -1;
                    String newstr = toRubles(amount);
                    str = str.substring(0, start) + newstr + str.substring(end + 1);
                }
                start = findInnerFunction(str);
            }
            sum += calculate(str);
            amount = 0;
        }

        return sum;
    }

    public static int findInnerFunction(String input) {
        int toRublesIndex = input.lastIndexOf("toRubles(");
        int toDollarsIndex = input.lastIndexOf("toDollars(");

        if (toRublesIndex == -1 && toDollarsIndex == -1) {
            return -1;
        } else if (toRublesIndex == -1) {
            func = 'd';
            return toDollarsIndex;
        } else if (toDollarsIndex == -1) {
            func = 'r';
            return toRublesIndex;
        } else {
            if (toRublesIndex > toDollarsIndex) {
                func = 'r';
                return toRublesIndex;
            } else {
                func = 'd';
                return toDollarsIndex;
            }
        }
    }

    public String toDollars(double amount) {

        return "$" + (amount / getRate());
    }

    public String toRubles(double amount) {

        return (amount * getRate()) + "р";
    }

    public double getRate() {
        File file = new File("currency.txt");
        double rate = 1;
        try {
            Scanner scanner = new Scanner(file);
            if (scanner.hasNextDouble()) {
                rate = scanner.nextDouble();
            } else {
                System.out.println("Number not found in file");
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        return rate;
    }
}