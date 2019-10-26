/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package particleevolution;

import java.awt.Color;
import static java.awt.Color.WHITE;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;
import static particleevolution.GUIManager.getScreenSize;

/**
 *
 * @author seanjhardy
 */
public class HomePanel extends JPanel{
    
    private JButton exitBtn,newBtn,creatorBtn;
    private GUIManager parent;
    public static int width = (int) getScreenSize().getWidth();
    public static int height = (int) getScreenSize().getHeight();
    public static ArrayList<ParticleType> particleTypes = new ArrayList<>();
    
    public HomePanel(GUIManager parent){
        setBackground(new Color(0, 0, 0));
        createWidgets();
        this.parent = parent;
    }
    
    public void createWidgets(){
        Border raisedBorder = BorderFactory.createRaisedBevelBorder();
        
        exitBtn = new JButton("EXIT");
        exitBtn.addActionListener((ActionEvent e) -> {
            if(e.getSource() == exitBtn){
                System.exit(0);
            }
        });
        exitBtn.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                exitBtn.setBackground(new Color(255, 0, 0,200));
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                exitBtn.setBackground(new Color(255, 0, 0,100));
                repaint();
            }
        });
        exitBtn.setForeground(WHITE);
        exitBtn.setBackground(new Color(255, 0, 0,100));
        exitBtn.setFocusPainted(false);
        exitBtn.setBorder(raisedBorder);
        add(exitBtn);
        //newMatchBtn
        newBtn = new JButton("New Simulation");
        newBtn.addActionListener((ActionEvent e) -> {
            if(e.getSource() == newBtn){
                parent.setCurrentPanel("simulation");
            }
        });
        newBtn.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                newBtn.setBackground(new Color(6, 65, 66,200));
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                newBtn.setBackground(new Color(6, 65, 66,100));
                repaint();
            }
        });
        newBtn.setForeground(WHITE);
        newBtn.setBackground(new Color(6, 65, 66,100));
        newBtn.setFocusPainted(false);
        newBtn.setBorder(raisedBorder);
        add(newBtn);
        //newMatchBtn
        creatorBtn = new JButton("System Creator");
        creatorBtn.addActionListener((ActionEvent e) -> {
            if(e.getSource() == creatorBtn){
                parent.setCurrentPanel("creator");
            }
        });
        creatorBtn.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                creatorBtn.setBackground(new Color(6, 65, 66,200));
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                creatorBtn.setBackground(new Color(6, 65, 66,100));
                repaint();
            }
        });
        creatorBtn.setForeground(WHITE);
        creatorBtn.setBackground(new Color(6, 65, 66,100));
        creatorBtn.setFocusPainted(false);
        creatorBtn.setBorder(raisedBorder);
        add(creatorBtn);
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponents(g);
        g.setColor(new Color(0,0,0));
        g.fillRect(0, 0, 1920, 1080);
        exitBtn.setBounds(width-150,0,150,50);
        newBtn.setBounds(width/2 - 150,300,300,50);
        creatorBtn.setBounds(width/2 - 150,400,300,50);
    }
    
}
