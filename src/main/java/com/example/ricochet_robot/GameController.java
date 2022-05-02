package com.example.ricochet_robot;

import com.example.ricochet_robot.backend.Game;
import com.example.ricochet_robot.backend.Orientation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    private Game game = new Game();
    private final Pane[][] board = new Pane[16][16];


    @FXML
    private GridPane boardPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Lancement du jeu
        this.game.play();
        //Création du plateau en frontend
        boardGeneration();
    }

    private void boardGeneration(){
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                Pane pane = new Pane();
                Image imageCell = new Image(new File("C:\\Users\\berja\\OneDrive\\Bureau\\Travail\\A1\\ricochet_robot\\src\\main\\resources\\com\\example\\ricochet_robot\\boards\\Cell.png").toURI().toString() , 44, 44, false, false);

                //Cas où la case contient des murs :
                //On créée une nouvelle boucle c=qui va détecter chaque mur, à l'aide de la classe Group on empile affiche les murs sur les cases.
                //Dans le cas où plusieurs murs sont présents sur une même case, on superpose les images de murs
                if (this.game.getBoard().getCells()[j + 1][i + 1].isThereWall()){
                    Group finalOverlaid = new Group();
                    for (int w = 0; w < this.game.getBoard().getCells()[j + 1][i + 1].getWalls().size(); w++){
                        if(this.game.getBoard().getCells()[j + 1][i + 1].getWalls().get(w).getOrientation() == Orientation.NORTH){
                            Image imageWall = new Image(new File("C:\\Users\\berja\\OneDrive\\Bureau\\Travail\\A1\\ricochet_robot\\src\\main\\resources\\com\\example\\ricochet_robot\\boards\\NorthWall.png").toURI().toString() , 44, 44, false, false);
                            Rectangle whiteRect = new Rectangle(imageWall.getWidth(), imageWall.getHeight());
                            whiteRect.setFill(Color.BLACK);
                            whiteRect.setBlendMode(BlendMode.DIFFERENCE);
                            Group inverted = new Group(
                                    new ImageView(imageWall),
                                    whiteRect
                            );
                            inverted.setBlendMode(BlendMode.MULTIPLY);
                            Group overlaidBlack = new Group(
                                    new ImageView(imageCell),
                                    inverted
                            );

                            Rectangle redRect = new Rectangle(imageWall.getWidth(), imageWall.getHeight());
                            redRect.setBlendMode(BlendMode.MULTIPLY);

                            Group imageWallNorth = new Group(
                                    new ImageView(imageWall),
                                    redRect
                            );

                            imageWallNorth.setBlendMode(BlendMode.ADD);
                            finalOverlaid = new Group(
                                    overlaidBlack,
                                    imageWallNorth
                            );


                        }

                        if(this.game.getBoard().getCells()[j + 1][i + 1].getWalls().get(w).getOrientation() == Orientation.SOUTH){
                            Image imageWall = new Image(new File("C:\\Users\\berja\\OneDrive\\Bureau\\Travail\\A1\\ricochet_robot\\src\\main\\resources\\com\\example\\ricochet_robot\\boards\\SouthWall.png").toURI().toString() , 44, 44, false, false);
                            Rectangle whiteRect = new Rectangle(imageWall.getWidth(), imageWall.getHeight());
                            whiteRect.setFill(Color.BLACK);
                            whiteRect.setBlendMode(BlendMode.DIFFERENCE);
                            Group inverted = new Group(
                                    new ImageView(imageWall),
                                    whiteRect
                            );

                            inverted.setBlendMode(BlendMode.MULTIPLY);
                            Group overlaidBlack = new Group(
                                    new ImageView(imageCell),
                                    inverted
                            );

                            Rectangle redRect = new Rectangle(imageWall.getWidth(), imageWall.getHeight());
                            redRect.setBlendMode(BlendMode.MULTIPLY);

                            Group imageWallSouth = new Group(
                                    new ImageView(imageWall),
                                    redRect
                            );

                            imageWallSouth.setBlendMode(BlendMode.ADD);
                            finalOverlaid = new Group(
                                    overlaidBlack,
                                    imageWallSouth,
                                    finalOverlaid
                            );

                        }

                        if(this.game.getBoard().getCells()[j + 1][i + 1].getWalls().get(w).getOrientation() == Orientation.EAST){
                            Image imageWall = new Image(new File("C:\\Users\\berja\\OneDrive\\Bureau\\Travail\\A1\\ricochet_robot\\src\\main\\resources\\com\\example\\ricochet_robot\\boards\\EastWall.png").toURI().toString() , 44, 44, false, false);
                            Rectangle whiteRect = new Rectangle(imageWall.getWidth(), imageWall.getHeight());
                            whiteRect.setFill(Color.BLACK);
                            whiteRect.setBlendMode(BlendMode.DIFFERENCE);
                            Group inverted = new Group(
                                    new ImageView(imageWall),
                                    whiteRect
                            );

                            inverted.setBlendMode(BlendMode.MULTIPLY);
                            Group overlaidBlack = new Group(
                                    new ImageView(imageCell),
                                    inverted
                            );

                            Rectangle redRect = new Rectangle(imageWall.getWidth(), imageWall.getHeight());
                            redRect.setBlendMode(BlendMode.MULTIPLY);

                            Group imageWallEast = new Group(
                                    new ImageView(imageWall),
                                    redRect
                            );

                            imageWallEast.setBlendMode(BlendMode.ADD);
                            finalOverlaid = new Group(
                                    overlaidBlack,
                                    imageWallEast,
                                    finalOverlaid
                            );

                        }

                        if(this.game.getBoard().getCells()[j + 1][i + 1].getWalls().get(w).getOrientation() == Orientation.WEST){
                            Image imageWall = new Image(new File("C:\\Users\\berja\\OneDrive\\Bureau\\Travail\\A1\\ricochet_robot\\src\\main\\resources\\com\\example\\ricochet_robot\\boards\\WestWall.png").toURI().toString() , 44, 44, false, false);
                            Rectangle whiteRect = new Rectangle(imageWall.getWidth(), imageWall.getHeight());
                            whiteRect.setFill(Color.BLACK);
                            whiteRect.setBlendMode(BlendMode.DIFFERENCE);
                            Group inverted = new Group(
                                    new ImageView(imageWall),
                                    whiteRect
                            );

                            inverted.setBlendMode(BlendMode.MULTIPLY);
                            Group overlaidBlack = new Group(
                                    new ImageView(imageCell),
                                    inverted
                            );

                            Rectangle redRect = new Rectangle(imageWall.getWidth(), imageWall.getHeight());
                            redRect.setBlendMode(BlendMode.MULTIPLY);

                            Group imageWallWest = new Group(
                                    new ImageView(imageWall),
                                    redRect
                            );

                            imageWallWest.setBlendMode(BlendMode.ADD);
                            finalOverlaid = new Group(
                                    overlaidBlack,
                                    imageWallWest,
                                    finalOverlaid
                            );

                        }

                        pane.getChildren().add(finalOverlaid);

                    }
                }else {
                    pane.getChildren().add(new ImageView(imageCell));
                }

                if((i != 8 && i != 7) || (j != 7 && j != 8)){
                    this.board[i][j] = pane;
                    this.boardPane.add(pane, i, j);
                }
            }
        }
    }
}
