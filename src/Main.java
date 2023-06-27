import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static double amount =0;

    public static void main(String[] args) {
        Currency s = new Currency();
        while(true) {
            s.sum = 0;
            s.func = ' ';
            s.cur = ' ';
            amount = 0;
            System.out.print("Enter an expression: ");
            Scanner input = new Scanner(System.in);
            String str = input.nextLine();

            str = s.simplifyString(str);
            if(str == null){
                System.out.println("Invalid input (addition or subtraction of different currencies)");
                continue;
            }
            double subsum = s.calculateStringsInStack();
            if(subsum == -1 ){
                System.out.println("Invalid input (addition or subtraction of different currencies)");
                continue;
            }
            amount+=subsum;
            if (!str.equals("")) amount += s.calculate(str);
            if(amount == -1) continue;

            if (s.cur == '$') System.out.println("Result: " + "$" + Math.round(amount * 100) / 100.0);
            else System.out.println("Result: " + Math.round(amount * 100) / 100.0 + "р");

        }
    }
    public static int checkSignsSeparated(String input) {
        int index = 0;
        while (index != -1) {
            index = input.indexOf("+", index);
            if (index == -1) continue;
            if(!(input.charAt(index-1) == ' ' && input.charAt(index+1) == ' ')) return -1;
            if (index != -1) {
                index++;
            }
        }
        index = 0;
        while (index != -1) {
            index = input.indexOf("-", index);
            if (index == -1) continue;
            if(!(input.charAt(index-1) == ' ' && input.charAt(index+1) == ' ')) return -1;
            if (index != -1) {
                index++;
            }
        }
        return 0;
    }
    public static int checkFormatOfNumbers(String input){
        Pattern pattern = Pattern.compile("(\\d+[.,]?\\d*)");
        Matcher matcher = pattern.matcher(input);
        int start = 0;
        int end = 0;
        while (matcher.find()) {
            String match = matcher.group(1);
            start = input.indexOf(match);
            end = start + match.length()-1;
            if(start>0 && end < input.length()-1) {
                if (!(input.charAt(start - 1) == '$' || input.charAt(end + 1) == 'р')) {
                    return -1;
                }
            }
            else if(start == 0 && end == input.length()-1) return -1;
            else if(start == 0 && input.charAt(end+1) != 'р') return -1;
            else if(end == input.length()-1 && (input.charAt(end) != 'р' && input.charAt(start-1) != '$')) return -1;
            if(end != input.length()-1)     input = input.substring(end+1,input.length());
        }
        return 0;
    }
}