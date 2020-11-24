package card;

public class TitleDeedCard {
    private String propertyName;
    private int mortgageValue;

    public TitleDeedCard(String propertyName, int mortgageValue){
        this.mortgageValue = mortgageValue;
        this.propertyName = propertyName;
    }

    public int getMortgageValue() {
        return mortgageValue;
    }

    public void setMortgageValue(int mortgageValue) {
        this.mortgageValue = mortgageValue;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }
}
