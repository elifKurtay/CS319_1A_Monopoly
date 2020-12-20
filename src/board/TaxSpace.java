package board;

public class TaxSpace extends Space {
    private enum TaxType {
        LUXURY, INCOME
    }

    private TaxType type;

    /**
     * This constructor initializes the object
     * by setting the tax type and index of the space.
     * @param taxType
     * @param index
     */
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

    /**
     * This method is to get the tax amount of the space.
     * @return tax
     */
    public int getTax() {
        if(type == TaxType.INCOME)
            return 100;
        else
            return 200;
    }

}
