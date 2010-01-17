package Fractals;

import com.jme.app.SimpleGame;
import com.jme.app.AbstractGame.ConfigShowMode;
import com.jme.math.Vector3f;
import com.jme.scene.TriMesh;
import com.jme.util.export.InputCapsule;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.OutputCapsule;
import com.jme.util.export.Savable;
import com.jme.util.geom.BufferUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jmetest.TutorialGuide.HelloAnimation;
 
/**
 *
 * @author Pirx
 */
public class Menger extends TriMesh implements Savable {
 
    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;
    private static final int FRONT = 4;
    private static final int BACK = 5;
    private static final Vector3f[] NORMALS = new Vector3f[] {
        Vector3f.UNIT_X.negate(),
        Vector3f.UNIT_X,
        Vector3f.UNIT_Y,
        Vector3f.UNIT_Y.negate(),
        Vector3f.UNIT_Z,
        Vector3f.UNIT_Z.negate()
    };
    private List<Vector3f> vertexList = new ArrayList<Vector3f>(5000);
    private List<Integer> normalList = new ArrayList<Integer>(5000);
    private List<Float> texList = new ArrayList<Float>(5000);
    private Map<int[],Integer> vertexMap = new HashMap<int[],Integer>(5000);
    private int length = 0; 
       
    private static int[][] PATTERN = new int[][]{
        {0,0,0}, {1,0,0}, {2,0,0},  
        {0,1,0}, /*****/  {2,1,0},  
        {0,2,0}, {1,2,0}, {2,2,0},  
        
        {0,0,1}, /*****/  {2,0,1},  
        /*****/  /*****/  /*****/         
        {0,2,1}, /*****/  {2,2,1},  
        
        {0,0,2}, {1,0,2}, {2,0,2},  
        {0,1,2}, /*****/  {2,1,2},  
        {0,2,2}, {1,2,2}, {2,2,2},  
    };
    
    private int depth = 3;
    private float size = 1.0f;
    
    public Menger(String name, int depth, float size) {
        super(name);
        this.depth = depth;
        this.size = size;
        allocateVertices();
    }
 
    public Menger(String name, int depth) {
        super(name);
        this.depth = depth;
        allocateVertices();
    }
    
    public Menger(String name) {
        super(name);
        allocateVertices();
    }    
    
    private void allocateVertices() {
       length = 1;
       for (int i = 1; i < depth; i++) {
           length *= 3;
       }
       boolean menger[][][] = new boolean[length][length][length];
       mengerize(menger, 0, 0, 0, length);
       vertexList.clear();
       vertexMap.clear();
       normalList.clear();
       List<Integer> triangles = new ArrayList<Integer>();
        for (int x = 0; x < length; x++) {
            for (int y = 0; y < length; y++) {
                for (int z = 0; z < length; z++) {
                    if (menger[x][y][z]) {
                        if (x + 1 == length || ! menger[x+1][y][z]) {
                            int k1 = getIndex(x+1, y, z, RIGHT);
                            int k2 = getIndex(x+1, y + 1, z, RIGHT);
                            int k3 = getIndex(x+1, y, z+1, RIGHT);
                            int k4 = getIndex(x+1, y + 1, z+1, RIGHT);
                            triangles.add(k1);  triangles.add(k2);  triangles.add(k3);
                            triangles.add(k2);  triangles.add(k4);  triangles.add(k3);
                        }
                        if (x == 0 || ! menger[x-1][y][z]) {
                            int k1 = getIndex(x, y, z, LEFT);
                            int k2 = getIndex(x, y + 1, z, LEFT);
                            int k3 = getIndex(x, y, z+1, LEFT);
                            int k4 = getIndex(x, y + 1, z+1, LEFT);
                            triangles.add(k1);  triangles.add(k3);  triangles.add(k2);
                            triangles.add(k2);  triangles.add(k3);  triangles.add(k4);
                        }
                        if (y + 1 == length || ! menger[x][y+1][z]) {
                            int k1 = getIndex(x, y + 1, z, UP);
                            int k2 = getIndex(x+1, y + 1, z, UP);
                            int k3 = getIndex(x, y + 1, z+1, UP);
                            int k4 = getIndex(x+1, y + 1, z+1, UP);
                            triangles.add(k1);  triangles.add(k3);  triangles.add(k2);
                            triangles.add(k2);  triangles.add(k3);  triangles.add(k4);
                        }
                        if (y == 0 || ! menger[x][y-1][z]) {
                            int k1 = getIndex(x, y, z, DOWN);
                            int k2 = getIndex(x+1, y, z, DOWN);
                            int k3 = getIndex(x, y, z+1, DOWN);
                            int k4 = getIndex(x+1, y, z+1, DOWN);
                            triangles.add(k1);  triangles.add(k2);  triangles.add(k3);
                            triangles.add(k2);  triangles.add(k4);  triangles.add(k3);
                        }
                        if (z + 1 == length || ! menger[x][y][z+1]) {
                            int k1 = getIndex(x, y, z+1, FRONT);
                            int k2 = getIndex(x+1, y, z+1, FRONT);
                            int k3 = getIndex(x, y + 1, z+1, FRONT);
                            int k4 = getIndex(x+1, y + 1, z+1, FRONT);
                            triangles.add(k1);  triangles.add(k2);  triangles.add(k3);
                            triangles.add(k2);  triangles.add(k4);  triangles.add(k3);
                        }
                        if (z == 0 || ! menger[x][y][z-1]) {
                            int k1 = getIndex(x, y, z, BACK);
                            int k2 = getIndex(x+1, y, z, BACK);
                            int k3 = getIndex(x, y + 1, z, BACK);
                            int k4 = getIndex(x+1, y + 1, z, BACK);
                            triangles.add(k1);  triangles.add(k3);  triangles.add(k2);
                            triangles.add(k2);  triangles.add(k3);  triangles.add(k4);
                        }
                    }
                }
            }
        }
        menger = null;
        vertexMap.clear();
        TriMesh  batch = this;
        batch.setVertexCount(vertexList.size());
        batch.setVertexBuffer(BufferUtils.createVector3Buffer(batch.getVertexBuffer(), batch.getVertexCount()));
        batch.setNormalBuffer(BufferUtils.createVector3Buffer(batch.getNormalBuffer(), batch.getVertexCount()));
      //  batch.getTextureCoords().set(0, BufferUtils.createVector2Buffer(batch.getVertexCount()));
        batch.setTriangleQuantity(triangles.size() / 3);
        batch.setIndexBuffer(BufferUtils.createIntBuffer(batch.getIndexBuffer(), 3 * batch.getTriangleCount()));
        for (Vector3f vector : vertexList) {
           batch.getVertexBuffer().put(vector.x).put(vector.y).put(vector.z);
        }        
        for (Integer normal : normalList) {
           batch.getNormalBuffer().put(NORMALS[normal].x).put(NORMALS[normal].y).put(NORMALS[normal].z);
        }        
        for (Integer index : triangles) {
           batch.getIndexBuffer().put(index);
        }
        for (Float tex : texList) {
      //     batch.getTextureCoords(0).put(tex);
        }
       vertexList.clear();
       normalList.clear();
    }
    
    public int getIndex(int... k) {
        if (vertexMap.containsKey(k)) {
            return vertexMap.get(k);
        }
        int index = vertexList.size();
        float unit = 2f*size/length;
        Vector3f vec = new Vector3f(k[0]*unit-size, k[1]*unit-size, k[2]*unit-size); 
        vertexList.add(vec);
        normalList.add(k[3]);
        vertexMap.put(k, index);
        switch (k[3]) {
           case LEFT: texList.add(k[2]/(length+1f)); texList.add(k[1]/(length+1f));  break;
           case RIGHT: texList.add(1 - k[2]/(length+1f)); texList.add(k[1]/(length+1f));  break;
           case BACK: texList.add(1 - k[0]/(length+1f)); texList.add(k[1]/(length+1f)); break;
           case FRONT: texList.add(k[0]/(length+1f)); texList.add(k[1]/(length+1f)); break;
           case UP: texList.add(k[0]/(length+1f)); texList.add(1 - k[2]/(length+1f)); break;
           case DOWN: texList.add(k[0]/(length+1f)); texList.add(k[2]/(length+1f)); break;
        }
        return index;
    }
    
    private static void mengerize(boolean[][][] menger, int x, int y, int z, int length) {
        if (length == 1) {
            menger[x][y][z] = true;
            return;
        }
        int newLength = length / 3;
        for (int i = 0; i < PATTERN.length; i++) {
            mengerize(menger, x + newLength * PATTERN[i][0], 
                              y + newLength * PATTERN[i][1],
                              z + newLength * PATTERN[i][2], newLength);
        }
    }
    
    @Override
    public void write(JMEExporter e) throws IOException {
        super.write(e);
        OutputCapsule capsule = e.getCapsule(this);
        capsule.write(getDepth(), "depth", 0);
        capsule.write(getSize(), "size", 0);
    }
 
    @Override
    public void read(JMEImporter e) throws IOException {
        super.read(e);
        InputCapsule capsule = e.getCapsule(this);
        setDepth(capsule.readInt("depth", 0));
        setSize(capsule.readFloat("size", 0));
    }
 
    public int getDepth() {
        return depth;
    }
 
    public void setDepth(int depth) {
        this.depth = depth <= 0 ? 1 : depth;
        allocateVertices();
    }
 
    public float getSize() {
        return size;
    }
 
    public void setSize(float size) {
        this.size = size;
        allocateVertices();
    }
}