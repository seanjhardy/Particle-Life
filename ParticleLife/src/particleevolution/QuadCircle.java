/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package particleevolution;

import net.jafama.FastMath;

/**
 *
 * @author seanjhardy
 */
public class QuadCircle implements QuadObject{
    private final double x,y,radius, radiusSquared;
    
    public QuadCircle(double x, double y, double radius){
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.radiusSquared = radius*radius;
    }
    public double getRadius(){
        return radius;
    }
    
    @Override
    public boolean contains(Particle P){
        double d = FastMath.hypot(P.getY() - y, P.getX()-x);
        return d <= radiusSquared;
    }
    
    @Override
    public boolean intersects(QuadRect range){
        double xDist = FastMath.abs(range.getX() - this.x);
        double yDist = FastMath.abs(range.getY() - this.y);

        // radius of the circle
        double r = this.radius;

        double w = range.getWidth();
        double h = range.getHeight();

        double edges = FastMath.pow((xDist - w), 2) + FastMath.pow((yDist - h), 2);
        // no intersection
        if (xDist > (r + w) || yDist > (r + h))
          return false;
        // intersection within the circle
        if (xDist <= w || yDist <= h)
          return true;
        // intersection on the edge of the circle
        return edges <= this.radiusSquared;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }
}
