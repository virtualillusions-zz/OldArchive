/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.misc;

import com.jme3.system.NanoTimer;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Kyle Williams
 */
public class quickTest1 {
    private enum Letters{a,b,c,d;private float timer=0;public Letters setTime(float time){timer=time; return this;}
}
    public static void main(String[] bitMasking){
       System.out.println("::::::::::::TEST 1:::::::::::");
       test1();
       System.out.println("::::::::::::TEST 2:::::::::::");
       test2();
    }
    
    public static void test1(){
        Set<Letters> e = EnumSet.noneOf(Letters.class);
        e.add(Letters.a.setTime(50));
        e.add(Letters.d);
        for(Letters a:e)
            System.out.println(a.timer);
    }

    public static void test2(){
       Set h = new HashSet();
       Set t = new TreeSet();
       Set e = EnumSet.noneOf(Letters.class);
       Queue hq = new LinkedList();
       List aL = new ArrayList();
       Letters[] a = new Letters[4];
       NanoTimer timer = new NanoTimer();
       float start = timer.getTime();
       h.add(Letters.b);
       h.add(Letters.c);
       h.add(Letters.d);
       h.add(Letters.a);
       System.out.println("HashSet: "+h+" finished with a time of: "+(timer.getTime()-start));
       start = timer.getTime();
       t.add(Letters.b);
       t.add(Letters.c);
       t.add(Letters.d);
       t.add(Letters.a);
       System.out.println("TreeSet: "+t+" finished with a time of: "+(timer.getTime()-start));
       start = timer.getTime();
       e.add(Letters.b);
       e.add(Letters.c);
       e.add(Letters.d);
       e.add(Letters.a);
       System.out.println("EnumSet: "+e+" finished with a time of: "+(timer.getTime()-start));
       start = timer.getTime();
       hq.add(Letters.b);
       hq.add(Letters.c);
       hq.add(Letters.d);
       hq.add(Letters.a);
       System.out.println("HashQueue: "+hq+" finished with a time of: "+(timer.getTime()-start));
       start = timer.getTime();
       aL.add(Letters.b);
       aL.add(Letters.c);
       aL.add(Letters.d);
       aL.add(Letters.a);
       System.out.println("ArrayList: "+aL+" finished with a time of: "+(timer.getTime()-start));
       start = timer.getTime();
       a[0]=Letters.b;
       a[1]=Letters.c;
       a[2]=Letters.d;
       a[3]=Letters.a;
       System.out.println("Array finished with a time of: "+(timer.getTime()-start));
       start = timer.getTime();
    }
}
