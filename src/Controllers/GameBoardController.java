package Controllers;

import Exceptions.InvalidCountException;
import GameFiles.Board;
import GameFiles.Cell;
import Main.Main;
import Models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class GameBoardController {

    // DECLARATIONS //

    private boolean running = false;
    private Board enemyBoard, playerBoard;

    private int shipsPlaced = 0;

    private boolean enemyTurn = true;

    private Random random = new Random();

    private int[] length = new int[5];
    private int[] hitScores = new int[5];
    private int[] sinkScores = new int[5];
    private String[] names = new String[5];

    private int enemyScore=0;
    private int myScore=0;

    private int myShots=0;
    private int enemyShots=0;

    private int myKill=0;
    private int enKill=0;
    private float myPerc =0;

    private ArrayList<Cell> playerHistory = new ArrayList<>(5);
    private ArrayList<Cell> enemyHistory = new ArrayList<>(5);

    private String scenarioID;
    private File scenario;

    // FIRST TURN BOOLEAN  //

    private boolean enemyTurn() {

        Random randomSTART = new Random();
        return randomSTART.nextBoolean();
    }


    // GAME //


    private Parent createContent() throws InvalidCountException{

        // SHIP VARIABLES //

        length[0] = 5;
        length[1] = 4;
        length[2] = 3;
        length[3] = 3;
        length[4] = 2;

        hitScores[0] = 350;
        hitScores[1] = 250;
        hitScores[2] = 100;
        hitScores[3] = 100;
        hitScores[4] = 50;

        sinkScores[0] = 1000;
        sinkScores[1] = 500;
        sinkScores[2] = 250;
        sinkScores[3] = 0;
        sinkScores[4] = 0;

        names[0] = "Carrier";
        names[1] = "Battleship";
        names[2] = "Cruiser";
        names[3] = "Submarine";
        names[4] = "Destroyer";

        // MAIN WINDOW //


        Image imageb = new Image("https://cdn.offshorewind.biz/wp-content/uploads/sites/10/2020/10/09125736/AU200055055.jpg");
        BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);

        BorderPane root = new BorderPane();
        root.setPrefSize(1500, 1000);

        root.setBackground(new Background(new BackgroundImage(imageb, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,bSize)));

        // TOP TOOLBAR //

        ToolBar toolbar = new ToolBar();
        toolbar.setStyle("-fx-min-width: 1520");
        root.setTop(toolbar);

        // SET TOOLBAR FIELDS //

        Text txt1 = new Text("Player Ships");
        TextField txt2 = new TextField("N/A");
        Text txt3 = new Text("Enemy Ships");
        TextField txt4 = new TextField("N/A");
        Text txt5 = new Text("Player Points");
        TextField txt6 = new TextField("N/A");
        Text txt7 = new Text("Enemy Points");
        TextField txt8 = new TextField("N/A");
        Text txt9 = new Text("Player Accuracy");
        TextField txt10 = new TextField("N/A");
        Text txt11 = new Text("Enemy Accuracy");
        TextField txt12 = new TextField("N/A");

        txt2.setDisable(true);
        txt2.setStyle("-fx-opacity: 1;" + "-fx-max-width: 120");
        txt4.setDisable(true);
        txt4.setStyle("-fx-opacity: 1;"  + "-fx-max-width: 120");
        txt6.setDisable(true);
        txt6.setStyle("-fx-opacity: 1;"  + "-fx-max-width: 120");
        txt8.setDisable(true);
        txt8.setStyle("-fx-opacity: 1;"  + "-fx-max-width: 120");
        txt10.setDisable(true);
        txt10.setStyle("-fx-opacity: 1;"  + "-fx-max-width: 120");
        txt12.setDisable(true);
        txt12.setStyle("-fx-opacity: 1;"  + "-fx-max-width: 120");

        Separator separator = new Separator();
        Separator separator2 = new Separator();

        // POPULATE TOOLBAR //

        toolbar.getItems().addAll(txt1,txt2,txt3,txt4,separator,txt5,txt6,txt7,txt8,separator2,txt9,txt10,txt11,txt12);

        // RIGHT SIDE //

        Text TotalShips = new Text("Your fleet to be placed in this order:");
        TotalShips.setStyle("-fx-font-weight: bold");

        Text carrierTotal = new Text("Carriers available:");
        carrierTotal.setStyle("-fx-stroke: blue;" + "-fx-font-size: 16");
        Text battleshipTotal = new Text("Battleships available:");
        battleshipTotal.setStyle("-fx-stroke: red;" + "-fx-font-size: 16");
        Text cruiserTotal = new Text("Cruisers available:");
        cruiserTotal.setStyle("-fx-stroke: orange;" + "-fx-font-size: 16");
        Text submarineTotal = new Text("Submarines available:");
        submarineTotal.setStyle("-fx-stroke: brown;" + "-fx-font-size: 16");
        Text destroyerTotal = new Text("Destroyers available:");
        destroyerTotal.setStyle("-fx-stroke: green;" + "-fx-font-size: 16");
        TextField carrier = new TextField("1");
        carrier.setDisable(true);
        carrier.setStyle("-fx-opacity: 1;");
        TextField battleship = new TextField("1");
        battleship.setDisable(true);
        battleship.setStyle("-fx-opacity: 1;");
        TextField cruiser = new TextField("1");
        cruiser.setDisable(true);
        cruiser.setStyle("-fx-opacity: 1;");
        TextField submarine = new TextField("1");
        submarine.setDisable(true);
        submarine.setStyle("-fx-opacity: 1;");
        TextField destroyer = new TextField("1");
        destroyer.setDisable(true);
        destroyer.setStyle("-fx-opacity: 1;");
        Text play = new Text("");
        Text howToPlay = new Text("How to play:");
        howToPlay.setStyle("-fx-font-weight: bold;" + "fx-font-size: 15");

        Text gameInstructions = new Text("The ships above consist of your entire fleet. To place them on your board you can click with either left click or right click." +
                                     " If you left click a cell, the ship in queue will be placed vertically starting from that cell. If you right click instead, the ship will be placed horizontally. "  +
                                     " The ships are chosen in the order above.");
        gameInstructions.setStyle("-fx-text-alignment: justify;" + "fx-font-size: 15");
        gameInstructions.setWrappingWidth(200);
        Text plTurns = new Text(" \n Player Turns Remaining");
        Text enTurns = new Text("Enemy Turns Remaining");
        TextField txt13 = new TextField("40");
        TextField txt14 = new TextField("40");
        txt13.setDisable(true);
        txt13.setStyle("-fx-opacity: 1;");
        txt14.setDisable(true);
        txt14.setStyle("-fx-opacity: 1;");

        plTurns.setStyle("-fx-font-weight: bold");
        enTurns.setStyle("-fx-font-weight: bold");

        // POPULATE RIGHT SIDE VBOX //

        VBox rightvbox = new VBox(10,TotalShips,carrierTotal, carrier, battleshipTotal, battleship, cruiserTotal, cruiser, submarineTotal, submarine, destroyerTotal,
                destroyer, play, howToPlay, gameInstructions, plTurns, txt13, enTurns, txt14);
        rightvbox.setBackground(new Background(new BackgroundFill(Color.rgb(200, 200, 200), CornerRadii.EMPTY, Insets.EMPTY)));
        rightvbox.setAlignment(Pos.TOP_CENTER);
        rightvbox.setStyle("-fx-padding: 16;" + "-fx-border-color: black;");
        root.setRight(rightvbox);

        // LEFT SIDE //

        //1st menu
        Text MenuItems = new Text("Menus");
        MenuItems.setStyle("-fx-font-weight: bold");

        MenuItem menuItem1 = new MenuItem("Restart");
        menuItem1.setStyle("-fx-padding: 5 22 10 22;");
        MenuItem menuItem2 = new MenuItem("Load");
        menuItem2.setStyle("-fx-padding: 5 22 10 22;");
        MenuItem menuItem3 = new MenuItem("Exit");
        menuItem3.setStyle("-fx-padding: 5 22 5 22;");
        MenuButton menuButton = new MenuButton("Application", null, menuItem1, menuItem2, menuItem3);
        menuButton.setStyle("-fx-padding: 0 0 0 0;");

        //2nd menu
        Text menu2 = new Text(" \n \n \n \n \n \n \n \n ");

        MenuItem menuItem4 = new MenuItem("Enemy Ships");
        menuItem4.setStyle("-fx-padding: 5 9 10 9;");
        MenuItem menuItem5 = new MenuItem("Player Shots");
        menuItem5.setStyle("-fx-padding: 5 9 10 9;");
        MenuItem menuItem6 = new MenuItem("Enemy Shots");
        menuItem6.setStyle("-fx-padding: 5 9 5 9;");
        MenuButton menuButton2 = new MenuButton("Details", null, menuItem4, menuItem5, menuItem6);
        menuButton2.setStyle("-fx-padding: 0 15 0 15;");

        // MENU FUNCTIONALITY //

        //1 menu
        menuItem1.setOnAction(event -> { //restart
           // infoBox("Game will now restart!", "Restart");
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Restart");
            alert.setHeaderText("Are you sure you want to restart?");
            new ButtonType("Yes");
            new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.CANCEL) {
                    event.consume();
                }
                else {
                    cleanup();
                    try {
                        restart();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        });

        menuItem2.setOnAction(event -> { //load scenario
            TextInputDialog scenarioText = new TextInputDialog();
            scenarioText.setHeaderText("Please enter the scenario name");
            scenarioText.showAndWait();
            scenarioID = scenarioText.getEditor().getText();
            try {
                scenarioLoadPlayer(txt2,txt8,txt14, txt12);
            } catch (Exception e) {
                e.printStackTrace();
            }
                });


        menuItem3.setOnAction(event -> { //exit
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Exit");
            alert.setHeaderText("Are you sure you want to exit?");
            new ButtonType("Yes");
            new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.CANCEL) {
                    event.consume();
                }
                else {
                    cleanup();
                }
            });
        });

        //2 menu
        menuItem4.setOnAction(event -> { //show enemy ships

            Ship Shipd;

            StringBuilder shipsText = new StringBuilder();
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"");
            alert.setTitle("Enemy Ships");
            alert.setHeaderText("Enemy Ships Information");
            if(enemyBoard.Ships.isEmpty()){
                shipsText = new StringBuilder("No enemy ships on the board");
            }
            else{
                for (int i = 0;i <enemyBoard.Ships.size(); i++){
                    Shipd = enemyBoard.Ships.get(i);
                    if(!Shipd.isHit()){
                        shipsText.append(Shipd.shipType).append(": healthy\n");
                    }
                    else{
                        if(Shipd.isAlive()){
                            shipsText.append(Shipd.shipType).append(": hit\n");
                        }
                        else{
                            shipsText.append(Shipd.shipType).append(": sunk\n");
                        }
                    }
                }
            }

            alert.setContentText(shipsText.toString());
            alert.showAndWait();
        });

        //show my Last 5 shots
        menuItem5.setOnAction(ePlayerShots -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"");
            alert.setHeaderText("My Shot History");
            alert.setTitle("Shot History");
            StringBuilder myShots = new StringBuilder();
            if(playerHistory.isEmpty()){
                myShots = new StringBuilder("You haven't shot yet");
            }
            for (Cell tmpCell : playerHistory) {
                if (tmpCell.ship == null) {
                    myShots.append("(").append(tmpCell.x).append(".").append(tmpCell.y).append(")").append(": Miss \n");
                } else {
                    myShots.append("(").append(tmpCell.x).append(".").append(tmpCell.y).append(")").append(": Hit ").append(tmpCell.ship.shipType).append("\n");
                }
            }
            alert.setContentText(myShots.toString());
            alert.showAndWait();
        });

        //show enemy last 5 shots
        menuItem6.setOnAction(eEnemyShots -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"");
            alert.setHeaderText("Enemy Shot History");
            alert.setTitle("Shot History");
            StringBuilder enemyShots = new StringBuilder();
            if(enemyHistory.isEmpty()){
                enemyShots = new StringBuilder("Enemy hasn't shot yet");
            }
            for (Cell tmpCell : enemyHistory) {
                if (tmpCell.ship == null) {
                    enemyShots.append("(").append(tmpCell.x).append(".").append(tmpCell.y).append(")").append(": Miss \n");
                } else {
                    enemyShots.append("(").append(tmpCell.x).append(".").append(tmpCell.y).append(")").append(": Hit ").append(tmpCell.ship.shipType).append("\n");
                }
            }
            alert.setContentText(enemyShots.toString());
            alert.showAndWait();
        });

        // POPULATE LEFT VBOX //

        VBox leftvbox = new VBox(10,MenuItems, menuButton, menu2, menuButton2);
        leftvbox.setBackground(new Background(new BackgroundFill(Color.rgb(200, 200, 200), CornerRadii.EMPTY, Insets.EMPTY)));
        leftvbox.setAlignment(Pos.TOP_CENTER);
        leftvbox.setStyle("-fx-padding: 16;" + "-fx-border-color: black;");
        root.setLeft(leftvbox);


        // ENEMY BOARD //

        enemyBoard = new Board(true, event -> {
            if (!running)
                return;

            Cell cell = (Cell) event.getSource();
            if (cell.wasShot) {
                return;
            }

            enemyTurn = !cell.shoot();
            myShots= myShots+1;
            myScore= myScore + cell.highscore;
            myKill=myKill+cell.perc;
            myPerc =(float) (((myKill)*100) / myShots);
            txt4.setText(String.valueOf(enemyBoard.ships));
            txt6.setText(String.valueOf(myScore));
            txt10.setText(String.valueOf(myPerc));
            txt13.setText(String.valueOf(40-myShots));

            if (playerHistory.size() == 5) {
                playerHistory.remove(0);
            }
            playerHistory.add(cell);

            if (enemyBoard.ships == 0 ||  ((myShots==40 || enemyShots==40) && myScore>enemyScore )) {
                System.out.println("YOU WIN");
                infoBox("You won!", "What a victory!");
                running = false;
            }

            if (enemyTurn)
                enemyMove(txt2,txt8,txt14, txt12);
        });

        // PLAYER BOARD //

        playerBoard = new Board(false, event -> {
            if (running)
                return;

            Cell cell = (Cell) event.getSource();
            if(shipsPlaced==0) {
                if (playerBoard.placeShip(new Ship(length[shipsPlaced], event.getButton() == MouseButton.PRIMARY, hitScores[shipsPlaced], sinkScores[shipsPlaced] , names[shipsPlaced]), cell.x, cell.y)) {
                    ++shipsPlaced;
                    carrier.setText("0");
                }
            }
                if(shipsPlaced==1){
                    if(playerBoard.placeShip(new Ship(length[shipsPlaced], event.getButton() == MouseButton.PRIMARY, hitScores[shipsPlaced-1], sinkScores[shipsPlaced] , names[shipsPlaced]), cell.x, cell.y)) {
                        battleship.setText("0");
                        ++shipsPlaced;
                    }
                }
                if(shipsPlaced==2) {
                    if (playerBoard.placeShip(new Ship(length[shipsPlaced], event.getButton() == MouseButton.PRIMARY, hitScores[shipsPlaced], sinkScores[shipsPlaced] , names[shipsPlaced]), cell.x, cell.y)) {
                        cruiser.setText("0");
                        ++shipsPlaced;
                    }
                }
                if(shipsPlaced==3) {
                    if (playerBoard.placeShip(new Ship(length[shipsPlaced], event.getButton() == MouseButton.PRIMARY, hitScores[shipsPlaced], sinkScores[shipsPlaced] , names[shipsPlaced]), cell.x, cell.y)) {
                        submarine.setText("0");
                        ++shipsPlaced;
                    }
                }
                if(shipsPlaced==4) {
                    if (playerBoard.placeShip(new Ship(length[shipsPlaced], event.getButton() == MouseButton.PRIMARY, hitScores[shipsPlaced], sinkScores[shipsPlaced] , names[shipsPlaced]), cell.x, cell.y)) {
                        destroyer.setText("0");
                        ++shipsPlaced;
                    }
                }
                if (shipsPlaced == 5) {
                    play.setText("All set! Fire at Will!");
                    try {
                        startGame(txt2,txt8,txt14,txt12);
                    } catch (InvalidCountException e) {
                        e.printStackTrace();
                    }
                }
        });

        // MAIN BOARD //

        Text txtME= new Text();
        Text txtENEMY = new Text();
        txtME.setText("My Board");
        txtME.setStyle("-fx-font-weight: bold;" + "-fx-font-size: 20");
        txtENEMY.setText("Enemy Board");
        txtENEMY.setStyle("-fx-font-weight: bold;" + "-fx-font-size: 20");

        VBox vbox = new VBox(30,txtENEMY, enemyBoard, txtME ,playerBoard);
        vbox.setAlignment(Pos.CENTER);

        root.setCenter(vbox);

        return root;
    }

    // ENEMY PLAYSTYLE //

    private void enemyMove(TextField shipText, TextField scoreText, TextField shotsText, TextField percText) {
        while (enemyTurn) {
            int x = random.nextInt(10); //shoot
            int y = random.nextInt(10);

            Cell cell = playerBoard.getCell(x, y);
            if (cell.wasShot)
                continue;

            enemyTurn = cell.shoot();

            //enemy variables
            enemyScore=enemyScore + cell.highscore;
            enemyShots= enemyShots+1;
            enKill=enKill+cell.perc;
            float enPerc = (float) (((enKill) * 100) / enemyShots);
            scoreText.setText(String.valueOf(enemyScore));
            shotsText.setText(String.valueOf(40-enemyShots));
            shipText.setText(String.valueOf(playerBoard.ships));
            percText.setText(String.valueOf(enPerc));

            if (enemyHistory.size() == 5) {
                enemyHistory.remove(0);
            }
            enemyHistory.add(cell);

            //loss
            if (playerBoard.ships == 0 ||  ((myShots==40 || enemyShots==40) && myScore<enemyScore )) {
                System.out.println("YOU LOSE");
                infoBox("You lost!", "Bad Luck!");
                running = false;
            }
        }
    }

    // PLACE ENEMY SHIPS. THEN GAME STARTS //

    private void startGame(TextField shipText,TextField scoreText, TextField shotsText, TextField percText) throws InvalidCountException{
        // place enemy ships
        int count = 0;

        while (count <= 4) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);

            if (enemyBoard.placeShip(new Ship(length[count], Math.random() < 0.5, hitScores[count], sinkScores[count], names[count]), x, y)) {
                count++;
            }
        }

        if (enemyTurn()) {
            infoBox("You go second!", "Unlucky, sir!");
            enemyMove(shipText,scoreText,shotsText, percText);
        }
        else {
            infoBox("You go first!", "We got the upper hand!");
        }

        running = true;
    }

    // LOAD SCENARIO PLAYER //

    private void scenarioLoadPlayer(TextField shipText,TextField scoreText, TextField shotsText, TextField percText){
        if(scenarioID == null){
            System.out.println("Error. File not found");
            Alert alert = new Alert(Alert.AlertType.WARNING, "Error. Scenario not found!");
            alert.showAndWait();
        }else if(shipsPlaced!=0){
            System.out.println("Error. Ships have already been placed!");
            infoBox("You have already placed ships! You can't load another game! Restart first if you want to load a scenario.", "Invalid Load");
        }
        else {
            try {
                int count=0;
                scenario = new File("C:\\Users\\jimmd\\IdeaProjects\\BattleShip Game\\src\\MediaLab\\player_"+ scenarioID +".txt");
                Scanner myReader = new Scanner(scenario);
                while (myReader.hasNext()) {
                    String data = myReader.next();
                    int type = Character.getNumericValue(data.charAt(0)) - 1;
                    int coordinateY = Character.getNumericValue(data.charAt(2));
                    int coordinateX = Character.getNumericValue(data.charAt(4));
                    int vertical = Character.getNumericValue(data.charAt(6));
                    boolean verticalCheck = (vertical == 2);
                    System.out.println(type+" "+coordinateX+" "+coordinateY+" "+vertical);
                    Cell cell = playerBoard.getCell(coordinateX,coordinateY);
                    if(playerBoard.placeShip(new Ship(length[type], verticalCheck ,hitScores[type],sinkScores[type],names[type]), cell.x, cell.y)){
                        count++;
                        if(count>4){
                            enemyScenarioLoad(shipText,scoreText,shotsText,percText);
                        }
                    }
                    else {
                        System.out.println("Error with ship"+ names[type]);
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Error with friendly ship: "+ names[type] +". Please load a working scenario!");
                        alert.showAndWait();
                        restart();
                    }
                }
            } catch (FileNotFoundException | InvalidCountException e) {
                System.out.print("File not found");
                Alert alert = new Alert(Alert.AlertType.WARNING, "Error. Scenario not found!");
                alert.showAndWait();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
    }

    //ENEMY SCENARIO LOAD //

    private void enemyScenarioLoad(TextField shipText,TextField scoreText, TextField shotsText, TextField percText) throws InvalidCountException{
        // place enemy ships
        int count = 0;

        if(scenarioID == null){
            System.out.println("Error");
            Alert alert = new Alert(Alert.AlertType.WARNING, "Error. Scenario not found!");
            alert.showAndWait();
        }
        else {
            try {
                scenario = new File("C:\\Users\\jimmd\\IdeaProjects\\BattleShip Game\\src\\MediaLab\\enemy_"+ scenarioID +".txt");
                Scanner myReader = new Scanner(scenario);
                while (myReader.hasNext()) {
                    String data = myReader.next();
                    int type = Character.getNumericValue(data.charAt(0)) - 1;
                    int coordinateY = Character.getNumericValue(data.charAt(2));
                    int coordinateX = Character.getNumericValue(data.charAt(4));
                    int vertical = Character.getNumericValue(data.charAt(6));
                    boolean verticalCheck = (vertical == 2);
                    System.out.println(type+" "+coordinateX+" "+coordinateY+" "+vertical);
                    Cell cell = enemyBoard.getCell(coordinateX,coordinateY);
                    if(enemyBoard.placeShip(new Ship(length[type], verticalCheck ,hitScores[type],sinkScores[type],names[type]), cell.x, cell.y)) {
                        count++;
                    }
                    else {
                        System.out.println("Error with ship"+ names[type]);
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Error with enemy ship: "+ names[type] +". Please load a working scenario!");
                        alert.showAndWait();
                        restart();
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.print("File not found");
                Alert alert = new Alert(Alert.AlertType.WARNING, "Error. Scenario not found!");
                alert.showAndWait();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }

        running = true;
    }

    // PLAY BUTTON //

    private Stage thisStage;

    @FXML
    private void GameScene(ActionEvent event) throws InvalidCountException {
        Scene scene2 = new Scene(createContent());
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        thisStage=window;
        window.setScene(scene2);
        window.show();
    }

    // INFO BOX POPUP //

    private static void infoBox(String infoMessage, String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    // INITIALIZE SCREEN TO EARLY STATE //

    private void cleanup() {
        thisStage.close();
    }

    // GAME RESTARTS //

    private void restart() throws Exception {
        cleanup();
        Main app = new Main();
        app.start(new Stage());
    }

}