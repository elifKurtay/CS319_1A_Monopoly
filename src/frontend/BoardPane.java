package frontend;

import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
public class BoardPane extends Pane {

    BoardPane() throws FileNotFoundException {
        System.out.println("abc");
        this.setStyle("-fx-background-color: #D8FBF5;");
        this.setPrefSize(679, 679);
        this.setLayoutY(77);
        this.setLayoutX(20);

        Rectangle innerRectangle = new Rectangle(107, 107, 465, 465);
        innerRectangle.setFill(Color.web("D8FBF5"));
        innerRectangle.setStroke(Color.BLACK);
        this.getChildren().add(innerRectangle);

        //Red Rectangles
        Rectangle redRectangle1 = new Rectangle(107, 91, 51, 15);
        redRectangle1.setFill(Color.web("FA0909"));
        redRectangle1.setStroke(Color.BLACK);
        this.getChildren().add(redRectangle1);

        Rectangle redRectangle2 = new Rectangle(210, 91, 51, 15);
        redRectangle2.setFill(Color.web("FA0909"));
        redRectangle2.setStroke(Color.BLACK);
        this.getChildren().add(redRectangle2);

        Rectangle redRectangle3 = new Rectangle(262, 91, 51, 15);
        redRectangle3.setFill(Color.web("FA0909"));
        redRectangle3.setStroke(Color.BLACK);
        this.getChildren().add(redRectangle3);

        //Yellow Rectangles
        Rectangle yellowRectangle1 = new Rectangle(365, 91, 51, 15);
        yellowRectangle1.setFill(Color.web("F5FA09"));
        yellowRectangle1.setStroke(Color.BLACK);
        this.getChildren().add(yellowRectangle1);

        Rectangle yellowRectangle2 = new Rectangle(417, 91, 51, 15);
        yellowRectangle2.setFill(Color.web("F5FA09"));
        yellowRectangle2.setStroke(Color.BLACK);
        this.getChildren().add(yellowRectangle2);

        Rectangle yellowRectangle3 = new Rectangle(520, 91, 51, 15);
        yellowRectangle3.setFill(Color.web("F5FA09"));
        yellowRectangle3.setStroke(Color.BLACK);
        this.getChildren().add(yellowRectangle3);

        //Green Rectangles
        Rectangle greenRectangle1 = new Rectangle(573, 107, 15, 51);
        greenRectangle1.setFill(Color.web("40BA21"));
        greenRectangle1.setStroke(Color.BLACK);
        this.getChildren().add(greenRectangle1);

        Rectangle greenRectangle2 = new Rectangle(573, 159, 15, 51);
        greenRectangle2.setFill(Color.web("40BA21"));
        greenRectangle2.setStroke(Color.BLACK);
        this.getChildren().add(greenRectangle2);

        Rectangle greenRectangle3 = new Rectangle(573, 262, 15, 51);
        greenRectangle3.setFill(Color.web("40BA21"));
        greenRectangle3.setStroke(Color.BLACK);
        this.getChildren().add(greenRectangle3);

        //Blue Rectangles
        Rectangle blueRectangle1 = new Rectangle(573, 417, 15, 51);
        blueRectangle1.setFill(Color.web("27397B"));
        blueRectangle1.setStroke(Color.BLACK);
        this.getChildren().add(blueRectangle1);

        Rectangle blueRectangle2 = new Rectangle(573, 520, 15, 51);
        blueRectangle2.setFill(Color.web("27397B"));
        blueRectangle2.setStroke(Color.BLACK);
        this.getChildren().add(blueRectangle2);

        //Brown Rectangles
        Rectangle brownRectangle1 = new Rectangle(520, 573, 51, 15);
        brownRectangle1.setFill(Color.web("955500"));
        brownRectangle1.setStroke(Color.BLACK);
        this.getChildren().add(brownRectangle1);

        Rectangle brownRectangle2 = new Rectangle(417, 573, 51, 15);
        brownRectangle2.setFill(Color.web("955500"));
        brownRectangle2.setStroke(Color.BLACK);
        this.getChildren().add(brownRectangle2);

        //Pale Blue Rectangles
        Rectangle paleRectangle1 = new Rectangle(108, 573, 51, 15);
        paleRectangle1.setFill(Color.web("5FEEF8"));
        paleRectangle1.setStroke(Color.BLACK);
        this.getChildren().add(paleRectangle1);

        Rectangle paleRectangle2 = new Rectangle(159, 573, 51, 15);
        paleRectangle2.setFill(Color.web("5FEEF8"));
        paleRectangle2.setStroke(Color.BLACK);
        this.getChildren().add(paleRectangle2);

        Rectangle paleRectangle3 = new Rectangle(262, 573, 51, 15);
        paleRectangle3.setFill(Color.web("5FEEF8"));
        paleRectangle3.setStroke(Color.BLACK);
        this.getChildren().add(paleRectangle3);

        //Orange Rectangles
        Rectangle orangeRectangle1 = new Rectangle(92, 107, 15, 51);
        orangeRectangle1.setFill(Color.web("FF7F09"));
        orangeRectangle1.setStroke(Color.BLACK);
        this.getChildren().add(orangeRectangle1);

        Rectangle orangeRectangle2 = new Rectangle(92, 159, 15, 51);
        orangeRectangle2.setFill(Color.web("FF7F09"));
        orangeRectangle2.setStroke(Color.BLACK);
        this.getChildren().add(orangeRectangle2);

        Rectangle orangeRectangle3 = new Rectangle(92, 262, 15, 51);
        orangeRectangle3.setFill(Color.web("FF7F09"));
        orangeRectangle3.setStroke(Color.BLACK);
        this.getChildren().add(orangeRectangle3);

        //Purple Rectangles
        Rectangle purpleRectangle1 = new Rectangle(92, 365, 15, 51);
        purpleRectangle1.setFill(Color.web("DD20B3"));
        purpleRectangle1.setStroke(Color.BLACK);
        this.getChildren().add(purpleRectangle1);

        Rectangle purpleRectangle2 = new Rectangle(92, 417, 15, 51);
        purpleRectangle2.setFill(Color.web("DD20B3"));
        purpleRectangle2.setStroke(Color.BLACK);
        this.getChildren().add(purpleRectangle2);

        Rectangle purpleRectangle3 = new Rectangle(92, 520, 15, 51);
        purpleRectangle3.setFill(Color.web("DD20B3"));
        purpleRectangle3.setStroke(Color.BLACK);
        this.getChildren().add(purpleRectangle3);



        for(int i = 0; i < 10; i++){
            Line l1 = new Line(0, 107.0 + (i*51.61), 107, 107.0 + (i*51.61));
            Line l2 = new Line(572, 107.0 + (i*51.61), 679, 107.0 + (i*51.61));
            Line l3 = new Line(107.0 + (i*51.61), 107, 107.0 + (i*51.61), 0);
            Line l4 = new Line(107.0 + (i*51.61), 572, 107.0 + (i*51.61), 679);
            this.getChildren().add(l1);
            this.getChildren().add(l2);
            this.getChildren().add(l3);
            this.getChildren().add(l4);
        }

        this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        //Inserting Monopoly logo
        Image monopolyImage = new Image(new FileInputStream("assets/img/boardImages/monopolyLogo.png"));
        ImageView monopolyView = new ImageView(monopolyImage);
        monopolyView.setX(65);
        monopolyView.setY(282);
        monopolyView.setFitHeight(173);
        monopolyView.setFitWidth(550);
        monopolyView.setPreserveRatio(true);
        monopolyView.setRotate(-45);

        this.getChildren().add(monopolyView);


        //Inserting Community Chest images on small rectangles
        Image communityChestImage = new Image(new FileInputStream("assets/img/boardImages/communityChestLogo.jpg"));

        //First chest image
        ImageView communityView1 = new ImageView(communityChestImage);
        communityView1.setX(22);
        communityView1.setY(215);
        communityView1.setFitHeight(100);
        communityView1.setFitWidth(50);
        communityView1.setPreserveRatio(true);
        communityView1.setRotate(90);
        communityView1.setBlendMode(BlendMode.MULTIPLY);
        this.getChildren().add(communityView1);

        //Second chest image
        ImageView communityView2 = new ImageView(communityChestImage);
        communityView2.setX(605);
        communityView2.setY(215);
        communityView2.setFitHeight(100);
        communityView2.setFitWidth(50);
        communityView2.setPreserveRatio(true);
        communityView2.setRotate(-90);
        communityView2.setBlendMode(BlendMode.MULTIPLY);
        this.getChildren().add(communityView2);


        //Third chest image
        ImageView communityView3 = new ImageView(communityChestImage);
        communityView3.setX(469);
        communityView3.setY(605);
        communityView3.setFitHeight(100);
        communityView3.setFitWidth(50);
        communityView3.setPreserveRatio(true);
        communityView3.setBlendMode(BlendMode.MULTIPLY);
        this.getChildren().add(communityView3);


        //Inserting question marks
        //Orange question mark
        Image orangeQuestionImage = new Image(new FileInputStream("assets/img/boardImages/orangeQuestionMark.png"));
        ImageView orangeQuestion = new ImageView(orangeQuestionImage);
        orangeQuestion.setX(605);
        orangeQuestion.setY(367);
        orangeQuestion.setFitHeight(100);
        orangeQuestion.setFitWidth(50);
        orangeQuestion.setPreserveRatio(true);
        orangeQuestion.setRotate(-90);
        orangeQuestion.setBlendMode(BlendMode.MULTIPLY);
        this.getChildren().add(orangeQuestion);

        //Pink question mark
        Image pinkQuestionImage = new Image(new FileInputStream("assets/img/boardImages/pinkQuestionMark.png"));
        ImageView pinkQuestion = new ImageView(pinkQuestionImage);
        pinkQuestion.setX(211);
        pinkQuestion.setY(605);
        pinkQuestion.setFitHeight(100);
        pinkQuestion.setFitWidth(50);
        pinkQuestion.setPreserveRatio(true);
        pinkQuestion.setBlendMode(BlendMode.MULTIPLY);
        this.getChildren().add(pinkQuestion);

        //Blue question mark
        Image blueQuestionImage = new Image(new FileInputStream("assets/img/boardImages/blueQuestionMark.jpg"));
        ImageView blueQuestion = new ImageView(blueQuestionImage);
        blueQuestion.setX(158);
        blueQuestion.setY(20);
        blueQuestion.setFitHeight(100);
        blueQuestion.setFitWidth(50);
        blueQuestion.setPreserveRatio(true);
        //blueQuestion.setRotate(180);
        blueQuestion.setBlendMode(BlendMode.MULTIPLY);
        this.getChildren().add(blueQuestion);

        //Inserting train pictures
        Image trainImage = new Image(new FileInputStream("assets/img/boardImages/train.jpg"));
        //First train image
        ImageView train1 = new ImageView(trainImage);
        train1.setX(19);
        train1.setY(313);
        train1.setFitHeight(120);
        train1.setFitWidth(50);
        train1.setPreserveRatio(true);
        train1.setRotate(90);
        train1.setBlendMode(BlendMode.MULTIPLY);
        this.getChildren().add(train1);


        //Second train image
        ImageView train2 = new ImageView(trainImage);
        train2.setX(313);
        train2.setY(605);
        train2.setFitHeight(120);
        train2.setFitWidth(50);
        train2.setPreserveRatio(true);
        train2.setBlendMode(BlendMode.MULTIPLY);
        this.getChildren().add(train2);

        //Third train image
        ImageView train3 = new ImageView(trainImage);
        train3.setX(315);
        train3.setY(27);
        train3.setFitHeight(100);
        train3.setFitWidth(50);
        train3.setPreserveRatio(true);
        //train3.setRotate(180);
        train3.setBlendMode(BlendMode.MULTIPLY);
        this.getChildren().add(train3);

        //Fourth train image
        ImageView train4 = new ImageView(trainImage);
        train4.setX(603);
        train4.setY(315);
        train4.setFitHeight(100);
        train4.setFitWidth(50);
        train4.setPreserveRatio(true);
        train4.setRotate(-90);
        train4.setBlendMode(BlendMode.MULTIPLY);
        this.getChildren().add(train4);

        //Inserting super tax image
        Image superTaxImage = new Image(new FileInputStream("assets/img/boardImages/superTax.jpg"));
        ImageView superTax = new ImageView(superTaxImage);
        superTax.setX(600);
        superTax.setY(470);
        superTax.setFitHeight(120);
        superTax.setFitWidth(48);
        superTax.setPreserveRatio(true);
        superTax.setRotate(-90);
        superTax.setBlendMode(BlendMode.MULTIPLY);
        this.getChildren().add(superTax);

        //Inserting electric company image
        Image electricCompanyImage = new Image(new FileInputStream("assets/img/boardImages/electricLogo.png"));
        ImageView electricCompany = new ImageView(electricCompanyImage);
        electricCompany.setX(20);
        electricCompany.setY(470);
        electricCompany.setFitHeight(120);
        electricCompany.setFitWidth(48);
        electricCompany.setPreserveRatio(true);
        electricCompany.setRotate(90);
        electricCompany.setBlendMode(BlendMode.MULTIPLY);
        this.getChildren().add(electricCompany);

        //Inserting wheel of fortune
        Image wheelImage = new Image(new FileInputStream("assets/img/boardImages/wheelOfFortune.png"));
        ImageView wheel = new ImageView(wheelImage);
        wheel.setX(13);
        wheel.setY(7);
        wheel.setFitHeight(120);
        wheel.setFitWidth(80);
        wheel.setPreserveRatio(true);
        wheel.setRotate(90);
        wheel.setBlendMode(BlendMode.COLOR_BURN);
        this.getChildren().add(wheel);

        //Wheel of Fortune text
        Text wheelOfFortuneText = new Text("Wheel of\nFortune");
        this.getChildren().add(wheelOfFortuneText);
        wheelOfFortuneText.setFont(Font.font(8));
        wheelOfFortuneText.setX(37);
        wheelOfFortuneText.setY(90);
        //wheelOfFortuneText.setRotate(-45);
        wheelOfFortuneText.setTextAlignment(TextAlignment.CENTER);

        //Inserting go to jail image
        Image goJailImage = new Image(new FileInputStream("assets/img/boardImages/goJail.jpg"));
        ImageView goJail = new ImageView(goJailImage);
        goJail.setX(597);
        goJail.setY(20);
        goJail.setFitHeight(80);
        goJail.setFitWidth(60);
        goJail.setPreserveRatio(true);
        //goJail.setRotate(45);
        goJail.setBlendMode(BlendMode.MULTIPLY);
        this.getChildren().add(goJail);

        //Inserting in jail image
        Image inJailImage = new Image(new FileInputStream("assets/img/boardImages/inJailLogo.png"));
        ImageView inJail = new ImageView(inJailImage);
        inJail.setX(32);
        inJail.setY(571);
        inJail.setFitHeight(76);
        inJail.setFitWidth(76);
        inJail.setPreserveRatio(true);
        inJail.setBlendMode(BlendMode.MULTIPLY);
        this.getChildren().add(inJail);

        //Go Logo image
        Image goImage = new Image(new FileInputStream("assets/img/boardImages/GoLogo.png"));
        ImageView go = new ImageView(goImage);
        go.setX(595);
        go.setY(595);
        go.setFitHeight(76);
        go.setFitWidth(76);
        go.setPreserveRatio(true);
        go.setBlendMode(BlendMode.MULTIPLY);
        this.getChildren().add(go);

        //Water Works image
        Image waterImage = new Image(new FileInputStream("assets/img/boardImages/waterWorks.png"));
        ImageView water = new ImageView(waterImage);
        water.setX(461);
        water.setY(20);
        water.setFitHeight(65);
        water.setFitWidth(65);
        water.setPreserveRatio(true);
        water.setBlendMode(BlendMode.MULTIPLY);
        water.setRotate(180);
        this.getChildren().add(water);

        //Income tax Logo
        Image incomeTaxImage = new Image(new FileInputStream("assets/img/boardImages/incomeTaxLogo.png"));
        ImageView income = new ImageView(incomeTaxImage);
        income.setX(372);
        income.setY(610);
        income.setFitHeight(35);
        income.setFitWidth(35);
        income.setPreserveRatio(true);
        income.setBlendMode(BlendMode.MULTIPLY);
        this.getChildren().add(income);

        //Big Chance Card Image
        Image bigChanceImage = new Image(new FileInputStream("assets/img/boardImages/bigChance.png"));
        ImageView bigChance = new ImageView(bigChanceImage);
        bigChance.setX(370);
        bigChance.setY(398);
        bigChance.setFitHeight(240);
        bigChance.setFitWidth(180);
        bigChance.setPreserveRatio(true);
        bigChance.setRotate(-45);
        this.getChildren().add(bigChance);

        //Big Community Chest Image
        Image bigChestImage = new Image(new FileInputStream("assets/img/boardImages/bigChest.png"));
        ImageView bigChest = new ImageView(bigChestImage);
        bigChest.setX(128);
        bigChest.setY(158);
        bigChest.setFitHeight(240);
        bigChest.setFitWidth(180);
        bigChest.setPreserveRatio(true);
        bigChest.setRotate(135);
        this.getChildren().add(bigChest);
    }
}
