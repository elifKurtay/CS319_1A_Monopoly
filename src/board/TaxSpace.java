package board;

public class TaxSpace extends Space {
    private enum TaxType {
        LUXURY, INCOME
    }

    private TaxType type;

    public TaxSpace(String taxType) {
        if (taxType.equals("LUXURY")) {
            type = TaxType.LUXURY;
        }
        else {
            type = TaxType.INCOME;
        }
    }

    public boolean payTax(int amount) {
        return getLatestPlayerOnSpace().payBank(amount);
    }

}
