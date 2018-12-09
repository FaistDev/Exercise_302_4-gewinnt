/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package busL;

import java.io.Serializable;

/**
 *
 * @author Ben
 */
public class Block implements Serializable {
    private int comRow;
    private int comCol;
    private int reactRow;
    private int reactCol;
    private int usedCount;
    private static int blocksCount=1;
    private int wonGames;
    private static int games=1;

    public Block(int comRow, int comCol) {
        this.comRow = comRow;
        this.comCol = comCol;
        usedCount++;
        blocksCount++;
    }
    
    public double calculateSenseValue(){
        double w1 = usedCount/blocksCount;
        double w2 = wonGames/games;
        return (w1+w2)/2;
    }
    
    public void usedCountUp(){
        usedCount++;
    }
    
    public static void blocksCountUp(){
        blocksCount++;
    }
    
    public static void countGamesUp(){
        games++;
    }
    
    public void countWinsUp(){
        wonGames++;
    }

    public void setReactRow(int reactRow) {
        this.reactRow = reactRow;
    }

    public void setReactCol(int reactCol) {
        this.reactCol = reactCol;
    }
    
    

    public int getComRow() {
        return comRow;
    }

    public int getComCol() {
        return comCol;
    }

    public int getReactRow() {
        return reactRow;
    }

    public int getReactCol() {
        return reactCol;
    }

    public int getUsedCount() {
        return usedCount;
    }

    public static int getBlocksCount() {
        return blocksCount;
    }

    public int getWonGames() {
        return wonGames;
    }

    public static int getGames() {
        return games;
    }
    
    
}
