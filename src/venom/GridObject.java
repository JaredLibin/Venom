/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package venom;

import java.awt.Point;

/**
 *
 * @author Jared
 */
public class GridObject {

    //<editor-fold defaultstate="collapsed" desc="constructors">
    public GridObject(GridObjectType type, Point location) {
        this.type = type;
        this.location = location;
    }
    
    public GridObject(GridObjectType type, int x, int y) {
        this.type = type;
        this.location = new Point(x,y);
//</editor-fold>
    }

//<editor-fold defaultstate="collapsed" desc="properties">
    private GridObjectType type;
    private Point location;

    /**
     * @return the type
     */
    public GridObjectType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(GridObjectType type) {
        this.type = type;
    }

    /**
     * @return the location
     */
    public Point getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(Point location) {
        this.location = location;
    }
//</editor-fold>

}
