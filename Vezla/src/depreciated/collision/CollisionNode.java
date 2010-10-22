/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package depreciated.collision;

import com.jme3.math.Vector3f;

 /**
  * This class acts as a collision object for the game world
  * @author Kyle Williams
  * TODO: use boxes 1x1x1 for bone collision instead of rectangles to reduce
  * issues later on
  */
public class CollisionNode extends com.jme3.scene.Node{
    private boolean hasBones, canJump;
    private Vector3f userVector = new Vector3f(0,0,0);
    private Vector3f collisionVector  = new Vector3f(0,0,0);
    private Vector3f resultingVector = new Vector3f(0,0,0);
    private java.awt.Rectangle[] boneCol;
    private float massInKG = 90.72f;
    private float MaxSpeed = 25f;
    private float NormalSpeed = 20f;
    private float jumpHeight = 5;
    private float MaxJumpHeight;
    

    public CollisionNode(String name){
        super(name);
        hasBones = false;
        canJump = false;
    }
/*
    public void update(float tpf){
        updateBoneTranslation();
        updateCollision(tpf);
    }
*/
    /**
     * This updates all movement
     * @param tps
     */
    private void updateCollision(float tps){
        //This section is for user input
       if(!userVector.equals(Vector3f.ZERO)){
           if(getLocalTranslation().getY()>=MaxJumpHeight||userVector.getY()==0)
               canJump=false;
           else if(canJump&&userVector.getY()>0)
               collisionVector.setY(0);
           if(!canJump)
               userVector.setY(0);
           if(collisionVector.getY()!=0||userVector.getY()!=0){
                userVector.setX(0);
                userVector.setZ(0);
           }
           resultingVector.addLocal(userVector);
           userVector.zero();
       }

       //This section is for collision input
       if(!collisionVector.equals(Vector3f.ZERO)){
           collisionVector.mult(userVector);
            resultingVector.addLocal(collisionVector);
            collisionVector.zero();
       }

       //This is the resulting Vector
        float unit = getMass()/10*tps;      
        resultingVector.multLocal(unit);
        super.move(resultingVector);      
    }

    public void setOnPlatform(boolean sOP){
        canJump=sOP;
        this.MaxJumpHeight=getLocalTranslation().getY()+jumpHeight;
    }
    /**
     * This Overrides com.jme3.scene.Spatial..move(float x, float y, float z);
     * It should be called only once not updated
     */
     public com.jme3.scene.Spatial force(float x, float y, float z){
        collisionVector.set(x, y, z);
        return this;
     }

    /**
     * This Overrides com.jme3.scene.Spatial.move(Vector3f offset);
     * It should be called only once not updated
     */
     public com.jme3.scene.Spatial force(com.jme3.math.Vector3f offset){
        collisionVector.set(offset);
        return this;
     }

    @Override
    /**
     * This Overrides com.jme3.scene.Spatial.move(float x, float y, float z);
     * It should be called only once not updated
     */
     public com.jme3.scene.Spatial move(float x, float y, float z){
        userVector.addLocal(x, y, z);
        return this;
     }
    @Override
    /**
     * This Overrides com.jme3.scene.Spatial.move(Vector3f offset);
     * It should be called only once not updated
     */
     public com.jme3.scene.Spatial move(com.jme3.math.Vector3f offset){
        userVector.addLocal(offset);
        return this;
     }

     /**
     * The original move function
     * Translates the spatial by the given translation vector.     *
     * @return The spatial on which this method is called, e.g <code>this</code>.
     */
    public com.jme3.scene.Spatial moveOriginal(float x, float y, float z){
        super.move(x,y,z);
        return this;
     }
     /**
     * The original move function
     * Translates the spatial by the given translation vector.     *
     * @return The spatial on which this method is called, e.g <code>this</code>.
     */
    public com.jme3.scene.Spatial moveOriginal(com.jme3.math.Vector3f offset){
        super.move(offset);
        return this;
     }

    public float getMass(){return massInKG;}
    public float getPosXSide(){return getWorldBound().getCenter().getX()+((com.jme3.bounding.BoundingBox)getWorldBound()).getXExtent();}
    public float getNegXSide(){return getWorldBound().getCenter().getX()-((com.jme3.bounding.BoundingBox)getWorldBound()).getXExtent();}
    public float getPosYSide(){return getWorldBound().getCenter().getY()+((com.jme3.bounding.BoundingBox)getWorldBound()).getYExtent();}
    public float getNegYSide(){return getWorldBound().getCenter().getY()-((com.jme3.bounding.BoundingBox)getWorldBound()).getYExtent();}
    public float getPosZSide(){return getWorldBound().getCenter().getZ()+((com.jme3.bounding.BoundingBox)getWorldBound()).getZExtent();}
    public float getNegZSide(){return getWorldBound().getCenter().getZ()-((com.jme3.bounding.BoundingBox)getWorldBound()).getZExtent();}

    public float getWidth(){return ((com.jme3.bounding.BoundingBox)getWorldBound()).getXExtent()*2;}
    public float getHeight(){return ((com.jme3.bounding.BoundingBox)getWorldBound()).getYExtent()*2;}
    public float getLength(){return ((com.jme3.bounding.BoundingBox)getWorldBound()).getZExtent()*2;}



/*
    @Override
    public int attachChild(com.jme3.scene.Spatial child){
        super.attachChild(child);
        if(child.getControl(com.jme3.animation.AnimControl.class)!=null){
         hasBones = true;
           com.jme3.animation.Skeleton skeleton = child.getControl(com.jme3.animation.AnimControl.class).getSkeleton();
            boneCol = new java.awt.Rectangle[skeleton.getBoneCount()];
            for(int i=0; i<boneCol.length ;i++){
                com.jme3.math.Vector3f vec=child.localToWorld(skeleton.getBone(i).getWorldPosition(), null);
                boneCol[i]=new java.awt.Rectangle((int)vec.getX(),(int)vec.getY(),1,1);
            }
        }
       updateGeometricState();
        return children.size();
    }

    public void updateBoneTranslation(){
        if(hasBones){
            for (int j = 0, cSize = children.size(); j < cSize; j++){
                com.jme3.scene.Spatial spat = children.get(j);
                com.jme3.animation.Skeleton skeleton = spat.getControl(com.jme3.animation.AnimControl.class).getSkeleton();
                for(int i=0; i<boneCol.length ;i++){
                    com.jme3.math.Vector3f vec=spat.localToWorld(skeleton.getBone(i).getWorldPosition(), null);
                    boneCol[i].setLocation((int)vec.getX(),(int)vec.getY());
                }
            }
        }
    }
    */
}