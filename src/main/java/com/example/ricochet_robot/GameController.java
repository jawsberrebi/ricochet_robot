package com.example.ricochet_robot;

import com.example.ricochet_robot.backend.Game;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    private Game game = new Game();
    private final Pane[][] board = new Pane[16][16];


    @FXML
    private GridPane boardPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Pour imageview : https://www.youtube.com/watch?v=ePT4maOtjVA
        /*
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {

                Pane pane = new Pane();
                Image image = new Image("C:\\Users\\berja\\OneDrive\\Bureau\\Travail\\A1\\ricochet_robot\\src\\main\\resources\\com\\example\\ricochet_robot\\boards\\planches robot 1-1.png");
                pane.getChildren().add(new ImageView(image));
                //pane.setBackground(new Background(new BackgroundFill(isBlack ? PieceColor.BLACK.toColor() : PieceColor.WHITE.toColor(), null, null)));
                int finalI = i;
                int finalJ = j;
                //pane.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> gameSystem.onBoardClick(finalI, finalJ));
                board[i][j] = pane;
                boardPane.add(pane, i, j);
            }
        }
         */

        Pane pane = new Pane();
        Image image = new Image("C:\\Users\\berja\\OneDrive\\Bureau\\Travail\\A1\\ricochet_robot\\src\\main\\resources\\com\\example\\ricochet_robot\\boards\\planches robot 1-1.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(0.00000001);
        pane.getChildren().add(imageView);
        //pane.setBackground(new Background(new BackgroundFill(isBlack ? PieceColor.BLACK.toColor() : PieceColor.WHITE.toColor(), null, null)));
        //pane.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> gameSystem.onBoardClick(finalI, finalJ));
        board[0][0] = pane;
        boardPane.add(new ImageView(image), 0, 0);
        /*
        pane = new Pane();
        image = new Image("C:\\Users\\berja\\OneDrive\\Bureau\\Travail\\A1\\ricochet_robot\\src\\main\\resources\\com\\example\\ricochet_robot\\boards\\planches robot 2-1.png");
        pane.getChildren().add(new ImageView(image));
        //pane.setBackground(new Background(new BackgroundFill(isBlack ? PieceColor.BLACK.toColor() : PieceColor.WHITE.toColor(), null, null)));
        //pane.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> gameSystem.onBoardClick(finalI, finalJ));
        board[0][8] = pane;
        boardPane.add(new ImageView(image), 0, 8);
        */
        this.game.play();
    }
}
