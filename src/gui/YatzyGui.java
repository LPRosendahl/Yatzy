package gui;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.Die;
import models.RaffleCup;
import models.YatzyResultCalculator;

import java.text.DecimalFormat;
import java.util.*;

import static java.lang.Integer.parseInt;

public class YatzyGui extends Application {


    private CheckBox[] chbHold = new CheckBox[5];
    private int kastTilbage = 3;
    private Button btnKast = new Button("Kast terningerne");
    private RaffleCup raffleCup = new RaffleCup();
    private Label[] lblTerninger;
    private Label lblThrowsLeft;


    private final DecimalFormat df = new DecimalFormat("#,##0"); //bruges til formatering af int


    private Map<TextField, Integer> values = new LinkedHashMap<>();
    // Set = "Samling" (her af TextFields) og kan ikke indeholde dubletter. HashSet giver TextField'erne en random placering. Bruges til de låste felter
    private Set<TextField> locked = new HashSet<>();
    private List<TextField> scoreFields; // List af TextFields (her felter) der kan låses



    // scorefelter
    private TextField txt1ere, txt2ere, txt3ere, txt4ere, txt5ere, txt6ere;
    private TextField txtSum, txtBonus;
    private TextField txtEtPar, txtToPar, txt3Ens, txt4Ens, txtLilleStraight, txtStoreStraight, txtFuldtHus, txtChance, txtYatzy, txtTotal;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Yatzy");
        GridPane diceBoxPane = new GridPane();
        GridPane scorePane = new GridPane();

        this.diceBox(diceBoxPane);
        this.scorePane(scorePane);

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(diceBoxPane, scorePane);

        primaryStage.setScene(new Scene(root, 400, 900));
        primaryStage.show();
    }

    // Øverste del: terninger og kast-knap
    private void diceBox(GridPane pane) {
        pane.setGridLinesVisible(false);
        pane.setStyle("-fx-border-color: black; -fx-border-width: 2;");
        pane.setPadding(new Insets(10));
        pane.setHgap(15);
        pane.setVgap(10);
        pane.setAlignment(Pos.CENTER);

        lblTerninger = new Label[5];
        for (int index = 0; index < 5; index++) {
            lblTerninger[index] = new Label("-");
            lblTerninger[index].setMinWidth(40);
            lblTerninger[index].setMinHeight(40);
            lblTerninger[index].setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-alignment: center;");
            pane.add(lblTerninger[index], index + 1,0);
        }

        CheckBox checkBoxFirst = new CheckBox();
        CheckBox checkBoxSecund = new CheckBox();
        CheckBox checkBoxThird = new CheckBox();
        CheckBox checkBoxFourth = new CheckBox();
        CheckBox checkBoxFifth = new CheckBox();

        chbHold[0] = checkBoxFirst;
        chbHold[1] = checkBoxSecund;
        chbHold[2] = checkBoxThird;
        chbHold[3] = checkBoxFourth;
        chbHold[4] = checkBoxFifth;


        Label lblHold = new Label("Hold");
        pane.add(lblHold, 0,2,1,1);

        lblThrowsLeft = new Label("Antal kast tilbage: 3");
        pane.add(lblThrowsLeft,1,3,3,1);

        pane.add(checkBoxFirst,1,2);
        pane.setHalignment(checkBoxFirst, HPos.CENTER);
        pane.add(checkBoxSecund,2,2);
        pane.setHalignment(checkBoxSecund, HPos.CENTER);
        pane.add(checkBoxThird,3,2);
        pane.setHalignment(checkBoxThird, HPos.CENTER);
        pane.add(checkBoxFourth,4,2);
        pane.setHalignment(checkBoxFourth, HPos.CENTER);
        pane.add(checkBoxFifth,5,2);
        pane.setHalignment(checkBoxFifth, HPos.CENTER);


        pane.add(btnKast, 4, 3, 3, 1);

        btnKast.setOnAction(e -> {
            updateComputedScores();
            if (kastTilbage > 0) {
                updateComputedScores(); // kast og vis resultater

                kastTilbage--;
                lblThrowsLeft.setText("Antal kast tilbage: " + kastTilbage);

                if (kastTilbage == 0) {
                    btnKast.setDisable(true);
                }
            }


        });
    }



    //Nederste del: pointtavlen
    private void scorePane(GridPane pane) {
        pane.setGridLinesVisible(false);
        pane.setPadding(new Insets(20));
        pane.setHgap(20);
        pane.setVgap(8);
        pane.setStyle("-fx-border-color: black; -fx-border-width: 2;");

        // Oprettelse af labels
        Label lbl1ere           = new Label("1'ere:");
        Label lbl2ere           = new Label("2'ere:");
        Label lbl3ere           = new Label("3'ere:");
        Label lbl4ere           = new Label("4'ere:");
        Label lbl5ere           = new Label("5'ere:");
        Label lbl6ere           = new Label("6'ere:");
        Label lblSum            = new Label("Sum:");
        Label lblBonus          = new Label("Bonus:");
        Label lblEtPar          = new Label("Et par:");
        Label lblToPar          = new Label("To par:");
        Label lbl3Ens           = new Label("3 ens:");
        Label lbl4Ens           = new Label("4 ens:");
        Label lblLilleStraight  = new Label("Lille straight:");
        Label lblStoreStraight  = new Label("Store straight:");
        Label lblFuldtHus       = new Label("Fuldt hus:");
        Label lblChance         = new Label("Chance:");
        Label lblYatzy          = new Label("Yatzy:");
        Label lblTotal          = new Label("Total:");

        // Placér labels
        pane.add(lbl1ere,          0, 1);
        pane.add(lbl2ere,          0, 2);
        pane.add(lbl3ere,          0, 3);
        pane.add(lbl4ere,          0, 4);
        pane.add(lbl5ere,          0, 5);
        pane.add(lbl6ere,          0, 6);
        pane.add(lblSum,           0, 8);
        pane.add(lblBonus,         0, 9);
        pane.add(lblEtPar,         0, 11);
        pane.add(lblToPar,         0, 12);
        pane.add(lbl3Ens,          0, 13);
        pane.add(lbl4Ens,          0, 14);
        pane.add(lblLilleStraight, 0, 15);
        pane.add(lblStoreStraight, 0, 16);
        pane.add(lblFuldtHus,      0, 17);
        pane.add(lblChance,        0, 18);
        pane.add(lblYatzy,         0, 19);
        pane.add(lblTotal,         0, 21);

        // Tekstfelter
        txt1ere          = new TextField();
        txt2ere          = new TextField();
        txt3ere          = new TextField();
        txt4ere          = new TextField();
        txt5ere          = new TextField();
        txt6ere          = new TextField();
        txtSum           = new TextField();
        txtBonus         = new TextField();
        txtEtPar         = new TextField();
        txtToPar         = new TextField();
        txt3Ens          = new TextField();
        txt4Ens          = new TextField();
        txtLilleStraight = new TextField();
        txtStoreStraight = new TextField();
        txtFuldtHus      = new TextField();
        txtChance        = new TextField();
        txtYatzy         = new TextField();
        txtTotal         = new TextField();

        // Tilføj tekstfelter på samme rækker
        pane.add(txt1ere,          1, 1);
        pane.add(txt2ere,          1, 2);
        pane.add(txt3ere,          1, 3);
        pane.add(txt4ere,          1, 4);
        pane.add(txt5ere,          1, 5);
        pane.add(txt6ere,          1, 6);
        pane.add(txtSum,           1, 8);
        pane.add(txtBonus,         1, 9);
        pane.add(txtEtPar,         1, 11);
        pane.add(txtToPar,         1, 12);
        pane.add(txt3Ens,          1, 13);
        pane.add(txt4Ens,          1, 14);
        pane.add(txtLilleStraight, 1, 15);
        pane.add(txtStoreStraight, 1, 16);
        pane.add(txtFuldtHus,      1, 17);
        pane.add(txtChance,        1, 18);
        pane.add(txtYatzy,         1, 19);
        pane.add(txtTotal,         1, 21);

        // Definer hvilke scorefelter der kan låses ved klik. Sum, Bonus og Total skal beregnes separat
        scoreFields = new ArrayList<>();

        scoreFields.add(txt1ere);
        scoreFields.add(txt2ere);
        scoreFields.add(txt3ere);
        scoreFields.add(txt4ere);
        scoreFields.add(txt5ere);
        scoreFields.add(txt6ere);
        scoreFields.add(txtEtPar);
        scoreFields.add(txtToPar);
        scoreFields.add(txt3Ens);
        scoreFields.add(txt4Ens);
        scoreFields.add(txtLilleStraight);
        scoreFields.add(txtStoreStraight);
        scoreFields.add(txtFuldtHus);
        scoreFields.add(txtChance);
        scoreFields.add(txtYatzy);

        // giv dem alle samme klik-adfærd: lås valgt, nulstil andre
        for (TextField f : scoreFields) {
            wireLockBehavior(f);
        }
    }

    // klik-adfærd for scorefelter
    // Denne metode tager ét TextField som parameter (tf) – fx txt3Ens – og kobler “klik-adfærd” på det.
    private void wireLockBehavior(TextField tf) {
        tf.setEditable(true);
        tf.setOnMouseClicked(e -> {
            if (locked.contains(tf)) return; // allerede låst
            lockField(tf);
        });
    }

    // låser valgt felt, nulstiller de andre
    private void lockField(TextField chosen) {
        locked.add(chosen);
        chosen.setStyle("-fx-background-color: lightgray; -fx-border-color: black;");

        for (TextField tf : scoreFields) {
            if (tf == chosen) continue;
            if (locked.contains(tf)) continue;
            tf.clear();

        }

        kastTilbage = 3;
        lblThrowsLeft.setText("Antal kast tilbage: 3");
        btnKast.setDisable(false);
        values.put(txtSum, sumScore());
        values.put(txtBonus,bonusScore());
        txtSum.setText(Integer.toString(sumScore()));
        txtTotal.setText(Integer.toString(totalScore()));

        for (CheckBox checkBox : chbHold) {
            checkBox.setSelected(false);
        }

        // Nulstil visningen af terninger
        for (int i = 0; i < lblTerninger.length; i++) {
            lblTerninger[i].setText("-");
        }
    }

    // beregner score, men opdaterer kun ulåste felter
    private void updateComputedScores() {
        YatzyResultCalculator calc = new YatzyResultCalculator(raffleCup.getDice());

        Die[] dice = raffleCup.getDice();
        for (int index = 0; index < dice.length; index++) {
            if (!chbHold[index].isSelected()){
                dice[index].roll();

            }
            lblTerninger[index].setText(String.valueOf(dice[index].getEyes()));

        }


        //Map betyder: en samling af par (nøgle + værdi)
        //LinkedHashMap betyder: en Map, der husker rækkefølgen af de elementer, man tilføjer
        //DVS. Opret en tom tabel (map), hvor hvert tekstfelt skal kobles sammen med et beregnet pointtal
        //Map<TextField, Integer> values = new LinkedHashMap<>();
        values.put(txt1ere, calc.upperSectionScore(1));
        values.put(txt2ere, calc.upperSectionScore(2));
        values.put(txt3ere, calc.upperSectionScore(3));
        values.put(txt4ere, calc.upperSectionScore(4));
        values.put(txt5ere, calc.upperSectionScore(5));
        values.put(txt6ere, calc.upperSectionScore(6));
        values.put(txtEtPar, calc.onePairScore());
        values.put(txtToPar, calc.twoPairScore());
        values.put(txt3Ens, calc.threeOfAKindScore());
        values.put(txt4Ens, calc.fourOfAKindScore());
        values.put(txtLilleStraight, calc.smallStraightScore());
        values.put(txtStoreStraight, calc.largeStraightScore());
        values.put(txtFuldtHus, calc.fullHouseScore());
        values.put(txtChance, calc.chanceScore());
        values.put(txtYatzy, calc.yatzyScore());

        // Denne for-løkke går igennem alle felter i Yatzy-GUI’en, tjekker om de er låst, og opdaterer ellers feltets tekst med den nyeste beregnede score
        for (Map.Entry<TextField, Integer> entry : values.entrySet()) {
            TextField tf = entry.getKey();
            if (locked.contains(tf)) continue; // bevar låste
            tf.setText(df.format(entry.getValue()));
        }
    }

    public int sumScore() {
        int upperSum = 0;
        TextField[] upperFields = {txt1ere, txt2ere, txt3ere, txt4ere, txt5ere, txt6ere};

        for (TextField tf : upperFields) {
            if (locked.contains(tf)) {          // kun felter som er låst
                String text = tf.getText();
                if (text != null && !text.isEmpty()) {
                    upperSum += Integer.parseInt(text);
                }
            }
        }
        return upperSum;
    }

    public int bonusScore() {
        int bonus = 0;
        if (sumScore() >= 63) {
            bonus = 50;
        } else {
            bonus = 0;
        }
        txtBonus.setText(String.valueOf(bonus)); // opdater GUI-feltet
        return bonus;
    }

    public int bottumSumScore(){

    int bottumSum = 0;
    TextField[] buttomFields = {txtEtPar, txtToPar, txt3Ens, txt4Ens, txtLilleStraight, txtStoreStraight, txtFuldtHus, txtChance,txtYatzy};

        for (TextField tf : buttomFields) {
            if (locked.contains(tf)) {          // kun felter som er låst
                String text = tf.getText();
                if (text != null && !text.isEmpty()) {
                    bottumSum += Integer.parseInt(text);
                }
            }
        }
        return bottumSum;
}

    public int totalScore() {
        int total = 0;

//        sumScore();
//        bonusScore();
//        bottumSumScore();
        total = sumScore() + bonusScore() + bottumSumScore();


        return total;
    }
}