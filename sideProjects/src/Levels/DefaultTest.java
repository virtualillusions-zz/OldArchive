package Levels;

import javax.swing.ImageIcon;

import util.gameSingleton;

import jmetest.terrain.TestTerrain;

import com.jme.image.Texture;
import com.jme.light.DirectionalLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.pass.RenderPass;
import com.jme.scene.Spatial;
import com.jme.scene.state.CullState;
import com.jme.scene.state.FogState;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import com.jmex.terrain.TerrainPage;
import com.jmex.terrain.util.HillHeightMap;
import com.jmex.terrain.util.ProceduralTextureGenerator;

import TestTerrain.sceneNode;

public class DefaultTest extends sceneNode{

	public DefaultTest() {	super("DefaultTest");fs.setEnabled(false);}
	TerrainPage terrain;
	public void setupTerrain(){
		// Set cam above terrain
		gameSingleton.get().getCamNode.setLocalTranslation(new Vector3f(0, 250, -20));
		gameSingleton.get().getCamNode.updateWorldData(0);


		// Set basic render states
		CullState cs = gameSingleton.get().getRenderer.createCullState();
		cs.setCullFace(CullState.Face.Back);
		cs.setEnabled(true);
		this.setRenderState(cs);

		// Some light
		DirectionalLight dl = new DirectionalLight();
		dl.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
		dl.setDirection(new Vector3f(1, -0.5f, 1));
		dl.setEnabled(true);
		gameSingleton.get().getLightState.attach(dl);

		// The terrain
		HillHeightMap heightMap = new HillHeightMap(129, 2000, 5.0f, 20.0f,
				(byte) 2);
		heightMap.setHeightScale(0.001f);
		Vector3f terrainScale = new Vector3f(10, 1, 10);
		terrain = new TerrainPage("Terrain", 33, heightMap
				.getSize(), terrainScale, heightMap.getHeightMap());
		terrain.setDetailTexture(1, 16);
		this.attachChild(terrain);

		// Some textures
		ProceduralTextureGenerator pt = new ProceduralTextureGenerator(
		        heightMap);
		    pt.addTexture(new ImageIcon(TestTerrain.class.getClassLoader()
		                                .getResource("jmetest/data/texture/grassb.png")),
		                  -128, 0, 128);
		    pt.addTexture(new ImageIcon(TestTerrain.class.getClassLoader()
		                                .getResource("jmetest/data/texture/dirt.jpg")),
		                  0, 128, 255);
		    pt.addTexture(new ImageIcon(TestTerrain.class.getClassLoader()
		                                .getResource("jmetest/data/texture/highest.jpg")),
		                  128, 255,
		                  384);

		    pt.createTexture(256);

		TextureState ts = gameSingleton.get().getRenderer.createTextureState();
		ts.setEnabled(true);
		Texture t1 = TextureManager.loadTexture(pt.getImageIcon().getImage(),
				Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear, true);
		ts.setTexture(t1, 0);

		Texture t2 = TextureManager.loadTexture(TestTerrain.class
				.getClassLoader().getResource("jmetest/data/texture/Detail.jpg"),
				Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
		ts.setTexture(t2, 1);
		t2.setWrap(Texture.WrapMode.Repeat);

		t1.setApply(Texture.ApplyMode.Combine);
		t1.setCombineFuncRGB(Texture.CombinerFunctionRGB.Modulate);
		t1.setCombineSrc0RGB(Texture.CombinerSource.CurrentTexture);
		t1.setCombineOp0RGB(Texture.CombinerOperandRGB.SourceColor);
		t1.setCombineSrc1RGB(Texture.CombinerSource.PrimaryColor);
		t1.setCombineOp1RGB(Texture.CombinerOperandRGB.SourceColor);

		t2.setApply(Texture.ApplyMode.Combine);
		t2.setCombineFuncRGB(Texture.CombinerFunctionRGB.AddSigned);
		t2.setCombineSrc0RGB(Texture.CombinerSource.CurrentTexture);
		t2.setCombineOp0RGB(Texture.CombinerOperandRGB.SourceColor);
		t2.setCombineSrc1RGB(Texture.CombinerSource.Previous);
		t2.setCombineOp1RGB(Texture.CombinerOperandRGB.SourceColor);
		this.setRenderState(ts);		
	}

	@Override
	public Vector3f spawnpoints() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector3f startpoint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TerrainPage getTerrain() {return terrain;}

}
