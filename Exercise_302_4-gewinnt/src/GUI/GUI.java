package GUI;

import busL.BL;
import busL.Block;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.scene.input.KeyCode.O;
import static javafx.scene.input.KeyCode.X;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import jdk.internal.org.objectweb.asm.tree.analysis.Value;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ben
 */
public class GUI extends JFrame {

    /* Ich war ein wenig übermotiviert
    und habe versucht eine KI zu implementieren
    welche dazu lernen soll -> tut sie auch, aber sie ist etwas dumm :)
    Leider hat der Code durch diese "Spielerei" an Sauberkeit verloren*/

    private BL bl = new BL();
    private JLabel[][] labels = new JLabel[7][7];

    public static void main(String[] args) {
        try {
            GUI gui = new GUI();
        } catch (Exception ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public GUI() throws Exception {
        super("4-Gewinnt");
        initComp();
        this.setVisible(true);
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        bl.loadLearning();
    }

    private void initComp() {
        Container container = this.getContentPane();
        container.setLayout(new GridLayout(7, 7, 3, 3));
        for (int cols = 0; cols < 7; cols++) {
            JButton button = new JButton();
            button.setHorizontalAlignment(JLabel.CENTER);
            button.setName(0 + "" + cols);
            button.setText("V");
            button.setOpaque(true);

            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent event) {
                    onButtonClick(event);
                }

            });

            container.add(button);
        }

        for (int rows = 1; rows < 7; rows++) {
            for (int cols = 0; cols < 7; ++cols) {
                JLabel label = new JLabel();
                label.setOpaque(true);
                label.setBackground(Color.GRAY);
                label.setForeground(Color.white);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setName(rows + "" + cols);
               // label.setText(rows + "" + cols);
                container.add(label);
                labels[rows][cols] = label;
            }
        }
    }

    private void onButtonClick(java.awt.event.MouseEvent event) {
        try {
            int colum;
            int labelRow;
            JButton button = (JButton) event.getSource();
            System.out.println("clicked" + button.getName());
            colum = Integer.parseInt(button.getName());
            labelRow = bl.makeMove(colum);
            this.drawIt(labelRow, colum);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void drawIt(int labelRow, int colum) {
        busL.Value val = bl.getValueAt(labelRow, colum);
        Thread thread = new Thread() {
            public void run() {
                for (int row = 1; row < labelRow; row++) {
                    switch (val) {
                        case X:
                            labels[row][colum].setBackground(Color.red);
                            break;
                        case O:
                            labels[row][colum].setBackground(Color.blue);
                            break;
                    }
                    try {
                        this.sleep(500);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    labels[row][colum].setBackground(Color.GRAY);
                }

                switch (val) {
                    case X:
                        labels[labelRow][colum].setBackground(Color.red);
                        break;
                    case O:
                        labels[labelRow][colum].setBackground(Color.blue);
                        break;
                }

                busL.Value winner = bl.checkWinner();
                if (winner != busL.Value.EMPTY) {
                    ArrayList<Integer> winCols = bl.getWinCols();
                    ArrayList<Integer> winRows = bl.getWinRows();

                    if (winRows.size() != 1 && winCols.size() != 1) {
                        int rows = 0;
                        int cols = 0;
                        while (rows < winRows.size()) {
                            labels[winRows.get(rows)][winCols.get(cols)].setBackground(Color.orange);
                            rows++;
                            cols++;
                        }
                    } else {
                        for (Integer winRow : winRows) {
                            for (Integer winCol : winCols) {
                                labels[winRow][winCol].setBackground(Color.orange);
                            }
                        }
                    }
                    JOptionPane.showMessageDialog(GUI.this, "Player " + winner + " has won the game!");
                    try {
                        Block.countGamesUp();
                        bl.saveLearning();
                    } catch (IOException ex) {
                        Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    bl.reset();
                    GUI.this.redraw();
                } else {
                    //Falls das Spiel nicht beendet ist:
                    if (bl.getCurrentPlayer() == busL.Value.O) {//Prüfen ob Computer dran ist
                        try {
                            bl.compute();//Computer Logik ausfürhen
                        } catch (Exception ex) {
                            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        GUI.this.drawIt(bl.getComputersLastRow(), bl.getComputersLastCol());//Darstellung vom ComputerMove
                    }
                }

            }
        };

        thread.start();
    }

    private void redraw() {
        for (int rows = 1; rows < labels.length; rows++) {
            for (int col = 0; col < labels.length; col++) {
                JLabel label = labels[rows][col];
                label.setBackground(Color.GRAY);
            }
        }
    }

    

}
