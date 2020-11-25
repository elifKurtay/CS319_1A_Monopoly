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


    // Decide on amount here or pass it from the GUI?
    // User has 2 choices for paying income tax, a fixed amount or 10%(? not sure rn) of their money(?)
    public boolean payTax(int amount) {
        return getLatestPlayerOnSpace().payBank(amount);
    }
}
