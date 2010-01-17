package Processing;

import com.g3d.app.SimpleApplication;
import com.g3d.material.Material;
import com.g3d.math.Vector3f;
import com.g3d.scene.Geometry;
import com.g3d.scene.shape.Box;
import com.g3d.texture.Texture;

public class TestBoxAccuracy extends SimpleApplication {

    public static void main(String[] args){
    	TestBoxAccuracy app = new TestBoxAccuracy();
        app.start();
    }

    @Override
    public void simpleInitApp() {
    	flyCam.setMoveSpeed(speed+700);
        drawBox(8,0,89,9);
        drawBox(120,9,67,35);
        drawBox(16,28,81,24);
        drawBox(15,68,172,28);
        drawBox(11,110,148,49);
        drawBox(152,170,15,27);
        drawBox(8,173,42,7);
        drawBox(74,174,24,21);
    }
    
    private void drawBox(int x,int y, int width, int height){
    	Box b = new Box(new Vector3f(x + width / 2,0,y + height / 2),width/2,10,height/2);
    	
        Geometry geom = new Geometry("Box", b);
        geom.updateModelBound();

        Material mat = new Material(manager, "plain_texture.j3md");
        Texture tex = manager.loadTexture("Monkey.jpg", true, true, false, 16);
        tex.setMinFilter(Texture.MinFilter.Trilinear);
        mat.setTexture("m_ColorMap", tex);

       geom.setMaterial(mat);
        
        rootNode.attachChild(geom);
    }

}