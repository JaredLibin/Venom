/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package venom;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

/**
 *
 * @author Jared
 */
public class Score {
    
   public void draw(Graphics graphics){
       graphics.setColor(Color.GREEN);   
       graphics.setFont(font);
       graphics.drawString("Score: " + value, position.x, position.y);
        
   }
    
    private int value = 0;
    private Point position;
    private Font font = new Font("Chiller", Font.ITALIC, 44);

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(int value) {
        this.value = value;
    }
    
    public void addToValue( int amount ){
        this.value += amount;
    }

    /**
     * @return the position
     */
    public Point getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Point position) {
        this.position = position;
    }
}
