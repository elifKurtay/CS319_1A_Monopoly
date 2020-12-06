package game;

import bank.Bank;
import board.*;
import card.Card;
import card.LandTitleDeedCard;
import entities.DigitalPlayer;
import entities.Player;
import entities.Property;
import entities.Token;
import event.AdvanceEvent;
import event.CollectEvent;
import event.GoToJailEvent;
import event.PayEvent;
import frontend.GameScreenController;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import static entities.Player.ownsAllTitlesFromSameGroup;

public class Game {

    private static final int LAP = 4;
    private final Board board;
    private final int lapLimit; //given -1 if game mod is survival
    private final int playerCount;

    private Player[] players;
    private Player currentPlayer;

    private int lapCount;
    private Bank bank;

    private GameScreenController controller;

    public Game(File map, int playerCount, String[] playerNames, int turnLimit, GameScreenController controller) {
        this.playerCount = playerCount;
        this.lapLimit = turnLimit;
        bank = new Bank();
        board = new Board(map);
        lapCount = 0;

        this.controller = controller;

        players = new Player[LAP];
        for(int i = 0; i < LAP; i++) {
            if (i < playerCount)
                players[i] = new Player(playerNames[i]);
            else
                players[i] = createDigitalPlayer("Computer " + i);
        }
    }

    public Game(Game loadedGame) {
        this.playerCount = loadedGame.playerCount;
        this.players = loadedGame.players;
        this.lapCount = loadedGame.lapCount;
        this.lapLimit = loadedGame.lapLimit;
        this.bank = loadedGame.bank;
        this.board = loadedGame.board;
        this.currentPlayer = loadedGame.currentPlayer;
    }

    void gameLoop() {
        int[] dice;
        int doublesCount = 0;
        boolean digitalPlayer = false;
        while( ! isGameEnd() ) {
            if(lapCount == 0)
                initializingLap();
            for(int i = 0; i < LAP &&  !isGameEnd(); i++) {
                currentPlayer = players[i];
                digitalPlayer = currentPlayer instanceof DigitalPlayer;
                //dice = rollDice();
                dice = controller.rollDice(currentPlayer.getPlayerName(),digitalPlayer);
                //System.out.println(currentPlayer.getPlayerName() + " rolled: " + dice[0] + " - " + dice[1]);

                //calculating next player
                if (dice[0] == dice[1]) {
                    doublesCount++;
                    /* why?
                    if (i != 0) {
                        i--;
                    }
                     */
                } else
                    doublesCount = 0;

                //checking jail conditions
                if (!currentPlayer.isJailed() && dice[0] == dice[1] && doublesCount == 3){
                    //if (sendToJail(scan, currentPlayer)) {
                    //if (sendToJail(currentPlayer)) {
                    sendToJail(currentPlayer);
                //System.out.println("You are sent to jail!");
                    controller.showMessage("You are sent to jail!");
                    continue;
                    //}
                }
                else if(currentPlayer.isJailed() && dice[0] == dice[1]){
                    currentPlayer.setJailed(false);
                    controller.showMessage("You are released from jail!");
                }
                else if( currentPlayer.isJailed() &&
                        currentPlayer.getJailedLapCount() >= currentPlayer.getToken().getJailTime() ) {
                    currentPlayer.setJailed(false);
                    currentPlayer.setMoney(currentPlayer.getMoney() - 50);
                    controller.showMessage("You are released from jail and have lost 5 money!");
                }

                int oldIndex = currentPlayer.getCurrentSpace().getIndex();
                //move on board
                int boardIndex = currentPlayer.getCurrentSpace().getIndex() + dice[0] + dice[1];
                if(boardIndex >= 40) { //passed Go space
                    currentPlayer.setMoney( currentPlayer.getMoney() + 200
                            + currentPlayer.getToken().getSalaryChange()); //salary 20?
                    boardIndex = boardIndex % 40;
                    //System.out.println("Your salary is paid!");
                    controller.drawPlayerBoxes(players);
                    controller.showMessage("Your salary is paid!");
                }

                Space space = board.getSpace( boardIndex );
                currentPlayer.setCurrentSpace( space );
                space.setLatestPlayer(currentPlayer);

                controller.drawToken(i, oldIndex, boardIndex);

                // Salary is paid twice
                if(space instanceof GoSpace) {
                    currentPlayer.setMoney( currentPlayer.getMoney() + 200
                            + currentPlayer.getToken().getSalaryChange()); //salary 20?
                    //System.out.println("Your salary is paid!");
                    controller.showMessage("Your salary is paid!");
                } else if( space instanceof CardSpace) {
                    System.out.println("Draw a card!");
                    //drawCard(scan, currentPlayer, (CardSpace) space);
                    drawCard(currentPlayer, (CardSpace) space);
                } else if( space instanceof GoToJailSpace) {
//                    if (sendToJail(scan, currentPlayer)) {
                    sendToJail(currentPlayer);
                    //System.out.println("You are sent to jail!");
                    controller.drawToken(i, boardIndex, 10);
                    controller.showMessage("You are sent to jail!");
                    continue;
                //}
                } else if( space instanceof JailSpace ) {
                    //System.out.println("You are a visitor in jail.");
                    controller.showMessage("You are visiting jail");
                } else if( space instanceof TaxSpace) {
                    int payment = (int) (((TaxSpace) space).getTax()
                            * currentPlayer.getToken().getTaxMultiplier());
                    currentPlayer.payBank( payment);
                    //System.out.println("You paid " + payment + "M for tax.");
                    controller.drawPlayerBoxes(players);
                    controller.showMessage("You paid " + payment + "M for tax.");
                } else if(space instanceof WheelOfFortuneSpace) {
                    //int gain = ((WheelOfFortuneSpace) space).spinWheel();
                    //currentPlayer.setMoney(currentPlayer.getMoney() + gain);
                    //System.out.println("You won " + gain + "M!");
                    //controller.showMessage("You won " + gain + "M!");
                    ((WheelOfFortuneSpace) space).spinWheel();
                } else if(space instanceof PropertySpace){
                    //cameToProperty(dice, scan, space);
                    cameToProperty(dice, space);
                } else {
                    //?
                    System.out.println("Such space does not exist.");
                }

                if (doublesCount > 0) {
                    i--;
                }
                //turn decisions
                //turnDecisions(scan, space);
                controller.finishTurn();
            }
            lapCount++;
        }
        endGame();
        System.out.println("Game Over!");
    }

//    private void cameToProperty(int[] dice, Scanner scan, Space space) {
    private void cameToProperty(int[] dice, Space space) {
        int choice;
        PropertySpace p = (PropertySpace) space;
        if(currentPlayer == p.getOwner()) { //own property
            //System.out.println("This is your own property.");
            controller.showMessage("This is your own property.");
            //can build on if they choose so
        } else if (p.getOwner() == null ) { //owned by bank
            //buy or auction
            //System.out.println("This property is not owned.\n" +
            //        "Would you like to buy this property for " + ((PropertySpace) space).getValue() +
            //        "M, or auction?\n(0-buy, 1-auction)"); //should we add a neither option ?
            //choice = scan.nextInt();
            //scan.nextLine();
            //if(choice == 0) {
            if (controller.buyProperty((PropertySpace) space)) {
                currentPlayer.addProperty(p.getAssociatedProperty());
                currentPlayer.payBank((int) (p.getAssociatedProperty().getValue()
                        * currentPlayer.getToken().getPropertyCostMultiplier() ));
                ((PropertySpace) space).setOwner(currentPlayer);
                controller.drawPlayerBoxes(players);
            } else {
                bank.startAuction(p.getAssociatedProperty());
            }
            bank.removeFromUnownedProperties(p.getAssociatedProperty());
            System.out.println(space.getName() + " belongs to " + ((PropertySpace) space).getOwner());
        } else { //owned by another player
            //pay rent
            int rentAmount = currentPlayer.payRent(((PropertySpace) space).getOwner(), dice);
            System.out.println("You paid rent to " + ((PropertySpace) space).getOwner());
            controller.drawPlayerBoxes(players);
            controller.showMessage("You paid M" + rentAmount + " rent to "
                    +  ((PropertySpace) space).getOwner().getPlayerName() + ".");
        }
    }

    //private boolean sendToJail(Scanner scan, Player player) {
    private void sendToJail(Player player) {
        //int choice;
/*
        if(player.getGetOutOfJailFreeCount() > 0 ) {
            System.out.println("Do you want to use your GOOJF card? (1-yes, 0-no) ");
            choice = scan.nextInt();
            scan.nextLine();
            if(choice == 1) {
                player.setGetOutOfJailFreeCount(player.getGetOutOfJailFreeCount() - 1);
            } else {
                player.setJailed(true);
                player.setCurrentSpace(board.getSpace(10));
                return true;
            }
        } else {
*/
            player.setJailed(true);
            player.setCurrentSpace(board.getSpace(10));
            /*
            return true;
        }
        return false;
        */
    }

    //private void drawCard(Scanner scan, Player player, CardSpace space) {
    private void drawCard(Player player, CardSpace space) {
        int choice;
        Card card = board.drawCard(space.getType());
        //System.out.println("Do you want to open it now or postpone?");
        //choice = scan.nextInt();
        //scan.nextLine();

        //if(choice == 1) {

        if(controller.postponeCard()) {
            ArrayList<Card> cards = player.getPostponedCards();
            cards.add(card);
            player.setPostponedCards( cards );
        } else {
            //System.out.println(card.getCardText());
            openCard(card);
        }
    }

    //need to be implemented in GUI
    private void openPostponedCard(Card card){
        if(currentPlayer.getPostponedCards().contains(card)) {
            openCard(card);
            ArrayList<Card> postponedCards = currentPlayer.getPostponedCards();
            postponedCards.remove(card);
            currentPlayer.setPostponedCards(postponedCards);
        }
    }

    private void openCard(Card card){
        System.out.println(card.getCardText());
        controller.showMessage(card.getCardText());
        if(card.isAdvance()){
            AdvanceEvent event = (AdvanceEvent) card.getCardEvent();
            int currentPos = currentPlayer.getCurrentSpace().getIndex();
            int currentPlayerIndex = -1;
            for (int i = 0; i < LAP; i++) {
                if (currentPlayer == players[i]) {
                    currentPlayerIndex = i;
                }
            }
            controller.drawToken(currentPlayerIndex, currentPos, event.getTargetSpace().getIndex());
            currentPlayer.setCurrentSpace(event.getTargetSpace());
            if(event.getTargetSpace().getIndex() <= currentPos && event.isCanCollectSalary()){
                currentPlayer.setMoney( currentPlayer.getMoney() + 200
                        + currentPlayer.getToken().getSalaryChange());
                controller.drawPlayerBoxes(players);
                controller.showMessage("Your salary is paid");
            }
        } else if(card.isCollect()){
            CollectEvent event = (CollectEvent) card.getCardEvent();
            if(event.isFromBank()){
                currentPlayer.setMoney( currentPlayer.getMoney() + event.getAmount());
            }
            else {
                for (int i = 0; i < 4; i++) {
                    if (!players[i].getPlayerName().equals(currentPlayer.getPlayerName())){
                        players[i].payPlayer(currentPlayer, event.getAmount());
                    }
                }
            }
        } else if(card.isGoToJail()){
            GoToJailEvent event = (GoToJailEvent) card.getCardEvent();
            //scanner???
            //sendToJail(scan, currentPlayer);
            if (event.isCanCollectSalary() && currentPlayer.getCurrentSpace().getIndex() >= 10){
                currentPlayer.setMoney( currentPlayer.getMoney() + 200
                        + currentPlayer.getToken().getSalaryChange());
            }
        } else if(card.isPay()) {
            PayEvent event = (PayEvent) card.getCardEvent();
            if (event.isToBank()){
                currentPlayer.setMoney(currentPlayer.getMoney() - event.getAmount());
            }
            else{
                for (int i = 0; i < 4; i++) {
                    if (!players[i].getPlayerName().equals(currentPlayer.getPlayerName())){
                        currentPlayer.payPlayer(players[i], event.getAmount());
                    }
                }
            }
        } else if(card.isGetOutOfJailFree()) {
            currentPlayer.setGetOutOfJailFreeCount(currentPlayer.getGetOutOfJailFreeCount() + 1 );
        } else if(card.isThief() ) {
            Player target = players[(int) (Math.random() * 4)];
            board.deployThief(target);
        }
    }

    /*
    private void turnDecisions(Scanner scan, Space space) {
        int choice;
        do {
            System.out.println("Select choice: ");
            choice = scan.nextInt();
            scan.nextLine();

            if (choice == 1) {
                // Trade
                System.out.println("Select player: ");
                int player = scan.nextInt();
                Player selectedPlayer = players[player];
                bank.startTrade(currentPlayer, selectedPlayer);
            }//trade
            else if (choice == 2) {
                build();
            }//build
            else if (choice == 3) {
                //show suitable properties
                System.out.println("Select property: ");
                int prop = scan.nextInt();
                currentPlayer.getProperties().get(prop).mortgage();
            } //mortgage
            else if (choice == 4) {
                //show suitable properties
                System.out.println("Select property: ");
                int prop = scan.nextInt();
                currentPlayer.getProperties().get(prop).liftMortgage();
            } //redeem

        } while (choice != 5); //finish turn
    }
    */

    private void build(Scanner scan, PropertySpace space) {
        if(space.getAssociatedProperty().getCard() instanceof LandTitleDeedCard &&
                ownsAllTitlesFromSameGroup(currentPlayer, space.getAssociatedProperty()) ) {
            System.out.println("Would you like to build on your property? (1-yes, 0-no)");
            int prop = scan.nextInt();
            if(prop == 1) {
                //show suitable properties
                ArrayList<Property> options = currentPlayer.getAllTitlesFromSameGroup(space.getAssociatedProperty());
                for(int i = 0; i < options.size(); i++)
                    System.out.println(i + ": " + options.get(i).getCard().getPropertyName());
                System.out.println("Select property: ");
                prop = scan.nextInt();
                boolean canBuild = true;
                for(int i = 0; i < options.size(); i++) {
                    if( i != prop && options.get(prop).getNumOfHouses() - options.get(i).getNumOfHouses() >= 1)
                        canBuild = false;
                }
                if(canBuild) {
                    options.get(prop).buildHouse();
                    int amount;
                    if(options.get(prop).isHotel())
                        amount = ((LandTitleDeedCard)options.get(prop).getCard()).getHouseCost();
                    else
                        amount = ((LandTitleDeedCard)options.get(prop).getCard()).getHotelCost();
                    currentPlayer.payBank((int)(amount * currentPlayer.getToken().getBuildingCostMultiplier()));
                    System.out.println("You upgraded your property.");
                } else {
                    System.out.println("You cannot build on this property before " +
                            "increasing the houses of the other properties.");
                }
            }
        } else
            System.out.println("Sorry, you have no property to build on. ");
    }

    //input from UI
    private boolean finishTurn() {
        return false;
    }

    private void initializingLap() {
        for (int i = 0; i < LAP; i++) {
            players[i].setCurrentSpace(board.getSpace(0));
        }
        int[] dice;
        int[] diceSums = new int[LAP];
        boolean digital;
        for(int i = 0; i < LAP; i++) {
            //dice = rollDice(); //when player clicks the button
            digital = ( players[i] instanceof DigitalPlayer);
            System.out.println(players[i] + " is computer: " + digital);
            dice = controller.rollDice(players[i].getPlayerName(), digital);
            diceSums[i] = dice[0] + dice[1];
            //change player turn
        }
        //calculate player turn order
        Player[] order = new Player[LAP];
        int player = 0;
        int max = 0;
        for(int i = 0; i < LAP; i++) {
            max = 0;
            for(int j = 0; j < LAP; j++) {
                if(max < diceSums[j]) {
                    max = diceSums[j];
                    player = j;
                }
            }
            order[i] = players[player];
            diceSums[player] = 0;
        }
        players = order;

        //token choose
        for(int i = 0; i < LAP; i++) {
            players[i].setToken(new Token(controller.chooseToken(players[i].getPlayerName(),
                        players[i] instanceof DigitalPlayer)));

            controller.setTokenImage(i, players[i].getToken().getTokenName());
            controller.drawToken(i, -1, 0);
            //change player turn
        }
        controller.drawPlayerBoxes(players);
    }

    private boolean isGameEnd() {
        int count = 0;
        for (Player p: players ) {
            if(p.isBankrupt() )
                count++;
        }
        return (count > 2) || (lapCount == lapLimit);
    }

    private int[] rollDice() {
        int[] dice = new int[2];
        dice[0] = (int) (Math.random() * 6 + 1);
        dice[1] = (int) (Math.random() * 6 + 1);
        return dice;
    }

    private Player createDigitalPlayer(String name) {
        return new DigitalPlayer(name);
    }

    public void startGame() {
        controller.setMap(board);
        gameLoop();
    }


    public void endGame() {
        int money = players[0].getMoney();
        int richest = 0;
        for (int i = 1; i < playerCount; i++ ) {
            if( money < players[i].getMoney() ) {
                money = players[i].getMoney();
                richest = i;
            } else if( money == players[i].getMoney() ) {
                if(players[richest].getProperties().size() < players[i].getProperties().size())
                    richest = i;
                money = players[i].getMoney();
                richest = i;
            }
        }
        //go to scoreboard with richest player as the winner
        System.out.println("Winner is: " + players[richest].getPlayerName());
    }

    //input from UI
    public void restartGame() {
        lapCount = 0;
        bank = new Bank();
        for ( Player p : players ) {
            p.reset();
        }
    }

    //input from UI
    public boolean saveGame() {
        //save to database
        return true;
    }
}
