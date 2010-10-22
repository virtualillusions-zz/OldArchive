/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TestCollision;

//import com.jme3.font.Rectangle;
import java.awt.Rectangle;
//import com.jme3.math.Rectangle;


/**
 *
 * @author Kyle Williams
 */
public class testLowLevelDetection {
    public static void main(String[] args){
        Rectangle collider1 = new Rectangle(1,1,1,1);
        Rectangle collider2 = new Rectangle(2,2,1,1);
        boolean isColliding=collider1.intersects(collider2);
        System.out.println("are the rectangles colliding: "+isColliding);

        Rectangle collider3 = new Rectangle(1,1,1,1);
        Rectangle collider4 = new Rectangle(1,1,1,1);
        isColliding=collider3.intersects(collider4);
        System.out.println("are the rectangles colliding: "+isColliding);

    }
}
