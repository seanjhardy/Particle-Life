/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package particleevolution;

/**
 *
 * @author seanjhardy
 */
public interface QuadObject {
    public double getX();
    public double getY();
    
    public boolean contains(Particle p);
    public boolean intersects(QuadRect range);
    
}
