package board;

public class TaxSpace extends Space {
    private enum TaxType {
        LUXURY, INCOME
    }

    private TaxType type;

    public TaxSpace(String taxType, int index) {
        super(null, index);
        if (taxType.equals("LUXURY")) {
            type = TaxType.LUXURY;
            setName("Luxury Tax");
        }
        else {
            type = TaxType.INCOME;
            setName("Income Tax");
        }
    }

    public int getTax() {
        if(type == TaxType.INCOME)
            return 100;
        else
            return 200;
    }

}
