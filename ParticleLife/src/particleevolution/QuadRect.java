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
public class QuadRect implements QuadObject{
    private double x,y,width,height;
    
    public QuadRect(double x, double y, double width, double height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public double getWidth(){
        return width;
    }
    public double getHeight(){
        return height;
    }
    
    @Override
    public boolean contains(Particle P){
        return (P.getX() >= x - width && P.getY() >= y - height && P.getX() <= x + width && P.getY() <= y + height);
    }
    
    @Override
    public boolean intersects(QuadRect range){
        return !(range.getX() - range.getWidth() > this.x + this.width ||
          range.getX() + range.getWidth() < this.x - this.width ||
          range.getY() - range.getHeight() > this.y + this.height ||
          range.getY() + range.getHeight() < this.y - this.height);
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
