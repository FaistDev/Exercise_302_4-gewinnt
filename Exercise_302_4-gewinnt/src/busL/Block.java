/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package busL;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    private double usedCount;
    private static double blocksCount;
    private double wonGames;
    private static double games;

    public Block(int comRow, int comCol) {
        this.comRow = comRow;
        this.comCol = comCol;
        usedCount++;
        blocksCount++;
    }

    public double calculateSenseValue() {
        double w1 = usedCount / blocksCount;
        double w2 = wonGames / games;
        return (w1 + w2 * 2) / 2;
    }

    public void usedCountUp() {
        usedCount++;
    }

    public static void blocksCountUp() {
        blocksCount++;
    }

    public static void countGamesUp() {
        games++;
    }

    public void countWinsUp() {
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

    public double getUsedCount() {
        return usedCount;
    }

    public static double getBlocksCount() {
        return blocksCount;
    }

    public double getWonGames() {
        return wonGames;
    }

    public static double getGames() {
        return games;
    }

    public String toString() {
        return String.format("Players: %d/%d\nComputersReact: %d/%d\nUsedThisMove: %.2f"
                + "\nMovesSum: %.2f\nWonWithThisMove: %.2f\nGamesInSum: %.2f\nSense: %.2f",
                comRow, comCol, reactRow, reactCol, usedCount, Block.blocksCount, wonGames, Block.games, calculateSenseValue());
    }

    private void writeObject(ObjectOutputStream os) {
        try {
            os.defaultWriteObject();
            os.writeDouble(Block.blocksCount);
            os.writeDouble(Block.games);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readObject(ObjectInputStream is) {
        try {
            is.defaultReadObject();
            Block.setBlocks(is.readDouble());
            Block.setGames(is.readDouble());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setGames(double games) {
        Block.games = games;
    }

    private static void setBlocks(double blocks) {
        Block.blocksCount = blocks;
    }
}
