package game;

import FileManagement.FileManager;
import board.*;
import card.Card;
import entities.DigitalPlayer;
import entities.EasyStrategy;
import entities.Player;
import entities.Token;
import event.*;
import frontend.GameScreenController;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;

public class Game {

    private FileManager fileManager = FileManager.getInstance();
    @Getter private static final int LAP = 4;
    @Getter private final Board board;
    @Getter private final int lapLimit; //given -1 if game mod is survival
    @Getter private final int playerCount;

    @Getter @Setter private Player[] players;
    @Getter @Setter private Player currentPlayer;

    @Getter private int lapCount;

    private GameScreenController controller;

    public Game(File map, int playerCount, String[] playerNames, int turnLimit, GameScreenController controller) {
        this.playerCount = playerCount;
        this.lapLimit = turnLimit;
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

    //for loading the game, to be called by the file manager
    public Game(Board board, Player[] players, int lapLimit, int playerCount, String currentPlayer, int lapCount,
                GameScreenController controller){
        this.board = board;
        this.players = players;
        this.lapLimit = lapLimit;
        this.playerCount = playerCount;
        for(Player p: players)
            if(p.getPlayerName().equals(currentPlayer)){
                this.currentPlayer = p;
                break;
            }

        this.lapCount = lapCount;
        this.controller = controller;
    }

    public Game(Game loadedGame) {
        this.playerCount = loadedGame.playerCount;
        this.players = loadedGame.players;
        this.lapCount = loadedGame.lapCount;
        this.lapLimit = loadedGame.lapLimit;
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
                dice = controller.rollDice(currentPlayer.getPlayerName(), digitalPlayer);

                currentPlayer.setCurrentDiceSum(dice[0] + dice[1]);
                //calculating next player
                if (dice[0] == dice[1]) {
                    doublesCount++;
                } else
                    doublesCount = 0;

                //checking jail conditions
                if (!currentPlayer.isJailed() && dice[0] == dice[1] && doublesCount == 3){
                    sendToJail(currentPlayer);
                    controller.showMessage("You are sent to jail!");
                    continue;
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
                //else if only in jail, ask to use GOOJF card OR PAY?

                int oldIndex = currentPlayer.getCurrentSpace().getIndex();
                //move on board
                int boardIndex = currentPlayer.getCurrentSpace().getIndex() + dice[0] + dice[1];
                if(boardIndex >= 40) { //passed Go space
                    currentPlayer.setMoney( currentPlayer.getMoney() + 200
                            + currentPlayer.getToken().getSalaryChange());
                    boardIndex = boardIndex % 40;
                    controller.drawPlayerBoxes(players);
                    controller.showMessage("Your salary is paid!");
                }

                Space space = board.getSpace( boardIndex );
                currentPlayer.setCurrentSpace( space );
                space.setLatestPlayer(currentPlayer);

                controller.drawToken(i, oldIndex, boardIndex);

                // Salary is paid twice if it is paid on GoSpace too
                /*
                if(space instanceof GoSpace) {
                    currentPlayer.setMoney( currentPlayer.getMoney() + 200
                            + currentPlayer.getToken().getSalaryChange());
                    controller.showMessage("Your salary is paid!");
                } else */
                if( space instanceof CardSpace) {
                    System.out.println("Draw a card!");
                    drawCard(currentPlayer, (CardSpace) space);
                } else if( space instanceof GoToJailSpace) {
                    sendToJail(currentPlayer);
                    controller.drawToken(i, boardIndex, 10);
                    controller.showMessage("You are sent to jail!");
                    continue;
                } else if( space instanceof JailSpace ) {
                    controller.showMessage("You are visiting jail");
                } else if( space instanceof TaxSpace) {
                    int payment = (int) (((TaxSpace) space).getTax()
                            * currentPlayer.getToken().getTaxMultiplier());
                    currentPlayer.setMoney(currentPlayer.getMoney() - payment);
                    controller.drawPlayerBoxes(players);
                    controller.showMessage("You paid " + payment + "M for tax.");
                } else if(space instanceof WheelOfFortuneSpace) {
                    ((WheelOfFortuneSpace) space).spinWheel();
                } else if(space instanceof PropertySpace){
                    cameToProperty((PropertySpace) space);
                }

                // if doubles count is greater than 0 the same player can play another round
                if (doublesCount > 0) {
                    i--;
                }
                if(currentPlayer instanceof DigitalPlayer) {
                    if(((DigitalPlayer) currentPlayer).decideOnTradeAction()) {
                        System.out.println("START TRADE");
                    }
                    if(((DigitalPlayer) currentPlayer).decideOnBuildAction()) {
                        System.out.println("DO BUILD");
                    }

                }
                controller.finishTurn(currentPlayer instanceof DigitalPlayer);
            }
            lapCount++;
        }
        endGame();
        System.out.println("Game Over!");
    }

    private void cameToProperty(PropertySpace space) {
        if(currentPlayer == space.getAssociatedProperty().getOwner()) { //own property
            controller.showMessage("This is your own property.");
            //can build on if they choose so
        } else if (space.getAssociatedProperty().getOwner() == null ) { //owned by bank
            //buy or auction
            if(currentPlayer instanceof DigitalPlayer)
                if( ((DigitalPlayer) currentPlayer).decideOnBuy(space.getAssociatedProperty()) ) {
                    //controller let others know of the buying action
                    System.out.println("Computer bought the property.");
                }
                else {
                    System.out.println("START AUCTION");
                    //int startingBid = ((DigitalPlayer) currentPlayer).startAuction(space.getAssociatedProperty());
                }
            else if (controller.buyProperty(space)) {
                currentPlayer.addProperty(space.getAssociatedProperty());
                currentPlayer.setMoney(currentPlayer.getMoney() - (int) (space.getAssociatedProperty().getValue()
                        * currentPlayer.getToken().getPropertyCostMultiplier() ));
                space.getAssociatedProperty().setOwner(currentPlayer);
                controller.drawPlayerBoxes(players);
            } else {
                //bank.startAuction(space.getAssociatedProperty());
                // START AUCTION FOR PROPERTY
            }
            System.out.println(space.getName() + " belongs to " + space.getAssociatedProperty().getOwner());
        } else { //owned by another player
            //pay rent
            //int rentAmount = currentPlayer.payRent(((PropertySpace) space).getOwner(), dice);
            int rentAmount = space.calculateRent(currentPlayer);
            currentPlayer.payPlayer(space.getAssociatedProperty().getOwner(), rentAmount);
            controller.drawPlayerBoxes(players);
            controller.showMessage("You paid M" + rentAmount + " rent to "
                    +  space.getAssociatedProperty().getOwner().getPlayerName() + ".");
        }
    }

    private void sendToJail(Player player) {
        player.setJailed(true);
        player.setCurrentSpace(board.getSpace(10));
    }

    // Could ask player in game loop controller.postponeCard() and draw card
    private void drawCard(Player player, CardSpace space) {
        Card card = board.drawCard(space.getType());

        /*
        if (player instanceof DigitalPlayer) {
            if(((DigitalPlayer) player).decidePostponeCard()) {
                ArrayList<Card> cards = player.getPostponedCards();
                cards.add(card);
                player.setPostponedCards( cards );
            } else {
                openCard(card);
            }
        }
        */
        //else {
            if(player instanceof DigitalPlayer) {
                //always open cards?
                card.getCardEvent().handleEvent(player, players, board);
            }
            else if(  controller.postponeCard()) {
                ArrayList<Card> cards = player.getPostponedCards();
                cards.add(card);
                player.setPostponedCards( cards );
            } else {
                openCard(card);
            }
        //}

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
        CardEvent ce = card.getCardEvent();
        ce.handleEvent(currentPlayer, players, board);
        /*controller.showMessage(card.getCardText());
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
        }*/
    }

    /*
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
     */

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
        return new DigitalPlayer(name, new EasyStrategy());
    }

    public void startGame() {
        controller.setMap(board);
        gameLoop();
    }

    public void continueGame() {
        controller.setMap(board);
        controller.drawPlayerBoxes(players);
        for(int i = 0; i < LAP; i++) {
            controller.setTokenImage(i, players[i].getToken().getTokenName());
            controller.drawToken(i, players[i].getCurrentSpace().getIndex(), players[i].getCurrentSpace().getIndex());
        }
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
        for ( Player p : players ) {
            p.reset();
        }
        gameLoop();
    }

    //input from UI
    public boolean saveGame() throws Exception{
        return fileManager.saveGame(this);
    }
}
