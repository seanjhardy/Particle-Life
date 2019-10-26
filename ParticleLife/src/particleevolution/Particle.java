/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package particleevolution;

import java.awt.BasicStroke;
import java.awt.Color;
import static java.awt.Color.RED;
import static java.awt.Color.WHITE;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import static java.util.Collections.reverse;
import java.util.Comparator;
import net.jafama.FastMath;
import static particleevolution.HomePanel.particleTypes;
import static particleevolution.SimulationPanel.*;

/**
 *
 * @author seanjhardy
 */
public class Particle {
    private double[] coords = new double[2];
    private double[] vel = new double[2];
    private int type = 0;
    public double dist = 0;
    public int[] interactions = new int[particleTypes.size()];
    int radius = 10;
    
    public Particle(double[] coords, double[] vel, int type){
        this.coords[0] = coords[0];
        this.coords[1] = coords[1];
        this.vel[0] = vel[0];
        this.vel[1] = vel[1];
        this.type = type;
    }
    
    public void update(Graphics g){
        double[] accel = calculateAcceleration(g);
        //System.out.println(Arrays.toString(accel));
        this.vel[0] += accel[0];
        this.vel[1] += accel[1];
        this.vel[0] *= 0.7;
        this.vel[1] *= 0.7;
        
        this.coords[0] += this.vel[0]*speed;
        this.coords[1] += this.vel[1]*speed;
        if(this.coords[0] > width-5){
            this.coords[0] = width-5;
            this.vel[0] *= -1;
        }if(this.coords[0] < 5){
            this.coords[0] = 5;
            this.vel[0] *= -1;
        }
        if(this.coords[1] > height-5){
            this.coords[1] = height-5;
            this.vel[1] *= -1;
        }if(this.coords[1] < 5){
            this.coords[1] = 5;
            this.vel[1] *= -1;
        }
        //tree.recalculatePosition(this);
    }
    
    public double[] calculateAcceleration(Graphics g){
        double size = (int)particleTypes.get(type).maxRange; 
        double[] acceleration = new double[2];
        QuadRect searchArea = new QuadRect(coords[0],coords[1],size,size);
        //g.setColor(WHITE);
        //g.drawRect((int)(searchArea.getX()-searchArea.getWidth()/2), (int)(searchArea.getY()-searchArea.getHeight()/2),(int)(searchArea.getWidth()),(int)(searchArea.getHeight()));
        //g.drawOval((int)(searchArea.getX()-searchArea.getWidth()/2), (int)(searchArea.getY()-searchArea.getHeight()/2),(int)(searchArea.getWidth()),(int)(searchArea.getHeight()));
        ArrayList<Particle> particleList = findParticlesInRange(g, searchArea,size/2);
        for(int particle = 0; particle < particleList.size(); particle++){
            Particle p = particleList.get(particle);
            p.interactions[type] += 1;
            double Y = p.getY()-coords[1];
            double X = p.getX()-coords[0];
            double distance = p.dist;
            double angle = FastMath.atan2(Y,X);
            double minDist = particleTypes.get(p.getType()).forces[type][0];
            double maxDist = particleTypes.get(p.getType()).forces[type][1];
            double scale = 1,force = 0;
            int numForces = (int) particleTypes.get(p.getType()).forces[type][3];
            
            if(distance < minDist){
                force = 2*minDist*(1.0/(minDist+2) - 1.0/(distance+2));
            }else if(distance < maxDist){
                double strength = particleTypes.get(p.getType()).forces[type][2];
                double n = 2*FastMath.abs(distance-0.5*(minDist+maxDist));
                double d =(maxDist-minDist);
                force = strength*(1.0-(n/d));
                //scale = FastMath.max(FastMath.tanh((50.0/numForces)*(numForces-((p.interactions[type])-0.5))),-0.05);
                if(p.interactions[type] > numForces){
                    scale = 0;
                }
            }
            acceleration[0] += force*Math.cos(angle)*scale;
            acceleration[1] += force*Math.sin(angle)*scale;
        }
        return acceleration;
    }
    
    public ArrayList<Particle> findParticlesInRange(Graphics g, QuadRect searchArea, double size){
        ArrayList<Particle> p = new ArrayList<>();
        ArrayList<Double> distances = new ArrayList<>();
        
        for(Particle particle: getParticles()){
            particle.dist = 0;
            if(particle != this){
                if(searchArea.contains(particle)){
                    double distance = FastMath.hypot(particle.getY()-this.coords[1],particle.getX()-this.coords[0]);
                    if(distance < size){
                        if(p.isEmpty()){
                            distances.add(distance);
                            particle.dist = distance;
                            p.add(particle);
                        }else{
                            boolean added = false;
                            for(int i = 0; i < distances.size() && !added; i ++){
                                if(distances.get(i) >= distance || i == distances.size()-1){
                                    p.add(i,particle);
                                    particle.dist = distance;
                                    distances.add(i,distance);
                                    added = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return p;
    }
    
    public void draw(Graphics g){
        g.setColor(particleTypes.get(type).colour);
        g.fillOval((int)coords[0]-radius/2,(int)coords[1]-radius/2,radius,radius);
        //Graphics2D g2d = (Graphics2D)g;
        //g2d.setStroke(new BasicStroke(2));
        //g.setColor(typeColours.get(type)[1]);
        //g.drawOval((int)coords[0]-5,(int)coords[1]-5,10,10);
        //g2d.setStroke(new BasicStroke(1));
    }
    public double getX(){
        return coords[0];
    }
    public double getY(){
        return coords[1];
    }
    public int getType(){
        return type;
    }
}
