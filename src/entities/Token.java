package entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class Token implements Serializable {

    //properties
    private String tokenName;
    private int jailTime;
    private double taxMultiplier;
    private int salaryChange;
    private double buildingCostMultiplier;
    private double propertyCostMultiplier;
    private double rentPayMultiplier;
    private double rentCollectMultiplier;
    private double mortgageInterest;

    //constructors
    public Token(int number) {
        jailTime = 3;
        taxMultiplier = 1;
        salaryChange = 0;
        buildingCostMultiplier = 1;
        propertyCostMultiplier = 1;
        rentPayMultiplier = 1;
        rentCollectMultiplier = 1;
        mortgageInterest = 1.1;

        if(number == 1) {
            tokenName = "thimble";
            taxMultiplier = 0.8;
            rentCollectMultiplier = 0.8;
        } else if (number == 2) {
            tokenName = "wheelbarrow";
            buildingCostMultiplier = 0.5;
            salaryChange = -50;
        } else if (number == 3) {
            tokenName = "boot";
            propertyCostMultiplier = 0.8;
            salaryChange = -100;
        } else if (number == 4) {
            tokenName = "horse";
            rentCollectMultiplier = 1.3;
            propertyCostMultiplier = 1.1;
        } else if (number == 5) {
            tokenName = "racecar";
            salaryChange = 200;
            jailTime = 4;
        } else if( number == 6) {
            tokenName = "iron";
            buildingCostMultiplier = 0.8;
            rentPayMultiplier = 1.2;
        } else if( number == 7 ) {
            tokenName = "tophat";
            jailTime = 2;
            taxMultiplier = 1.2;
        } else {
            tokenName = "battleship";
            rentPayMultiplier = 0.7;
            buildingCostMultiplier = 1.5;
        }
    }

    public Token() {
        tokenName = "no-name";
        jailTime = 3;
        taxMultiplier = 1;
        salaryChange = 0;
        buildingCostMultiplier = 1;
        propertyCostMultiplier = 1;
        rentPayMultiplier = 1;
        rentCollectMultiplier = 1;
    }

    //methods
}