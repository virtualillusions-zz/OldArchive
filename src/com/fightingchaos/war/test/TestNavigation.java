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

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.fightingchaos.war.controller.Entity;
import com.fightingchaos.war.navigation.Cell;
import com.fightingchaos.war.navigation.Mesh;
import com.fightingchaos.war.navigation.Plane;
import com.jme.app.SimpleGame;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Line;
import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;
import com.jme.scene.Spatial.NormalsMode;
import com.jme.scene.shape.Cone;
import com.jme.scene.state.CullState;
import com.jme.scene.state.FogState;
import com.jme.scene.state.WireframeState;
import com.jme.util.geom.BufferUtils;
import com.jme.util.geom.Debugger;

/**
 * 
 */
public class TestNavigation extends SimpleGame {

	/**
	 * Entry point for the test,
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(System.getProperty("java.library.path"));
		TestNavigation app = new TestNavigation();
		app.setConfigShowMode(ConfigShowMode.ShowIfNoConfig);
		app.start();
	}

	private float count = 0.0f;
	private Mesh NaviMesh = new Mesh();
	private TriMesh vMesh;
	private Cone target;
	private Cone pen;
	private Entity entity;
	private Entity tEnt;


	/**
	 * builds the trimesh.
	 * 
	 * @see com.jme.app.SimpleGame#initGame()
	 */
	protected void simpleInitGame() {
		display.setTitle("Navigation Test");
		cam.setLocation(new Vector3f(120, 205, -121));
		cam.lookAt(new Vector3f(-58,29,61), Vector3f.UNIT_Y);
		cam.update();
		
		FogState fs = display.getRenderer().createFogState();
		fs.setEnabled(false);
		rootNode.setRenderState(fs);		

		CullState cs = display.getRenderer().createCullState();
		cs.setCullFace(CullState.Face.Back);
		cs.setEnabled(true);

		lightState.setTwoSidedLighting(true);
		Debugger.AUTO_NORMAL_RATIO = .02f;

		((PointLight) lightState.get(0))
				.setLocation(new Vector3f(100, 500, 50));

		rootNode.setRenderState(cs);

		rootNode.setRenderQueueMode(Renderer.QUEUE_OPAQUE);
		
		init();
		
		input = new TestNavigationHandler(cam, entity, tEnt, 40.0f,1.0f);

	}
	
	private void init(){
		
//		long map_totalpoints = 20;
//		long map_totalpolys = 8;
//		 
//		Vector3f map_points[] = 
//		{
//		 new Vector3f(-20, 10, -40),
//		 new Vector3f(20, 10, -40),
//		 new Vector3f(-20, 10, -20),
//		 new Vector3f(20, 10, -20),
//		 new Vector3f(-20, 10, 0),
//		 new Vector3f(20, 10, 0),
//		 new Vector3f(-20, 10, 20),
//		 new Vector3f(20, 10, 20),
//		 new Vector3f(-20, 10, 40),
//		 new Vector3f(20, 10, 40),
//		 new Vector3f(-20, 10, 60),
//		 new Vector3f(20, 10, 60),
//		 new Vector3f(-20, 10, 80),
//		 new Vector3f(20, 10, 80),
//		 new Vector3f(-20, 10, 100),
//		 new Vector3f(20, 10, 100),
//		 new Vector3f(-20, 10, 120),
//		 new Vector3f(20, 10, 120),
//		 new Vector3f(-20, 10, 140),
//		 new Vector3f(20, 10, 140),
//		 new Vector3f(-20, 10, 160),
//		 new Vector3f(20, 10, 160)
//		};
//		
//		short map_polys[][] = 
//		{
//		 { 1, 0, 2},
//		 { 1, 2, 3},
//		 { 3, 2, 4},
//		 { 3, 4, 5},
//		 { 5, 4, 6},
//		 { 5, 6, 7},
//		 { 7, 6, 8},
//		 { 7, 8, 9},
//		 { 9, 8, 10},
//		 { 9, 10, 11},
//		 { 11, 10, 12},
//		 { 11, 12, 13},
//		 { 13, 12, 14},
//		 { 13, 14, 15},
//		 { 15, 14, 16},
//		 { 15, 16, 17},
//		 { 17, 16, 18},
//		 { 17, 18, 19}
//		};
		 
		long map_totalpoints = 77;
		 
		Vector3f map_points[] = 
		{
		 new Vector3f(-41.4328f, 40.0000f, 24.0671f),
		 new Vector3f(-23.1315f, 40.0000f, 42.3684f),
		 new Vector3f(-23.1315f, 40.0000f, 92.3684f),
		 new Vector3f(-16.4328f, 40.0000f, 67.3684f),
		 new Vector3f(-66.4328f, 40.0000f, 17.3684f),
		 new Vector3f(-91.4328f, 40.0000f, 24.0671f),
		 new Vector3f(-109.7341f, 40.0000f, 42.3684f),
		 new Vector3f(-116.4328f, 40.0000f, 67.3684f),
		 new Vector3f(-109.7341f, 40.0000f, 92.3684f),
		 new Vector3f(-91.4328f, 40.0000f, 110.6697f),
		 new Vector3f(-66.4328f, 40.0000f, 117.3684f),
		 new Vector3f(-41.4328f, 40.0000f, 110.6697f),
		 new Vector3f(-40.4520f, 40.0000f, 82.3684f),
		 new Vector3f(-51.4328f, 40.0000f, 93.3492f),
		 new Vector3f(-66.4328f, 40.0000f, 97.3684f),
		 new Vector3f(-81.4328f, 40.0000f, 93.3492f),
		 new Vector3f(-92.4136f, 40.0000f, 82.3684f),
		 new Vector3f(-96.4328f, 40.0000f, 67.3684f),
		 new Vector3f(-92.4136f, 40.0000f, 52.3684f),
		 new Vector3f(-81.4328f, 40.0000f, 41.3876f),
		 new Vector3f(-66.4328f, 40.0000f, 37.3684f),
		 new Vector3f(-51.4328f, 40.0000f, 41.3876f),
		 new Vector3f(-40.4520f, 40.0000f, 52.3684f),
		 new Vector3f(-36.4328f, 40.0000f, 67.3684f),
		 new Vector3f(-56.5907f, 10.0000f, 67.1996f),
		 new Vector3f(-56.5907f, 10.0000f, 67.1996f),
		 new Vector3f(-81.4328f, 40.0000f, 93.3492f),
		 new Vector3f(-67.5715f, 10.0000f, 56.2188f),
		 new Vector3f(-67.5715f, 10.0000f, 56.2188f),
		 new Vector3f(-92.4136f, 40.0000f, 82.3684f),
		 new Vector3f(65.0024f, 5.7630f, -65.5516f),
		 new Vector3f(35.0030f, 5.7630f, -84.3647f),
		 new Vector3f(0.4275f, 5.7630f, -91.4832f),
		 new Vector3f(-33.1311f, 5.7630f, -83.8563f),
		 new Vector3f(-63.6390f, 5.7630f, -65.0431f),
		 new Vector3f(-83.9775f, 5.7630f, -36.0606f),
		 new Vector3f(-93.1299f, 5.7630f, -0.4682f),
		 new Vector3f(-88.0453f, 5.7630f, 20.3789f),
		 new Vector3f(-66.1813f, 5.7630f, 14.2773f),
		 new Vector3f(-52.4528f, 5.7630f, 18.3450f),
		 new Vector3f(-44.3174f, 5.7630f, 21.3958f),
		 new Vector3f(-60.0797f, 5.7630f, 41.7344f),
		 new Vector3f(-63.4004f, 5.7630f, 51.8567f),
		 new Vector3f(-52.4528f, 5.7630f, 62.7418f),
		 new Vector3f(-39.2327f, 5.7630f, 55.4629f),
		 new Vector3f(-19.4026f, 5.7630f, 43.2598f),
		 new Vector3f(-13.8095f, 5.7630f, 64.6152f),
		 new Vector3f(-19.4026f, 5.7630f, 89.0215f),
		 new Vector3f(0.4275f, 5.7630f, 92.0723f),
		 new Vector3f(36.0200f, 5.7630f, 84.4454f),
		 new Vector3f(65.5109f, 5.7630f, 66.6491f),
		 new Vector3f(85.8495f, 5.7630f, 34.6159f),
		 new Vector3f(93.4764f, 5.7630f, 0.5488f),
		 new Vector3f(86.3579f, 5.7630f, -35.0437f),
		 new Vector3f(86.3579f, 5.7630f, -35.0437f),
		 new Vector3f(63.9855f, 5.7630f, -21.8236f),
		 new Vector3f(41.1046f, 5.7630f, -18.7729f),
		 new Vector3f(63.4770f, 5.7630f, 3.0911f),
		 new Vector3f(49.2400f, 5.7630f, 25.9720f),
		 new Vector3f(27.3761f, 5.7630f, 36.1413f),
		 new Vector3f(0.4275f, 5.7630f, 35.6328f),
		 new Vector3f(-21.9449f, 5.7630f, 22.4127f),
		 new Vector3f(-33.6396f, 5.7630f, 3.0911f),
		 new Vector3f(-36.6904f, 5.7630f, -24.3660f),
		 new Vector3f(-23.4703f, 5.7630f, -45.2130f),
		 new Vector3f(0.4275f, 5.7630f, -59.9585f),
		 new Vector3f(26.3592f, 5.7630f, -59.9585f),
		 new Vector3f(19.2407f, 5.7630f, -37.5860f),
		 new Vector3f(6.5291f, 5.7630f, -38.0945f),
		 new Vector3f(-5.1656f, 5.7630f, -30.4675f),
		 new Vector3f(-11.7756f, 5.7630f, -18.2644f),
		 new Vector3f(-11.2672f, 5.7630f, -5.0443f),
		 new Vector3f(-4.1487f, 5.7630f, 7.6673f),
		 new Vector3f(6.0206f, 5.7630f, 14.2773f),
		 new Vector3f(20.2576f, 5.7630f, 14.7858f),
		 new Vector3f(32.4607f, 5.7630f, 7.6673f),
		 new Vector3f(39.0708f, 5.7630f, -3.0105f),
		};
		 
		 
		 
		short map_polys[][] = 
		{
		 { 67, 68, 69},
		 { 67, 69, 70},
		 { 67, 70, 71},
		 { 67, 71, 72},
		 { 67, 72, 73},
		 { 67, 73, 74},
		 { 67, 74, 75},
		 { 67, 75, 76},
		 { 67, 76, 56},
		 { 60, 61, 45},
		 { 60, 45, 46},
		 { 36, 37, 38},
		 { 35, 36, 38},
		 { 46, 47, 48},
		 { 46, 48, 49},
		 { 60, 46, 49},
		 { 42, 43, 44},
		 { 60, 49, 50},
		 { 41, 42, 44},
		 { 40, 41, 44},
		 { 40, 44, 45},
		 { 40, 45, 61},
		 { 40, 61, 62},
		 { 39, 40, 62},
		 { 39, 62, 63},
		 { 34, 35, 38},
		 { 38, 39, 63},
		 { 34, 38, 63},
		 { 33, 34, 63},
		 { 32, 33, 63},
		 { 66, 67, 56},
		 { 66, 56, 55},
		 { 59, 60, 50},
		 { 59, 50, 51},
		 { 58, 59, 51},
		 { 57, 58, 51},
		 { 57, 51, 52},
		 { 55, 57, 52},
		 { 55, 52, 53},
		 { 66, 55, 53},
		 { 54, 30, 31},
		 { 54, 31, 32},
		 { 32, 63, 64},
		 { 32, 64, 65},
		 { 32, 65, 66},
		 { 66, 53, 54},
		 { 54, 32, 66},
		 { 28, 24, 43},
		 { 28, 43, 42},
		 { 11, 2, 12},
		 { 12, 2, 3},
		 { 12, 3, 1},
		 { 10, 11, 12},
		 { 1, 0, 4},
		 { 1, 4, 5},
		 { 6, 7, 8},
		 { 6, 8, 9},
		 { 10, 12, 13},
		 { 9, 10, 13},
		 { 23, 12, 1},
		 { 22, 23, 1},
		 { 21, 22, 1},
		 { 21, 1, 5},
		 { 20, 21, 5},
		 { 20, 5, 6},
		 { 19, 20, 6},
		 { 18, 19, 6},
		 { 17, 18, 6},
		 { 17, 6, 9},
		 { 16, 17, 9},
		 { 15, 16, 9},
		 { 9, 13, 14},
		 { 9, 14, 15},
		 { 25, 27, 29},
		 { 25, 29, 26},
		};
		 
		 
		long map_totalpolys = 75;
		
		vMesh = new TriMesh("map");
		FloatBuffer verts = BufferUtils.createFloatBuffer((int) map_totalpolys*9);
		IntBuffer ind = BufferUtils.createIntBuffer((int) map_totalpolys*3);
		int v = 0;
	    NaviMesh.Clear();

	    Plane up = new Plane();
	    up.setPlanePoints( Vector3f.UNIT_X, Vector3f.ZERO, Vector3f.UNIT_Z);
	    up.getNormal();
		for (int i=0;i<map_totalpolys;++i)
		{
			 Vector3f vertA = map_points[map_polys[i][0]];
			 Vector3f vertB = map_points[map_polys[i][1]];
			 Vector3f vertC = map_points[map_polys[i][2]];
			 
			Plane p = new Plane();
			p.setPlanePoints(vertA, vertB, vertC);
			com.jme.math.Plane p2 = new com.jme.math.Plane();
			p2.setPlanePoints(vertA, vertB, vertC);
			if(up.pseudoDistance(p.getNormal())<= 0.0f){
System.out.println("Warning, normal of the plane faces downward!!! ");
			continue;
			}
			// some art programs can create linear polygons which have two or more
			// identical vertices. This creates a poly with no surface area,
			// which will wreak havok on our navigation mesh algorythms.
			// We only except polygons with unique vertices.
			if (!vertA.equals(vertB) && !vertB.equals(vertC) && !vertC.equals(vertA))
			{
				verts.put(vertA.x);
				verts.put(vertA.y);
				verts.put(vertA.z);
				verts.put(vertB.x);
				verts.put(vertB.y);
				verts.put(vertB.z);
				verts.put(vertC.x);
				verts.put(vertC.y);
				verts.put(vertC.z);
				for(int x =0;x<3;x++,v++)
					ind.put(v);
				
				NaviMesh.AddCell(vertA, vertB, vertC);
			}else
				System.out.println("face with wrong winding:"+vertA+" "+vertB+" "+vertC);
		}
		vMesh.reconstruct(verts, null, null, null,ind);
		vMesh.setVertexCount((int)map_totalpoints);
		vMesh.setTriangleQuantity((int)map_totalpolys);
		vMesh.setLightCombineMode(Spatial.LightCombineMode.Off);
		vMesh.setTextureCombineMode(Spatial.TextureCombineMode.Off);		
		vMesh.setDefaultColor(ColorRGBA.gray);

		rootNode.attachChild(vMesh);
		NaviMesh.LinkCells();
		
		target = new Cone("target",24,12,5,8,true);
		target.setLocalTranslation(new Vector3f(-2,4,-2));
//		target.setLocalRotation(new Quaternion(-0.707f,0,0,0.707f));
		target.setLightCombineMode(Spatial.LightCombineMode.Off);
        target.setTextureCombineMode(Spatial.TextureCombineMode.Off);		
		target.setDefaultColor(ColorRGBA.blue);
//		rootNode.attachChild(target);
		tEnt = new Entity(NaviMesh, target.getLocalTranslation(),NaviMesh.FindClosestCell(target.getLocalTranslation()));
		tEnt.setGfxNode(target);
		rootNode.attachChild(tEnt);
		
		pen = new Cone("pen",24,12,5,8,true);

//        pen.setLocalRotation(new Quaternion(-0.707f,0,0,0.707f));
		pen.setLocalTranslation(new Vector3f(2,4,2));
		pen.setDefaultColor(ColorRGBA.red);
		pen.setLightCombineMode(Spatial.LightCombineMode.Off);
        pen.setTextureCombineMode(Spatial.TextureCombineMode.Off);
		rootNode.attachChild(pen);
		
		entity = new Entity(NaviMesh,pen.getLocalTranslation(),null);
		entity.setGfxNode(pen);
		rootNode.attachChild(entity);
		
		WireframeState ws = display.getRenderer().createWireframeState();
		ws.setEnabled(true);
		vMesh.setRenderState(ws);
		
//		attachDebugNormals();

	}
	
	public void simpleUpdate() {
		if (timer.getTimeInSeconds() > count) {
			count = timer.getTimeInSeconds()+5.0f;
System.out.println("--Timer-- pen at:"+pen.getLocalTranslation()+" tgt at:"+target.getLocalTranslation());

		}
	}
	
	private void attachDebugNormals(){
		Vector3f[] points = new Vector3f[NaviMesh.TotalCells()*2];
		for(int i = 0, j=0; j < NaviMesh.TotalCells()*2; i++,j+=2){
			Cell c = NaviMesh.Cell(i);
			points[j] = c.CenterPoint();
			points[j+1] = c.CenterPoint().add(c.getNormal().mult(20));
		}
		Line lines = new Line("Cell Normals", points,null,null,null);
		lines.setDefaultColor(ColorRGBA.white);
		lines.setLightCombineMode(Spatial.LightCombineMode.Off);
		lines.setTextureCombineMode(Spatial.TextureCombineMode.Off);
		rootNode.attachChild(lines);
	}
}
