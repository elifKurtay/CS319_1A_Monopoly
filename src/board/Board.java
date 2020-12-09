package board;

import card.*;
import entities.Player;
import entities.Property;
import event.AdvanceEvent;
import event.CardEvent;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

@Getter
@Setter
public class Board {
    private Space[] spaces;
    private String[] propertyGroupColors;
    private ArrayList<Card> chanceCards;
    private ArrayList<Card> communityChestCards;
    private Thief thief;

    public Board(File map) {
        spaces = new Space[40];
        // Maybe also read the cards from a file and instantiate them here
        chanceCards = null;
        communityChestCards = null;
        propertyGroupColors = null;
        thief = null;
        try {
            Scanner scan = new Scanner(map);
            // Using \Z as the delimiter matches to the end of file except a newline at end
            String json = scan.useDelimiter("\\Z").next();

            // Create a json object containing the map and then access the spaces
            JSONObject jsonMap = new JSONObject(json);
            int propertyGroupCount = jsonMap.getInt("propertyGroupCount");
            System.out.println("Prop group count: " + propertyGroupCount);
            propertyGroupColors = new String[propertyGroupCount];
            JSONArray colorsJson = jsonMap.getJSONArray("propertyGroupColors");
            for (int i = 0; i < colorsJson.length(); i++) {
                propertyGroupColors[i] = colorsJson.getString(i);
            }

            JSONArray mapSpaces = jsonMap.getJSONArray("spaces");

            for (int i = 0; i < mapSpaces.length(); i++) {
                JSONObject currentSpace =  mapSpaces.getJSONObject(i);
                switch (currentSpace.getString("type")) {
                    case "PropertySpace":
                        TitleDeedCard card = null;
                        Property p = null;
                        JSONObject titleDeedCard = currentSpace.getJSONObject("titleDeedCard");
                        switch (currentSpace.getString("propertyType")) {
                            case "LAND":
                                JSONArray rentsJson = titleDeedCard.getJSONArray("rents");
                                int[] rents = new int[6];
                                for (int j = 0; j < rentsJson.length(); j++) {
                                    rents[j] = rentsJson.getInt(j);
                                }
                                card = new LandTitleDeedCard(currentSpace.getString("name"), titleDeedCard.getInt("mortgageValue"),
                                        currentSpace.getInt("propertyGroup"), rents, titleDeedCard.getInt("houseCost"), titleDeedCard.getInt("hotelCost"));
                                break;
                            case "UTILITY":
                                JSONArray multipliersJson = titleDeedCard.getJSONArray("multipliers");
                                int[] multipliers = new int[2];
                                for (int j = 0; j < multipliersJson.length(); j++) {
                                    multipliers[j] = multipliersJson.getInt(j);
                                }
                                card = new UtilityTitleDeedCard(currentSpace.getString("name"), titleDeedCard.getInt("mortgageValue"), multipliers);
                                break;
                            case "TRANSPORT":
                                card = new TransportTitleDeedCard(currentSpace.getString("name"), titleDeedCard.getInt("mortgageValue"));
                                break;
                        }
                        p = new Property(card, currentSpace.getInt("value"));
                        spaces[i] = new PropertySpace(currentSpace.getString("name"), i, currentSpace.getString("propertyType"), p);

                        break;
                    case "CardSpace":
                        spaces[i] = new CardSpace(currentSpace.getString("cardType"), i);
                        break;
                    case "TaxSpace":
                        spaces[i] = new TaxSpace(currentSpace.getString("taxType"), i);
                        break;
                    case "GoSpace":
                        spaces[i] = new GoSpace(i);
                        break;
                    case "JailSpace":
                        spaces[i] = new JailSpace(i);
                        break;
                    case "WheelOfFortuneSpace":
                        spaces[i] = new WheelOfFortuneSpace(i);
                        break;
                    case "GoToJailSpace":
                        spaces[i] = new GoToJailSpace(i);
                        break;
                }
            }

            chanceCards = new ArrayList<>();
            JSONObject cardsJSON = jsonMap.getJSONObject("cards");
            JSONArray chanceCardsJSON = cardsJSON.getJSONArray("chanceCards");
            for (int i = 0; i < chanceCardsJSON.length(); i++) {
                JSONObject card = chanceCardsJSON.getJSONObject(i);
                JSONObject cardEvent = card.getJSONObject("cardEvent");
                CardEvent e = null;
                switch (cardEvent.getString("type")) {
                    case "ADVANCE":
                        String targetSpace = cardEvent.getString("targetSpace");
                        for (Space s : spaces) {
                            if (targetSpace.equals(s.getName())) {
                                e = new AdvanceEvent(s, 1 == cardEvent.getInt("canCollectSalary"));
                                chanceCards.add(new Card(card.getString("cardText"), e));
                            }
                        }
                }
            }
            communityChestCards = new ArrayList<>();
            JSONArray communityChestCardsJSON = cardsJSON.getJSONArray("communityChestCards");
            for (int i = 0; i < communityChestCardsJSON.length(); i++) {
                JSONObject card = communityChestCardsJSON.getJSONObject(i);
                JSONObject cardEvent = card.getJSONObject("cardEvent");
                CardEvent e = null;
                switch (cardEvent.getString("type")) {
                    case "ADVANCE":
                        String targetSpace = cardEvent.getString("targetSpace");
                        for (Space s : spaces) {
                            if (targetSpace.equals(s.getName())) {
                                e = new AdvanceEvent(s, 1 == cardEvent.getInt("canCollectSalary"));
                                communityChestCards.add(new Card(card.getString("cardText"), e));
                            }
                        }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Space getSpace(int index) {return spaces[index];}

    public void deployThief(Player target) {
        thief = new Thief(target);
    }

    public Card drawCard(CardSpace.CardType type) {
        Card drawn;
        if(type == CardSpace.CardType.CHANCE) {
            drawn = chanceCards.get(0);
            chanceCards.remove(0);
            chanceCards.add(drawn);
        } else {
            drawn = communityChestCards.get(0);
            communityChestCards.remove(0);
            communityChestCards.add(drawn);
        }
        return drawn;
    }
}
