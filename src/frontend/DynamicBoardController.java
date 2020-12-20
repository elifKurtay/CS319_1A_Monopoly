package frontend;

import board.*;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;

public class DynamicBoardController {
    @FXML
    private AnchorPane dynamicBoard;
    @FXML
    private Pane bottomBoard, rightBoard, leftBoard, topBoard, bottomLeftBoard, bottomRightBoard, topLeftBoard, topRightBoard;

    private ImageView[] tokenImages;

    public void initialize() {
        tokenImages = new ImageView[5];
    }

    public void setDynamicBoard(Board gameBoard) {
        System.out.println(gameBoard);

        if(gameBoard == null) {
            dynamicBoard = new AnchorPane();
            bottomBoard = new Pane();
            rightBoard = new Pane();
            leftBoard = new Pane();
            topBoard = new Pane();
            bottomLeftBoard = new Pane();
            bottomRightBoard = new Pane();
            topLeftBoard = new Pane();
            topRightBoard = new Pane();
            return;
        }
        // Need to reverse the non-corner spaces on the left and bottom sides of the map,
        // because the index 0 corresponds to the lower right space of the map
        // and the insertion to GUI elements happen from left to right or top to bottom
        // so the ordering of the spaces will be wrong

        Space[] spaces = gameBoard.getSpaces();
        String[] colors = gameBoard.getPropertyGroupColors();

        Space[] newSpaces = new Space[40];
        newSpaces[0] = spaces[0];
        for (int i = 0; i < 9; i++) {
            newSpaces[1+i] = spaces[9-i];
        }
        newSpaces[10] = spaces[10];
        for (int i = 0; i < 9; i++) {
            newSpaces[11+i] = spaces[19-i];
        }
        for (int i = 20; i < 40; i++) {
            newSpaces[i] = spaces[i];
        }
        spaces = newSpaces;

        for (int i = 0; i < 40; i++) {
            BorderPane spacePane = new BorderPane();

            Label propertyName = new Label(spaces[i].getName());
            propertyName.setWrapText(true);
            propertyName.textAlignmentProperty().setValue(TextAlignment.CENTER);

            VBox vb = new VBox();
            vb.setAlignment(Pos.CENTER);
            spacePane.setCenter(vb);

            if (spaces[i] instanceof PropertySpace) {
                PropertySpace currentSpace = (PropertySpace) spaces[i];
                Label price = new Label("M" + currentSpace.getAssociatedProperty().getValue());

                vb.getChildren().addAll(propertyName, price);

                if (currentSpace.getType() == PropertySpace.PropertyType.LAND) {
                    Pane colorPane = new Pane();
                    // Get the color specified for this property group, read from the json file
                    colorPane.setStyle("-fx-background-color: #"
                            + colors[currentSpace.getAssociatedProperty().getPropertyGroup()]);
                    colorPane.setId("colorPane");
                    if (i < 10) {
                        colorPane.getStyleClass().add("colortop");
                        spacePane.setTop(colorPane);
                    }
                    else if (i < 20) {
                        colorPane.getStyleClass().add("colorside");
                        spacePane.setRight(colorPane);
                    }
                    else if (i < 30) {
                        colorPane.getStyleClass().add("colortop");
                        spacePane.setBottom(colorPane);
                    }
                    else {
                        colorPane.getStyleClass().add("colorside");
                        spacePane.setLeft(colorPane);
                    }
                }
            }
            else {
                vb.getChildren().add(propertyName);
            }
            HBox tokenBox = new HBox();
            tokenBox.setId("tokenBox");
            vb.getChildren().add(tokenBox);

            if (i % 10 == 0) {
                spacePane.getStyleClass().add("corner");
            }

            if (i == 0) {
                bottomRightBoard.getChildren().add(spacePane);
            }
            else if (i < 10) {
                spacePane.getStyleClass().add("vspace");
                bottomBoard.getChildren().add(spacePane);
            }
            else if (i == 10) {
                bottomLeftBoard.getChildren().add(spacePane);
            }
            else if (i < 20) {
                spacePane.getStyleClass().add("hspace");
                leftBoard.getChildren().add(spacePane);
            }
            else if (i == 20) {
                topLeftBoard.getChildren().add(spacePane);
            }
            else if (i < 30) {
                spacePane.getStyleClass().add("vspace");
                topBoard.getChildren().add(spacePane);
            }
            else if (i == 30) {
                topRightBoard.getChildren().add(spacePane);
            }
            else {
                spacePane.getStyleClass().add("hspace");
                rightBoard.getChildren().add(spacePane);
            }

        }

    }

    public void setTokenImage(int playerNo, String tokenName) {
        Image token = new Image("img/token/cropped/" + tokenName + ".png");
        ImageView iv = new ImageView(token);
        iv.setFitHeight(30);
        iv.setFitWidth(30);

        tokenImages[playerNo] = iv;
    }

    public void drawToken(int playerNo, int oldIndex, int newIndex) {
        ImageView iv = tokenImages[playerNo];

        // oldIndex can only be -1 when the token hasn't been drawn on the board
        if (oldIndex == -1) {
            // draw the token on the "go space"
           //getTokenBox(0).getChildren().add(iv);
            if (playerNo < 4) {
                ((HBox) getSpaceBox(0).lookup("#tokenBox")).getChildren().add(iv);
            }
            else{
                if (((HBox) getSpaceBox(newIndex).lookup("#tokenBox")).getChildren().contains(iv))
                    ((HBox) getSpaceBox(newIndex).lookup("#tokenBox")).getChildren().remove(iv);
                ((HBox) getSpaceBox(newIndex).lookup("#tokenBox")).getChildren().add(iv);
            }
        }
        else {
            // remove the token from the old index before drawing it at the new index
            //getTokenBox(oldIndex).getChildren().remove(iv);
            ((HBox) getSpaceBox(oldIndex).lookup("#tokenBox")).getChildren().remove(iv);

            // draw the token at the new index
            //getTokenBox(newIndex).getChildren().add(iv);
            if (newIndex != -1) {
                ((HBox) getSpaceBox(newIndex).lookup("#tokenBox")).getChildren().add(iv);
            }
        }
    }

    public void drawHouse(int index, int numOfHouses) {
        ((Pane) getSpaceBox(index).lookup("#colorPane")).getChildren().clear();
        if (numOfHouses == 5) {
            ((Pane) getSpaceBox(index).lookup("#colorPane")).getChildren().add(new Label("HOTEL"));
        }
        else {
            for (int i = 0; i < numOfHouses; i++) {
                ((Pane) getSpaceBox(index).lookup("#colorPane")).getChildren().add(new Label("HOUSE"));
            }
        }
    }

    // returns the tokenBox for the space with given index
    private Node getSpaceBox(int index) {
        Pane[] corners = {bottomRightBoard, bottomLeftBoard, topLeftBoard, topRightBoard};
        Pane[] sides = {bottomBoard, leftBoard, topBoard, rightBoard};

        Node spaceBoxNode;

        // if the index is a corner
        if (index % 10 == 0) {
            spaceBoxNode = corners[index/10].getChildren().get(0);
        }
        // if the index is on the sides
        else {
            if (index < 10) {
                spaceBoxNode = sides[index/10].getChildren().get(9 - index);
            }
            else if (index < 20) {
                spaceBoxNode = sides[index/10].getChildren().get(19 - index);
            }
            else if (index < 30) {
                spaceBoxNode = sides[index/10].getChildren().get(index - 21);
            }
            else {
                spaceBoxNode = sides[index/10].getChildren().get(index - 31);
            }
        }

        return spaceBoxNode;
    }

}
