/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vza.director;

/**
 * This Director handles all collision within this game world
 * @author Kyle Williams
 */
public final class PhysicsDirector extends com.jme3.bullet.BulletAppState{
    public PhysicsDirector(){
        setThreadingType(threadingType.PARALLEL);
    }
}