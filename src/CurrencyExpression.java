import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurrencyExpression {
    private static final Pattern CURRENCY_PATTERN = Pattern.compile("([$р])([\\d\\.]+)");

    private CurrencyConverter converter;
    private String expression;

    public CurrencyExpression(CurrencyConverter converter, String expression) {
        this.converter = converter;
        this.expression = expression;
    }

    public CurrencyValue evaluate() {
        Matcher matcher = CURRENCY_PATTERN.matcher(expression);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String currency = matcher.group(1);
            double value = Double.parseDouble(matcher.group(2));

            if (currency.equals("$")) {
                value = converter.convertFrom("руб", value);
                currency = "р";
            } else if (currency.equals("р")) {
                value = converter.convertTo("руб", value);
                currency = "$";
            }

            matcher.appendReplacement(result, currency + value);
        }
        matcher.appendTail(result);

        return new CurrencyValue("$", Double.parseDouble(result.toString()));
    }
}
