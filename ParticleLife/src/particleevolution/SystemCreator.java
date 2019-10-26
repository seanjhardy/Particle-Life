/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package particleevolution;

import java.awt.BasicStroke;
import java.awt.Color;
import static java.awt.Color.WHITE;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import static particleevolution.GUIManager.getScreenSize;
import static particleevolution.HomePanel.particleTypes;
import static particleevolution.SimulationPanel.loadFile;
import static particleevolution.SimulationPanel.random;
import static particleevolution.SimulationPanel.saveFile;
import net.jafama.FastMath;

/**
 *
 * @author seanjhardy
 */
public class SystemCreator extends JPanel implements MouseListener{
    private GUIManager parent;
    private JButton exitBtn, saveBtn, loadBtn, addBtn, removeBtn, newBtn;
    JComboBox loadFile;
    private JTextField fileName;
    private JLabel saveLabel;
    private JButton overwriteBtn, cancelBtn;
    public static int width = (int) getScreenSize().getWidth();
    public static int height = (int) getScreenSize().getHeight();
    public ArrayList<ParticleEditor> editors = new ArrayList<>();
    public ParticleEditor selectedEditor, selectedEditor2;
    public int radius = 400, mouseX,mouseY;
    public JSlider R, G, B;
    public JSlider strength, maxDist, minDist, numForces;
    public JLabel valuesLabel;
    public boolean ctrl = false, overwriteDialogueActive=false;
    
    public SystemCreator(GUIManager parent){
        this.parent = parent;
        createWidgets();
        createEditor();
        setBindings();
        addMouseListener(this);
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(new Color(0,0,0));
        g.fillRect(0, 0, 1920, 1080);
        mouseX = (int) MouseInfo.getPointerInfo().getLocation().getX();
        mouseY = (int) MouseInfo.getPointerInfo().getLocation().getY();
        setBounds();
        drawEditor(g);
        revalidate();
        repaint();
    }
    
    public void createWidgets(){
        Border raisedBorder = BorderFactory.createRaisedBevelBorder();
        
        exitBtn = new JButton("BACK");
        exitBtn.addActionListener((ActionEvent e) -> {
            if(e.getSource() == exitBtn){
                parent.setCurrentPanel("home");
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
            }

            @Override
            public void mouseExited(MouseEvent e) {
                exitBtn.setBackground(new Color(255, 0, 0,100));
            }
        });
        exitBtn.setForeground(WHITE);
        exitBtn.setBackground(new Color(255, 0, 0,100));
        exitBtn.setFocusPainted(false);
        exitBtn.setBorder(raisedBorder);
        add(exitBtn);
        
        //save dialogue box
        overwriteBtn = new JButton("Overwrite");
        overwriteBtn.addActionListener((ActionEvent e) -> {
            if(e.getSource() == overwriteBtn){
                saveFile(fileName.getText());
                overwriteDialogueActive = false;
            }
        });
        overwriteBtn.addMouseListener(new MouseListener(){
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
                overwriteBtn.setBackground(new Color(50, 97, 31,200));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                overwriteBtn.setBackground(new Color(50, 97, 31,100));
            }
        });
        overwriteBtn.setForeground(WHITE);
        overwriteBtn.setBorder(raisedBorder);
        overwriteBtn.setBackground(new Color(50, 97, 31,100));
        overwriteBtn.setFocusPainted(false);
        add(overwriteBtn);
        
        cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener((ActionEvent e) -> {
            if(e.getSource() == cancelBtn){
                overwriteDialogueActive = false;
            }
        });
        cancelBtn.addMouseListener(new MouseListener(){
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
                cancelBtn.setBackground(new Color(156, 29, 0,200));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                cancelBtn.setBackground(new Color(156, 29, 0,100));
            }
        });
        cancelBtn.setForeground(WHITE);
        cancelBtn.setBorder(raisedBorder);
        cancelBtn.setBackground(new Color(156, 29, 0,100));
        cancelBtn.setFocusPainted(false);
        add(cancelBtn);
        
        saveBtn = new JButton("Save");
        saveBtn.addActionListener((ActionEvent e) -> {
            if(e.getSource() == saveBtn){
                saveFile(fileName.getText());
                loadFile.removeAllItems();
                final File folder = new File("E:\\Java\\ParticleEvolution\\Saves");
                for (final File fileEntry : folder.listFiles()) {
                    if (!fileEntry.isDirectory()){
                        loadFile.addItem(fileEntry.getName().substring(0,fileEntry.getName().length()-4));
                    }
                }
                parent.simulationPanel.loadFile.removeAllItems();
                for (final File fileEntry : folder.listFiles()) {
                    if (!fileEntry.isDirectory()){
                        parent.simulationPanel.loadFile.addItem(fileEntry.getName().substring(0,fileEntry.getName().length()-4));
                    }
                }
            }
        });
        saveBtn.addMouseListener(new MouseListener(){
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
                saveBtn.setBackground(new Color(50, 97, 31,200));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                saveBtn.setBackground(new Color(50, 97, 31,100));
            }
        });
        saveBtn.setForeground(WHITE);
        saveBtn.setBorder(raisedBorder);
        saveBtn.setBackground(new Color(50, 97, 31,100));
        saveBtn.setFocusPainted(false);
        add(saveBtn);
        
        saveLabel = new JLabel("<html><div style='text-align: center;'>This file already exists.<br>Do you want to overwrite it?</div></html>", SwingConstants.CENTER);
        saveLabel.setForeground(WHITE);
        //saveLabel.setBorder(raisedBorder);
        saveLabel.setBackground(new Color(0, 0, 0,100));
        saveLabel.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        add(saveLabel);
        //===========================
        loadFile = new JComboBox<>();
        loadFile.removeAllItems();
        final File folder = new File("E:\\Java\\ParticleEvolution\\Saves");
        for (final File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory()){
                loadFile.addItem(fileEntry.getName().substring(0,fileEntry.getName().length()-4));
            }
        }
        loadFile.setBounds(500,500,200,200);
        loadFile.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        loadFile.setOpaque(false);
        loadFile.requestFocus(true);
        loadFile.setFocusable(true);
        loadFile.setForeground(WHITE);
        loadFile.setBackground(new Color(0, 0, 0,100));
        loadFile.setMaximumRowCount(12);
        loadFile.setPreferredSize(new Dimension(150, 200));
        loadFile.setBounds(width-150,450,150,50);
        add(loadFile);
        
        loadBtn = new JButton("Load");
        loadBtn.addActionListener((ActionEvent e) -> {
            if(e.getSource() == loadBtn){
                try {
                    String string = (String)loadFile.getSelectedItem();
                    loadFile(string);
                    loadEditor();
                } catch (IOException ex) {
                    Logger.getLogger(SimulationPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        loadBtn.addMouseListener(new MouseListener(){
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
                loadBtn.setBackground(new Color(31, 97, 84,200));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loadBtn.setBackground(new Color(31, 97, 84,100));
            }
        });
        loadBtn.setForeground(WHITE);
        loadBtn.setBorder(raisedBorder);
        loadBtn.setBackground(new Color(31, 97, 84,100));
        loadBtn.setFocusPainted(false);
        add(loadBtn);
        
        addBtn = new JButton("Add");
        addBtn.addActionListener((ActionEvent e) -> {
            if(e.getSource() == addBtn){
                for(ParticleType p: particleTypes){
                    p.updateForces(particleTypes.size()+1,-1);
                }
                
                ParticleType p = new ParticleType(particleTypes.size()+1);
                for(int t  = 0; t < particleTypes.size()+1; t++){
                    double minDist = 5+(random.nextDouble()*15);//min range
                    double maxDist = minDist + random.nextDouble()*100;//max range
                    double strength = (random.nextDouble()-0.5)*2;//strength
                    int numForces = FastMath.max((int)((random.nextDouble()*10+10)),1);
                    p.addForce(t, minDist, maxDist, strength, numForces);
                }
                for(int t  = 0; t < particleTypes.size(); t++){
                    if(particleTypes.size() > 0){
                        double minDist = 5+(random.nextDouble()*15);//min range
                        double maxDist = minDist + random.nextDouble()*100;//max range
                        double strength = (random.nextDouble()-0.5)*2;//strength
                        int numForces = FastMath.max((int)((random.nextDouble()*10+10)),1);
                        particleTypes.get(t).addForce(particleTypes.size(), minDist, maxDist, strength, numForces);
                    }
                }
                int R = (int) ((1/(1+Math.exp(random.nextInt(8)-4)))*255);
                int G = (int) ((1/(1+Math.exp(random.nextInt(8)-4)))*255);
                int B = (int) ((1/(1+Math.exp(random.nextInt(8)-4)))*255);
                Color colour = new Color(R,G,B);
                p.setColour(colour);
                particleTypes.add(p);
                
                for(int i = 0; i < particleTypes.size(); i++){
                    for(int t  = 0; t < particleTypes.size(); t++){
                        if(particleTypes.get(i).forces[t][1] > particleTypes.get(t).maxRange){
                            particleTypes.get(t).maxRange = particleTypes.get(i).forces[t][1];
                        }
                    }
                }
                loadEditor();
            }
        });
        addBtn.addMouseListener(new MouseListener(){
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
                addBtn.setBackground(new Color(0, 156, 62,200));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                addBtn.setBackground(new Color(0, 156, 62,100));
            }
        });
        addBtn.setForeground(WHITE);
        addBtn.setBorder(raisedBorder);
        addBtn.setBackground(new Color(0, 156, 62,100));
        addBtn.setFocusPainted(false);
        add(addBtn);
        
        removeBtn = new JButton("Remove");
        removeBtn.addActionListener((ActionEvent e) -> {
            if(e.getSource() == removeBtn){
                if(selectedEditor != null){
                    for(ParticleType p: particleTypes){
                        p.updateForces(particleTypes.size()-1,particleTypes.indexOf(selectedEditor.parent));
                    }
                    particleTypes.remove(selectedEditor.parent);
                    loadEditor();
                }
            }
        });
        removeBtn.addMouseListener(new MouseListener(){
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
                removeBtn.setBackground(new Color(207, 6, 6,200));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                removeBtn.setBackground(new Color(207, 6, 6,100));
            }
        });
        removeBtn.setForeground(WHITE);
        removeBtn.setBorder(raisedBorder);
        removeBtn.setBackground(new Color(207, 6, 6,100));
        removeBtn.setFocusPainted(false);
        add(removeBtn);
        
        newBtn = new JButton("New");
        newBtn.addActionListener((ActionEvent e) -> {
            if(e.getSource() == newBtn){
                particleTypes = new ArrayList<>();
                loadEditor();
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
                newBtn.setBackground(new Color(0, 156, 62,200));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                newBtn.setBackground(new Color(0, 156, 62,100));
            }
        });
        newBtn.setForeground(WHITE);
        newBtn.setBorder(raisedBorder);
        newBtn.setBackground(new Color(0, 156, 62,100));
        newBtn.setFocusPainted(false);
        add(newBtn);
        
        fileName = new JTextField("",50);
        fileName.addMouseListener(new MouseListener(){
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
                fileName.setBackground(new Color(50, 50, 50,200));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                fileName.setBackground(new Color(0, 0, 0,100));
            }
        });
        fileName.setForeground(WHITE);
        fileName.setCaretColor(WHITE);
        fileName.setBackground(new Color(0, 0, 0,100));
        add(fileName);
    }
    public void createEditor(){
        Border raisedBorder = BorderFactory.createRaisedBevelBorder();
        
        R = new JSlider(JSlider.HORIZONTAL, 0,255,0);
        R.setForeground(WHITE);
        R.setMajorTickSpacing(50);
        R.setMinorTickSpacing(10);
        R.setPaintTicks(true);
        R.setPaintLabels(true);
        R.setBackground(new Color(16, 29, 48));
        add(R);
        
        G = new JSlider(JSlider.HORIZONTAL, 0,255,0);
        G.setForeground(WHITE);
        G.setMajorTickSpacing(50);
        G.setMinorTickSpacing(10);
        G.setPaintTicks(true);
        G.setPaintLabels(true);
        G.setBackground(new Color(16, 29, 48));
        add(G);
        
        B = new JSlider(JSlider.HORIZONTAL, 0,255,0);
        B.setForeground(WHITE);
        B.setMajorTickSpacing(50);
        B.setMinorTickSpacing(10);
        B.setPaintTicks(true);
        B.setPaintLabels(true);
        B.setBackground(new Color(16, 29, 48));
        add(B);
        
        strength = new JSlider(JSlider.HORIZONTAL, -1000,1000,0);
        strength.setForeground(WHITE);
        strength.setMajorTickSpacing(500);
        strength.setMinorTickSpacing(100);
        strength.setPaintTicks(true);
        strength.setPaintLabels(true);
        strength.setBackground(new Color(16, 29, 48));
        add(strength);
        
        minDist = new JSlider(JSlider.HORIZONTAL, 2,30,5);
        minDist.setForeground(WHITE);
        minDist.setMajorTickSpacing(10);
        minDist.setMinorTickSpacing(5);
        minDist.setPaintTicks(true);
        minDist.setPaintLabels(true);
        minDist.setBackground(new Color(16, 29, 48));
        add(minDist);
        
        maxDist = new JSlider(JSlider.HORIZONTAL, 0,200,0);
        maxDist.setForeground(WHITE);
        maxDist.setMajorTickSpacing(50);
        maxDist.setMinorTickSpacing(10);
        maxDist.setPaintTicks(true);
        maxDist.setPaintLabels(true);
        maxDist.setBackground(new Color(16, 29, 48));
        add(maxDist);
        
        numForces = new JSlider(JSlider.HORIZONTAL, 0,200,0);
        numForces.setForeground(WHITE);
        numForces.setMajorTickSpacing(50);
        numForces.setMinorTickSpacing(10);
        numForces.setPaintTicks(true);
        numForces.setPaintLabels(true);
        numForces.setBackground(new Color(16, 29, 48));
        add(numForces);
        
        valuesLabel = new JLabel("",SwingConstants.CENTER);
        valuesLabel.setForeground(WHITE);
        valuesLabel.setBorder(raisedBorder);
        valuesLabel.setBackground(new Color(0, 0, 0,100));
        valuesLabel.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        add(valuesLabel);
        
    }
    public void setBindings(){
        InputMap im = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap am = getActionMap();
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0, false), "CTRLUp");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0, true), "CTRLDown");
        am.put("CTRLUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ctrl = true;
            }
        });
        am.put("CTRLDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
               ctrl = false;
            }   
        });
    }
    
    public void loadEditor(){
        selectedEditor = null;
        selectedEditor2 = null;
        editors = new ArrayList<>();
        int length = particleTypes.size();
        for(int i = 0; i < particleTypes.size(); i++){
            int x = width/2 + (int)(Math.cos(Math.toRadians(360 * i/length - 90))*radius);
            int y = height/2 + (int)(Math.sin(Math.toRadians(360 * i/length -90))*radius);
            ParticleEditor editor = new ParticleEditor(particleTypes.get(i),x,y);
            editors.add(editor);
        }
    }
    public void drawEditor(Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        for(ParticleEditor p: editors){
            g2d.setStroke(new BasicStroke(10));
            for(ParticleEditor p2 : editors){
                if(selectedEditor != null){
                    if((p == selectedEditor || p2 == selectedEditor) && (selectedEditor2 == null || (p == selectedEditor2 || p2 == selectedEditor2))){
                        g2d.setColor(new Color(4, 217, 93));
                    }else{
                        g2d.setColor(new Color(6, 46, 69,100));
                    }
                }else{
                    g2d.setColor(new Color(25, 79, 110));
                }
                if(p == p2){
                    g2d.setColor(p.colour);
                    int newRadius = (int) (p.radius * 2);
                    g2d.drawOval(p.x-newRadius,p.y-newRadius,newRadius*2,newRadius*2);
                }else{
                    double angle = FastMath.atan2(p2.y-p.y,p2.x-p.x);
                    int x = p.x +(int) (Math.cos(angle)*p.radius*2.5);
                    int y = p.y +(int) (Math.sin(angle)*p.radius*2.5);
                    
                    int x2 = p2.x-(int) (Math.cos(angle)*p2.radius*2.5);
                    int y2 = p2.y-(int) (Math.sin(angle)*p2.radius*2.5);
                    g2d.drawLine(x,y,x2,y2);
                }
            }
            g2d.setStroke(new BasicStroke(1));
        }
        for(ParticleEditor p: editors){
            p.draw(g2d, FastMath.hypot(mouseY-p.y,mouseX-p.x) < p.radius || p == selectedEditor);
        } 
    }
    public void setBounds(){
        exitBtn.setBounds(width-150,0,150,50);
        newBtn.setBounds(width-150,50,150,50);
        
        fileName.setBounds(width-150,100,150,50);
        saveBtn.setBounds(width-150,150,150,50);
        loadFile.setBounds(width-150,200,150,50);
        loadBtn.setBounds(width-150,250,150,50);
        //loadFile.getComponents()[0].setSize(20, 50);
        //loadFile.getComponents()[0].setLocation(280- getComponents()[0].getWidth(),0);
        
        if(overwriteDialogueActive){
            saveLabel.setBounds(width/2-200,height/2-200,400,100);
            overwriteBtn.setBounds(width/2-200,height/2-100,100,50);
            cancelBtn.setBounds(width/2 + 100,height/2-100,100,50);
        }else{
            saveLabel.setBounds(-100,-100,100,50);
            overwriteBtn.setBounds(-100,-100,100,50);
            cancelBtn.setBounds(-100,-100,100,50);
        }
        addBtn.setBounds(width-150,300,150,50);
        removeBtn.setBounds(width-150,350,150,50);
        
        if(selectedEditor == null){
            R.setBounds(-100,-100,100,100);
            G.setBounds(-100,-100,100,100);
            B.setBounds(-100,-100,100,100);
            strength.setBounds(-100,-100,100,50);
            minDist.setBounds(-100,-100,100,50);
            maxDist.setBounds(-100,-100,100,50);
            numForces.setBounds(-100,-100,100,50);
            valuesLabel.setBounds(-100,-100,100,100);
        }else{
            if(selectedEditor2 != null){
                int x = ((selectedEditor.x+selectedEditor2.x)/2)+50;
                int y = ((selectedEditor.y+selectedEditor2.y)/2)-93;
                strength.setBounds(x,y,200,50);
                minDist.setBounds(x,y+50,200,50);
                maxDist.setBounds(x,y+100,200,50);
                numForces.setBounds(x,y+150,200,50);
                valuesLabel.setBounds(x+180,y,200,200);
                valuesLabel.setText("<html>Force: " + strength.getValue()/1000.0+"<br><br>"
                        + "Min Dist: " + minDist.getValue() + "<br><br>"
                        + "Max Dist: " + maxDist.getValue() + "<br><br>"
                        + "Num Forces: " + numForces.getValue() + "</html>");
                selectedEditor.parent.forces[particleTypes.indexOf(selectedEditor2.parent)][0] = minDist.getValue();
                selectedEditor.parent.forces[particleTypes.indexOf(selectedEditor2.parent)][1] = maxDist.getValue();
                selectedEditor.parent.forces[particleTypes.indexOf(selectedEditor2.parent)][2] = (double)strength.getValue()/1000.0;
                selectedEditor.parent.forces[particleTypes.indexOf(selectedEditor2.parent)][3] = numForces.getValue();
                for(int i = 0; i < particleTypes.size(); i++){
                    for(int t  = 0; t < particleTypes.size(); t++){
                        if(particleTypes.get(i).forces[t][1] > particleTypes.get(t).maxRange){
                            particleTypes.get(t).maxRange = particleTypes.get(i).forces[t][1];
                        }
                    }
                }
                R.setBounds(-100,-100,100,50);
                G.setBounds(-100,-100,100,50);
                B.setBounds(-10,-100,100,50);
            }else{
                int x = selectedEditor.x+50;
                int y = selectedEditor.y-70;
                R.setBounds(x,y,200,50);
                G.setBounds(x,y+50,200,50);
                B.setBounds(x,y+100,200,50);
                selectedEditor.parent.setColour(new Color(R.getValue(),G.getValue(),B.getValue()));
                strength.setBounds(-100,-100,100,50);
                minDist.setBounds(-100,-100,100,50);
                maxDist.setBounds(-100,-100,100,50);
                numForces.setBounds(-100,-100,100,50);
                valuesLabel.setBounds(-100,-100,100,100);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(ctrl){
            selectedEditor2 = null;
            for(ParticleEditor p: editors){
                if(Math.hypot(mouseY-p.y,mouseX-p.x) < p.radius){
                    selectedEditor2 = p;
                    double[] force = selectedEditor.parent.forces[particleTypes.indexOf(selectedEditor2.parent)];
                    minDist.setValue((int)(force[0]));
                    maxDist.setValue((int)(force[1]));
                    strength.setValue((int)(1000*force[2]));//scale by 1000
                    numForces.setValue((int)(force[3]));
                }
            }
        }else{
            selectedEditor = null;
            selectedEditor2 = null;
            for(ParticleEditor p: editors){
                if(Math.hypot(mouseY-p.y,mouseX-p.x) < p.radius){
                    selectedEditor = p;
                    R.setValue(p.parent.colour.getRed());
                    G.setValue(p.parent.colour.getGreen());
                    B.setValue(p.parent.colour.getBlue());
                }
            } 
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
