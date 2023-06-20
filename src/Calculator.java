import java.math.BigDecimal;

public class Calculator {
    private CurrencyConverter converter;

    public Calculator(CurrencyConverter converter) {
        this.converter = converter;
    }

    public BigDecimal evaluateExpression(String expression) {
        expression = expression.replaceAll("\\s+", "");

        while (expression.contains("toDollars(") || expression.contains("toRubles(")) {
            int startIndex = expression.lastIndexOf("toDollars(");
            int endIndex = expression.indexOf(")", startIndex);

            if (startIndex != -1 && endIndex != -1) {
                String subExpression = expression.substring(startIndex + 10, endIndex);
                BigDecimal subResult = evaluateExpression(subExpression);
                BigDecimal convertedAmount = converter.toDollars(subResult);
                expression = expression.substring(0, startIndex) + CurrencyConverter.DOLLAR_SYMBOL +
                        convertedAmount.toString() + expression.substring(endIndex + 1);
            } else {
                throw new IllegalArgumentException("Invalid expression format.");
            }
        }

        while (expression.contains("toRubles(") || expression.contains("toDollars(")) {
            int startIndex = expression.lastIndexOf("toRubles(");
            int endIndex = expression.indexOf(")", startIndex);

            if (startIndex != -1 && endIndex != -1) {
                String subExpression = expression.substring(startIndex + 9, endIndex);
                BigDecimal subResult = evaluateExpression(subExpression);
                BigDecimal convertedAmount = converter.toRubles(subResult);
                expression = expression.substring(0, startIndex) + convertedAmount.toString() +
                        CurrencyConverter.RUBLE_SYMBOL + expression.substring(endIndex + 1);
            } else {
                throw new IllegalArgumentException("Invalid expression format.");
            }
        }

        BigDecimal result = evaluateSimpleExpression(expression);
        return result.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal evaluateSimpleExpression(String expression) {
        String[] tokens = expression.split("[+\\-]");

        if (tokens.length != 2) {
            throw new IllegalArgumentException("Invalid expression format.");
        }

        BigDecimal operand1 = converter.parseAmount(tokens[0]);
        BigDecimal operand2 = converter.parseAmount(tokens[1]);

        if (expression.contains("+")) {
            return operand1.add(operand2);
        } else if (expression.contains("-")) {
            return operand1.subtract(operand2);
        } else {
            throw new IllegalArgumentException("Invalid expression format.");
        }
    }
}

