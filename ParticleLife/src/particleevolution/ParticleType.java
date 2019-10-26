/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package particleevolution;

import java.awt.Color;

/**
 *
 * @author seanjhardy
 */
public class ParticleType {
    public double[][] forces;
    public double maxRange;
    public Color colour;
    
    public ParticleType(int numParticles){
        forces = new double[numParticles][4];
    }
    
    public void addForce(int ID, double min, double max, double strength, int numForces){
        double[] f = {min,max,strength, numForces};
        forces[ID] = f;
    }
    
    public void updateForces(int numParticles, int removedID){
        if(numParticles > forces.length){
            double[][] newForces = new double[numParticles][4]; 
            for(int i = 0; i < forces.length; i++){
                newForces[i] = forces[i];
            }
            this.forces = newForces;
        }else{
            double[][] newForces = new double[numParticles][4]; 
            for(int i = 0; i < forces.length; i++){
                if(i != removedID){
                    if(i > removedID){
                        newForces[i-1] = forces[i];
                    }else{
                        newForces[i] = forces[i];
                    }
                }
            }
            this.forces = newForces;
        }
    }
    public void setColour(Color colour){
        this.colour = colour;
    }
}
