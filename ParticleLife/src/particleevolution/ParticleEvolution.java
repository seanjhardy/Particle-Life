/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package particleevolution;

import javax.swing.JFrame;

/**
 *
 * @author seanjhardy
 */
public class ParticleEvolution {

    private JFrame frame;
    public static void main(String[] args){
        //creates a new, non static instance of the tourno class
        ParticleEvolution main = new ParticleEvolution();
    }
    public ParticleEvolution(){
        frame = new GUIManager();
    }
}
