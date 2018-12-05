package GUI;


import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
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
    
    public static void main(String[] args) {
        GUI gui = new GUI();
    }
    
    public GUI(){
        super("4-Gewinnt");
        initComp();
        this.setVisible(true);
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    private void initComp(){
        Container container = this.getContentPane();
        container.setLayout(new GridLayout(7, 7, 3, 3));
        for(int cols=0;cols<7;cols++){
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
        
        for(int cols=0;cols<7;cols++){
            for(int rows=1;rows<7;rows++){
                JLabel label = new JLabel();
                label.setOpaque(true);
                label.setBackground(Color.GRAY);
                label.setForeground(Color.white);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setName(rows + "" + cols);
                container.add(label);
            }
        }
    }
    
    
    private void onButtonClick(java.awt.event.MouseEvent event) {
        try {
            Button button = (Button) event.getSource();
            System.out.println("clicked" + button.getName());
            int index = Integer.parseInt(button.getName());
            int colum = index % 10;
            int rows = index / 10;
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
    
    
}
