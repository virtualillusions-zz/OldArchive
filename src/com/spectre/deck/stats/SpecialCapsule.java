/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.deck.stats;

/**
 * The Stat capsule is a private class for storing card statistics
 * This Base Class Only Handles all Miscellaneous Statistics and should be used accordingly
 * Special
 *	Boolean
 *      projectile,crawler,instantHit, AutoUse, Mine, Pierce, Immobilize, StatusImmobolize, Brush, Reflect, ShuffleSkills, AbsorbToHP, AbsorbToMP, AbsorbToFlair,CostIncrease
 *      shelterDefense,barrierDefense,deLockon,deLockonInvisible,eraseEnemyStatus,eraseEveryoneStatus,eraseSelfStatus
 *	Integer
 *	LowerLevelAmount, IncreaseLevelAmount, DrawTowardsSelf(in case of trap or whirlwind can also be negated to give force field effect)
 *	Misc
 *	EraseSkill[None,Hand,One,Card][Random,SameButton,SpecificTrait], 
 
 
 * TODO: Look Into Teleporting Conditions
 * @author Kyle Williams
 */
public class SpecialCapsule{
    private boolean projectile=true,crawler=false,instantHit=false,autoUse=false, mine=false,pierce=false, immobilize=false, statusImmobolize=false, brush=false;
    private boolean reflect=false, shuffleSkills=false, absorbToHP=false, absorbToMP=false, absorbToFlair=false,costIncrease=false;
    private boolean shelterDefense=false,barrierDefense=false;
    private boolean deLockOn=false,deLockOnInvisible=false;
    private boolean eraseEnemyStatus=false,eraseEveryoneStatus=false,eraseSelfStatus;
    private int[] lowerLevelAmount=new int[]{0,0}, increaseLevelAmount=new int[]{0,0}, drawTowardsSelf=new int[]{0,0};
    private int eraseSkill=0;
    
    public SpecialCapsule(){}
    
    public SpecialCapsule(String params){
        String[] parameter = params.split(";");        
        projectile = Boolean.parseBoolean(parameter[0]);
        crawler = Boolean.parseBoolean(parameter[1]);
        instantHit = Boolean.parseBoolean(parameter[2]);
        autoUse = Boolean.parseBoolean(parameter[3]);
        mine = Boolean.parseBoolean(parameter[4]);
        pierce = Boolean.parseBoolean(parameter[5]);
        immobilize = Boolean.parseBoolean(parameter[6]);
        statusImmobolize = Boolean.parseBoolean(parameter[7]);
        brush = Boolean.parseBoolean(parameter[8]);
        reflect = Boolean.parseBoolean(parameter[9]);
        shuffleSkills = Boolean.parseBoolean(parameter[10]);
        absorbToHP = Boolean.parseBoolean(parameter[11]);
        absorbToMP = Boolean.parseBoolean(parameter[12]);
        absorbToFlair = Boolean.parseBoolean(parameter[13]);
        costIncrease  = Boolean.parseBoolean(parameter[14]); 
        shelterDefense=Boolean.parseBoolean(parameter[15]); 
        barrierDefense=Boolean.parseBoolean(parameter[16]); 
        deLockOn=Boolean.parseBoolean(parameter[17]); 
        deLockOnInvisible=Boolean.parseBoolean(parameter[18]); 
        eraseEnemyStatus=Boolean.parseBoolean(parameter[19]); 
        eraseEveryoneStatus=Boolean.parseBoolean(parameter[20]); 
        eraseSelfStatus=Boolean.parseBoolean(parameter[21]); 
        
        
        
        String[] temp = parameter[22].split("|");
        lowerLevelAmount[0] = Integer.parseInt(temp[0]);
        lowerLevelAmount[1] = Integer.parseInt(temp[1]);
                 temp = parameter[23].split("|");
        increaseLevelAmount[0] = Integer.parseInt(temp[0]);
        increaseLevelAmount[1] = Integer.parseInt(temp[1]);
                temp = parameter[24].split("|");
        drawTowardsSelf[0] = Integer.parseInt(temp[0]);
        drawTowardsSelf[1] = Integer.parseInt(temp[1]);

        eraseSkill = Integer.parseInt(parameter[25]);
    }
    public boolean isProjectile(){return projectile;}
    public boolean isCrawler(){return crawler;}
    public boolean isInstantHit(){return instantHit;}
    
    public boolean isAutoUse(){return autoUse;}
    public boolean isMine(){return mine;}
    public boolean isPierce(){return pierce;}
    
    public boolean isImmobolize(){return immobilize;}
    public boolean isStatusImmobolize(){return statusImmobolize;}
    public boolean isBrush(){return brush;}
    
    public boolean isReflect(){return reflect;}
    public boolean isShuffleSkills(){return shuffleSkills;}
    public boolean isAbsorbToHp(){return absorbToHP;}
    
    public boolean isAbsorbToMp(){return absorbToMP;}
    public boolean isAbsorbToFlair(){return absorbToFlair;}
    public boolean isCostIncrease(){return costIncrease;}
    
    public boolean isShelterDefenstType(){return shelterDefense;}
    public boolean isBarrierDefenseType(){return barrierDefense;}
    public boolean isDeLockOn(){return deLockOn;}     
    public boolean isDeLockOnInvisible(){return deLockOnInvisible;}
    
    public boolean isEraseEnemyStatus(){return eraseEnemyStatus;}
    public boolean isEraseEveryOneStatus(){return eraseEveryoneStatus;}     
    public boolean isEraseSelfStatus(){return eraseSelfStatus;}
    
    
    
    public int getLowerLevelAmount(){return lowerLevelAmount[0];}
    public int getSelfLowerLevelAmount(){return lowerLevelAmount[1];}
    public int getIncreaseLevelAmount(){return increaseLevelAmount[0];}
    public int getSelfIncreaseLevelAmount(){return increaseLevelAmount[1];}
    public int getDrawTowardsSelfAmount(){return drawTowardsSelf[0];}
    public int getSelfDrawTowardsSelfAmount(){return drawTowardsSelf[1];}
     /**
      * 0 None
      * 1 Random In My Hand
      * 2 Random In Opponent Hand
      * 3 All in My Hand
      * 4 All in Opponent Hand
      * 5 Specific Card in My Hand
      * 6 Specific Card in Opponent Hand
      * TODO: Not well thoughtout however no where near implimenting this leave this for later
     */
    public int getEraseSkill(){return eraseSkill;}
    
    public void setProjectile(boolean param){projectile = param;}
    public void setCrawler(boolean param){crawler=param;}
    public void setInstantHit(boolean param){instantHit = param;}
    
    public void setAutoUse(boolean param){autoUse = param;}
    public void setMine(boolean param){mine=param;}
    public void setPierce(boolean param){pierce = param;}
    
    public void setImmobolize(boolean param){immobilize = param;}
    public void setStatusImmobolize(boolean param){statusImmobolize = param;}
    public void setBrush(boolean param){brush = param;}
    
    public void setReflect(boolean param){reflect = param;}
    public void setShuffleSkills(boolean param){shuffleSkills = param;}
    public void setAbsorbToHp(boolean param){absorbToHP = param;}
    
    public void setAbsorbToMp(boolean param){absorbToMP = param;}
    public void setAbsorbToFlair(boolean param){absorbToFlair = param;}
    public void setCostIncrease(boolean param){costIncrease = param;}

    public void setShelterDefense(boolean param){shelterDefense = param;}
    public void setBarrierDefense(boolean param){barrierDefense = param;}
    public void setDeLockOn(boolean param){deLockOn = param;}
    
    public void setDeLockOnInvisible(boolean param){deLockOnInvisible = param;}
    public void setEraseEnemyStatus(boolean param){eraseEnemyStatus = param;}
    public void setEraseEveryOneStatus(boolean param){eraseEveryoneStatus = param;}
    public void setEraseSelfStatus(boolean param){eraseSelfStatus = param;}
    
    
    
    public void setLowerLevelAmount(int param){lowerLevelAmount[0]=param;}
    public void setSelfLowerLevelAmount(int param){lowerLevelAmount[1]=param;}
    public void setIncreaseLevelAmount(int param){increaseLevelAmount[0]=param;}
    public void setSelfIncreaseLevelAmount(int param){increaseLevelAmount[1]=param;}
    public void setDrawTowardsSelfAmount(int param){drawTowardsSelf[0]=param;}
    public void setSelfDrawTowardsSelfAmount(int param){drawTowardsSelf[1]=param;}
    /**
      * 0 None
      * 1 Random In My Hand
      * 2 Random In Opponent Hand
      * 3 All in My Hand
      * 4 All in Opponent Hand
      * 5 Specific Card in My Hand
      * 6 Specific Card in Opponent Hand
     * TODO: Not well thoughtout however no where near implimenting this leave this for later
     */
    public void setEraseSkill(int param){eraseSkill=param;}
    
     @Override
     public String toString(){
        return projectile+";"+crawler+";"+instantHit+";"+autoUse+";"
               +mine+";"+pierce+";"+immobilize+";"+statusImmobolize+";"
               +brush+";"+reflect+";"+shuffleSkills+";"+absorbToHP+";"
               +absorbToMP+";"+absorbToFlair+";"+costIncrease+";"
               +shelterDefense+";"+barrierDefense+";"+deLockOn+";"
               +deLockOnInvisible+";"+eraseEnemyStatus+";"
               +eraseEveryoneStatus+";"+eraseSelfStatus+";"  
               +lowerLevelAmount[0]+"|"+lowerLevelAmount[1]+";"+
               +increaseLevelAmount[0]+"|"+increaseLevelAmount[1]+";"+
               +drawTowardsSelf[0]+"|"+drawTowardsSelf[1]+";"
               +eraseSkill;
    }
}
