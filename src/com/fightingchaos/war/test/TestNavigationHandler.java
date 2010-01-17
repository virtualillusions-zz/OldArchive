/*
 * Copyright (c) 2003-2008 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.fightingchaos.war.test;

import com.fightingchaos.war.controller.Entity;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.KeyboardLookHandler;
import com.jme.input.MouseLookHandler;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.KeyBackwardAction;
import com.jme.input.action.KeyForwardAction;
import com.jme.input.action.KeyInputAction;
import com.jme.renderer.Camera;

/**
 * InputHandler for the Navigation mesh example WSAD move a pen around, 
 * arrows and mouse move the camera around
 */
public class TestNavigationHandler extends InputHandler {
    private MouseLookHandler mouseLookHandler;
    private KeyboardLookHandler keyboardLookHandler;
    
    private class NaviKeyboardLookHandler extends KeyboardLookHandler {
		    private KeyInputAction forward;
		    private KeyInputAction backward;
		    private KeyInputAction sLeft;
		    private KeyInputAction sRight;
		    private KeyInputAction space;
		    private KeyInputAction inc;
		    private KeyInputAction dec;
			private Entity target;
			private Entity entity;

		    
		    public NaviKeyboardLookHandler( Camera cam, Entity pen, Entity tgt, float moveSpeed, float rotateSpeed ) {
		        super(cam,moveSpeed, rotateSpeed);
		        KeyBindingManager keyboard = KeyBindingManager.getKeyBindingManager();

		        removeAction("forward");
		        removeAction("backward");
		        removeAction("strafeRight");
		        removeAction("strafeLeft");
		        removeAction("space");
		        removeAction( "lookUp");
		        removeAction( "lookDown");
		        keyboard.set( "forward", KeyInput.KEY_W );
		        keyboard.set( "backward", KeyInput.KEY_S );
		        keyboard.set( "strafeLeft", KeyInput.KEY_A );
		        keyboard.set( "strafeRight", KeyInput.KEY_D );
		        keyboard.set( "space", KeyInput.KEY_SPACE );
		        keyboard.set( "inc", KeyInput.KEY_ADD );
		        keyboard.set( "dec", KeyInput.KEY_MINUS );
		        keyboard.set("f", KeyInput.KEY_UP);
		        keyboard.set("b", KeyInput.KEY_DOWN);
				entity = pen;
				target = tgt;
		        addAction( new KeyForwardAction( cam, moveSpeed ), "f", true );
		        addAction( new KeyBackwardAction( cam, moveSpeed ), "b", true );
		        forward =  new KeyInputAction() {
					public void performAction(InputActionEvent evt) {
						target.SetMovementZ(2.0f);
					}
		        };
		        addAction( forward, "forward", true );
		        backward = new KeyInputAction() {
					public void performAction(InputActionEvent evt) {
						target.SetMovementZ(-2.0f);
					}
		        };
		        addAction( backward, "backward", true );
		        sLeft = new KeyInputAction() {
					public void performAction(InputActionEvent evt) {
						target.SetMovementX(2.0f);
					}
		        };
		        addAction( sLeft, "strafeLeft", true );
		        sRight = new KeyInputAction() {
					public void performAction(InputActionEvent evt) {
						target.SetMovementX(-2.0f);
					}
		        };
		        addAction( sRight, "strafeRight", true );
		        space = new KeyInputAction() {
					public void performAction(InputActionEvent evt) {
						// FIXME start movement of pen towards target here, disable WSAD until goal reached	
						System.out.println("-- SPACE pressed, start walking --");
						entity.GotoLocation(target.Position());
					}
		        };
		        addAction(space, "space", false);
		       
		        inc = new KeyInputAction() {
					public void performAction(InputActionEvent evt) {
						entity.SetMaxSpeed(entity.getMaxSpeed()+1.0f);
						target.SetMaxSpeed(target.getMaxSpeed()+1.0f);
					}
		        };
		        addAction(inc, "inc", false);
		        dec = new KeyInputAction() {
					public void performAction(InputActionEvent evt) {
						entity.SetMaxSpeed(entity.getMaxSpeed()-1.0f);
						target.SetMaxSpeed(target.getMaxSpeed()-1.0f);
					}
		        };
		        addAction(dec, "dec", false);
		       
		    }
    }

    /**
     * @return handler for keyboard controls
     */
    public KeyboardLookHandler getKeyboardLookHandler() {
        return keyboardLookHandler;
    }

    /**
     * @return handler for mouse controls
     */
    public MouseLookHandler getMouseLookHandler() {
        return mouseLookHandler;
    }
    
    public void setButtonPressRequired(boolean value) {
        mouseLookHandler.requireButtonPress(value);
    }

    /**
     * Creates a first person handler.
     * @param cam The camera to move by this handler.
     */
    public TestNavigationHandler( Camera cam, Entity pen, Entity tgt ) {
        mouseLookHandler = new MouseLookHandler( cam, 1 );
        addToAttachedHandlers( mouseLookHandler );
        keyboardLookHandler = new NaviKeyboardLookHandler( cam, pen, tgt, 0.5f, 0.01f );
        addToAttachedHandlers( keyboardLookHandler );
    }

    /**
     * Creates a first person handler.
     * @param cam The camera to move by this handler.
     * @param moveSpeed action speed for move actions
     * @param turnSpeed action speed for rotating actions
     * @param pen 
     */
    public TestNavigationHandler(Camera cam, Entity pen, Entity tgt, float moveSpeed, float turnSpeed ) {
        mouseLookHandler = new MouseLookHandler( cam, turnSpeed );
        addToAttachedHandlers( mouseLookHandler );
        keyboardLookHandler = new NaviKeyboardLookHandler( cam, pen, tgt, moveSpeed, turnSpeed );
        addToAttachedHandlers( keyboardLookHandler );
    }
}
