package com.tools.flairEditor.sTax;

import com.jme3.audio.AudioNode;
import com.jme3.effect.shapes.EmitterBoxShape;
import com.jme3.effect.shapes.EmitterPointShape;
import com.jme3.effect.shapes.EmitterShape;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.shapes.EmitterMeshConvexHullShape;
import com.jme3.effect.shapes.EmitterMeshFaceShape;
import com.jme3.effect.shapes.EmitterMeshVertexShape;
import com.spectre.deck.Card;
import com.spectre.deck.MasterDeck;
import com.spectre.util.Attribute.CardSeries;
import com.sun.xml.internal.txw2.output.IndentingXMLStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Exports a Deck of cards to an XML database for storage
 * @author Kyle Williams
 */
public class CardXmlExporter{

  public static void main(String[] args) throws Exception{
    
  }
  
  public void export(MasterDeck cards){
        try {
            for(CardSeries type:cards.getSeriesList()){
                XMLStreamWriter xsw = XMLOutputFactory.newInstance()
                        .createXMLStreamWriter(System.out);
                xsw = new IndentingXMLStreamWriter(xsw);
                xsw.writeStartDocument();
                xsw.writeStartElement(type.toString());
                for(Card card:cards.filterCardsBySeries(type)){                        
                    xsw.writeStartElement("Card");
                        xsw.writeAttribute("name", card.getName()); 
                        xsw.writeAttribute("maxUses", card.getMaxUses()+""); 
                        xsw.writeAttribute("prev", card.getPredecessor()); 
                        for(String c:card.getSuccessor()){
                            xsw.writeAttribute("next", c);
                        }
                        xsw.writeEmptyElement("Info");
                            xsw.writeAttribute("description", card.getDescription());
                        xsw.writeEmptyElement("Info");
                            xsw.writeAttribute("range", card.getRange().toString());
                            xsw.writeAttribute("angle", card.getAngle()+"");
                            xsw.writeAttribute("series", card.getSeries().toString());
                            xsw.writeAttribute("trait", card.getTrait().toString());
                        xsw.writeEmptyElement("Info");
                            xsw.writeAttribute("animationName", card.getAnimName());
                        xsw.writeEmptyElement("Stats");
                            xsw.writeAttribute("Hp", card.getHPStat().toString());
                            xsw.writeAttribute("Mp", card.getMPStat().toString());
                            xsw.writeAttribute("knockback", card.getKnockbackStat().toString());
                            xsw.writeAttribute("speed", card.getSpeedStat().toString());
                            xsw.writeAttribute("defense", card.getDeffenseStat().toString());
                        xsw.writeEmptyElement("Stats");
                            xsw.writeAttribute("special", card.getSpecialStat().toString());
                        xsw.writeEmptyElement("Stats");
                            xsw.writeAttribute("translation", card.getTranslationStat().toString());
                        xsw.writeStartElement("Effects");
                            for(ParticleEmitter effect:card.getEffects()){
                                xsw.writeStartElement("Effect");
                                xsw.writeAttribute("name", effect.getName());
                                //xsw.writeAttribute("preset", effect.getUserData("preset").toString());
                                xsw.writeAttribute("type", effect.getMeshType().toString());
                                xsw.writeAttribute("particleNumber", effect.getParticles().length+"");                                    
                                xsw.writeEmptyElement("EffectShape");
                                    EmitterShape s = effect.getShape();
                                    if(s instanceof EmitterBoxShape){
                                        xsw.writeAttribute("shape","box");
                                        xsw.writeAttribute("min", ((EmitterBoxShape)s).getMin()+"");
                                        xsw.writeAttribute("max", ((EmitterBoxShape)s).getLen().addLocal(((EmitterBoxShape)s).getMin()) +"");
                                    }else if(s instanceof EmitterPointShape){
                                        xsw.writeAttribute("shape","point");
                                        xsw.writeAttribute("point", ((EmitterPointShape)s).getPoint()+"");
                                    }else if(s instanceof EmitterSphereShape){
                                        xsw.writeAttribute("shape","sphere");
                                        xsw.writeAttribute("center", ((EmitterSphereShape)s).getCenter()+"");  
                                        xsw.writeAttribute("radius", ((EmitterSphereShape)s).getRadius()+"");
                                    } else if(s instanceof EmitterMeshConvexHullShape){
                                        xsw.writeAttribute("shape","convexHull");
                                        xsw.writeAttribute("mesh",effect.getUserData("mesh")+"");
                                    } else if(s instanceof EmitterMeshFaceShape){
                                        xsw.writeAttribute("shape","meshFace");
                                        xsw.writeAttribute("mesh",effect.getUserData("mesh")+"");
                                    }   else if(s instanceof EmitterMeshVertexShape){
                                        xsw.writeAttribute("shape","meshVertex");
                                        xsw.writeAttribute("mesh",effect.getUserData("mesh")+"");
                                    }                            
                                xsw.writeEmptyElement("EffectProperty");
                                    xsw.writeAttribute("bone", effect.getUserData("bone").toString());
                                    xsw.writeAttribute("startColor", effect.getStartColor()+"");
                                    xsw.writeAttribute("endColor", effect.getEndColor()+"");
                                    xsw.writeAttribute("startSize", effect.getStartSize()+"");
                                    xsw.writeAttribute("endSize", effect.getEndSize()+"");
                                    xsw.writeAttribute("highLife", effect.getHighLife()+"");
                                    xsw.writeAttribute("lowLife", effect.getLowLife()+"");
                                    xsw.writeAttribute("rotateSpeed", effect.getRotateSpeed()+"");
                                xsw.writeEmptyElement("EffectProperty");
                                    xsw.writeAttribute("image", effect.getUserData("image")+"");
                                    xsw.writeAttribute("randImgSelect", effect.isSelectRandomImage()+"");
                                    xsw.writeAttribute("imagesX", effect.getImagesX()+"");
                                    xsw.writeAttribute("imagesY", effect.getImagesY()+"");
                                xsw.writeEmptyElement("EffectProperty");
                                    xsw.writeAttribute("normal", effect.getFaceNormal()+"");
                                    xsw.writeAttribute("facingVelocity", effect.isFacingVelocity()+"");
                                    xsw.writeAttribute("gravity", effect.getGravity()+"");
                                xsw.writeEmptyElement("EffectProperty");
                                    xsw.writeAttribute("numParticles", effect.getParticles().length+"");
                                    xsw.writeAttribute("particlesPerSec", effect.getParticlesPerSec()+"");                                
                                    xsw.writeAttribute("randomAngle", effect.isRandomAngle()+"");                                                                    
                                    xsw.writeAttribute("initialVelocity", effect.getInitialVelocity()+"");
                                    xsw.writeAttribute("velocityVariation", effect.getVelocityVariation()+"");
                                xsw.writeEndElement();//End for effect
                            }
                        xsw.writeEndElement();//End for effects
                        xsw.writeStartElement("Audio");
                            for(AudioNode audio: card.getSoundEffects()){                           
                            xsw.writeEmptyElement("Track");
                                xsw.writeAttribute("location", audio.getName());
                                xsw.writeAttribute("pitch", audio.getPitch()+"");
                                xsw.writeAttribute("reverb", audio.getReverbFilter()+"");
                                xsw.writeAttribute("timeOffset", audio.getTimeOffset()+"");
                                xsw.writeAttribute("volume", audio.getVolume()+"");
                            }
                        xsw.writeEndElement();//End for audio                                   
                    xsw.writeEndElement();//End for Card                     
                }
                xsw.writeEndElement();//End For Series
                xsw.writeEndDocument();
                xsw.close();
            }
        } catch (XMLStreamException ex) {
            Logger.getLogger(CardXmlExporter.class.getName()).log(Level.SEVERE, null, ex);
        }
  }
}