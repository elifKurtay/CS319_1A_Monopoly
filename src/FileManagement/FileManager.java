package FileManagement;


import board.Board;
import entities.DigitalPlayer;
import entities.Player;
import frontend.GameScreenController;
import game.Game;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class FileManager {
    private static FileManager instance;
    private String name;

    private FileManager(){

    }

    public static FileManager getInstance () {
        if(instance == null)
            instance = new FileManager();
        return instance;
    }

    public boolean saveGame(Game game) throws Exception{
        try {
            new File("savedGames").mkdir();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH.mm");
            String folderName = "savedGames\\" + game.getPlayers()[0].getPlayerName() + "_" + game.getPlayers()[1].getPlayerName() + "_" +
                    game.getPlayers()[2].getPlayerName() + "_" + game.getPlayers()[3].getPlayerName() + "_" +
                    dtf.format(LocalDateTime.now());
            new File(folderName).mkdir();
            FileOutputStream fileOut;
            ObjectOutputStream out;

            //player 1
            fileOut = new FileOutputStream(folderName + "\\p1.ser");
            out = new ObjectOutputStream(fileOut);
            out.writeObject(game.getPlayers()[0]);

            //player 2
            fileOut = new FileOutputStream(folderName + "\\p2.ser");
            out = new ObjectOutputStream(fileOut);
            out.writeObject(game.getPlayers()[1]);

            //player 3
            fileOut = new FileOutputStream(folderName + "\\p3.ser");
            out = new ObjectOutputStream(fileOut);
            out.writeObject(game.getPlayers()[2]);

            //player 4
            fileOut = new FileOutputStream(folderName + "\\p4.ser");
            out = new ObjectOutputStream(fileOut);
            out.writeObject(game.getPlayers()[3]);

            //board
            fileOut = new FileOutputStream(folderName + "\\board.ser");
            out = new ObjectOutputStream(fileOut);
            out.writeObject(game.getBoard());

            //other game information
            //written in the form: lapLimit|playerCount|currentPlayerName|lapCount|Player1ISdigital|Player2ISdigital|Player3ISdigital|Player4ISdigital
            String otherGameInfo = "" + game.getLapLimit() + "|" + game.getPlayerCount() + "|" +
                    game.getCurrentPlayer().getPlayerName() + "|" + game.getLapCount();
            for(int i = 0; i < 4; i++)
                if(game.getPlayers()[i] instanceof DigitalPlayer)
                    otherGameInfo += "|true";
                else
                    otherGameInfo += "|false";
            FileWriter gameInfoWriter = new FileWriter(folderName + "\\other_game_info.txt");
            gameInfoWriter.write(otherGameInfo);
            gameInfoWriter.close();

            //closing file input stream and object output stream
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved");
            return true;
        } catch (IOException i) {
            i.printStackTrace();
            return false;
        }
    }

    public Game loadGame(String folderName, GameScreenController controller) throws Exception{
        Game g = null;
        try {
            int lapLimit, playerCount, lapCount;
            String currentPlayerName, otherGameInformation;
            boolean p1Dig, p2Dig, p3Dig, p4Dig;
            otherGameInformation = "";
            System.out.println(folderName + "\\otherGameInformation.ser");
            File myObj = new File(folderName + "\\other_game_info.txt");
            Scanner scanner = new Scanner(myObj);
            while (scanner.hasNextLine())
                otherGameInformation += scanner.nextLine();

            scanner.close();

            System.out.println(otherGameInformation);
            String[] sArray = otherGameInformation.split("\\|");
            System.out.println(sArray[0]);
            lapLimit = Integer.parseInt(sArray[0]);
            playerCount = Integer.parseInt(sArray[1]);
            currentPlayerName = sArray[2];
            lapCount = Integer.parseInt(sArray[3]);
            p1Dig = Boolean.parseBoolean(sArray[4]);
            p2Dig = Boolean.parseBoolean(sArray[5]);
            p3Dig = Boolean.parseBoolean(sArray[6]);
            p4Dig = Boolean.parseBoolean(sArray[7]);

            FileInputStream fileIn;
            ObjectInputStream in;

            Player p1, p2, p3, p4;


            fileIn = new FileInputStream(folderName + "\\p1.ser");
            in = new ObjectInputStream(fileIn);
            if(p1Dig)
                p1 = (DigitalPlayer) in.readObject();
            else
                p1 = (Player) in.readObject();

            fileIn = new FileInputStream(folderName + "\\p2.ser");
            in = new ObjectInputStream(fileIn);
            if(p2Dig)
                p2 = (DigitalPlayer) in.readObject();
            else
                p2 = (Player) in.readObject();

            fileIn = new FileInputStream(folderName + "\\p3.ser");
            in = new ObjectInputStream(fileIn);
            if(p3Dig)
                p3 = (DigitalPlayer) in.readObject();
            else
                p3 = (Player) in.readObject();

            fileIn = new FileInputStream(folderName + "\\p4.ser");
            in = new ObjectInputStream(fileIn);
            if(p4Dig)
                p4 = (DigitalPlayer) in.readObject();
            else
                p4 = (Player) in.readObject();

            fileIn = new FileInputStream(folderName + "\\board.ser");
            in = new ObjectInputStream(fileIn);
            Board board = (Board) in.readObject();

            Player[] players = new Player[4];
            players[0] = p1;
            players[1] = p2;
            players[2] = p3;
            players[3] = p4;

            g = new Game(board, players, lapLimit, playerCount, currentPlayerName, lapCount, controller);
            in.close();
            fileIn.close();
            return g;
        } catch (Exception e) {
            e.printStackTrace();
            return g;
        }
    }
}
