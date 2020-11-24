package entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Token {

    //properties
    private String tokenName;
    private int jailTime;
    private double taxMultiplier;
    private int salaryChange;
    private double buildingCostMultiplier;
    private double propertyCostMultiplier;
    private double rentPayMultiplier;
    private double rentCollectMultiplier;

    //constructors
    public Token() {}

    public Token(String tokenName, int jailTime, double taxMultiplier, int salaryChange, double buildingCostMultiplier, double propertyCostMultiplier, double rentPayMultiplier, double rentCollectMultiplier) {
        this.tokenName = tokenName;
        this.jailTime = jailTime;
        this.taxMultiplier = taxMultiplier;
        this.salaryChange = salaryChange;
        this.buildingCostMultiplier = buildingCostMultiplier;
        this.propertyCostMultiplier = propertyCostMultiplier;
        this.rentPayMultiplier = rentPayMultiplier;
        this.rentCollectMultiplier = rentCollectMultiplier;
    }


    //methods
}