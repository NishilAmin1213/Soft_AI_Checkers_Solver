package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class MyWindow extends JFrame {
    JButton[][] buttons = new JButton[8][8];

    GameState thisGame = new GameState();
    ImageIcon black = new ImageIcon("black.png");
    ImageIcon white = new ImageIcon("white.png");
    int selectedX = -1, selectedY = -1;

    public MyWindow(){
        setSize(1000,1000);
        setTitle("Checkers Window");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        AI myAI = new AI(GameState.Piece.WHITE);

        Container content = getContentPane();
        content.setLayout(new GridLayout(8,8));

        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++) {
                final int x=i, y=j;
                JButton button = new JButton();
                content.add(button);
                button.setFont(new Font(button.getFont().getName(), button.getFont().getStyle(), 40));
                buttons[i][j] = button;
                if(thisGame.brd[i][j] == GameState.Piece.BLACK){
                    button.setIcon(black);
                }else if(thisGame.brd[i][j] == GameState.Piece.WHITE){
                    button.setIcon(white);
                }

                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //find out whose turn it is
                        if(selectedX>-1 && selectedY>-1){
                            //make a swap, a piece has already been selected
                            ArrayList<GameState.GameMove> PossibleMoves = thisGame.PossibleMoves(selectedX,selectedY);
                            for (GameState.GameMove m: PossibleMoves){
                                if (m.movedPos[0] == x && m.movedPos[1] == y){
                                    //move is valid
                                    thisGame.makeMove(m);
                                    //swap icons on board
                                    buttons[x][y].setIcon(buttons[selectedX][selectedY].getIcon());
                                    buttons[selectedX][selectedY].setIcon(null);
                                    if(m.takingMove){
                                        buttons[(x+selectedX)/2][(y+selectedY)/2].setIcon(null);
                                    }
                                    //as a valid move has now been made by the user, the AI can not make a move
                                    GameState.GameMove moveToMake = myAI.bestMove(thisGame);
                                    //make move
                                    thisGame.makeMove(moveToMake);
                                    //change board visuals
                                    buttons[moveToMake.movedPos[0]][moveToMake.movedPos[1]].setIcon(buttons[moveToMake.originalPos[0]][moveToMake.originalPos[1]].getIcon());
                                    buttons[moveToMake.originalPos[0]][moveToMake.originalPos[1]].setIcon(null);
                                    if(moveToMake.takingMove) {
                                        buttons[(moveToMake.movedPos[0] + moveToMake.originalPos[0]) / 2][(moveToMake.movedPos[1] + moveToMake.originalPos[1]) / 2].setIcon(null);
                                    }
                                    break;
                                }
                            }
                            selectedX = -1;
                            selectedY = -1;
                        }else if(thisGame.turnPiece() == thisGame.getPiece(x,y)){
                            selectedX = x; selectedY = y;
                        }else{
                            //invalid move
                        }

                    }
                });

            }
        }

        this.setVisible(true);
    }

}


