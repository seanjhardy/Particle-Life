/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package particleevolution;

import java.awt.Color;
import static java.awt.Color.GREEN;
import static java.awt.Color.WHITE;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import static particleevolution.SimulationPanel.*;

/**
 *
 * @author seanjhardy
 */
public class QuadTree {
    private QuadTree parent = null;
    private int depth = 0;
    private QuadRect boundary;
    private ArrayList<Particle> points = new ArrayList<>();
    private boolean divided = false;
    QuadTree NW;
    QuadTree NE;
    QuadTree SW;
    QuadTree SE;
    
    public QuadTree(QuadTree parent, QuadRect bounds){
        this.boundary = bounds;
        this.parent = parent;
        if(parent != null){
            this.depth = parent.depth+1;
        }
    }
    
    public boolean insert(Particle P){
        if(!this.boundary.contains(P)){
            return false;
        }
        if(this.points.size() < capacity){
            this.points.add(P);
            return true;
        }else if(!divided){
            if(depth < maxDepth){
                subdivide();
            }
        }
        if(divided){
            this.points.add(P);
            return(NE.insert(P) || NW.insert(P) || SE.insert(P) || SW.insert(P));
        }else{
            this.points.add(P);
            return true;
        }
    }
    private void subdivide(){
        double x = this.boundary.getX();
        double y = this.boundary.getY();
        double width = this.boundary.getWidth()/2;
        double height = this.boundary.getHeight()/2;
        QuadRect ne = new QuadRect(x + width, y - height, width, height);
        this.NE = new QuadTree(this, ne);
        QuadRect nw = new QuadRect(x - width, y - height, width, height);
        this.NW = new QuadTree(this, nw);
        QuadRect se = new QuadRect(x + width, y + height, width, height);
        this.SE = new QuadTree(this, se);
        QuadRect sw = new QuadRect(x - width, y + height, width, height);
        this.SW = new QuadTree(this, sw);
        divided = true;
        for(Particle p : this.points){
            if(!NE.insert(p)){
                if(!NW.insert(p)){
                    if(!SE.insert(p)){
                        SW.insert(p);
                    }
                }
            }
        }
    }
    
    public void recalculatePosition(Particle p){
        QuadTree pTree = getQuadTree(p);
        if(!pTree.boundary.contains(p)){
            pTree.removeParticle(p);
            getRootQuadTree().insert(p);
        }
    }
    
    public void recalibrate(){
        if(divided){
            NE.recalibrate(); 
            NW.recalibrate(); 
            SE.recalibrate(); 
            SW.recalibrate();
            if(this.points.size() < capacity){
                this.divided = false;
                NE = null;
                NW = null;
                SE = null;
                SW = null;
            }
        }
    }
    
    public QuadTree getQuadTree(Particle p){
        if(divided){
            if(NE.points.contains(p)){
                return NE.getQuadTree(p);
            }else if(NW.points.contains(p)){
                return NW.getQuadTree(p);
            }else if(SE.points.contains(p)){
                return SE.getQuadTree(p);
            }else if(SW.points.contains(p)){
                return SW.getQuadTree(p);
            }else{
                if(NE.boundary.contains(p)){
                    NE.insert(p);
                    return NE.getQuadTree(p);
                }else if(NW.boundary.contains(p)){
                    NW.insert(p);
                    return NW.getQuadTree(p);
                }else if(SE.boundary.contains(p)){
                    SE.insert(p);
                   return SE.getQuadTree(p); 
                }else{
                    SW.insert(p);
                    return SW.getQuadTree(p);
                }
            }
        }
        return this;
    }
    
    public ArrayList<Particle> query(QuadObject range, ArrayList<Particle> found){
        if(!range.intersects(this.boundary)){
            return found;
        }
        for(Particle P: this.points){
            if(range.contains(P)){
                found.add(P);
            }
        }
        if(divided){
            this.NE.query(range,found);
            this.NW.query(range,found);
            this.SE.query(range,found);
            this.SW.query(range,found);
        }
        return found;
    }
    
    public void removeParticle(Particle p){
        points.remove(p);
        if(parent != null){
            parent.removeParticle(p);
        }
    }
    
    public void drawTree(Graphics g){
        double x = this.boundary.getX();
        double y = this.boundary.getY();
        double width = this.boundary.getWidth();
        double height = this.boundary.getHeight();
        g.setColor(WHITE);
        g.drawRect((int)(x-width),(int)(y-height),(int)(width*2),(int)(height*2));
        if(divided){
            NW.drawTree(g);
            NE.drawTree(g);
            SW.drawTree(g);
            SE.drawTree(g);
        }
        //g.drawString(Integer.toString(points.size()), (int)(this.boundary.getX()), (int)(this.boundary.getY()));
        
    }
    
}
