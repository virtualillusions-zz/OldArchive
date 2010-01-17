package Fractals;

import com.jme.math.Vector3f;
import com.jme.scene.TriMesh;
import com.jme.util.export.InputCapsule;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.OutputCapsule;
import com.jme.util.export.Savable;
import com.jme.util.geom.BufferUtils;
import java.io.IOException;
import static com.jme.math.FastMath.*;
 
public class Sierpinski extends TriMesh implements Savable {
 
    private int depth = 3;
    private float size = 1.0f;
   
    //normals
    private static Vector3f DOWN = new Vector3f(0, -1f, 0);
    private static Vector3f LEFT = new Vector3f(-0.942809f, 0.3333333f, 0);
    private static Vector3f BACK = new Vector3f(0.4714045f, 0.3333333f, - 0.81649655f);
    private static Vector3f FRONT = new Vector3f(0.4714045f, 0.3333333f, 0.81649655f);
           
    public Sierpinski(String name, int depth, float size) {
        super(name);
        this.depth = depth;
        this.size = size;
        allocateVertices();
    }
 
    public Sierpinski(String name, int depth) {
        super(name);
        this.depth = depth;
        allocateVertices();
    }
    
    public Sierpinski(String name) {
        super(name);
        allocateVertices();
    }
    
    private void allocateVertices() {
        TriMesh batch = this;
        batch.setVertexCount(4*(4 + 2*((1 << (2*depth - 2)) - 1)));
        batch.setVertexBuffer(BufferUtils.createVector3Buffer(batch.getVertexBuffer(), batch.getVertexCount()));
        batch.setNormalBuffer(BufferUtils.createVector3Buffer(batch.getNormalBuffer(), batch.getVertexCount()));
       // batch.getTextureCoords().set(0, BufferUtils.createVector2Buffer(batch.getVertexCount()));
        batch.setTriangleQuantity(1 << (depth << 1));
        batch.setIndexBuffer(BufferUtils.createIntBuffer(batch.getIndexBuffer(), 3 * batch.getTriangleCount()));
 
        setData();
    }
    
    private void setData() {
        Vector3f[] points = new Vector3f[] {
            new Vector3f(0, size, 0),
            new Vector3f(0.942809f * size, -0.3333333f * size, 0),
            new Vector3f(-0.4714045f * size, -0.3333333f * size, 0.81649655f * size),
            new Vector3f(-0.4714045f * size, -0.3333333f * size, -0.81649655f * size),
        };
        TriMesh batch = this;
        for (int i = 0; i < 4; i++) {
           batch.getVertexBuffer().put(points[i].x).put(points[i].y).put(points[i].z);
           batch.getVertexBuffer().put(points[i].x).put(points[i].y).put(points[i].z);
           batch.getVertexBuffer().put(points[i].x).put(points[i].y).put(points[i].z);
           batch.getVertexBuffer().put(points[i].x).put(points[i].y).put(points[i].z);
           addNormals(FRONT, BACK, LEFT, DOWN);
        }
        float[] tex = new float[]{
            0.5f, 1, 0.5f, 1, 0.5f, 1, 0.5f, 0.5f,
            1, 0, 0, 0, 0.5f, 0.5f, 0.5f, 1,
            0, 0, 0.5f, 0.5f, 1, 0, 0, 0,
            0.5f, 0.5f, 1, 0, 0, 0, 1, 0
        };
       // batch.getTextureCoords(0).put(tex);
        recurse(points, new int[]{0, 4, 8, 12}, tex, 1, 16);
    }
    
    private void addNormals(Vector3f... normals) {
    	TriMesh batch = this;   
         for (Vector3f normal : normals) {
             batch.getNormalBuffer().put(normal.x).put(normal.y).put(normal.z);
         }
    }
    
    private int recurse(Vector3f[] points, int[] index, float[] tex, int level, int vertexCount) {
    	TriMesh batch = this;
        if (level == depth) {
            batch.getIndexBuffer().put(index[0]).put(index[2]).put(index[1]);
            batch.getIndexBuffer().put(index[0]+2).put(index[3]+2).put(index[2]+2);
            batch.getIndexBuffer().put(index[0]+1).put(index[1]+1).put(index[3]+1);
            batch.getIndexBuffer().put(index[1]+3).put(index[2]+3).put(index[3]+3);
            return vertexCount;
        } else {
            int v = vertexCount;
           Vector3f[] newPoints = new Vector3f[] {
               middle(points[0], points[1]),  middle(points[0], points[2]),
               middle(points[0], points[3]),  middle(points[1], points[2]),
               middle(points[1], points[3]),  middle(points[2], points[3])
           }; 
           float[] newTex = new float[48];
           setTex(newTex, tex, 0, 0, 1);
           setTex(newTex, tex, 1, 0, 2);
           setTex(newTex, tex, 2, 0, 3);
           setTex(newTex, tex, 3, 1, 2);
           setTex(newTex, tex, 4, 1, 3);
           setTex(newTex, tex, 5, 2, 3);
        //   batch.getTextureCoords(0).put(newTex);
           for (int i = 0; i < 6; i++) {
              batch.getVertexBuffer().put(newPoints[i].x).put(newPoints[i].y).put(newPoints[i].z);
              batch.getVertexBuffer().put(newPoints[i].x).put(newPoints[i].y).put(newPoints[i].z);
              batch.getVertexBuffer().put(newPoints[i].x).put(newPoints[i].y).put(newPoints[i].z);
              batch.getVertexBuffer().put(newPoints[i].x).put(newPoints[i].y).put(newPoints[i].z);
              addNormals(FRONT, BACK, LEFT, DOWN);
           }
           float[] t1 = new float[48];
           setTex(t1,tex,0,0); setTex(t1,newTex,1,0); setTex(t1,newTex,2,1); setTex(t1,newTex,3,2);
           vertexCount = recurse(new Vector3f[]{points[0], newPoints[0], newPoints[1], newPoints[2]},
                   new int[]{index[0], v, v + 4, v + 8}, t1, level + 1, v + 24);
           setTex(t1,newTex,0,0); setTex(t1,tex,1,1); setTex(t1,newTex,2,3); setTex(t1,newTex,3,4);
           vertexCount = recurse(new Vector3f[]{newPoints[0], points[1], newPoints[3], newPoints[4]},
                   new int[]{v, index[1], v + 12, v + 16}, t1, level + 1, vertexCount);
           setTex(t1,newTex,0,1); setTex(t1,newTex,1,3); setTex(t1,tex,2,2); setTex(t1,newTex,3,5);
           vertexCount = recurse(new Vector3f[]{newPoints[1], newPoints[3], points[2], newPoints[5]},
                   new int[]{v + 4, v + 12, index[2], v + 20}, t1, level + 1, vertexCount);
           setTex(t1,newTex,0,2); setTex(t1,newTex,1,4); setTex(t1,newTex,2,5); setTex(t1,tex,3,3);
           return recurse(new Vector3f[]{newPoints[2], newPoints[4], newPoints[5], points[3]},
                   new int[]{v + 8, v + 16, v + 20, index[3]}, t1, level + 1, vertexCount);
        }   
    }
    
    private static void setTex(float[] newTex, float[] tex, int res, int one, int two)  {
        for (int i = 0; i < 8; i++) { 
           newTex[8 * res + i] = 0.5f * (tex[8 * one + i] + tex[8 * two + i]);
        }
    }
    
    private static void setTex(float[] newTex, float[] tex, int pos, int from) {
        for (int i = 0; i < 8; i++) {
            newTex[8*pos + i] = tex[8*from + i];
        }
    }
    
    private static Vector3f middle(Vector3f a, Vector3f b) {
        return a.add(b).mult(0.5f);
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
