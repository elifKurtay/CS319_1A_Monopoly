package board;
import card.Card;
import entities.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Board {
    private Space spaces[];
    private ArrayList<Card> chanceCards;
    private ArrayList<Card> communityChestCards;
    private Thief thief;

    public Board(File map) {
        spaces = new Space[40];
        // Maybe also read the cards from a file and instantiate them here
        chanceCards = null;
        communityChestCards = null;
        thief = null;

        try {
            Scanner scan = new Scanner(map);
            // Using \Z as the delimiter matches to the end of file except a newline at end
            String json = scan.useDelimiter("\\Z").next();

            // Create a json object containing the map and then access the spaces
            JSONObject jsonMap = new JSONObject(json);
            JSONArray mapSpaces = jsonMap.getJSONArray("spaces");

            for (int i = 0; i < mapSpaces.length(); i++) {
                JSONObject currentSpace =  mapSpaces.getJSONObject(i);
                switch (currentSpace.getString("type")) {
                    case "PropertySpace":
                        spaces[i] = new PropertySpace(currentSpace.getString("name"),
                            currentSpace.getString("propertyType"), Integer.parseInt(currentSpace.getString("value")));
                        break;
                    case "CardSpace":
                        spaces[i] = new CardSpace(currentSpace.getString("cardType"));
                        break;
                    case "TaxSpace":
                        spaces[i] = new TaxSpace(currentSpace.getString("taxType"));
                    case "GoSpace":
                        spaces[i] = new GoSpace();
                        break;
                    case "JailSpace":
                        spaces[i] = new JailSpace();
                        break;
                    case "WheelOfFortuneSpace":
                        spaces[i] = new WheelOfFortuneSpace();
                        break;
                    case "GoToJailSpace":
                        spaces[i] = new GoToJailSpace();
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void deployThief(Player target) {
        thief = new Thief(target);
    }
}
