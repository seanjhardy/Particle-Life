/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package particleevolution;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import static particleevolution.SimulationPanel.brightness;

/**
 *
 * @author seanjhardy
 */
public class ParticleEditor {
    int x,y;
    Color colour;
    ParticleType parent;
    int radius = 25;
    
    public ParticleEditor(ParticleType p, int x, int y){
        parent = p;
        this.x = x;
        this.y = y;
    }
    
    public void draw(Graphics2D g, boolean highlighted){
        if(highlighted){
            g.setColor(parent.colour);
            g.fillOval(x-radius,y-radius,radius*2,radius*2);
            
            g.setColor(parent.colour);
            g.setStroke(new BasicStroke(6));
            g.drawOval(x-radius,y-radius,radius*2,radius*2);
            g.setStroke(new BasicStroke(1));
        }else{
            g.setColor(parent.colour);
            g.fillOval(x-radius,y-radius,radius*2,radius*2);
        }
    }
}
