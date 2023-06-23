public class Currency {
    private double amount;
    private String currencyType;

    public Currency(double amount, String currencyType) {
        this.amount = amount;
        this.currencyType = currencyType;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public Currency add(Currency other) {
        if (!currencyType.equals(other.getCurrencyType())) {
            throw new IllegalArgumentException("Currency types do not match");
        }

        double sum = amount + other.getAmount();
        return new Currency(sum, currencyType);
    }

    public Currency subtract(Currency other) {
        if (!currencyType.equals(other.getCurrencyType())) {
            throw new IllegalArgumentException("Currency types do not match");
        }

        double difference = amount - other.getAmount();
        return new Currency(difference, currencyType);
    }

    @Override
    public String toString() {
        return String.format("%s%.2f", currencyType, amount);
    }
}