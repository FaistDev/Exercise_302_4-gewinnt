/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package busL;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ben
 */
public class BL {

    private Value[][] field = new Value[7][7];
    private Value currentPlayer;
    ArrayList<Integer> winRows = new ArrayList<Integer>();//Für die Orange hervorhebung;
    ArrayList<Integer> winCols = new ArrayList<Integer>();
    private int moveCount = 0;//Fürs Machine Learning;
    private int computersLastRow = -1;//Damit die GUI weiß, was sie zeigen soll;
    private int computersLastCol = -1;
    private int blocksVertikalInRow = 0;//Damit er sich merkt, wieviele er schon in einer Reihe hat;
    private int lastFightRow = -1;//Falls er verdeitigen muss, ändert sich die computerLastRow, damit er weiß wo er fortsetzten soll;
    private int lastFightCol = -1;
    private ArrayList<Block> allBlocks = new ArrayList<>();//Computersgehirn;
    private ArrayList<Block> thisGameBlocks = new ArrayList<>();//temporäresch Gehirn;
    private Block lastBlock;//Block mit den Daten des Value.O (also Computer sein move);
    private int usersLastRow = -1; //Merkt sich letzten Value.X move; für die Block-Abfrage
    private int userLastCol = -1;

    public BL() {
        this.reset();
    }

    public void reset() {
        for (int col = 0; col < field.length; col++) {
            for (int row = 1; row < field.length; row++) {
                field[row][col] = Value.EMPTY;
            }
        }
        currentPlayer = Value.X;
        thisGameBlocks = new ArrayList<>();
        lastBlock = null;
        userLastCol = -1;
        usersLastRow = -1;
    }

    public int makeMove(int col) throws Exception {
        int labelRow = -1;
        boolean setPlayer = false;
        for (int row = field.length - 1; row > 0; row--) {
            if (field[row][col] == Value.EMPTY) {
                field[row][col] = currentPlayer;
                labelRow = row;
                setPlayer = true;

                if (moveCount != 0) {//Erster Move wird nicht gespeichert
                    if (currentPlayer == Value.O) {
                        lastBlock = new Block(row, col);//Block mit Daten von Value.O
                    } else {//Wenn Value.X dran ist
                        userLastCol = col;
                        usersLastRow = row;
                        boolean newBlock = true;
                        Block oldBlock = null;
                        for (Block block : allBlocks) {//Erkennen ob bereits eine gleiche Information besteht
                            if (block.getComRow() == lastBlock.getComRow() && block.getComCol() == lastBlock.getComCol() && block.getReactRow() == row && block.getReactCol() == col) {
                                newBlock = false;
                                oldBlock = block;
                                break;
                            }
                        }
                        if (newBlock) {//Falls noch kein Block exitiert
                            lastBlock.setReactRow(row);
                            lastBlock.setReactCol(col);
                            allBlocks.add(lastBlock);
                            thisGameBlocks.add(lastBlock);
                            lastBlock = null;
                        } else {
                            oldBlock.usedCountUp();
                            Block.blocksCountUp();
                            thisGameBlocks.add(oldBlock);
                            lastBlock = null;
                        }
                    }
                }
                moveCount++;
                break;
            }
        }

        if (setPlayer) {
            currentPlayer = (currentPlayer == Value.X) ? Value.O : Value.X;
        } else {
            throw new Exception("No Space aviable!");
        }

        return labelRow;
    }

    public Value getValueAt(int row, int col) {
        return field[row][col];
    }

    private void playerWonGame() {
        for (Block block : thisGameBlocks) {
            block.countWinsUp();
        }
        Block.countGamesUp();
    }

    public void saveLearning() throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("computersBrain.bin")));
        for (Block b : allBlocks) {
            oos.writeObject(b);
        }
        oos.flush();
        oos.close();
    }

    public void loadLearning() throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("computersBrain.bin")));
        Object b = null;
        while ((b = ois.readObject()) != null) {
            allBlocks.add((Block) b);
        }
    }

    public Value checkWinner() {
        int countX = 1;
        int countO = 1;

        //horizontal
        for (int rows = 1; rows < field.length; rows++) {
            for (int cols = 0; cols < field.length - 3; cols++) {
                countX = 1;
                countO = 1;
                winCols.clear();
                winRows.clear();
                if (field[rows][cols] != Value.EMPTY) {
                    winCols.add(cols);
                    winRows.add(rows);
                    for (int c = cols + 1; c < field.length; c++) {
                        if (field[rows][cols] == field[rows][c]) {
                            switch (field[rows][cols]) {
                                case X:
                                    countX++;
                                    break;
                                case O:
                                    countO++;
                                    break;
                            }
                            winCols.add(c);
                        } else {
                            break;
                        }
                    }
                    if (countX == 4) {
                        playerWonGame();
                        return Value.X;
                    } else if (countO == 4) {
                        Block.countGamesUp();
                        return Value.O;
                    }
                }
            }
        }

        //vertikal
        for (int cols = 0; cols < field.length; cols++) {
            for (int rows = 1; rows < field.length - 3; rows++) {
                countX = 1;
                countO = 1;
                winCols.clear();
                winRows.clear();
                if (field[rows][cols] != Value.EMPTY) {
                    winCols.add(cols);
                    winRows.add(rows);
                    for (int r = rows + 1; r < field.length; r++) {
                        if (field[rows][cols] == field[r][cols]) {
                            winRows.add(r);
                            switch (field[rows][cols]) {
                                case X:
                                    countX++;
                                    break;
                                case O:
                                    countO++;
                                    break;
                            }
                        } else {
                            break;
                        }
                    }
                    if (countX == 4) {
                        playerWonGame();
                        return Value.X;
                    } else if (countO == 4) {
                        Block.countGamesUp();
                        return Value.O;
                    }
                }
            }
        }

        //diagonal
        //left to right
        for (int rows = 1; rows < field.length - 3; rows++) {
            for (int cols = 0; cols < field.length - 3; cols++) {
                countX = 1;
                countO = 1;
                winCols.clear();
                winRows.clear();
                if (field[rows][cols] != Value.EMPTY) {
                    winCols.add(cols);
                    winRows.add(rows);
                    int r = rows + 1;
                    int c = cols + 1;
                    while (c < field.length && r < field.length) {
                        if (field[rows][cols] == field[r][c]) {
                            winCols.add(c);
                            winRows.add(r);
                            switch (field[rows][cols]) {
                                case X:
                                    countX++;
                                    break;
                                case O:
                                    countO++;
                                    break;
                            }
                            c++;
                            r++;
                        } else {
                            break;
                        }
                    }
                    if (countX == 4) {
                        playerWonGame();
                        return Value.X;
                    } else if (countO == 4) {
                        Block.countGamesUp();
                        return Value.O;
                    }
                }
            }
        }

        //diagonal
        //right to left
        for (int rows = 1; rows < field.length - 3; rows++) {
            for (int cols = field.length - 1; cols > 2; cols--) {
                countX = 1;
                countO = 1;
                winCols.clear();
                winRows.clear();
                if (field[rows][cols] != Value.EMPTY) {
                    winCols.add(cols);
                    winRows.add(rows);
                    int r = rows + 1;
                    int c = cols - 1;
                    while (c >= 0 && r < field.length) {
                        if (field[rows][cols] == field[r][c]) {
                            winCols.add(c);
                            winRows.add(r);
                            switch (field[rows][cols]) {
                                case X:
                                    countX++;
                                    break;
                                case O:
                                    countO++;
                                    break;
                            }
                            c--;
                            r++;
                        } else {
                            break;
                        }
                    }
                    if (countX == 4) {
                        playerWonGame();
                        return Value.X;
                    } else if (countO == 4) {
                        Block.countGamesUp();
                        return Value.O;
                    }
                }
            }
        }

        return Value.EMPTY;
    }

    public ArrayList<Integer> getWinCols() {
        return this.winCols;
    }

    public ArrayList<Integer> getWinRows() {
        return this.winRows;
    }

    //Computer Logik
    public void compute() throws Exception {
        if (computersLastRow != -1) {
            int defenseCol = this.defense();
            if (defenseCol == 23) {//Verdeitigung hat höchste Priorität
                ArrayList<Block> possibleBlocks = new ArrayList<>();
                for (Block block : allBlocks) {
                    if (block.getComRow() == usersLastRow && block.getComCol() == userLastCol) {
                        //Gibt es Daten, wie der Computer auf den Move des Nutzers reagieren soll?
                        if (field[block.getReactRow()][block.getReactCol()] == Value.EMPTY && (block.getReactRow() == field.length - 1 ? true : field[block.getReactRow() + 1][block.getReactCol()] != Value.EMPTY)) {
                            possibleBlocks.add(block);
                        }
                    }
                }
                if (possibleBlocks.size() != 0) {
                    //Now check which block is the best to use
                    Block bestBlock = possibleBlocks.get(0);
                    for (Block possibleBlock : possibleBlocks) {
                        if (possibleBlock.calculateSenseValue() > bestBlock.calculateSenseValue()) {
                            bestBlock = possibleBlock;
                        }
                    }
                    System.out.println("Hey! I remembered that I can set a block at:\n" + bestBlock.toString());
                    computersLastRow = BL.this.makeMove(bestBlock.getReactCol());
                    lastFightRow = computersLastRow;
                    computersLastCol = bestBlock.getReactCol();
                } else {
                    //Wenn es keine Daten gibt, herkömliche Logik
                    //Er baut seine Blöcke nur Vertikal
                    int freeField = blocksVertikalInRow;
                    for (int rows = lastFightRow - 1; rows > 0; rows--) {
                        if (field[rows][lastFightCol] == Value.EMPTY) {
                            freeField++;
                        } else {
                            blocksVertikalInRow = 0;
                            break;
                        }
                    }
                    if (freeField >= 4) {
                        computersLastRow = BL.this.makeMove(lastFightCol);
                        lastFightRow = computersLastRow;
                        computersLastCol = lastFightCol;
                        blocksVertikalInRow++;
                        System.out.println("Here are my rows+cols: " + computersLastRow + " " + computersLastCol);
                        //BL.this.countMoveCountUp();
                    } else {
                        this.searchNewStartPos();
                    }
                }
            } else {
                //Falls er verdeitigen muss
                computersLastRow = BL.this.makeMove(defenseCol);
                if (computersLastCol == defenseCol) {
                    blocksVertikalInRow = 0;
                    lastFightCol = defenseCol;
                }
                computersLastCol = defenseCol;

            }
        } else {
            this.searchNewStartPos();
        }
    }

    private int defense() {
        //Defense against vertikal blocks
        int inRowX = 1;
        for (int cols = 0; cols < field.length; cols++) {
            for (int rows = field.length - 1; rows > 3; rows--) {
                if (field[rows][cols] == Value.X) {
                    for (int r = rows - 1; r > 0; r--) {
                        if (field[r][cols] == Value.X) {
                            inRowX++;
                        } else {
                            break;
                        }
                    }
                    if (inRowX == 3) {
                        try {
                            if (field[rows - 3][cols] == Value.EMPTY) {
                                //Defense
                                System.out.println("Defense in Row: " + rows + " Col: " + cols);
                                return cols;
                            }
                        } catch (Exception e) {
                        }
                        inRowX = 1;
                    } else {
                        inRowX = 1;
                    }
                }
            }
        }

        //Defense against horizontal Blocks
        int inColX = 1;
        for (int rows = 1; rows < field.length; rows++) {
            for (int cols = 0; cols < field.length; cols++) {
                if (field[rows][cols] == Value.X) {
                    for (int c = cols + 1; c < field.length; c++) {
                        if (field[rows][c] == Value.X) {
                            inColX++;
                        } else {
                            break;
                        }
                    }
                    if (inColX == 3) {
                        try {
                            if (rows != field.length - 1) {
                                if (cols + 3 < field.length) {
                                    if (field[rows][cols + 3] == Value.EMPTY && field[rows + 1][cols + 3] != Value.EMPTY) {
                                        //Defense
                                        System.out.println("Defense in Row: " + rows + " Col: " + cols);
                                        return cols + 3;
                                    }
                                } else {
                                    if (field[rows][cols - 1] == Value.EMPTY && field[rows + 1][cols - 1] != Value.EMPTY) {
                                        //Defense
                                        System.out.println("Defense in Row: " + rows + " Col: " + cols);
                                        return cols - 1;
                                    }
                                }
                            } else {
                                if (cols + 3 < field.length) {
                                    if (field[rows][cols + 3] == Value.EMPTY) {
                                        //Defense
                                        System.out.println("Defense in Row: " + rows + " Col: " + cols);
                                        return cols + 3;
                                    }
                                } else {
                                    if (field[rows][cols - 1] == Value.EMPTY) {
                                        //Defense
                                        System.out.println("Defense in Row: " + rows + " Col: " + cols);
                                        return cols - 1;
                                    }
                                }
                            }

                        } catch (Exception e) {

                        }
                        inColX = 1;
                    } else {
                        inColX = 1;
                    }
                }
            }
        }
        return 23;
    }

    private void searchNewStartPos() throws Exception {
        one:
        for (int cols = 0; cols < field.length; cols++) {
            two:
            for (int rows = field.length - 1; rows > 3; rows--) {
                if (field[rows][cols] == Value.EMPTY) {
                    computersLastRow = BL.this.makeMove(cols);
                    computersLastCol = cols;
                    lastFightRow = computersLastRow;
                    lastFightCol = computersLastCol;
                    blocksVertikalInRow++;
                    System.out.println("Here are my rows+cols: " + computersLastRow + " " + computersLastCol);
                    //  BL.this.countMoveCountUp();
                    break one;
                }
            }
        }
    }

    /*public int getMoveCount() {
        return moveCount;
    }

    public void countMoveCountUp() {
        moveCount++;
    }
     */
    public int getComputersLastRow() {
        return computersLastRow;
    }

    public int getComputersLastCol() {
        return computersLastCol;
    }

    public Value getCurrentPlayer() {
        return currentPlayer;
    }

}
