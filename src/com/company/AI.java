package com.company;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AI {
    GameState.Piece AIPiece;
    static int num_nodes = 0;
    //ArrayList<String> SeenBefore = new ArrayList<>();
    HashSet<String> SeenBefore = new HashSet<>();

    public AI(GameState.Piece p){
        AIPiece = p;
    }

    public boolean finder(ArrayList<String> SeenBefore, String thisMove){ //if linear search is too slow, can use a has table
        for(String s : SeenBefore){
            if(s==thisMove){
                return true;
            }
        }
        return false;
    }

    public int Maximize(GameState thisGame, int Depth, int a, int b){
        num_nodes++;
        //A will be changed before we call minimize
        ArrayList<GameState.GameMove> possibleMoves = thisGame.nextTurnMoves();
        int bestValue = -1000;
        for(GameState.GameMove m : possibleMoves){
            GameState nextTurn = new GameState(thisGame);
            String thisMove = nextTurn.makeMove(m);
            if(SeenBefore.contains(thisMove)){
                continue;
            }
            SeenBefore.add(thisMove);
            //2 possibilities, we are at bottom of tree or we haven't
            int value;
            if(Depth == 0){
                //we are at bottom of tree
                value = nextTurn.winning();
            }else{
                //we are doing the next turn, so call minimize
                value = Minimize(nextTurn, Depth-1,a,b);
            }
            bestValue = Math.max(bestValue, value);
            a = Math.max(a, bestValue);
            if(a >= b){
                break;
            }
        }
        return bestValue;
    }

    public int Minimize(GameState thisGame, int Depth, int a, int b){
        num_nodes++;
        //B will be changed before we call maximize
        ArrayList<GameState.GameMove> possibleMoves = thisGame.nextTurnMoves();
        int bestValue = 1000;
        for(GameState.GameMove m : possibleMoves){
            GameState nextTurn = new GameState(thisGame);
            String thisMove = nextTurn.makeMove(m);
            if(SeenBefore.contains(thisMove)){
                continue;
            }
            SeenBefore.add(thisMove);
            int value;
            if(Depth == 0){
                value = nextTurn.winning();
            }else{
                value = Maximize(nextTurn, Depth,a,b);
            }
            bestValue = Math.min(bestValue, value);
            b = Math.min(b, bestValue);
            if(b <= a){
                break;
            }
        }
        return bestValue;
    }


    public GameState.GameMove bestMove(GameState thisGame){
        SeenBefore = new HashSet<>(); //reinitialise SeenBefore
        num_nodes = 0;
        int depth = 10; // change depth HERE ************************
        GameState.GameMove bestMove = null;
        if(thisGame.turnPiece() == AIPiece){
            ArrayList<GameState.GameMove> possibleMoves = thisGame.nextTurnMoves();
            int bestWin = (AIPiece == GameState.Piece.BLACK) ? -1000 : 1000; //if piece is black, bestwin is -1000, else, its 1000
            for(GameState.GameMove m : possibleMoves){
                GameState gameCopy = new GameState(thisGame);
                String thisMove = gameCopy.makeMove(m);
                SeenBefore.add(thisMove);
                if(AIPiece== GameState.Piece.BLACK){
                    //we minimise here, so the AI benefits
                    int value = Minimize(gameCopy, depth, -Integer.MAX_VALUE, Integer.MAX_VALUE);
                    if(value>bestWin){
                        bestWin = value;
                        bestMove = m;
                    }
                }else if(AIPiece== GameState.Piece.WHITE){
                    //we maximise here, so the AI benefits
                    int value = Maximize(gameCopy, depth, -Integer.MAX_VALUE, Integer.MAX_VALUE);
                    if(value<bestWin) {
                        bestWin = value;
                        bestMove = m;
                    }
                }
            }


        }
        System.out.println("num nodes: "+num_nodes);
        return bestMove;
    }

}




/*
public GameState.GameMove bestMove(GameState thisGame){
        GameState.GameMove bestMove = null;
        if(thisGame.turnPiece() == AIPiece){
            ArrayList<GameState.GameMove> possibleMoves = thisGame.nextTurnMoves();
            int bestWin = (AIPiece== GameState.Piece.BLACK) ? -1000 : 1000; //if pieve is black, bestwin is -1000, else, its 1000
            for(GameState.GameMove m : possibleMoves){
                GameState gameCopy = new GameState(thisGame);
                gameCopy.makeMove(m);
                if(AIPiece== GameState.Piece.BLACK && gameCopy.winning()>bestWin){
                    //black winning
                    bestWin = gameCopy.winning();
                    bestMove = m;
                }else if(AIPiece== GameState.Piece.WHITE && gameCopy.winning()<bestWin){
                    //white winning
                    bestWin = gameCopy.winning();
                    bestMove = m;
                }
            }
        }
        return bestMove;
    }
 */
