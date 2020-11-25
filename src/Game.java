import bank.Bank;
import board.Board;
import entities.Player;
import entities.Token;
import event.CardEventHandler;

import java.io.File;
import java.util.Scanner;

public class Game {
    private final int LAP = 4;
    private int playerCount;
    private Player[] players;

    private int turnCount;
    private int turnLimit; //given -1 if game mod is survival

    private Bank bank;
    private Board board;
    private CardEventHandler eventHandler;

    public Game(int playerCount, String[] playerNames, int turnLimit, File map ) {
        this.playerCount = playerCount;
        this.turnLimit = turnLimit;
        bank = new Bank();
        board = new Board(map);
        eventHandler = new CardEventHandler();
        turnCount = 0;

        players = new Player[LAP];
        for(int i = 0; i < LAP; i++) {
            if (i < playerCount)
                players[i] = new Player(playerNames[i]);
            else
                players[i] = createDigitalPlayer();
        }
    }

    public Game(Game loadedGame) {
        this.playerCount = loadedGame.playerCount;
        this.players = loadedGame.players;
        this.turnCount = loadedGame.turnCount;
        this.turnLimit = loadedGame.turnLimit;
        this.bank = loadedGame.bank;
        this.board = loadedGame.board;
        this.eventHandler = loadedGame.eventHandler;
    }

    void gameLoop() {
        int[] dice;
        int doublesCount = 0;
        Scanner scan = new Scanner(System.in);
        int choice = 0;

        while(! isGameEnd() ) {
            if(turnCount == 0)
                initilizingLap();
            for(int i = 0; i < LAP; i++) {
                dice = rollDice();
                if(!players[i].isJailed() && dice[0] == dice[1] && doublesCount == 3)
                    players[i].setJailed(true);
                else if(players[i].isJailed() && dice[0] == dice[1])
                    players[i].setJailed(false);
                else if( players[i].isJailed() && players[i].getJailedLapCount() >= players[i].getToken().getJailTime() ) {
                    players[i].setJailed(false);
                    players[i].setMoney(players[i].getMoney() - 5);
                }

                //move on board

                if(!players[i].isJailed()) {
                    do {
                        System.out.println("Select choice: ");
                        choice = scan.nextInt();
                        scan.nextLine();

                        if (choice == 1) {
                            System.out.println("Select player: ");
                            int player = scan.nextInt();
                            Player selectedPlayer = players[player];
                            bank.startTrade(players[i], selectedPlayer);
                        }//trade
                        else if (choice == 2) {
                            //show suitable properties
                            System.out.println("Select property: ");
                            int prop = scan.nextInt();
                            players[i].getProperties().get(prop).buildHouse();
                        }//build
                        else if (choice == 3) {
                            //show suitable properties
                            System.out.println("Select property: ");
                            int prop = scan.nextInt();
                            players[i].getProperties().get(prop).mortgage();
                        } //mortgage
                        else if (choice == 4) {
                            //show suitable properties
                            System.out.println("Select property: ");
                            int prop = scan.nextInt();
                            players[i].getProperties().get(prop).liftMortgage();
                        } //redeem

                    } while (choice != 5); //finish turn
                }
                //calculating next player
                if(dice[0] == dice[1]) {
                    doublesCount++;
                    i--;
                } else
                    doublesCount = 0;
                //change player
            }
            turnCount++;
        }
        endGame();
    }

    //input from UI
    private boolean finishTurn() {
        return false;
    }

    void initilizingLap() {
        int[] dice;
        int[] diceSums = new int[LAP];
        for(int i = 0; i < LAP; i++) {
            dice = rollDice(); //when player clicks the button
            diceSums[i] = dice[0] + dice[1];
            //change player turn
        }
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
            players[i].setToken(new Token((int) Math.random() * 8 + 1)); //when player clicks the button
            //change player turn
        }
    }

    boolean isGameEnd() {
        int count = 0;
        for (Player p: players ) {
            if(p.isBankrupt() )
                count++;
        }
        return (count > 2) || (turnCount == turnLimit);
    }

    int[] rollDice() {
        int[] dice = new int[2];
        dice[0] = (int) (Math.random() * 6 + 1);
        dice[1] = (int) (Math.random() * 6 + 1);
        return dice;
    }

    void endGame() {
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
    }

    boolean saveGame() {
        //save to database
        return true;
    }

    void restartGame() {
        turnCount = 0;
        bank = new Bank();
        for ( Player p : players ) {
            p.reset();
        }
    }

    Player createDigitalPlayer() {
        return new Player("John");
    }
}
