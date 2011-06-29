/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.util;

import java.util.Comparator;

/**
 * List of Game Types ranging from characters to Cards
 * @author Kyle Williams
 */
public class Attribute {
    /**
     *  This is a list of all Card Series in the game
     * It should be noted that Ryujin is special and instead of its ancestors being both DragonPrime and Dragoon it is only Dragon
     */
    public enum CardSeries{
//                Series 1             Series 2                                 Series 3                                         Series 4
/*Base*/           Raw(0),
 
/*Nature*/         Nature(1),             Muse(Nature,2),                       Cleric(Muse,3),                              BlueLight(Cleric,4),           
                                          Dragon(Nature,2),                     DragonPrime(Dragon,3),Dragoon(Dragon,3),     Ryujin(Dragon,4),
                                          NaturalDefender(Nature,2),
                                     

/*Technology*/     Technology(1),         Nanobot(Technology,2),                Hacker(Nanobot,3),                           Virus(Hacker,4),
                                          Cybernetics(Technology,2),            CyberRaptor(Cybernetics,3),                    

/*Phantom*/        Phantom(1),            Necromancer(Phantom,2),               Devildom(Necromancer,3),                     Satonic(Devildom,4),
                                          Shaman(Phantom,2),                    Reaper(Shaman,3),            

/*Celestial*/      Celestial(1),          Zodiac(Celestial,2),                  Destiny(Zodiac,3),
                                          Astro(Celestial,2),                   Devine(Astro,3),

/*Aether*/         Creation(1),
                   MatterControl(1);
                   private CardSeries(int s){prev=null;series=s;}
                   private CardSeries(CardSeries k, int s){prev=k;series=s;}                
                   public CardSeries getAncestor(){return prev==null?Raw:prev;}
                   public int getSeries(){return series;} 
                   private final CardSeries prev;
                   private final int series;
    } 
    /**
     *  This is a list of all Card Traits supported in the game
     */
    public enum CardTrait {
        Damage,Defense,Buff,DeBuff,Special,Summon,Transform;
    } 
     /**
     *  This is a list of all Card Ranges supported in the game
     */   
    public enum CardRange{
        Long,Med,Short,Self;
    }
    /**
     * This is a list of all possible special player movement
     */
    public enum SpecialMovement {
        jump,dash,dive;
    }
    /**
     * This is a list of all possible 
     */
    public enum SpecialWeapon{
        pistol(2,4),sniper(3,1),submachine(1,10),sword(2),katana(2),gauntlet(1);
        int dmg=0;int spd=0;
        private SpecialWeapon(int d){dmg=d;}
        private SpecialWeapon(int d,int s){dmg=d;spd=s;}
        public int getDamage(){return dmg;}
        public int getAttackSpeed(){return spd!=0?spd:null;}
    }
    
    public final static Comparator lexiSortSeries = new Comparator<Attribute.CardSeries>(){
            public int compare(CardSeries o1, CardSeries o2) {
                return o1.toString().compareTo(o2.toString());
            }   
        };
}
