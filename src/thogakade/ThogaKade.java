/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thogakade;

import thogakade.db.DataBaseUpdater;

/**
 *
 * @author cmjd
 */
public class ThogaKade {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DataBaseUpdater  updater =new DataBaseUpdater();
        updater.runDbUpdate();
    }
}
