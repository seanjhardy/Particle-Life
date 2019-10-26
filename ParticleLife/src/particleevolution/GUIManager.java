/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package particleevolution;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author seanjhardy
 */
/**
 *
 * @author s-hardy
 */
public class GUIManager extends JFrame{
    private CardLayout layoutController;
    private static JPanel mainPanel;
    private String currentPanel = "home";
    SimulationPanel simulationPanel;
    private HomePanel homePanel;
    SystemCreator creatorPanel;
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    
    public GUIManager(){
        super("Electric Field Simulation");
        
        homePanel = new HomePanel(this);
        simulationPanel = new SimulationPanel(this);
        creatorPanel = new SystemCreator(this);
        
        layoutController = new CardLayout();
        mainPanel = new JPanel(layoutController);
        
        layoutController.addLayoutComponent(simulationPanel, "simulation");  
        layoutController.addLayoutComponent(homePanel, "home");  
        layoutController.addLayoutComponent(creatorPanel, "creator");  
        mainPanel.add(simulationPanel);
        mainPanel.add(homePanel);
        mainPanel.add(creatorPanel);
        add(mainPanel);
        setCurrentPanel("home");
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setSize((int)screenSize.getWidth(), (int)screenSize.getHeight());
        setBackground(new Color(0, 0, 0));
        setVisible(true); 
    }
    
    public static Dimension getScreenSize(){
        return screenSize;
    }
    
    public void setCurrentPanel(String panel){
        currentPanel = panel;
        layoutController.show(mainPanel, currentPanel);
        switch (currentPanel) {
            case "home":
                homePanel.requestFocus();
                homePanel.revalidate();
                homePanel.repaint();
                break;
            case "creator":
                creatorPanel.requestFocus();
                creatorPanel.loadEditor();
                creatorPanel.revalidate();
                creatorPanel.repaint();
                break;
            case "simulation":
                simulationPanel.requestFocus();
                simulationPanel.particles = new ArrayList<>();
                simulationPanel.revalidate();
                simulationPanel.repaint();
                break;
            default:
                break;
        }
        //The frame then needs to be repainted after the update
        revalidate();
        repaint();
    }
}
