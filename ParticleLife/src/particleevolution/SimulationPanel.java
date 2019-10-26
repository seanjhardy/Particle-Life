/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package particleevolution;

import java.awt.Color;
import static java.awt.Color.GREEN;
import static java.awt.Color.WHITE;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.List;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
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
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import static particleevolution.GUIManager.*;
import static particleevolution.HomePanel.particleTypes;
import net.jafama.FastMath;

/**
 *
 * @author seanjhardy
 */
public class SimulationPanel extends JPanel implements MouseListener{
   
    private GUIManager parent;
    private JButton exitBtn, slowDownBtn, speedUpBtn;
    private JButton saveBtn, resetBtn, loadBtn;
    JComboBox loadFile;
    private JLabel infoLabel;
    private JTextField fileName;
    private JLabel saveLabel;
    private JButton overwriteBtn, cancelBtn;
    public static int width = (int) getScreenSize().getWidth();
    public static int height = (int) getScreenSize().getHeight();
    private static QuadTree quadTree;
    public static ArrayList<Particle> particles = new ArrayList<>();
    public static Random random = new Random();
    private int step = 0;
    public static DecimalFormat df = new DecimalFormat("#.##");
    public static int capacity = 4;
    public static int maxDepth = 8;
    private long lastTime;
    private double fps;
    public boolean showQuadTree = false, createParticles = false, overwriteDialogueActive = false;
    public int numTypes = 10;
    public static double speed = 1;
    
    public SimulationPanel(GUIManager parent){
        createWidgets();
        setBindings();
        this.parent = parent;
        //quadTree = new QuadTree(null, new QuadRect(width/2,height/2,width/2,height/2));
        df.setRoundingMode(RoundingMode.CEILING);
        reset();
        /*for(int i = 0; i < 00; i++){
            double[] coords = {random.nextInt(width), random.nextInt(height)};
            double[] vel = {0,0};
            int type = random.nextInt(particleTypes.size());
            Particle p = new Particle(coords, vel, type);
            particles.add(p);
            quadTree.insert(p);
        }*/
        addMouseListener(this);
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        step ++;
        g.setColor(new Color(0,0,0));
        g.fillRect(0, 0, 1920, 1080);
        setBounds();
        updateParticles(g);
        //if(showQuadTree){
        //    quadTree.drawTree(g);
        //}
        //quadTree.recalibrate();
        drawParticles(g);
        drawData(g);
        
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
        //speed btns
        slowDownBtn = new JButton("<<");
        slowDownBtn.addActionListener((ActionEvent e) -> {
            if(e.getSource() == slowDownBtn){
                speed /= 1.5;
            }
        });
        slowDownBtn.addMouseListener(new MouseListener(){
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
                slowDownBtn.setBackground(new Color(6, 65, 66,200));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                slowDownBtn.setBackground(new Color(6, 65, 66,100));
            }
        });
        slowDownBtn.setForeground(WHITE);
        slowDownBtn.setBackground(new Color(6, 65, 66,100));
        slowDownBtn.setFocusPainted(false);
        slowDownBtn.setBorder(raisedBorder);
        add(slowDownBtn);
        
        speedUpBtn = new JButton(">>");
        speedUpBtn.addActionListener((ActionEvent e) -> {
            if(e.getSource() == speedUpBtn){
                speed *= 1.5;
            }
        });
        speedUpBtn.addMouseListener(new MouseListener(){
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
                speedUpBtn.setBackground(new Color(6, 65, 66,200));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                speedUpBtn.setBackground(new Color(6, 65, 66,100));
            }
        });
        speedUpBtn.setForeground(WHITE);
        speedUpBtn.setBackground(new Color(6, 65, 66,100));
        speedUpBtn.setFocusPainted(false);
        speedUpBtn.setBorder(raisedBorder);
        add(speedUpBtn);
        
        //reset btn
        
        resetBtn = new JButton("Randomise");
        resetBtn.addActionListener((ActionEvent e) -> {
            if(e.getSource() == resetBtn){
                reset();
            }
        });
        resetBtn.addMouseListener(new MouseListener(){
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
                resetBtn.setBackground(new Color(156, 29, 0,200));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                resetBtn.setBackground(new Color(156, 29, 0,100));
            }
        });
        resetBtn.setForeground(WHITE);
        resetBtn.setBackground(new Color(156, 29, 0,100));
        resetBtn.setFocusPainted(false);
        resetBtn.setBorder(raisedBorder);
        add(resetBtn);
        
        saveBtn = new JButton("Save");
        saveBtn.addActionListener((ActionEvent e) -> {
            if(e.getSource() == saveBtn){
                if(new File("Saves\\"+fileName.getText()+".txt").exists()){
                    overwriteDialogueActive = true;
                }else{
                    saveFile(fileName.getText());
                    loadFile.removeAllItems();
                    final File folder = new File("E:\\Java\\ParticleEvolution\\Saves");
                    for (final File fileEntry : folder.listFiles()){
                        if (!fileEntry.isDirectory()){
                            loadFile.addItem(fileEntry.getName().substring(0,fileEntry.getName().length()-4));
                        }
                    }
                    
                    parent.creatorPanel.loadFile.removeAllItems();
                    for (final File fileEntry : folder.listFiles()) {
                        if (!fileEntry.isDirectory()){
                            parent.creatorPanel.loadFile.addItem(fileEntry.getName().substring(0,fileEntry.getName().length()-4));
                        }
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
        loadFile.setForeground(WHITE);
        loadFile.setBackground(new Color(0, 0, 0));
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
        
        infoLabel = new JLabel("", SwingConstants.CENTER);
        infoLabel.setBorder(raisedBorder);
        infoLabel.setForeground(WHITE);
        infoLabel.setBackground(new Color(31, 97, 84,100));
        add(infoLabel);
    }
    public void setBindings(){
        InputMap im = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap am = getActionMap();
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_1, 0), "showQuadTree");
        
        am.put("showQuadTree", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
               showQuadTree = !showQuadTree;
            }
        });
    }
    public void setBounds(){
        resetBtn.setBounds(width-150,50,150,50);
        exitBtn.setBounds(width-150,0,150,50);
        
        infoLabel.setBounds(width-150,100,150,200);
        
        slowDownBtn.setBounds(width-150,300,75,50);
        speedUpBtn.setBounds(width-75,300,75,50);
        
        fileName.setBounds(width-150,350,150,50);
        saveBtn.setBounds(width-150,400,150,50);
        loadFile.setBounds(width-150,450,150,50);
        loadBtn.setBounds(width-150,500,150,50);
        loadFile.getComponents()[0].setSize(20, 50);
        loadFile.getComponents()[0].setLocation(280- getComponents()[0].getWidth(),0);
        
        if(overwriteDialogueActive){
            saveLabel.setBounds(width/2-200,height/2-200,400,100);
            overwriteBtn.setBounds(width/2-200,height/2-100,100,50);
            cancelBtn.setBounds(width/2 + 100,height/2-100,100,50);
        }else{
            saveLabel.setBounds(-100,-100,100,50);
            overwriteBtn.setBounds(-100,-100,100,50);
            cancelBtn.setBounds(-100,-100,100,50);
        }
    }
    
    public static Color brightness(Color c, double scale) {
        int r = FastMath.min(255, (int) (c.getRed() * scale));
        int g = FastMath.min(255, (int) (c.getGreen() * scale));
        int b = FastMath.min(255, (int) (c.getBlue() * scale));
        return new Color(r,g,b);
    }
    public void drawData(Graphics g){
        if(step % 50 == 0){
            fps = Double.parseDouble(df.format(1000000000.0 / (System.nanoTime() - lastTime)*50));
            lastTime = System.nanoTime();
        }
        String text = "<html>"
                + "FPS: " +Double.toString(fps) + "<br>"
                + "Num Particles: " + particles.size() + "<br>"
                + "Speed: " + df.format(speed) + "<br>"
                + "</html>";
        g.setColor(WHITE);
        infoLabel.setText(text);
        if(overwriteDialogueActive){
            g.setColor(new Color(33, 54, 82,200));
            g.fillRect(width/2-225,height/2-225,450,200);
        }
    }
    public void updateParticles(Graphics g){
        if(createParticles){
            for(int i = 0; i < 1;i++){
                int mouseX = (int) MouseInfo.getPointerInfo().getLocation().getX();
                int mouseY = (int) MouseInfo.getPointerInfo().getLocation().getY();
                double[] coords = {mouseX, mouseY};
                double[] vel = {0.01, 0};
                Particle p = new Particle(coords, vel, random.nextInt(particleTypes.size()));
                particles.add(p);
                //quadTree.insert(p);
            }
            createParticles= false;
        }
        for(Particle p : particles){
            p.interactions = new int[particleTypes.size()];
        }
        for(Particle p : particles){
            p.update(g);
        }
    }
    public void drawParticles(Graphics g){
        g.setColor(WHITE);
        for(Particle p : particles){
            p.draw(g);
            g.setColor(WHITE);
            g.drawString(Integer.toString(p.interactions[0] + p.interactions[1]),(int)p.getX()-3, (int)p.getY()+3);
        }
    }
    public static ArrayList<Particle> getParticles(){
        return particles;
    }
    
    public static void saveFile(String filename){
        try ( BufferedWriter bw = new BufferedWriter (new FileWriter ("Saves\\"+filename+".txt")) ) 
        {	
            String text = Integer.toString(particleTypes.size());
            bw.write(text);
            bw.newLine();
            for(ParticleType p: particleTypes){
                text = "";
                for(double[] f: p.forces){
                    for(double prop: f){
                        text += prop+",";
                    }
                }
                text += p.colour.getRed() + "," + p.colour.getGreen() + "," + p.colour.getBlue() + ",";
                text += p.maxRange;
                bw.write(text);
                bw.newLine();
            }
            bw.close ();
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }
    public static void loadFile(String filename) throws FileNotFoundException, IOException{
        particleTypes = new ArrayList<>();
        particles = new ArrayList<>();
        
        File file = new File("Saves\\"+filename+".txt"); 
        BufferedReader br = new BufferedReader(new FileReader(file)); 
        String str; 
        int numParticles = Integer.parseInt(br.readLine());
        int count = 0;
        while ((str = br.readLine()) != null && count < numParticles) {
            ParticleType p = new ParticleType(numParticles);
            ArrayList<String> list = new ArrayList<String>(Arrays.asList(str.split(",")));
            int length = (list.size()-4)/4;
            for(int i = 0; i < length && i < numParticles; i++){
                double min = Double.parseDouble(list.get(i*4));
                double max = Double.parseDouble(list.get(i*4 + 1));
                double range = Double.parseDouble(list.get(i*4 + 2));
                int numForces = (int)Double.parseDouble(list.get(i*4 + 3));
                p.addForce(i,min, max,range, numForces);
            }
            int R = Integer.parseInt(list.get(list.size()-4));
            int G = Integer.parseInt(list.get(list.size()-3));
            int B = Integer.parseInt(list.get(list.size()-2));
            p.setColour(new Color(R,G,B));
            p.maxRange = Double.parseDouble(list.get(list.size()-1));
            //System.out.println(Arrays.deepToString(p.forces));
            particleTypes.add(p);
            count++;
        }
    }
    
    public void reset(){
        particles = new ArrayList<>();
        particleTypes = new ArrayList<>();
        for(int i = 0; i < numTypes; i++){
            ParticleType type = new ParticleType(numTypes);
            for(int t  = 0; t < numTypes; t++){
                double minDist = 5+(random.nextDouble()*15);//min range
                double maxDist = minDist + random.nextDouble()*100;//max range
                double strength = (random.nextDouble()-0.5)*2;//strength
                int numForces = FastMath.max((int)((random.nextDouble()*10+10)),1);
                type.addForce(t, minDist, maxDist, strength, numForces);
            }
            int R = (int) ((1/(1+Math.exp(random.nextInt(8)-4)))*255);
            int G = (int) ((1/(1+Math.exp(random.nextInt(8)-4)))*255);
            int B = (int) ((1/(1+Math.exp(random.nextInt(8)-4)))*255);
            Color colour = new Color(R,G,B);
            type.setColour(colour);
            particleTypes.add(type);
        }
        
        for(int i = 0; i < numTypes; i++){
            for(int t  = 0; t < numTypes; t++){
                if(particleTypes.get(i).forces[t][1] > particleTypes.get(t).maxRange){
                    particleTypes.get(t).maxRange = particleTypes.get(i).forces[t][1];
                }
            }
        }
    }
    public static QuadTree getRootQuadTree(){
        return quadTree;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
    }
    @Override
    public void mousePressed(MouseEvent e) {
        createParticles = true;
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        createParticles = false;
    }
    @Override
    public void mouseEntered(MouseEvent e) {
    }
    @Override
    public void mouseExited(MouseEvent e) {
    }
}
