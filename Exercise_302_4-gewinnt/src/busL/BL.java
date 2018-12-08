/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package busL;

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
    ArrayList<Integer> winRows = new ArrayList<Integer>();
    ArrayList<Integer> winCols = new ArrayList<Integer>();
    private int moveCount = 0;
    private int computersLastRow = -1;
    private int computersLastCol = -1;
    private int blocksVertikalInRow=0;

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
    }

    public int makeMove(int col) throws Exception {
        int labelRow = -1;
        boolean setPlayer = false;
        for (int row = field.length - 1; row > 0; row--) {
            if (field[row][col] == Value.EMPTY) {
                field[row][col] = currentPlayer;
                labelRow = row;
                setPlayer = true;
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
                        return Value.X;
                    } else if (countO == 4) {
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
                        return Value.X;
                    } else if (countO == 4) {
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
                    while (c < field.length) {
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
                        return Value.X;
                    } else if (countO == 4) {
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
                        return Value.X;
                    } else if (countO == 4) {
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

    //Computer
    public void compute() throws Exception {
        if (computersLastRow != -1) {
            int freeField = blocksVertikalInRow;
            for (int rows = computersLastRow - 1; rows > 0; rows--) {
                if (field[rows][computersLastCol] == Value.EMPTY) {
                    freeField++;
                }else{
                    blocksVertikalInRow=0;
                    break;
                }
            }
            if (freeField >= 4) {
                computersLastRow = BL.this.makeMove(computersLastCol);
                blocksVertikalInRow++;
                System.out.println("Here are my rows+cols: " + computersLastRow + " " + computersLastCol);
                BL.this.countMoveCountUp();
            } else {
                this.searchNewStartPos();
            }
        } else {
            this.searchNewStartPos();
        }
    }

    private void searchNewStartPos() throws Exception {
        one:
        for (int cols = 0; cols < field.length; cols++) {
            two:
            for (int rows = field.length - 1; rows > 3; rows--) {
                if (field[rows][cols] == Value.EMPTY) {
                    computersLastRow = BL.this.makeMove(cols);
                    computersLastCol = cols;
                    blocksVertikalInRow++;
                    System.out.println("Here are my rows+cols: " + computersLastRow + " " + computersLastCol);
                    BL.this.countMoveCountUp();
                    break one;
                }
            }
        }
    }

    public int getMoveCount() {
        return moveCount;
    }

    public void countMoveCountUp() {
        moveCount++;
    }

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
