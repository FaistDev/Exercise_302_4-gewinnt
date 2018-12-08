package GUI;

import busL.BL;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
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

    private BL bl = new BL();
    private JLabel[][] labels = new JLabel[7][7];

    public static void main(String[] args) {
        GUI gui = new GUI();
    }

    public GUI() {
        super("4-Gewinnt");
        initComp();
        this.setVisible(true);
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
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
                label.setText(rows + "" + cols);
                container.add(label);
                labels[rows][cols] = label;
            }
        }
    }

    private void onButtonClick(java.awt.event.MouseEvent event) {
        try {
            JButton button = (JButton) event.getSource();
            System.out.println("clicked" + button.getName());
            int colum = Integer.parseInt(button.getName());
            int labelRow = bl.makeMove(colum);
            busL.Value val = bl.getValueAt(labelRow, colum);

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
                
                if (winRows.size()!=1 && winCols.size()!=1) {
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
                JOptionPane.showMessageDialog(this, "Player " + winner + " has won the game!");
                bl.reset();
                this.redraw();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

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
