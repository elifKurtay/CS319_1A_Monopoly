package FileManagement;


import board.Board;
import entities.DigitalPlayer;
import entities.Player;
import entities.Property;
import frontend.GameScreenController;
import game.Game;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

//This class is to save and load game and log exceptions
public class FileManager {

    //This instance is static in order to apply singleton pattern.
    private static FileManager instance;

    //Constructor is private and it can only be called from the getInstance method
    private FileManager(){

    }

    /**
     * This method is static so that the instance attribute will be created only once.
     * This method checks if the instance is null and if it is null it constructs a
     * FileManager object, otherwise it returns the instance.
     * @return the instance which is a FileManager object
     */
    public static FileManager getInstance() {
        if(instance == null)
            instance = new FileManager();
        return instance;
    }

    /**
     * This method saves the player and board objects in the game in serialized form together
     * with other game information written into a text file. The files are saved into a folder
     * named as the player names and the date of the game.
     * @param game current serialized game object
     * @return true if the game is saved succesfully, otherwise false
     */
    public boolean saveGame(Game game) {
        try {
            if( ! new File("savedGames").mkdir())
                System.out.println("SAVE DIR CANNOT BE CREATED or ALREADY CREATED");

            //check for existing save
            File folder = new File("savedGames");
            File[] listOfFiles = folder.listFiles();
            ArrayList<String> fileNames = new ArrayList<>();
            String folderName = "savedGames\\";
            if(listOfFiles != null) {
                for (File listOfFile : listOfFiles) {
                    fileNames.add(listOfFile.getName());
                    if( listOfFile.getName().contains(game.getGameName()) ) {
                        folderName += listOfFile.getName();
                        break;
                    }
                }
            }
            //creating new folder, a new save
            if(! folderName.contains(game.getGameName())) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH.mm");
                folderName += game.getPlayers()[0].getPlayerName() + "_" + game.getPlayers()[1].getPlayerName() + "_" +
                        game.getPlayers()[2].getPlayerName() + "_" + game.getPlayers()[3].getPlayerName() + "_" +
                        dtf.format(LocalDateTime.now()) + "_" + game.getGameName();
            }
            if( ! new File(folderName).mkdir())
                System.out.println("SAVE DIR CANNOT BE CREATED");
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
            //written in the form: lapLimit|playerCount|currentPlayerName|lapCount|Player1ISdigital|Player2ISdigital|Player3ISdigital|Player4ISdigital|groupArray
            String otherGameInfo = "" + game.getLapLimit() + "|" + game.getPlayerCount() + "|" +
                    game.getCurrentPlayer().getPlayerName() + "|" + game.getLapCount();
            for(int i = 0; i < 4; i++)
                if(game.getPlayers()[i] instanceof DigitalPlayer)
                    otherGameInfo += "|true";
                else
                    otherGameInfo += "|false";

            int[] noOfPropertiesInGroups = Property.numberOfPropertiesInGroups;
            for(int i = 0; i < noOfPropertiesInGroups.length; i++){
                otherGameInfo += "|" + noOfPropertiesInGroups[i];
            }

            FileWriter gameInfoWriter = new FileWriter(folderName + "\\other_game_info.txt");
            gameInfoWriter.write(otherGameInfo);
            gameInfoWriter.close();

            //closing file input stream and object output stream
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved");
            return true;
        } catch (IOException i) {
            i.printStackTrace();
            return false;
        }
    }


    /**
     * This method loads the game with the given folder name. It deserializes the files inside that folder
     * and creates a new game.
     * @param folderName
     * @param controller
     * @return the constructed game
     */
    public Game loadGame(String folderName, GameScreenController controller){
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

            int[] arr = new int[sArray.length - 8];
            for(int i = 8; i < sArray.length; i++)
                arr[i-8] = Integer.parseInt(sArray[i]);

            Property.setNumberOfPropertiesInGroups(arr);

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

            String gameName = folderName.split("_")[5];
            g = new Game(gameName, board, players, lapLimit, playerCount, currentPlayerName, lapCount, controller);
            in.close();
            fileIn.close();
            return g;
        } catch (Exception e) {
            e.printStackTrace();
            return g;
        }
    }

    /**
     * This method writes the exceptions into a text file for usage in the future. The exceptions
     * are written into a folder named Exceptions. The name of the text files are combination of the exception name
     * and the date of the exception.
     * @param e
     */
    public void log(Exception e) {
        try{
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            new File("Exceptions").mkdir();

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH.mm");
            FileWriter exceptionWriter = new FileWriter("Exceptions\\" + e.getClass().getCanonicalName() +
                    dtf.format(LocalDateTime.now()) + ".txt");
            exceptionWriter.write(exceptionAsString);
            exceptionWriter.close();
        }
        catch (Exception exception) {}
    }

    public void delete(String folderName){
        File folder = new File(folderName);
        File[] listOfFiles = folder.listFiles();
        if(listOfFiles != null) {
            for (File f : listOfFiles) {
                f.delete();
            }
            folder.delete();
        }
    }
}
