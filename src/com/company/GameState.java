package com.company;

import java.util.ArrayList;

public class GameState {
    //enumerate represents 3 different categories
    //long BlackBrd = 0b0000000000000000000000000000000000000000101010110101010010101010L;
    //long WhiteBrd = 0b0101010110101010010101010000000000000000000000000000000000000000L;
    public enum Piece {
        SPACE,
        BLACK,
        WHITE
    };

    public class GameMove {
        int[] originalPos = new int[2];
        int[] movedPos = new int[2];
        boolean takingMove;
        Piece movingColor;

        public GameMove(int[] originalPos, int[] movedPos, boolean takingMove, Piece movingColor) {
            this.originalPos = originalPos;
            this.movedPos = movedPos;
            this.takingMove = takingMove;
            this.movingColor = movingColor;
        }

    }

    void print_binary(long l) {
        String res = "";
        long mask = 1;
        for(int i=0; i<64; i++) {
            res = (((l&mask)>0) ? 1 : 0)+res;
            mask *= 2;
        }
        System.out.println(res);
    }

    /*public static final int SPACE = 0;
    public static final int BLACK = 1;
    public static final int WHITE = 2;*/

    Piece[][] brd = new Piece[][]{
            {Piece.SPACE, Piece.WHITE,Piece.SPACE, Piece.WHITE,Piece.SPACE, Piece.WHITE,Piece.SPACE, Piece.WHITE},
            {Piece.WHITE,Piece.SPACE, Piece.WHITE,Piece.SPACE, Piece.WHITE,Piece.SPACE, Piece.WHITE,Piece.SPACE},
            {Piece.SPACE, Piece.WHITE,Piece.SPACE, Piece.WHITE,Piece.SPACE, Piece.WHITE,Piece.SPACE, Piece.WHITE},
            {Piece.SPACE,Piece.SPACE,Piece.SPACE,Piece.SPACE,Piece.SPACE,Piece.SPACE,Piece.SPACE,Piece.SPACE},
            {Piece.SPACE,Piece.SPACE,Piece.SPACE,Piece.SPACE,Piece.SPACE,Piece.SPACE,Piece.SPACE,Piece.SPACE},
            {Piece.BLACK, Piece.SPACE,Piece.BLACK, Piece.SPACE,Piece.BLACK, Piece.SPACE,Piece.BLACK, Piece.SPACE},
            {Piece.SPACE,Piece.BLACK, Piece.SPACE,Piece.BLACK, Piece.SPACE,Piece.BLACK, Piece.SPACE,Piece.BLACK},
            {Piece.BLACK, Piece.SPACE,Piece.BLACK, Piece.SPACE,Piece.BLACK, Piece.SPACE,Piece.BLACK, Piece.SPACE}};
    int remainingBlack = 12;
    int remainingWhite = 12;
    int turnNum = 0;

    public Piece turnPiece(){
        return turnNum%2==0 ? Piece.BLACK : Piece.WHITE;
        /*if(turnNum%2 == 0){
            //blacks move
            return Piece.BLACK;
        }else{
            return Piece.WHITE;
        }*/
    }

    public int winning(){
        return remainingBlack - remainingWhite; //if 0, draw, if +ve, black, if -ve, white winning
    }



    public Piece getPiece(int x, int y){
        return brd[x][y];
    }

    public String representBrd(){
        String res = new String();
        for(int i=0;i<brd.length;i++){
            for(int j=0;j<brd[i].length;j++){
                Piece q = brd[i][j];
                if(q == Piece.BLACK){
                    res+="1";
                }else if(q == Piece.WHITE){
                    res+="2";
                }else{
                    res+="0";
                }
            }
        }
        return res;
    }

    public GameState() {

    }

    public String makeMove(GameMove m){
        brd[m.originalPos[0]][m.originalPos[1]] = Piece.SPACE;
        int posRemove = m.originalPos[0] + (8*m.originalPos[1]);
        int posAdd = m.movedPos[0] + (8*m.movedPos[1]);
        /*
        if(m.movingColor==Piece.BLACK) {
            BlackBrd ^= 1<<(64-posRemove); //XOR and shifting the 1 a number of pieces
            BlackBrd ^= 1<<(64-posAdd);
        }
        if(m.movingColor==Piece.WHITE) {
            WhiteBrd ^= 1<<(64-posRemove); //XOR and shifting the 1 a number of pieces
            WhiteBrd ^= 1<<(64-posAdd);
        }
         */
        brd[m.movedPos[0]][m.movedPos[1]] = m.movingColor;
        if(m.takingMove){
            int takingX = (m.originalPos[0] + m.movedPos[0])/2;
            int takingY = (m.originalPos[1] + m.movedPos[1])/2;
            brd[takingX][takingY] = Piece.SPACE;
            //int remTaken = takingX + (8*takingY);
            if(m.movingColor == Piece.BLACK){
                //WhiteBrd ^= 1<<(64-remTaken);
                remainingWhite --;
            }else{
                //BlackBrd ^= 1<<(64-remTaken);
                remainingBlack --;
            }
        }
        turnNum++;
        String myBrd = representBrd();
        //System.out.println(myBrd);
        return myBrd;
    }

    public GameState(GameState gs) { //copy constructor
        this.remainingBlack = gs.remainingBlack;
        this.remainingWhite = gs.remainingWhite;
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                this.brd[i][j] = gs.brd[i][j];
            }
        }
    }

    public ArrayList<GameMove> nextTurnMoves(){
        ArrayList<GameMove> res = new ArrayList<>();
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if(brd[i][j] == this.turnPiece()){
                    res.addAll(PossibleMoves(i,j));
                }
            }
        }
        return res;
    }

    public ArrayList<GameMove> PossibleMoves(int x, int y){
        ArrayList<GameMove> res = new ArrayList<>();
        if(brd[x][y] == Piece.SPACE){
            return res;
        }
        int[][] offs = {{1,1},{1,-1},{-1,1},{-1,-1}};
        for(int i=0;i<4;i++){
            int tmpX = x+offs[i][0];
            int tmpY = y+offs[i][1];
            //check its within board dimensions
            if(!(tmpX<0 || tmpX>7 || tmpY<0 || tmpY>7)){ //can us demorgans rule here (tmpX>=0 && tmpX<=7 && tmpY>=0 && tmpY<=7)
                //check the space is empty
                if(brd[tmpX][tmpY] == Piece.SPACE){
                    res.add(new GameMove(new int[] {x,y},new int[] {tmpX,tmpY},false, turnPiece()));
                }else if(brd[tmpX][tmpY] != brd[x][y]){
                    //they are different pieces
                    //find out direction
                    tmpX = x+offs[i][0]+offs[i][0];
                    tmpY = y+offs[i][1]+offs[i][1];
                    if(!(tmpX<0 || tmpX>7 || tmpY<0 || tmpY>7)&&(brd[tmpX][tmpY] == Piece.SPACE)){
                        res.add(new GameMove(new int[] {x,y},new int[] {tmpX,tmpY},true, turnPiece()));
                    }
                }
            }
        }
        return  res;
    }

}
