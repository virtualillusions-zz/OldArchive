/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 *
 * @author Kyle Williams
 */
public class test {
     private enum BUTTONS{BLANK(0),Attack1(1),Attack2(1),
                            Attack3(1),Attack4(1),Up(0),Down(0),Left(0),
                                Right(0);
                  private int priority;
                  private BUTTONS(int priority){
                      this.priority = priority;
                  }
                  public int getPriority(){return priority;}
     }

    public static void main(String[] test){
        ArrayList<BUTTONS> r = new ArrayList<BUTTONS>();
        r.add(BUTTONS.Attack4);
        r.add(BUTTONS.Attack1);
        r.add(BUTTONS.Left);
        r.add(BUTTONS.BLANK);
        r.add(BUTTONS.Attack3);
        System.out.println(BUTTONS.Up.getPriority());
    }
}