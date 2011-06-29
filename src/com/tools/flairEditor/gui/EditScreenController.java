/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tools.flairEditor.gui;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticlePointMesh;
import com.jme3.effect.shapes.EmitterBoxShape;
import com.jme3.effect.shapes.EmitterMeshConvexHullShape;
import com.jme3.effect.shapes.EmitterMeshFaceShape;
import com.jme3.effect.shapes.EmitterMeshVertexShape;
import com.jme3.effect.shapes.EmitterPointShape;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.spectre.deck.CardSetter;
import com.spectre.util.Attribute;
import com.tools.flairEditor.FlairDirector;

import de.lessvoid.nifty.Nifty; 
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.CheckBoxStateChangedEvent;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.Slider;
import de.lessvoid.nifty.controls.SliderChangedEvent;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.checkbox.builder.CheckboxBuilder;
import de.lessvoid.nifty.controls.dropdown.builder.DropDownBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.controls.listbox.builder.ListBoxBuilder;
import de.lessvoid.nifty.controls.scrollpanel.builder.ScrollPanelBuilder;
import de.lessvoid.nifty.controls.slider.builder.SliderBuilder;
import de.lessvoid.nifty.controls.textfield.builder.TextFieldBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;

/** 
 * TODO: add method to hide all extra tabs apply where needed....mainly where buttons unhide other tabs
 * TODO: Figure Out Way To Offset Effects and Audio in time
 * Remember: Particle Influencer Not Yet Implimented
 * @author Kyle Williams
 */ 
public class EditScreenController implements ScreenController{

    public void bind(final Nifty nifty, Screen screen) {
        if(card==null){throw new UnsupportedOperationException("Card Needs to be intialized first");}
        this.nifty = nifty;
        this.screen=screen;
        tL = screen.findElementByName("Top Left");
        sect1=tL.findElementByName("section1");
        sect2=tL.findElementByName("section2");
        tM = screen.findElementByName("Top Middle");
        anim=tM.findElementByName("animationList");
        stats=tM.findElementByName("statsList");
        tR = screen.findElementByName("Top Right");
        effect=tR.findElementByName("effects");
        audio=tR.findElementByName("audio");
        Element extra=screen.findElementByName("extra");
        title = screen.findElementByName("editTitle").getRenderer(TextRenderer.class);
        ListBox l;
        java.util.ArrayList<String> List = new java.util.ArrayList<String>();
        //Section1 TAB
        new ScrollPanelBuilder("section1List") {{
            width("*");
            height("*");
            panel(new PanelBuilder(){{
                childLayoutVertical();width("100%");style("nifty-panel-no-shadow"); 
                panel(new PanelBuilder(){{
                        childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingTop(percentage(30));paddingLeft(percentage(3));
                        control(new LabelBuilder("name","Name: "));
                        control(new TextFieldBuilder("nameT"){{width("50%");paddingLeft(percentage(3));}});
                }});                
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingTop(percentage(30));paddingLeft(percentage(3));
                    control(new LabelBuilder("MaxUse","Max Uses: "){{width("30%");}});
                    /*0 means infinity*/
                    control(new SliderBuilder("MaxUseS", false){{ paddingLeft(percentage(3));min(-1); max(10); stepSize(1); buttonStepSize(1); }});
                }});
                /*
                panel(new PanelBuilder(){{
                     childLayoutHorizontal();
                     width(percentage(100));
                     height(pixels(40));
                     paddingTop(percentage(30));
                     paddingLeft(percentage(3));
                     style("nifty-panel-no-shadow");
                     control(new LabelBuilder("prev",   "Predacessor: "));
                     control(new TextFieldBuilder("prevField"){{width("50%");paddingLeft(percentage(3));}});
                }});
                panel(new PanelBuilder("successor"){{
                    childLayoutVertical(); 
                    width(percentage(100));
                    style("nifty-panel-no-shadow");
                    paddingTop(pixels(10));
                    paddingLeft(percentage(3));
                    for(String s:card.getSuccessor()){
                        panel(createNewSuccessorPanel(s));
                    }
                }});
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingLeft(percentage(50));
                    control(new ButtonBuilder("addNewSuccessor", "Add Successor"){{width("*"); }});
                }});
               */
            }});
        }}.build(nifty, screen, sect1);  
        //Section2 TAB
        new ScrollPanelBuilder("section2List") {{
            width("*");
            height("*");
            panel(new PanelBuilder(){{
                childLayoutVertical();width("100%");style("nifty-panel-no-shadow"); 
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingLeft(percentage(3));
                    control(new LabelBuilder("Range", "Range: Long"){{width("30%");}});
                    control(new SliderBuilder("RangeS", false){{ paddingLeft(percentage(3));min(0); max(Attribute.CardRange.values().length-1); stepSize(1); buttonStepSize(1); initial(0);}});
                }});
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingLeft(percentage(3));control(new LabelBuilder("Angle","Angle: 0.0"){{width("30%");}});
                    control(new SliderBuilder("AngleS", false){{ paddingLeft(percentage(3));min(0); max(360); stepSize(.01f); buttonStepSize(5); initial(0);}});                    
                }});
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100)); height(pixels(40)); paddingLeft(percentage(3));
                    control(new LabelBuilder("Series","Series: Raw"){{width("30%");}});
                    control(new SliderBuilder("SeriesS", false){{ paddingLeft(percentage(3));min(0); max(Attribute.CardSeries.values().length-1); stepSize(1); buttonStepSize(1); initial(0);}});
                }});
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingLeft(percentage(3));
                    control(new LabelBuilder("Trait","Trait: Damage"){{width("30%");}});
                    control(new SliderBuilder("TraitS", false){{ paddingLeft(percentage(3));min(0); max(Attribute.CardTrait.values().length-1); stepSize(1); buttonStepSize(1); initial(0);}});
                }});
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingLeft(percentage(3));
                    control(new LabelBuilder("Info", "Description: "));
                    control(new TextFieldBuilder("InfoT"){{width("*");paddingLeft(percentage(3));}});
                }});
            }});
        }}.build(nifty, screen, sect2);  
        //ANIMATION TAB
       new PanelBuilder(){{
            childLayoutVertical();width("100%");style("nifty-panel-no-shadow"); 
            control(new ListBoxBuilder("animationListBoxL"){{
                        optionalVerticalScrollbar();optionalHorizontalScrollbar();
                        width(percentage(100));
                        displayItems(nifty.getRenderEngine().getHeight()/65);
                        selectionModeSingle();
                        set("forceSelection","true");}}); 
//TODO: Reimpliment multiple Animations Later
//            panel(new PanelBuilder(){{
//                    childLayoutHorizontal();width(percentage(100));
//                    control(new ButtonBuilder("createNewAnimControlB","Create"){{width(percentage(50));}});
//                    control(new ButtonBuilder("cancelNewAnimControleB","Cancel"){{width(percentage(50)); }});
//                }});
        }}.build(nifty, screen, anim);       
       l = anim.findNiftyControl("animationListBoxL", ListBox.class);
       l.addAllItems(FlairDirector.getAnimList());
        //STATS TAB
        new ListBoxBuilder("statsListBoxL"){{
                optionalVerticalScrollbar();optionalHorizontalScrollbar();
                width(percentage(100));
                displayItems(nifty.getRenderEngine().getHeight()/65);
                selectionModeSingle();
                set("forceSelection","true");
              
        }}.build(nifty, screen, stats);  
        l = stats.findNiftyControl("statsListBoxL", ListBox.class);
        List.clear();
        List.add("HP");List.add("MP");List.add("Knockback");List.add("Speed");List.add("Defense");List.add("Special");List.add("Translation");
        l.addAllItems(List);
        //EFFECTS TAB
        new ListBoxBuilder("effectsListBoxL"){{
                optionalVerticalScrollbar();optionalHorizontalScrollbar();
                width(percentage(100));
                displayItems(nifty.getRenderEngine().getHeight()/65);
                selectionModeSingle();
                set("forceSelection","true");
               
        }}.build(nifty, screen, effect);  
        //AUDIO TAB
         new ListBoxBuilder("audioListBoxL"){{
                optionalVerticalScrollbar();optionalHorizontalScrollbar();
                width(percentage(100));
                displayItems(nifty.getRenderEngine().getHeight()/65);
                selectionModeSingle();
                set("forceSelection","true");

        }}.build(nifty, screen, audio);
         //EFFECT AND AUDIO EDITING PANEL
         Element temp = screen.findElementByName("EffectsAudioPane");
         //new Effect
        newEffectsPane=new ScrollPanelBuilder("newEffectsAttr"){{
            width("*");
            height("*");
            panel(new PanelBuilder(){{
                childLayoutVertical();width("100%");style("nifty-panel-no-shadow"); 
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingLeft(percentage(3));
                    control(new LabelBuilder("EffectName",    "Name: "){{width("20%");}});
                    control(new TextFieldBuilder("EffectNameT"));
                }});                
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingLeft(percentage(3));
                    control(new LabelBuilder("EffectType",    "Type: Point"){{width("50%");}});
                    control(new SliderBuilder("EffectTypeS", false){{ paddingLeft(percentage(3));min(0); max(com.jme3.effect.ParticleMesh.Type.values().length-1); stepSize(1); buttonStepSize(1);initial(0);}});
                }});                
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingLeft(percentage(3));
                    control(new LabelBuilder("EffectNumParticles",    "NumberOfParticles: "){{width("50%");}});
                    control(new TextFieldBuilder("EffectNumParticlesT"));
                }});                
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingLeft(percentage(3));
                    control(new ButtonBuilder("createNewEffectB","Create"){{width(percentage(50));}});
                    control(new ButtonBuilder("cancelNewEffectB","Cancel"){{width(percentage(50)); }});
                }});
            }});
        }}.build(nifty, screen, temp);
        
        //Effects ListBox
        effectsPane=new ScrollPanelBuilder("effectsAttr"){{
            width("*");
            height("*");
            panel(new PanelBuilder(){{
                childLayoutVertical();width("100%");style("nifty-panel-no-shadow"); 
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingLeft(percentage(3));
                    control(new LabelBuilder("effectName",    "Name: "){{width("20%");}});
                    control(new TextFieldBuilder("effectNameT"));//must be named this way other way in use
                }});  
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingLeft(percentage(3));
                    control(new LabelBuilder("EmitterShape", "EmitterShape: DEFAULT EmitterPointShape(Vector3f.ZERO) "){{width("75%");}});
                    control(new ButtonBuilder("EmitterShapeB","Change"){{width("*");}});
                }}); 
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingLeft(percentage(3));
                    control(new LabelBuilder("StartColor", "Start Color: "){{width("20%");}});
                    panel(new PanelBuilder("StartColorP"){{childLayoutHorizontal();width(percentage(10));height("*");}});
                    control(new TextFieldBuilder("StartColorT"){{width(percentage(50));}});
                }}); 
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingLeft(percentage(3));
                    control(new LabelBuilder("EndColor", "End Color: "){{width("20%");}});
                    panel(new PanelBuilder("EndColorP"){{childLayoutHorizontal();width(percentage(10));height("*");}});
                    control(new TextFieldBuilder("EndColorT"){{width(percentage(50));}});
                }}); 
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingLeft(percentage(3));
                    control(new LabelBuilder("StartSize", "Start Size: "){{width(percentage(30));}});
                    control(new TextFieldBuilder("StartSizeT"){{width(percentage(50));}});
                }}); 
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingLeft(percentage(3));
                    control(new LabelBuilder("EndSize", "End Size: "){{width(percentage(30));}});
                    control(new TextFieldBuilder("EndSizeT"){{width(percentage(50));}});
                }}); 
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingLeft(percentage(3));
                    control(new LabelBuilder("HighLife", "High Life: "){{width(percentage(30));}});
                    control(new TextFieldBuilder("HighLifeT"){{width(percentage(50));}});
                }});
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingLeft(percentage(3));
                    control(new LabelBuilder("LowLife", "Low Life: "){{width(percentage(30));}});
                    control(new TextFieldBuilder("LowLifeT"){{width(percentage(50));}});
                }});  
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingLeft(percentage(3));
                    control(new LabelBuilder("RotateSpeed", "RotateSpeed: "){{width(percentage(30));}});
                    control(new TextFieldBuilder("RotateSpeedT"){{width(percentage(50));}});
                }});  
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));height(pixels(120));paddingLeft(percentage(3));
                    control(new LabelBuilder("Image", "Image: "){{width("20%");}});                    
                    image(new ImageBuilder("ImageI"){{width(percentage(70));paddingLeft(percentage(3));}});
                    control(new ButtonBuilder("ImageB","Change"){{width("*");}});
                }});   
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingLeft(percentage(3));
                    control(new LabelBuilder("RandomImageSelect", "SelectRandomImage:          "){{width("20%");}});
                    control(new CheckboxBuilder("RandomImageSelectC"){{ paddingLeft(percentage(15));}});
                
                }});  
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingLeft(percentage(3));
                    control(new LabelBuilder("ImagesX", "ImagesX: "){{width("20%");}});
                    control(new TextFieldBuilder("ImagesXT"){{width(percentage(50));}});
                }});   
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingLeft(percentage(3));
                    control(new LabelBuilder("ImagesY", "ImagesY: "){{width("20%");}});
                    control(new TextFieldBuilder("ImagesYT"){{width(percentage(50));}});
                }});   
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingLeft(percentage(3));
                    control(new LabelBuilder("Normal", "Normal: "){{width("20%");}});
                    control(new TextFieldBuilder("NormalT"){{width(percentage(70));}});
                }});   
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingLeft(percentage(3));
                    control(new LabelBuilder("FacingVelocity", "Facing Velocity: "){{width("20%");}});
                    control(new CheckboxBuilder("FacingVelocityC"){{ paddingLeft(percentage(15));}});
                }});   
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingLeft(percentage(3));
                    control(new LabelBuilder("Gravity", "Gravity: "){{width("20%");}});
                    control(new TextFieldBuilder("GravityT"){{width(percentage(50));}});
                }});    
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100)); height(pixels(40));paddingLeft(percentage(3));
                    control(new LabelBuilder("NumberOfParticles", "Number Of Particles: "){{width("25%");}});
                    control(new TextFieldBuilder("NumberOfParticlesT"){{width(percentage(50));}});
                }});   
                panel(new PanelBuilder(){{
                    childLayoutHorizontal(); width(percentage(100));height(pixels(40));paddingLeft(percentage(3));
                    control(new LabelBuilder("ParticlesPerSecond", "Particles Per Second: "){{width("20%");}});
                    control(new TextFieldBuilder("ParticlesPerSecondT"){{width(percentage(50));}});
                }});   
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingLeft(percentage(3));
                    control(new LabelBuilder("RandomAngle", "Random Angle: "){{width("20%");}});
                    control(new CheckboxBuilder("RandomAngleC"){{ paddingLeft(percentage(15));}});
                }});   
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingLeft(percentage(3));
                    control(new LabelBuilder("InitialVelocity", "Initial Velocity: "){{width("20%");}});
                     control(new TextFieldBuilder("InitialVelocityT"){{width(percentage(50));}});
                }});   
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingLeft(percentage(3));
                    control(new LabelBuilder("VelocityVariation", "Velocity Variation: "){{width("20%");}});
                    control(new TextFieldBuilder("VelocityVariationT"){{width(percentage(50));}});
                }}); 
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingLeft(percentage(3));
                    control(new LabelBuilder("","Bone: "){{width("25%");}});
                    control(new DropDownBuilder("BoneListD"){{width("*");}});
                }}); 
            }});
        }}.build(nifty, screen, temp);
        effectsPane.findNiftyControl("BoneListD", DropDown.class).addAllItems(FlairDirector.getBoneList()); 
        //Audio ListBox
        audioPane=new ScrollPanelBuilder("audioAttr"){{
            width("*");
            height("*");
             panel(new PanelBuilder(){{
                childLayoutVertical();width("100%");style("nifty-panel-no-shadow"); 
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingLeft(percentage(3));
                    control(new LabelBuilder("audioLocationLabel",    "Location: "){{width("70%");}});
                    control(new ButtonBuilder("playAudioB","Play"));
                }});
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingLeft(percentage(3));
                    control(new LabelBuilder("Volume",    "Volume: "){{width("30%");}});
                    control(new SliderBuilder("VolumeS", false){{ paddingLeft(percentage(3));min(0); max(1); stepSize(.01000000f); buttonStepSize(.10000000f);}});
                 }}); 
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingLeft(percentage(3));
                    control(new LabelBuilder("Pitch",    "Pitch: "){{width("30%");}});
                    control(new SliderBuilder("PitchS", false){{ paddingLeft(percentage(3));min(.5f); max(2); stepSize(.01000000f); buttonStepSize(.10000000f);}});        
                }}); 
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingLeft(percentage(3));                   
                    control(new LabelBuilder("TimeOffset",    "Time Offset: "){{width("30%");}});
                    control(new SliderBuilder("TimeOffsetS", false){{ paddingLeft(percentage(3));min(0); max(100); stepSize(.01000000f); buttonStepSize(.10000000f);}});
                }});
//TODO: Figure out how to implement reverb          
//                panel(new PanelBuilder(){{
//                    childLayoutHorizontal();
//                    width(percentage(100));
//                    height(pixels(40));
//                    paddingLeft(percentage(3));
//                    style("nifty-panel-no-shadow");
//                    control(new LabelBuilder("reverb",    "Reverb: "));
//                    //control(new SliderBuilder("TimeOffset", false){{ paddingLeft(percentage(3));min(0); max(com.jme3.audio.Filter.); stepSize(1); buttonStepSize(1); initial(0);}});
//                }});            
            }});   
        }}.build(nifty, screen, temp);
        
        emitterShapePane = new ScrollPanelBuilder("emitterShape"){{
             width("*");height("*");
             panel(new PanelBuilder(){{
                childLayoutVertical();width("100%");style("nifty-panel-no-shadow"); 
                control(new DropDownBuilder("EmitterShapeD") {{width("*");}});
                panel(new PanelBuilder("EmitterBoxShape"){{
                    childLayoutVertical();width("100%"); 
                    panel(new PanelBuilder(){{
                        childLayoutHorizontal();width(percentage(100));height(pixels(40));
                        control(new LabelBuilder("EmitterShapeParam0",    "Mesh: "){{width("50%");}});
                        control(new ButtonBuilder("EmitterShapeParam0B", "Locate Mesh"));
                    }}); 
                    panel(new PanelBuilder(){{
                        childLayoutHorizontal();width(percentage(100));height(pixels(40));
                        control(new LabelBuilder("EmitterShapeParam1", "N/A"){{width("20%");}});
                        control(new TextFieldBuilder("EmitterShapeParam1T"){{width(percentage(50));}});
                    }}); 
                    panel(new PanelBuilder(){{
                        childLayoutHorizontal();width(percentage(100));height(pixels(40));
                        control(new LabelBuilder("EmitterShapeParam2", "N/A"){{width("20%");}});
                        control(new TextFieldBuilder("EmitterShapeParam2T"){{width(percentage(50));}});
                    }});
                }});                                     
                panel(new PanelBuilder(){{
                    childLayoutHorizontal();width(percentage(100));
                    control(new ButtonBuilder("createNewEmitterShapeB","Create"){{width(percentage(50));}});
                    control(new ButtonBuilder("cancelNewEmitterShapeB","Cancel"){{width(percentage(50)); }});
                }});
             }});
        }}.build(nifty, screen, extra);
        List.clear();
        List.add("EmitterBoxShape");List.add("EmitterPointShape");List.add("EmitterSphereShape");List.add("EmitterMeshConvexHullShape");List.add("EmitterMeshFaceShape");List.add("EmitterMeshVertexShape");
        extra.findNiftyControl("EmitterShapeD", DropDown.class).addAllItems(List); 
  
        statsPane=new ScrollPanelBuilder("Card Statistics"){{
           width("*");height("*");           
            panel(new PanelBuilder(){{
                childLayoutVertical();width("100%");
            
                panel(new PanelBuilder("statsCapsule"){{

            childLayoutVertical();width("100%");
            panel(new PanelBuilder(){{
                    childLayoutAbsolute();width(percentage(100));height(pixels(40));
                    control(new LabelBuilder("s", "Stat"){{width("20%");x("5%");y("0%");}});
                    control(new LabelBuilder("o", "Opponent"){{width("20%");x("40%");y("0%");}});
                    control(new LabelBuilder("p", "Self"){{width("20%");x("70%");y("0%");}});
                }}); 
             panel(new PanelBuilder(){{
                    childLayoutAbsolute();width(percentage(100));height(pixels(40));
                    control(new LabelBuilder("stat1", "Amount"){{width("20%");x("5%");y("5%");}});
                    control(new TextFieldBuilder("statsParam1T"){{width("20%");x("40%");y("5%");}});
                    control(new TextFieldBuilder("statsSelfParam1T"){{width("20%");x("70%");y("5%");}});
             }}); 
             panel(new PanelBuilder(){{
                    childLayoutAbsolute();width(percentage(100));height(pixels(40));
                    control(new LabelBuilder("stat2", "Rate"){{width("20%");x("5%");y("10%");}});
                    control(new TextFieldBuilder("statsParam2T"){{width("20%");x("40%");y("10%");}});
                    control(new TextFieldBuilder("statsSelfParam2T"){{width("20%");x("70%");y("10%");}});
             }}); 
             panel(new PanelBuilder(){{
                    childLayoutAbsolute();width(percentage(100));height(pixels(40));
                    control(new LabelBuilder("stat3", "Misc"){{width("20%");x("5%");y("15%");}});
                    control(new TextFieldBuilder("statsParam3T"){{width("20%");x("40%");y("15%");}});
                    control(new TextFieldBuilder("statsSelfParam3T"){{width("20%");x("70%");y("15%");}});
             }}); 

              }});
            
            }});  
        }}.build(nifty, screen, temp);        
    
    
        specialStatsPane = new ScrollPanelBuilder("Special Card Statistics"){{
        width("*");height("*");           
            panel(new PanelBuilder(){{
                childLayoutVertical();width("100%");            
                panel(new PanelBuilder("specialCapsule"){{
                    childLayoutVertical();width("100%");
                    panel(new PanelBuilder(){{
                        childLayoutAbsolute();width(percentage(100));height(pixels(40));
                        control(new LabelBuilder("PT", "Projectile Type:"){{width("20%");x("0%");y("20%");}});
                        control(new CheckboxBuilder("specialStatProjectile"){{x("20%");y("20%");}});
                        control(new LabelBuilder("CT", "Crawler Type:"){{width("20%");x("25%");y("20%");}});
                        control(new CheckboxBuilder("specialStatCrawler"){{x("45%");y("20%");}});
                        control(new LabelBuilder("IHT", "Instant Hit Type:"){{width("20%");x("50%");y("20%");}});
                        control(new CheckboxBuilder("specialStatInstantHit"){{x("70%");y("20%");}});
                    }}); 
                    panel(new PanelBuilder(){{
                        childLayoutAbsolute();width(percentage(100));height(pixels(40));
                        control(new LabelBuilder("AU", "Auto Use:"){{width("20%");x("0%");y("20%");}});
                        control(new CheckboxBuilder("specialStatAutoUse"){{x("20%");y("20%");}});
                        control(new LabelBuilder("MT", "Mine Type:"){{width("20%");x("25%");y("20%");}});
                        control(new CheckboxBuilder("specialStatMineType"){{x("45%");y("20%");}});
                        control(new LabelBuilder("PiT", "Pierce Type:"){{width("20%");x("50%");y("20%");}});
                        control(new CheckboxBuilder("specialStatPierceType"){{x("70%");y("20%");}});
                    }}); 
                     panel(new PanelBuilder(){{
                        childLayoutAbsolute();width(percentage(100));height(pixels(40));
                        control(new LabelBuilder("IM", "Immobolize:"){{width("20%");x("0%");y("20%");}});
                        control(new CheckboxBuilder("specialStatImmobolize"){{x("20%");y("20%");}});
                        control(new LabelBuilder("SIM", "Status Immobolize:"){{width("20%");x("25%");y("20%");}});
                        control(new CheckboxBuilder("specialStatStatusImmobolize"){{x("45%");y("20%");}});
                        control(new LabelBuilder("CI", "Cost Increase:"){{width("20%");x("50%");y("20%");}});
                        control(new CheckboxBuilder("specialStatCostIncrease"){{x("70%");y("20%");}});
                    }}); 
                    panel(new PanelBuilder(){{
                        childLayoutAbsolute();width(percentage(100));height(pixels(40));                       
                        control(new LabelBuilder("BT", "Brush Type:"){{width("20%");x("0%");y("20%");}});
                        control(new CheckboxBuilder("specialStatBrushType"){{x("20%");y("20%");}});
                        control(new LabelBuilder("RT", "Reflect Type:"){{width("20%");x("25%");y("20%");}});
                        control(new CheckboxBuilder("specialStatReflectType"){{x("45%");y("20%");}});
                        control(new LabelBuilder("SS", "Shuffle Skills:"){{width("20%");x("50%");y("20%");}});
                        control(new CheckboxBuilder("specialStatShuffleSkills"){{x("70%");y("20%");}});
                    }}); 
                    panel(new PanelBuilder(){{
                        childLayoutAbsolute();width(percentage(100));height(pixels(40));
                        control(new LabelBuilder("ATH", "Absorb To Hp:"){{width("20%");x("0%");y("20%");}});
                        control(new CheckboxBuilder("specialStatAbsorbToHP"){{x("20%");y("20%");}});
                        control(new LabelBuilder("ATM", "Absorb To Mp:"){{width("20%");x("25%");y("20%");}});
                        control(new CheckboxBuilder("specialStatAbsorbToMP"){{x("45%");y("20%");}});
                        control(new LabelBuilder("ATF", "Absorb To Flair:"){{width("20%");x("50%");y("20%");}});
                        control(new CheckboxBuilder("specialStatAbsorbToFlair"){{x("70%");y("20%");}});                        
                    }});                     
                   panel(new PanelBuilder(){{
                        childLayoutAbsolute();width(percentage(100));height(pixels(40));
                        control(new LabelBuilder("SD", "Shelter Defense:"){{width("20%");x("0%");y("20%");}});
                        control(new CheckboxBuilder("specialStatShelterDefense"){{x("20%");y("20%");}});
                        control(new LabelBuilder("BD", "Barrier Defense:"){{width("20%");x("25%");y("20%");}});
                        control(new CheckboxBuilder("specialStatBarrierDefense"){{x("45%");y("20%");}});
                        control(new LabelBuilder("DLO", "DeLockOn:"){{width("20%");x("50%");y("20%");}});
                        control(new CheckboxBuilder("specialStatDeLockOn"){{x("70%");y("20%");}});     

                    }});  
                   panel(new PanelBuilder(){{
                        childLayoutAbsolute();width(percentage(100));height(pixels(40));
                        control(new LabelBuilder("DLOI", "DeLockOnInvisible:"){{width("20%");x("5%");y("20%");}});
                        control(new CheckboxBuilder("specialStatDeLockOnInvisible"){{x("25%");y("20%");}});
                        control(new LabelBuilder("EES", "EraseEnemyStatus:"){{width("20%");x("50%");y("20%");}});
                        control(new CheckboxBuilder("specialStatEraseEnemyStatus"){{x("70%");y("20%");}});
                   }});
                    panel(new PanelBuilder(){{
                        childLayoutAbsolute();width(percentage(100));height(pixels(40));
                        control(new LabelBuilder("EEvS", "EraseEveryoneStatus:"){{width("20%");x("5%");y("20%");}});
                        control(new CheckboxBuilder("specialStatEraseEveryoneStatus"){{x("25%");y("20%");}});
                        control(new LabelBuilder("ESS", "EraseSelfStatus:"){{width("20%");x("50%");y("20%");}});
                        control(new CheckboxBuilder("specialStatEraseSelfStatus"){{x("70%");y("20%");}});                        
                    }}); 
                    panel(new PanelBuilder(){{
                            childLayoutAbsolute();width(percentage(100));height(pixels(40));
                            control(new LabelBuilder("S", "Stat"){{width("20%");x("5%");y("0%");}});
                            control(new LabelBuilder("O", "Opponent"){{width("20%");x("40%");y("0%");}});
                            control(new LabelBuilder("P", "Self"){{width("20%");x("70%");y("0%");}});
                        }}); 
                     panel(new PanelBuilder(){{
                            childLayoutAbsolute();width(percentage(100));height(pixels(40));
                            control(new LabelBuilder("LLA", "Lower Level Amount:"){{width("20%");x("5%");y("5%");}});
                            control(new TextFieldBuilder("specialStatLowerLevelAmount"){{width("20%");x("40%");y("5%");}});
                            control(new TextFieldBuilder("specialStatLowerSelfLevelAmount"){{width("20%");x("70%");y("5%");}});
                     }}); 
                     panel(new PanelBuilder(){{
                            childLayoutAbsolute();width(percentage(100));height(pixels(40));
                            control(new LabelBuilder("ILA", "Increase Level Amount:"){{width("20%");x("5%");y("10%");}});
                            control(new TextFieldBuilder("specialStatIncreaseLevelAmount"){{width("20%");x("40%");y("10%");}});
                            control(new TextFieldBuilder("specialStatIncreaseSelfLevelAmount"){{width("20%");x("70%");y("10%");}});
                     }}); 
                     panel(new PanelBuilder(){{
                            childLayoutAbsolute();width(percentage(100));height(pixels(40));
                            control(new LabelBuilder("DTS", "Draw Towards Slef:"){{width("20%");x("5%");y("15%");}});
                            control(new TextFieldBuilder("specialStatDrawTowardsSelf"){{width("20%");x("40%");y("15%");}});
                            control(new TextFieldBuilder("specialStatDrawSelfTowardsSelf"){{width("20%");x("70%");y("15%");}});
                     }}); 
//                    panel(new PanelBuilder(){{
//                        childLayoutHorizontal();width(percentage(100));height(pixels(40));paddingLeft(percentage(3));                   
//                        control(new LabelBuilder("SpecialStatEraseSkill",    "EraseSkill: None"){{width("30%");}});
//                        control(new SliderBuilder("SpecialStatEraseSkillS", false){{ paddingLeft(percentage(3));min(0); max(6); stepSize(1); buttonStepSize(1);initial(0);}});
//                    }});
                 }});                     
           }});  
        }}.build(nifty, screen, temp);  
    }
    
    public void setCard(CardSetter card){this.card=card;previousCardName=card.getName();}

    @NiftyEventSubscriber(id="back")
    public void onBackButtonClicked(String id, ButtonClickedEvent event){
       nifty.gotoScreen("start"); 
       previousCardName = "";
    }
    
    @NiftyEventSubscriber(id="sync")
    public void onSyncButtonClicked(String id, ButtonClickedEvent event){
        FlairDirector.AdvancedAddCard(previousCardName, card);        
        previousCardName = card.getName();
    }
    
    
    
    
    
    /**
     * CHANGE TABS
     * NIFTY CALL
     * @param i 
     */
    public void section(String i){
        Element a=null,b=null;        
        if(i.equals("1")||i.equals("2")){
            if(i.equals("1")&&!section1Tab){
                a = tL.findElementByName("section1Tab");
                b = tL.findElementByName("section2Tab");
                section1Tab = !section1Tab;
                sect1.show();  
                sect2.hide();  
            }else if(i.equals("2")&&section1Tab){
                b = tL.findElementByName("section1Tab");
                a = tL.findElementByName("section2Tab");
                section1Tab = !section1Tab;
                sect1.hide();  
                sect2.show(); 
            } 
        }else if(i.equals("3")||i.equals("4")){
            if(i.equals("3")&&!animationTab){
                a = tM.findElementByName("animationTab");
                b = tM.findElementByName("statsTab");
                animationTab = !animationTab;
                anim.show();  
                stats.hide();  
            }else if(i.equals("4")&&animationTab){
                b = tM.findElementByName("animationTab");
                a = tM.findElementByName("statsTab");
                animationTab = !animationTab;
                anim.hide();  
                stats.show(); 
            }            
        }else if(i.equals("5")||i.equals("6")){           
            if(i.equals("5")&&!effectTab){
                a = tR.findElementByName("effectsTab");
                b = tR.findElementByName("audioTab");
                effectTab = !effectTab;
                effect.show();   
                audio.hide(); 
            }else if(i.equals("6")&&effectTab){
                b = tR.getParent().findElementByName("effectsTab");
                a = tR.getParent().findElementByName("audioTab");
                effectTab = !effectTab;
                effect.hide();   
                audio.show(); 
            }
        }        
        a.setStyle("nifty-panel-red-no-shadow");
        b.setStyle("nifty-panel-bright");     
        hideAllExtraPanes();
    }
    /*
     * Hide All irrelivent Panels When Switching
     */
    public void hideAllExtraPanes(){emitterShapePane.hide();newEffectsPane.hide();effectsPane.hide();audioPane.hide();statsPane.hide();specialStatsPane.hide();}
    
    
     private void refreshSection1And2(){
         sect1.findNiftyControl("nameT", TextField.class).setText(card.getName());
         sect1.findNiftyControl("MaxUseS", Slider.class).setValue(card.getMaxUses());
         sect2.findNiftyControl("RangeS", Slider.class).setValue(card.getRange().ordinal());
         sect2.findNiftyControl("AngleS", Slider.class).setValue(card.getAngle());
         sect2.findNiftyControl("SeriesS", Slider.class).setValue(card.getSeries().ordinal());
         sect2.findNiftyControl("TraitS", Slider.class).setValue(card.getTrait().ordinal());
         sect2.findNiftyControl("InfoT", TextField.class).setText(card.getDescription());
     }
     //////////////////////Section1///////////////////
     @NiftyEventSubscriber(id="nameT")
     public void onNameFieldChanged(String id, TextFieldChangedEvent event){card.setName(event.getText());title.setText("Editing Card: "+card.getName());}
     @NiftyEventSubscriber(id="MaxUseS")
     public void onMaxUseBarChanged(String id, SliderChangedEvent event){int i = (int)event.getValue();sect1.findNiftyControl("MaxUse", Label.class).setText("Max Uses: "+i+"");card.setMaxUses(i+"");} 
     //////////////////////Section2///////////////////
     @NiftyEventSubscriber(id="RangeS")
     public void onRangeBarChanged(String id, SliderChangedEvent event){card.setRange(Attribute.CardRange.values()[(int)event.getValue()]+"");sect2.findNiftyControl("Range", Label.class).setText("Range: "+card.getRange()); } 
     @NiftyEventSubscriber(id="AngleS")
     public void onAngleBarChanged(String id, SliderChangedEvent event){sect2.findNiftyControl("Angle", Label.class).setText("Angle: "+event.getValue()+"");card.setAngle(event.getValue()+"");} 
      @NiftyEventSubscriber(id="SeriesS")
     public void onSeriesBarChanged(String id, SliderChangedEvent event){card.setSeries(Attribute.CardSeries.values()[(int)event.getValue()]+"");sect2.findNiftyControl("Series", Label.class).setText("Series: "+card.getSeries()); } 
     @NiftyEventSubscriber(id="TraitS")
     public void onTraitBarChanged(String id, SliderChangedEvent event){card.setTrait(Attribute.CardTrait.values()[(int)event.getValue()]+"");sect2.findNiftyControl("Trait", Label.class).setText("Trait: "+card.getTrait()); } 
     @NiftyEventSubscriber(id="InfoT")
     public void onDescriptionFieldChanged(String id, TextFieldChangedEvent event){card.setDescription(event.getText());} 
     /////////////////////////////////Animation & STATS//////////////////////////////////
     @NiftyEventSubscriber(id="animationListBoxL")
    public void onListOfAnimationSelectionChanged(String id, ListBoxSelectionChangedEvent changed){
        card.setAnimName((String)changed.getSelection().get(0));hideAllExtraPanes();
    }
     
     @NiftyEventSubscriber(id="statsListBoxL")
    public void onListOfStatsSelectionChanged(String id, ListBoxSelectionChangedEvent changed){
         hideAllExtraPanes();
        statsListBoxSelection = (String)changed.getSelection().get(0);
        statsPane.show();
        if(!statsListBoxSelection.equals("Special")){
            if(statsListBoxSelection.equals("Translation")){
                statsPane.findNiftyControl("stat1", Label.class).setText("Forward(1) or Backward(-1)");
                statsPane.findNiftyControl("stat2", Label.class).setText("Left(1) or Right(-1)");
                statsPane.findNiftyControl("stat3", Label.class).setText("Speed");
            }else{
                statsPane.findNiftyControl("stat1", Label.class).setText("Amount");
                statsPane.findNiftyControl("stat2", Label.class).setText("Rate");
                if(!statsListBoxSelection.equals("Speed")&&!statsListBoxSelection.equals("Defense")){statsPane.findNiftyControl("stat3", Label.class).setText("Not Used");}
                else{statsPane.findNiftyControl("stat3", Label.class).setText("Duration");}    
            }
            statsPane.show();
            specialStatsPane.hide();
            refreshStatCapsuleFields();
        }else if(statsListBoxSelection.equals("Special")){
            statsPane.hide();
            specialStatsPane.show();
            refreshSpecialStatCapsuleFields();
        }         
    }
     
    private void refreshStatCapsuleFields(){
        if(statsListBoxSelection.equals("HP")){
            statsPane.findNiftyControl("statsParam1T", TextField.class).setText(card.getHPStat().getAmount()+"");
            statsPane.findNiftyControl("statsSelfParam1T", TextField.class).setText(card.getHPStat().getSelfAmount()+"");
            statsPane.findNiftyControl("statsParam2T", TextField.class).setText(card.getHPStat().getRate()+"");
            statsPane.findNiftyControl("statsSelfParam2T", TextField.class).setText(card.getHPStat().getSelfRate()+"");
            statsPane.findNiftyControl("statsParam3T", TextField.class).setText(card.getHPStat().getMisc()+"");
            statsPane.findNiftyControl("statsSelfParam3T", TextField.class).setText(card.getHPStat().getMisc()+"");        
        }else if(statsListBoxSelection.equals("MP")){
            statsPane.findNiftyControl("statsParam1T", TextField.class).setText(card.getMPStat().getAmount()+"");
            statsPane.findNiftyControl("statsSelfParam1T", TextField.class).setText(card.getMPStat().getSelfAmount()+"");
            statsPane.findNiftyControl("statsParam2T", TextField.class).setText(card.getMPStat().getRate()+"");
            statsPane.findNiftyControl("statsSelfParam2T", TextField.class).setText(card.getMPStat().getSelfRate()+"");
            statsPane.findNiftyControl("statsParam3T", TextField.class).setText(card.getMPStat().getMisc()+"");
            statsPane.findNiftyControl("statsSelfParam3T", TextField.class).setText(card.getMPStat().getMisc()+"");    
        }else if(statsListBoxSelection.equals("Knockback")){
            statsPane.findNiftyControl("statsParam1T", TextField.class).setText(card.getKnockbackStat().getAmount()+"");
            statsPane.findNiftyControl("statsSelfParam1T", TextField.class).setText(card.getKnockbackStat().getSelfAmount()+"");
            statsPane.findNiftyControl("statsParam2T", TextField.class).setText(card.getKnockbackStat().getRate()+"");
            statsPane.findNiftyControl("statsSelfParam2T", TextField.class).setText(card.getKnockbackStat().getSelfRate()+"");
            statsPane.findNiftyControl("statsParam3T", TextField.class).setText(card.getKnockbackStat().getMisc()+"");
            statsPane.findNiftyControl("statsSelfParam3T", TextField.class).setText(card.getKnockbackStat().getMisc()+"");    
        }else if(statsListBoxSelection.equals("Speed")){
            statsPane.findNiftyControl("statsParam1T", TextField.class).setText(card.getSpeedStat().getAmount()+"");
            statsPane.findNiftyControl("statsSelfParam1T", TextField.class).setText(card.getSpeedStat().getSelfAmount()+"");
            statsPane.findNiftyControl("statsParam2T", TextField.class).setText(card.getSpeedStat().getRate()+"");
            statsPane.findNiftyControl("statsSelfParam2T", TextField.class).setText(card.getSpeedStat().getSelfRate()+"");
            statsPane.findNiftyControl("statsParam3T", TextField.class).setText(card.getSpeedStat().getMisc()+"");
            statsPane.findNiftyControl("statsSelfParam3T", TextField.class).setText(card.getSpeedStat().getMisc()+"");    
        }else if(statsListBoxSelection.equals("Defense")){
            statsPane.findNiftyControl("statsParam1T", TextField.class).setText(card.getDeffenseStat().getAmount()+"");
            statsPane.findNiftyControl("statsSelfParam1T", TextField.class).setText(card.getDeffenseStat().getSelfAmount()+"");
            statsPane.findNiftyControl("statsParam2T", TextField.class).setText(card.getDeffenseStat().getRate()+"");
            statsPane.findNiftyControl("statsSelfParam2T", TextField.class).setText(card.getDeffenseStat().getSelfRate()+"");
            statsPane.findNiftyControl("statsParam3T", TextField.class).setText(card.getDeffenseStat().getMisc()+"");
            statsPane.findNiftyControl("statsSelfParam3T", TextField.class).setText(card.getDeffenseStat().getMisc()+"");    
        }else if(statsListBoxSelection.equals("Translation")){
            statsPane.findNiftyControl("statsParam1T", TextField.class).setText(card.getTranslationStat().getForward()+"");
            statsPane.findNiftyControl("statsSelfParam1T", TextField.class).setText(card.getTranslationStat().getSelfForward()+"");
            statsPane.findNiftyControl("statsParam2T", TextField.class).setText(card.getTranslationStat().getLeft()+"");
            statsPane.findNiftyControl("statsSelfParam2T", TextField.class).setText(card.getTranslationStat().getSelfLeft()+"");   
            statsPane.findNiftyControl("statsParam3T", TextField.class).setText(card.getTranslationStat().getSpeed()+"");
            statsPane.findNiftyControl("statsSelfParam3T", TextField.class).setText(card.getTranslationStat().getSelfSpeed()+"");  
        }
    }
    private void refreshSpecialStatCapsuleFields(){
        specialStatsPane.findNiftyControl("specialStatProjectile", CheckBox.class).setChecked(card.getSpecialStat().isProjectile());
        specialStatsPane.findNiftyControl("specialStatCrawler", CheckBox.class).setChecked(card.getSpecialStat().isCrawler());
        specialStatsPane.findNiftyControl("specialStatInstantHit", CheckBox.class).setChecked(card.getSpecialStat().isInstantHit());       
                
        specialStatsPane.findNiftyControl("specialStatAutoUse", CheckBox.class).setChecked(card.getSpecialStat().isAutoUse());
        specialStatsPane.findNiftyControl("specialStatMineType", CheckBox.class).setChecked(card.getSpecialStat().isMine());
        specialStatsPane.findNiftyControl("specialStatPierceType", CheckBox.class).setChecked(card.getSpecialStat().isPierce());
        
        specialStatsPane.findNiftyControl("specialStatImmobolize", CheckBox.class).setChecked(card.getSpecialStat().isImmobolize());
        specialStatsPane.findNiftyControl("specialStatStatusImmobolize", CheckBox.class).setChecked(card.getSpecialStat().isStatusImmobolize());
        specialStatsPane.findNiftyControl("specialStatCostIncrease", CheckBox.class).setChecked(card.getSpecialStat().isCostIncrease());

        specialStatsPane.findNiftyControl("specialStatBrushType", CheckBox.class).setChecked(card.getSpecialStat().isBrush());
        specialStatsPane.findNiftyControl("specialStatReflectType", CheckBox.class).setChecked(card.getSpecialStat().isReflect());
        specialStatsPane.findNiftyControl("specialStatShuffleSkills", CheckBox.class).setChecked(card.getSpecialStat().isShuffleSkills());
        
        specialStatsPane.findNiftyControl("specialStatAbsorbToHP", CheckBox.class).setChecked(card.getSpecialStat().isAbsorbToHp());
        specialStatsPane.findNiftyControl("specialStatAbsorbToMP", CheckBox.class).setChecked(card.getSpecialStat().isAbsorbToMp());
        specialStatsPane.findNiftyControl("specialStatAbsorbToFlair", CheckBox.class).setChecked(card.getSpecialStat().isAbsorbToFlair());
        
        specialStatsPane.findNiftyControl("specialStatShelterDefense", CheckBox.class).setChecked(card.getSpecialStat().isShelterDefenstType());
        specialStatsPane.findNiftyControl("specialStatBarrierDefense", CheckBox.class).setChecked(card.getSpecialStat().isBarrierDefenseType());
        specialStatsPane.findNiftyControl("specialStatDeLockOn", CheckBox.class).setChecked(card.getSpecialStat().isDeLockOn());
        specialStatsPane.findNiftyControl("specialStatDeLockOnInvisible", CheckBox.class).setChecked(card.getSpecialStat().isDeLockOnInvisible());

        specialStatsPane.findNiftyControl("specialStatEraseEnemyStatus", CheckBox.class).setChecked(card.getSpecialStat().isEraseEnemyStatus());
        specialStatsPane.findNiftyControl("specialStatEraseEveryoneStatus", CheckBox.class).setChecked(card.getSpecialStat().isEraseEveryOneStatus());
        specialStatsPane.findNiftyControl("specialStatEraseSelfStatus", CheckBox.class).setChecked(card.getSpecialStat().isEraseSelfStatus());            
        
        specialStatsPane.findNiftyControl("specialStatLowerLevelAmount", TextField.class).setText(card.getSpecialStat().getLowerLevelAmount()+"");
        specialStatsPane.findNiftyControl("specialStatLowerSelfLevelAmount", TextField.class).setText(card.getSpecialStat().getSelfLowerLevelAmount()+"");
        
        specialStatsPane.findNiftyControl("specialStatIncreaseLevelAmount", TextField.class).setText(card.getSpecialStat().getIncreaseLevelAmount()+"");
        specialStatsPane.findNiftyControl("specialStatIncreaseSelfLevelAmount", TextField.class).setText(card.getSpecialStat().getSelfIncreaseLevelAmount()+"");
        
        specialStatsPane.findNiftyControl("specialStatDrawTowardsSelf", TextField.class).setText(card.getSpecialStat().getDrawTowardsSelfAmount()+"");
        specialStatsPane.findNiftyControl("specialStatDrawSelfTowardsSelf", TextField.class).setText(card.getSpecialStat().getSelfDrawTowardsSelfAmount()+""); 
        
        //specialStatsPane.findNiftyControl("SpecialStatEraseSkillS", Slider.class).setValue(card.getSpecialStat().getEraseSkill());        
    } 
     
     @NiftyEventSubscriber(id="statsParam1T")
     public void onStat1ParamFieldChanged(String id, TextFieldChangedEvent event){    
        if(!vectorField){
            vectorField=true;
            if(statsListBoxSelection.equals("HP")){
                try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),-1500,1500);card.getHPStat().setAmount(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getHPStat().getAmount()+"");} 
            }else if(statsListBoxSelection.equals("MP")){
                try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),-1500,1500);card.getMPStat().setAmount(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getMPStat().getAmount()+"");} 
            }else if(statsListBoxSelection.equals("Knockback")){
                try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),-1500,1500);card.getKnockbackStat().setAmount(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getKnockbackStat().getAmount()+"");} 
            }else if(statsListBoxSelection.equals("Speed")){
                try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),-1500,1500);card.getSpeedStat().setAmount(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getSpeedStat().getAmount()+"");} 
            }else if(statsListBoxSelection.equals("Defense")){
                try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),-1500,1500);card.getDeffenseStat().setAmount(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getDeffenseStat().getAmount()+"");} 
            }else if(statsListBoxSelection.equals("Translation")){
               try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),-1,1);card.getTranslationStat().setForward(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getTranslationStat().getForward()+"");}  
            }
            vectorField=false;
        }
    } 
     @NiftyEventSubscriber(id="statsSelfParam1T")
     public void onSelfStat1ParamFieldChanged(String id, TextFieldChangedEvent event){ 
        if(!vectorField){
            vectorField=true;
            if(statsListBoxSelection.equals("HP")){
                try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),-1500,1500);card.getHPStat().setSelfAmount(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getHPStat().getSelfAmount()+"");} 
            }else if(statsListBoxSelection.equals("MP")){
                try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),-1500,1500);card.getMPStat().setSelfAmount(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getMPStat().getSelfAmount()+"");} 
            }else if(statsListBoxSelection.equals("Knockback")){
                try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),-1500,1500);card.getKnockbackStat().setSelfAmount(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getKnockbackStat().getSelfAmount()+"");} 
            }else if(statsListBoxSelection.equals("Speed")){
                try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),-1500,1500);card.getSpeedStat().setSelfAmount(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getSpeedStat().getSelfAmount()+"");} 
            }else if(statsListBoxSelection.equals("Defense")){
                try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),-1500,1500);card.getDeffenseStat().setSelfAmount(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getDeffenseStat().getSelfAmount()+"");} 
            }else if(statsListBoxSelection.equals("Translation")){
               try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),-1,1);card.getTranslationStat().setSelfForward(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getTranslationStat().getSelfForward()+"");}  
            }
            vectorField=false;
        }
    } 
     @NiftyEventSubscriber(id="statsParam2T")
     public void onStat2ParamFieldChanged(String id, TextFieldChangedEvent event){         
        if(!vectorField){
            vectorField=true;
             if(statsListBoxSelection.equals("HP")){
                try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),-1500,1500);card.getHPStat().setRate(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getHPStat().getRate()+"");} 
            }else if(statsListBoxSelection.equals("MP")){
                try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),-1500,1500);card.getMPStat().setRate(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getMPStat().getRate()+"");} 
            }else if(statsListBoxSelection.equals("Knockback")){
                try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),-1500,1500);card.getKnockbackStat().setRate(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getKnockbackStat().getRate()+"");} 
            }else if(statsListBoxSelection.equals("Speed")){
                try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),-1500,1500);card.getSpeedStat().setRate(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getSpeedStat().getRate()+"");} 
            }else if(statsListBoxSelection.equals("Defense")){
                try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),-1500,1500);card.getDeffenseStat().setRate(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getDeffenseStat().getRate()+"");} 
            }else if(statsListBoxSelection.equals("Translation")){
               try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),-1,1);card.getTranslationStat().setLeft(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getTranslationStat().getLeft()+"");}  
            }
            vectorField=false;
        }
    }   
     @NiftyEventSubscriber(id="statsSelfParam2T")
     public void onSelfStat2ParamFieldChanged(String id, TextFieldChangedEvent event){         
     if(!vectorField){
         vectorField=true;
             if(statsListBoxSelection.equals("HP")){
                try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),-1500,1500);card.getHPStat().setSelfRate(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getHPStat().getSelfRate()+"");} 
            }else if(statsListBoxSelection.equals("MP")){
                try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),-1500,1500);card.getMPStat().setSelfRate(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getMPStat().getSelfRate()+"");} 
            }else if(statsListBoxSelection.equals("Knockback")){
                try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),-1500,1500);card.getKnockbackStat().setSelfRate(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getKnockbackStat().getSelfRate()+"");} 
            }else if(statsListBoxSelection.equals("Speed")){
                try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),-1500,1500);card.getSpeedStat().setSelfRate(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getSpeedStat().getSelfRate()+"");} 
            }else if(statsListBoxSelection.equals("Defense")){
                try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),-1500,1500);card.getDeffenseStat().setSelfRate(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getDeffenseStat().getSelfRate()+"");} 
            }else if(statsListBoxSelection.equals("Translation")){
               try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),-1,1);card.getTranslationStat().setSelfLeft(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getTranslationStat().getSelfLeft()+"");}  
            }
            vectorField=false;
        }
    } 
     @NiftyEventSubscriber(id="statsParam3T")
     public void onStat3ParamFieldChanged(String id, TextFieldChangedEvent event){         
        if(!vectorField){
            vectorField=true;
             if(statsListBoxSelection.equals("HP")){
                try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),-1500,1500);card.getHPStat().setMisc(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getHPStat().getMisc()+"");} 
            }else if(statsListBoxSelection.equals("MP")){
                try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),-1500,1500);card.getMPStat().setMisc(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getMPStat().getMisc()+"");} 
            }else if(statsListBoxSelection.equals("Knockback")){
                try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),-1500,1500);card.getKnockbackStat().setMisc(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getKnockbackStat().getMisc()+"");} 
            }else if(statsListBoxSelection.equals("Speed")){
                try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),-1500,1500);card.getSpeedStat().setRate(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getSpeedStat().getMisc()+"");} 
            }else if(statsListBoxSelection.equals("Defense")){
                try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),-1500,1500);card.getDeffenseStat().setRate(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getDeffenseStat().getMisc()+"");} 
            }else if(statsListBoxSelection.equals("Translation")){
               try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),0,10);card.getTranslationStat().setSpeed(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getTranslationStat().getSpeed()+"");}  
            }
            vectorField=false;
        }
    }     
     @NiftyEventSubscriber(id="statsSelfParam3T")
     public void onSelfStat3ParamFieldChanged(String id, TextFieldChangedEvent event){         
        if(!vectorField){
            vectorField=true;
             if(statsListBoxSelection.equals("HP")){
                try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),-1500,1500);card.getHPStat().setSelfMisc(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getHPStat().getSelfMisc()+"");} 
            }else if(statsListBoxSelection.equals("MP")){
                try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),-1500,1500);card.getMPStat().setSelfMisc(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getMPStat().getSelfMisc()+"");} 
            }else if(statsListBoxSelection.equals("Knockback")){
                try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),-1500,1500);card.getKnockbackStat().setSelfMisc(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getKnockbackStat().getSelfMisc()+"");} 
            }else if(statsListBoxSelection.equals("Speed")){
                try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),-1500,1500);card.getSpeedStat().setSelfMisc(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getSpeedStat().getSelfMisc()+"");} 
            }else if(statsListBoxSelection.equals("Defense")){
                try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),-1500,1500);card.getDeffenseStat().setSelfMisc(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getDeffenseStat().getSelfMisc()+"");} 
            }else if(statsListBoxSelection.equals("Translation")){
               try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),0,10);card.getTranslationStat().setSelfSpeed(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getTranslationStat().getSelfSpeed()+"");}  
            }
            vectorField=false;
        }
    }   
     @NiftyEventSubscriber(id="specialStatProjectile")
    public void onProjectileTypeCheckBoxClicked(String id, CheckBoxStateChangedEvent event){
        card.getSpecialStat().setProjectile(event.isChecked());
        if(event.isChecked()){
            screen.findNiftyControl("specialStatCrawler", CheckBox.class).uncheck();
            screen.findNiftyControl("specialStatInstantHit", CheckBox.class).uncheck();
        }        
    } 
     @NiftyEventSubscriber(id="specialStatCrawler")
    public void onCrawlerTypeCheckBoxClicked(String id, CheckBoxStateChangedEvent event){
        card.getSpecialStat().setCrawler(event.isChecked());
        if(event.isChecked()){
            screen.findNiftyControl("specialStatProjectile", CheckBox.class).uncheck();
            screen.findNiftyControl("specialStatInstantHit", CheckBox.class).uncheck();
        }        
    } 
     @NiftyEventSubscriber(id="specialStatInstantHit")
    public void onInstantHitCheckBoxClicked(String id, CheckBoxStateChangedEvent event){
        card.getSpecialStat().setInstantHit(event.isChecked());
        if(event.isChecked()){
            screen.findNiftyControl("specialStatProjectile", CheckBox.class).uncheck();
            screen.findNiftyControl("specialStatCrawler", CheckBox.class).uncheck();
        }        
    } 
    @NiftyEventSubscriber(id="specialStatAutoUse")
    public void onAutoUseCheckBoxClicked(String id, CheckBoxStateChangedEvent event){
        card.getSpecialStat().setAutoUse(event.isChecked());
    }
    @NiftyEventSubscriber(id="specialStatMineType")
    public void onMineTypeCheckBoxClicked(String id, CheckBoxStateChangedEvent event){
       card.getSpecialStat().setMine(event.isChecked());
    }
    @NiftyEventSubscriber(id="specialStatPierceType")
    public void onPierceTypeCheckBoxClicked(String id, CheckBoxStateChangedEvent event){
        card.getSpecialStat().setPierce(event.isChecked());
    }
    @NiftyEventSubscriber(id="specialStatImmobolize")
    public void onImmobolizeBoxClicked(String id, CheckBoxStateChangedEvent event){
        card.getSpecialStat().setImmobolize(event.isChecked());
    }
    @NiftyEventSubscriber(id="specialStatStatusImmobolize")
    public void onStatusImmobolizeCheckBoxClicked(String id, CheckBoxStateChangedEvent event){
       card.getSpecialStat().setStatusImmobolize(event.isChecked());
    }
    @NiftyEventSubscriber(id="specialStatCostIncrease")
    public void onCostIncreaseCheckBoxClicked(String id, CheckBoxStateChangedEvent event){
        card.getSpecialStat().setCostIncrease(event.isChecked());
    }       
    @NiftyEventSubscriber(id="specialStatBrushType")
    public void onBrushTypeCheckBoxClicked(String id, CheckBoxStateChangedEvent event){
        card.getSpecialStat().setBrush(event.isChecked());
        if(event.isChecked()){
            screen.findNiftyControl("specialStatReflectType", CheckBox.class).uncheck();
        }        
    } 
    @NiftyEventSubscriber(id="specialStatReflectType")
    public void onReflectTypeCheckBoxClicked(String id, CheckBoxStateChangedEvent event){
        card.getSpecialStat().setReflect(event.isChecked());
        if(event.isChecked()){
            screen.findNiftyControl("specialStatBrushType", CheckBox.class).uncheck();
        }        
    } 
    @NiftyEventSubscriber(id="specialStatAbsorbToHP")
    public void onAbsorbToHPCheckBoxClicked(String id, CheckBoxStateChangedEvent event){
        card.getSpecialStat().setAbsorbToHp(event.isChecked());
    }
    @NiftyEventSubscriber(id="specialStatAbsorbToMP")
    public void onAbsorbToMpCheckBoxClicked(String id, CheckBoxStateChangedEvent event){
       card.getSpecialStat().setAbsorbToMp(event.isChecked());
    }
    @NiftyEventSubscriber(id="specialStatAbsorbToFlair")
    public void onAbsorbToFlairCheckBoxClicked(String id, CheckBoxStateChangedEvent event){
        card.getSpecialStat().setAbsorbToFlair(event.isChecked());
    }  
    @NiftyEventSubscriber(id="specialStatShelterDefense")
    public void onShelterDefenseCheckBoxClicked(String id, CheckBoxStateChangedEvent event){
        card.getSpecialStat().setShelterDefense(event.isChecked());
        if(event.isChecked()){
            screen.findNiftyControl("specialStatBarrierDefense", CheckBox.class).uncheck();
        }        
    } 
    @NiftyEventSubscriber(id="specialStatBarrierDefense")
    public void onBarrierDefenseCheckBoxClicked(String id, CheckBoxStateChangedEvent event){
        card.getSpecialStat().setBarrierDefense(event.isChecked());
        if(event.isChecked()){
            screen.findNiftyControl("specialStatShelterDefense", CheckBox.class).uncheck();
        }        
    }     
    @NiftyEventSubscriber(id="specialStatDeLockOn")
    public void onDeLockOnCheckBoxClicked(String id, CheckBoxStateChangedEvent event){
        card.getSpecialStat().setDeLockOn(event.isChecked());
        if(event.isChecked()){
            screen.findNiftyControl("specialStatDeLockOnInvisible", CheckBox.class).uncheck();
        }        
    } 
    @NiftyEventSubscriber(id="specialStatDeLockOnInvisible")
    public void onDeLockOnInvisibleCheckBoxClicked(String id, CheckBoxStateChangedEvent event){
        card.getSpecialStat().setDeLockOnInvisible(event.isChecked());
        if(event.isChecked()){
            screen.findNiftyControl("specialStatDeLockOn", CheckBox.class).uncheck();
        }        
    } 
    @NiftyEventSubscriber(id="specialStatEraseEnemyStatus")
    public void onEraseEnemyStatusCheckBoxClicked(String id, CheckBoxStateChangedEvent event){
        card.getSpecialStat().setEraseEnemyStatus(event.isChecked());
        if(event.isChecked()){
            screen.findNiftyControl("specialStatEraseEveryoneStatus", CheckBox.class).uncheck();
            screen.findNiftyControl("specialStatEraseSelfStatus", CheckBox.class).uncheck();
        }        
    } 
     @NiftyEventSubscriber(id="specialStatEraseEveryoneStatus")
    public void onEraseEveryoneStatusCheckBoxClicked(String id, CheckBoxStateChangedEvent event){
        card.getSpecialStat().setEraseEveryOneStatus(event.isChecked());
        if(event.isChecked()){
            screen.findNiftyControl("specialStatEraseEnemyStatus", CheckBox.class).uncheck();
            screen.findNiftyControl("specialStatEraseSelfStatus", CheckBox.class).uncheck();
        }        
    } 
     @NiftyEventSubscriber(id="specialStatEraseSelfStatus")
    public void onEraseSelfStatusCheckBoxClicked(String id, CheckBoxStateChangedEvent event){
        card.getSpecialStat().setEraseSelfStatus(event.isChecked());
        if(event.isChecked()){
            screen.findNiftyControl("specialStatEraseEnemyStatus", CheckBox.class).uncheck();
            screen.findNiftyControl("specialStatEraseEveryoneStatus", CheckBox.class).uncheck();
        }        
    } 
    
    
     @NiftyEventSubscriber(id="specialStatLowerLevelAmount")
     public void onLowerLevelAmountFieldChanged(String id, TextFieldChangedEvent event){         
     if(!vectorField){
         vectorField=true;             
               try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),0,10000);card.getSpecialStat().setLowerLevelAmount(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getSpecialStat().getLowerLevelAmount()+"");}              
            vectorField=false;
        }
    } 
     @NiftyEventSubscriber(id="specialStatLowerSelfLevelAmount")
     public void onLowerSelfLevelAmountFieldChanged(String id, TextFieldChangedEvent event){         
     if(!vectorField){
         vectorField=true;             
               try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),0,10000);card.getSpecialStat().setSelfLowerLevelAmount(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getSpecialStat().getSelfLowerLevelAmount()+"");}              
            vectorField=false;
        }
    } 
     @NiftyEventSubscriber(id="specialStatIncreaseLevelAmount")
     public void onIncreaseLevelAmountFieldChanged(String id, TextFieldChangedEvent event){         
     if(!vectorField){
         vectorField=true;             
               try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),0,10000);card.getSpecialStat().setIncreaseLevelAmount(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getSpecialStat().getIncreaseLevelAmount()+"");}              
            vectorField=false;
        }
    } 
    @NiftyEventSubscriber(id="specialStatIncreaseSelfLevelAmount")
     public void onIncreaseSelfLevelAmountFieldChanged(String id, TextFieldChangedEvent event){         
     if(!vectorField){
         vectorField=true;             
               try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),0,10000);card.getSpecialStat().setSelfIncreaseLevelAmount(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getSpecialStat().getSelfIncreaseLevelAmount()+"");}              
            vectorField=false;
        }
    } 
     @NiftyEventSubscriber(id="specialStatDrawTowardsSelf")
     public void onDrawTowardsSelfFieldChanged(String id, TextFieldChangedEvent event){         
     if(!vectorField){
         vectorField=true;             
               try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),0,10000);card.getSpecialStat().setDrawTowardsSelfAmount(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getSpecialStat().getDrawTowardsSelfAmount()+"");}              
            vectorField=false;
        }
    } 
     @NiftyEventSubscriber(id="specialStatDrawSelfTowardsSelf")
     public void onDrawSelfTowardsSelfFieldChanged(String id, TextFieldChangedEvent event){         
     if(!vectorField){
         vectorField=true;             
               try{int f = (int)FastMath.clamp(Integer.parseInt(event.getText()),0,10000);card.getSpecialStat().setSelfDrawTowardsSelfAmount(f);event.getTextFieldControl().setText(f+"");
                }catch(NumberFormatException e){event.getTextFieldControl().setText(card.getSpecialStat().getSelfDrawTowardsSelfAmount()+"");}              
            vectorField=false;
        }
    } 
         
     //////////////////////Audio & Effects///////////////////
    private void refreshEffectList(){
        ListBox<ParticleEmitter> ef = effect.findNiftyControl("effectsListBoxL", ListBox.class);
        if(ef.getItems().size()>0)ef.clear(); 
        ef.addAllItems(card.getEffects());
        ef.selectItemByIndex(0);               
    }
    private void refreshAudioList(){
        ListBox<AudioNode> au = audio.findNiftyControl("audioListBoxL", ListBox.class);
        if(au.getItems().size()>0)au.clear();
        au.addAllItems(card.getSoundEffects());
        au.selectItemByIndex(0);               
    }
    @NiftyEventSubscriber(id="effectsListBoxL")
    public void onListOfEffectsSelectionChanged(String id, ListBoxSelectionChangedEvent changed){
        hideAllExtraPanes();
        if(changed.getSelection().size()>0){
            selectedEffect=(ParticleEmitter)changed.getSelection().get(0);
            effectsPane.show();
        }else{
            effectsPane.hide();
        }
        newEffectsPane.hide();
        effectsPane.findNiftyControl("effectNameT", TextField.class).setText(selectedEffect.getName());
        //START COLOR
        ColorRGBA tC = selectedEffect.getStartColor();
        effectsPane.findNiftyControl("StartColorT", TextField.class).setText(tC.getRed()*255+" , "+tC.getGreen()*255+" , "+tC.getBlue()*255+" , "+tC.getAlpha()*255);
        //End Color
        tC = selectedEffect.getEndColor();
        effectsPane.findNiftyControl("EndColorT", TextField.class).setText(tC.getRed()*255+" , "+tC.getGreen()*255+" , "+tC.getBlue()*255+" , "+tC.getAlpha()*255);
        effectsPane.findNiftyControl("StartSizeT",TextField.class).setText(selectedEffect.getStartSize()+"");
        effectsPane.findNiftyControl("EndSizeT",TextField.class).setText(selectedEffect.getEndSize()+"");
        effectsPane.findNiftyControl("HighLifeT", TextField.class).setText(selectedEffect.getHighLife()+"");
        effectsPane.findNiftyControl("LowLifeT", TextField.class).setText(selectedEffect.getLowLife()+"");
        effectsPane.findNiftyControl("RotateSpeedT", TextField.class).setText(selectedEffect.getRotateSpeed()+"");
        effectsPane.findNiftyControl("RandomImageSelectC", CheckBox.class).setChecked(selectedEffect.isSelectRandomImage());
        effectsPane.findNiftyControl("ImagesXT", TextField.class).setText(selectedEffect.getImagesX()+"");
        effectsPane.findNiftyControl("ImagesYT", TextField.class).setText(selectedEffect.getImagesY()+"");
        effectsPane.findNiftyControl("NormalT", TextField.class).setText("0,0,0");//value is nan so need to initialize
        effectsPane.findNiftyControl("FacingVelocityC", CheckBox.class).setChecked(selectedEffect.isFacingVelocity());
        effectsPane.findNiftyControl("GravityT", TextField.class).setText(selectedEffect.getGravity()+"");
        effectsPane.findNiftyControl("NumberOfParticlesT", TextField.class).setText(selectedEffect.getParticles().length+"");
        effectsPane.findNiftyControl("ParticlesPerSecondT", TextField.class).setText(selectedEffect.getParticlesPerSec()+"");
        effectsPane.findNiftyControl("RandomAngleC", CheckBox.class).setChecked(selectedEffect.isRandomAngle());
        Vector3f tV = selectedEffect.getInitialVelocity();
        effectsPane.findNiftyControl("InitialVelocityT", TextField.class).setText(tV.getX()+","+tV.getY()+","+tV.getZ());
        effectsPane.findNiftyControl("VelocityVariationT", TextField.class).setText(selectedEffect.getVelocityVariation()+"");
        if(selectedEffect.getUserData("bone")==null)
            effectsPane.findNiftyControl("BoneListD",DropDown.class).selectItem("head"); 
        else effectsPane.findNiftyControl("BoneListD",DropDown.class).selectItem(selectedEffect.getUserData("bone")); 
    }
    
    @NiftyEventSubscriber(id="audioListBoxL")
    public void onListOfAudioSelectionChanged(String id, ListBoxSelectionChangedEvent changed){
        hideAllExtraPanes();
        if(changed.getSelection().size()>0){
            selectedAudio=(AudioNode)changed.getSelection().get(0);
            audioPane.show();
        }else{
            audioPane.hide();
        } 
        audioPane.findNiftyControl("audioLocationLabel", Label.class).setText("Location: "+selectedAudio.getName());
        audioPane.findNiftyControl("VolumeS", Slider.class).setValue(selectedAudio.getVolume());
        audioPane.findNiftyControl("PitchS", Slider.class).setValue(selectedAudio.getPitch());
        audioPane.findNiftyControl("TimeOffsetS", Slider.class).setMax(selectedAudio.getAudioData().getDuration());
        audioPane.findNiftyControl("TimeOffsetS", Slider.class).setValue(selectedAudio.getTimeOffset());
    }
    @NiftyEventSubscriber(id="addEffectAudio")
    public void onAddEffectAudioButtonClicked(String id, ButtonClickedEvent event){
        hideAllExtraPanes();
        if(effectTab){ 
            effectParam= new Object[3];
            newEffectsPane.show();
            newEffectsPane.findNiftyControl("EffectNameT",TextField.class).setText("new Effect");  
            newEffectsPane.findNiftyControl("EffectTypeS",Slider.class).setValue(0);  
            effectParam[1]=com.jme3.effect.ParticleMesh.Type.Point;//fix a problem since sometimes slider doesn't change since the value may be previously selected
            newEffectsPane.findNiftyControl("EffectNumParticlesT",TextField.class).setText("32");
        }else{
            String loc;
            try{loc = fileExplorer("SoundEffects");}catch(Exception e){return;}            
            SimpleApplication app=FlairDirector.getApp();
            selectedAudio = new AudioNode(app.getAudioRenderer(),app.getAssetManager(),"/Deck/SoundEffects/"+loc){@Override public String toString(){return "Audio File: "+name;}};
            selectedAudio.setName(loc);
            selectedAudio.play();
            card.addSoundEffect(selectedAudio);
            refreshAudioList();  
            ListBox<ParticleEmitter> ef = effect.findNiftyControl("audioListBoxL", ListBox.class);
            ef.selectItem(ef.getItems().get(ef.getItems().size()-1));
        }
    }
    @NiftyEventSubscriber(id="deleteEffectAudio")
    public void onDeleteEffectAudidoButtonClicked(String id, ButtonClickedEvent event){
        if(effectTab){
            card.getEffects().remove(selectedEffect);
            refreshEffectList();
        }else{
            card.getSoundEffects().remove(selectedAudio);
            refreshAudioList();            
        }
    }
    ////////////////////////AUDIOATTR///////////////////////
    @NiftyEventSubscriber(id="playAudioB")
    public void onPlayAudidoClicked(String id, ButtonClickedEvent event){selectedAudio.play();}
    @NiftyEventSubscriber(id="VolumeS")
    public void onVolumeBarChanged(String id, SliderChangedEvent event){
        selectedAudio.setVolume(event.getValue());
        audioPane.findNiftyControl("Volume", Label.class).setText("Volume: "+selectedAudio.getVolume());
    }
    @NiftyEventSubscriber(id="PitchS")
    public void onPitchBarChanged(String id, SliderChangedEvent event){
        selectedAudio.setPitch(event.getValue());
        audioPane.findNiftyControl("Pitch", Label.class).setText("Pitch: "+selectedAudio.getPitch());
    }    
    @NiftyEventSubscriber(id="TimeOffsetS")
    public void onTimeOffsetBarChanged(String id, SliderChangedEvent event){
        selectedAudio.setTimeOffset(event.getValue());
        audioPane.findNiftyControl("TimeOffset", Label.class).setText("Time Offset: "+selectedAudio.getTimeOffset());
    }
    ////////////////////////EFFECTSATTR///////////////////////
    @NiftyEventSubscriber(id="EmitterShapeB")
    public void onEmitterShapeButtonClicked(String id, ButtonClickedEvent event){   
           emitterShapePane.show();
           emitterShapePane.findNiftyControl("EmitterShapeD", DropDown.class).selectItemByIndex(1);        
    }
    //SECTION FOR EMITTERSHAPE PARAMS
    //RESUME
     @NiftyEventSubscriber(id="effectNameT")
     public void oneffectNameFieldChanged(String id, TextFieldChangedEvent event){selectedEffect.setName(event.getText());} 
    @NiftyEventSubscriber(id="StartColorT")
    public void onStartColorChanged(String id, TextFieldChangedEvent event){
        if(!vectorField)
            try{
                vectorField=true;
                String[] s = event.getText().split(",");
                float r = FastMath.clamp(Float.parseFloat(s[0]),0,255);
                float g = FastMath.clamp(Float.parseFloat(s[1]),0,255);
                float b = FastMath.clamp(Float.parseFloat(s[2]),0,255);
                float a = FastMath.clamp(Float.parseFloat(s[3]),0,255);
                effectsPane.findElementByName("StartColorP").getRenderer(PanelRenderer.class).setBackgroundColor(new Color(r/255,g/255,b/255,1));
                selectedEffect.setStartColor(new ColorRGBA(r/255,g/255,b/255,a/255));                          
                event.getTextFieldControl().setText(r+","+g+","+b+","+a);
                vectorField=false;
            }catch(Exception e){
                ColorRGBA tC = selectedEffect.getStartColor();
                event.getTextFieldControl().setText(tC.getRed()*255+","+tC.getGreen()*255+","+tC.getBlue()*255+","+tC.getAlpha()*255);
                vectorField=false;
            } 
    }
     @NiftyEventSubscriber(id="EndColorT")
    public void onEndColorChanged(String id, TextFieldChangedEvent event){        
        if(!vectorField)
            try{ 
                vectorField=true;
                String[] s = event.getText().split(",");
                float r = FastMath.clamp(Float.parseFloat(s[0]),0,255);
                float g = FastMath.clamp(Float.parseFloat(s[1]),0,255);
                float b = FastMath.clamp(Float.parseFloat(s[2]),0,255);
                float a = FastMath.clamp(Float.parseFloat(s[3]),0,255);
                effectsPane.findElementByName("EndColorP").getRenderer(PanelRenderer.class).setBackgroundColor(new Color(r/255,g/255,b/255,1));
                selectedEffect.setEndColor(new ColorRGBA(r/255,g/255,b/255,a/255));  
                event.getTextFieldControl().setText(r+","+g+","+b+","+a);
                vectorField=false;
            }catch(Exception e){
                ColorRGBA tC = selectedEffect.getEndColor();
                event.getTextFieldControl().setText(tC.getRed()*255+","+tC.getGreen()*255+","+tC.getBlue()*255+","+tC.getAlpha()*255);
                vectorField=false;
            }
    }
    @NiftyEventSubscriber(id="StartSizeT")
     public void onStartSizeFieldChanged(String id, TextFieldChangedEvent event){
        try{float f = Float.parseFloat(event.getText());selectedEffect.setStartSize(f);
        }catch(NumberFormatException e){event.getTextFieldControl().setText(selectedEffect.getStartSize()+"");}
    } 
    @NiftyEventSubscriber(id="EndSizeT")
     public void onEndSizeFieldChanged(String id, TextFieldChangedEvent event){
        try{float f = Float.parseFloat(event.getText());selectedEffect.setEndSize(f);
        }catch(NumberFormatException e){event.getTextFieldControl().setText(selectedEffect.getEndSize()+"");}
    } 
    @NiftyEventSubscriber(id="HighLifeT")
     public void onHighLifeFieldChanged(String id, TextFieldChangedEvent event){
        try{float f = Float.parseFloat(event.getText());selectedEffect.setHighLife(f);
        }catch(NumberFormatException e){event.getTextFieldControl().setText(selectedEffect.getHighLife()+"");}
    } 
    @NiftyEventSubscriber(id="LowLifeT")
     public void onLowLifeFieldChanged(String id, TextFieldChangedEvent event){
        try{float f = Float.parseFloat(event.getText());selectedEffect.setLowLife(f);
        }catch(NumberFormatException e){event.getTextFieldControl().setText(selectedEffect.getLowLife()+"");} 
    } 
    @NiftyEventSubscriber(id="RotateSpeedT") 
     public void onRotateSpeedFieldChanged(String id, TextFieldChangedEvent event){
        try{float f = Float.parseFloat(event.getText());selectedEffect.setRotateSpeed(f);
        }catch(NumberFormatException e){event.getTextFieldControl().setText(selectedEffect.getRotateSpeed()+"");} 
    } 
    @NiftyEventSubscriber(id="ImageB")
    public void onChangeImageClicked(String id, ButtonClickedEvent event){
        String temp;
        try{temp = fileExplorer("Effects");}catch(Exception e){return;}        
        String loc = "/Deck/Effects/"+temp;
        selectedEffect.setUserData("image",loc);         
        Material mat = new Material(FlairDirector.getApp().getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
                 mat.setTexture("Texture", FlairDirector.getApp().getAssetManager().loadTexture(loc));                            
                 mat.setBoolean("PointSprite", selectedEffect.getMesh() instanceof ParticlePointMesh);
        selectedEffect.setMaterial(mat);
        effectsPane.findElementByName("ImageI").getRenderer(ImageRenderer.class).setImage(nifty.createImage(loc, false));
    }
    @NiftyEventSubscriber(id="RandomImageSelectC")
    public void onRandomImageSelectCheckBoxClicked(String id, CheckBoxStateChangedEvent event){
        selectedEffect.setSelectRandomImage(event.isChecked());
    }
    @NiftyEventSubscriber(id="ImagesXT")
     public void onImagesXFieldChanged(String id, TextFieldChangedEvent event){
        try{int f = Integer.parseInt(event.getText());selectedEffect.setImagesX(f);
        }catch(NumberFormatException e){event.getTextFieldControl().setText(selectedEffect.getImagesX()+"");} 
    } 
    @NiftyEventSubscriber(id="ImagesYT")
     public void onImagesYFieldChanged(String id, TextFieldChangedEvent event){
        try{int f = Integer.parseInt(event.getText());selectedEffect.setImagesY(f);
        }catch(NumberFormatException e){event.getTextFieldControl().setText(selectedEffect.getImagesY()+"");} 
    } 
    @NiftyEventSubscriber(id="NormalT")
     public void onNormalFieldChanged(String id, TextFieldChangedEvent event){
        if(!vectorField)
            try{
                vectorField=true;
                String[] f = event.getText().split(",");
                float x = Float.parseFloat(f[0]);
                float y = Float.parseFloat(f[1]);
                float z = Float.parseFloat(f[2]);
                selectedEffect.setFaceNormal(new Vector3f(x,y,z));
                event.getTextFieldControl().setText(x+","+y+","+z);                
                vectorField=false;
            }catch(NumberFormatException e){
                Vector3f tV = selectedEffect.getFaceNormal();
                event.getTextFieldControl().setText(tV.getX()+" , "+tV.getY()+" , "+tV.getZ());
                vectorField=false;
            } 
    } 
    @NiftyEventSubscriber(id="FacingVelocityC")
    public void onFacingVelocityCheckBoxClicked(String id, CheckBoxStateChangedEvent event){
        selectedEffect.setFacingVelocity(event.isChecked());
    } 
    @NiftyEventSubscriber(id="GravityT")
     public void onGravityFieldChanged(String id, TextFieldChangedEvent event){
       if(!vectorField)
            try{
                vectorField=true;
                String[] f = event.getText().split(",");
                float x = Float.parseFloat(f[0]);
                float y = Float.parseFloat(f[1]);
                float z = Float.parseFloat(f[2]);
                selectedEffect.setGravity(new Vector3f(x,y,z));
                event.getTextFieldControl().setText(x+","+y+","+z);                
                vectorField=false;
            }catch(NumberFormatException e){
                Vector3f tV = selectedEffect.getGravity();
                event.getTextFieldControl().setText(tV.getX()+" , "+tV.getY()+" , "+tV.getZ());
                vectorField=false;
            } 
    } 
    @NiftyEventSubscriber(id="NumberOfParticlesT")
     public void onNumberOfParticlesFieldChanged(String id, TextFieldChangedEvent event){
        try{int f = Integer.parseInt(event.getText());selectedEffect.setNumParticles(f);
        }catch(NumberFormatException e){event.getTextFieldControl().setText(selectedEffect.getParticles().length+"");} 
    } 
    @NiftyEventSubscriber(id="ParticlesPerSecondT")
     public void onParticlesPerSecondFieldChanged(String id, TextFieldChangedEvent event){
        try{float f = Float.parseFloat(event.getText());selectedEffect.setParticlesPerSec(f);
        }catch(NumberFormatException e){event.getTextFieldControl().setText(selectedEffect.getParticlesPerSec()+"");} 
    } 
    @NiftyEventSubscriber(id="RandomAngleC")
    public void onRandomAngleHBarChanged(String id, CheckBoxStateChangedEvent event){
        selectedEffect.setRandomAngle(event.isChecked());
    }
     @NiftyEventSubscriber(id="InitialVelocityT")
     public void onInitialVelocityFieldChanged(String id, TextFieldChangedEvent event){
        if(!vectorField)
            try{
                vectorField=true;
                String[] f = event.getText().split(",");
                float x = Float.parseFloat(f[0]);
                float y = Float.parseFloat(f[1]);
                float z = Float.parseFloat(f[2]);
                selectedEffect.setInitialVelocity(new Vector3f(x,y,z));
                event.getTextFieldControl().setText(x+","+y+","+z);                
                vectorField=false;
            }catch(NumberFormatException e){
                Vector3f tV = selectedEffect.getInitialVelocity();
                event.getTextFieldControl().setText(tV.getX()+" , "+tV.getY()+" , "+tV.getZ());
                vectorField=false;
            } 
    } 
    @NiftyEventSubscriber(id="VelocityVariationT")
     public void onVelocityVariationFieldChanged(String id, TextFieldChangedEvent event){
        try{float f = Float.parseFloat(event.getText());selectedEffect.setVelocityVariation(f);
        }catch(NumberFormatException e){event.getTextFieldControl().setText(selectedEffect.getVelocityVariation()+"");} 
    } 
    @NiftyEventSubscriber(id="BoneListD")
    public void onBoneListSelectionChanged(String id, DropDownSelectionChangedEvent event){
        selectedEffect.setUserData("bone", event.getSelection().toString());
    }  
    ////////////////////////NEW EFFECTS///////////////////////
    @NiftyEventSubscriber(id="EffectNameT")
     public void onEffectNameFieldChanged(String id, TextFieldChangedEvent event){effectParam[0]=event.getText();} 
     @NiftyEventSubscriber(id="EffectTypeS")
     public void onEffectTypeBarChanged(String id, SliderChangedEvent event){
         effectParam[1]=com.jme3.effect.ParticleMesh.Type.values()[(int)event.getValue()];
         newEffectsPane.findNiftyControl("EffectType", Label.class).setText("Type: "+effectParam[1]);
     }      
    @NiftyEventSubscriber(id="EffectNumParticlesT")
     public void onEffectNumParticlesFieldChanged(String id, TextFieldChangedEvent event){
        try{int f = Integer.parseInt(event.getText());effectParam[2]=f;
        }catch(NumberFormatException e){event.getTextFieldControl().setText(effectParam[2]+"");} 
    }     
    @NiftyEventSubscriber(id="createNewEffectB")
    public void onCreateNewEffectButtonClicked(String id, ButtonClickedEvent event){
        String a =(String)effectParam[0];
        com.jme3.effect.ParticleMesh.Type t = (com.jme3.effect.ParticleMesh.Type)effectParam[1];
        int i = (Integer)effectParam[2];
        ParticleEmitter pE = new ParticleEmitter(a,t,i){@Override public String toString(){return name;}}; 
        card.addEffect(pE);        
        refreshEffectList(); 
        ListBox<ParticleEmitter> ef = effect.findNiftyControl("effectsListBoxL", ListBox.class);
        ef.selectItem(ef.getItems().get(ef.getItems().size()-1));
        newEffectsPane.hide();
    }
    @NiftyEventSubscriber(id="cancelNewEffectB")
    public void onCancelNewEffectButtonClicked(String id, ButtonClickedEvent event){
        refreshEffectList();
        newEffectsPane.hide();
    }    
    ///////////////////////////Emitter Shape///////////////////
    
    @NiftyEventSubscriber(id="EmitterShapeD")
    public void onEmitterShapeTypeChanged(String id, DropDownSelectionChangedEvent event){
        emitterShapeDropDownSelection = (String) event.getSelection();
        if(emitterShapeDropDownSelection.equals("EmitterBoxShape")){
            effectParam=new Object[2];
            emitterShapePane.findNiftyControl("EmitterShapeParam0", Label.class).getElement().hide();  
            emitterShapePane.findNiftyControl("EmitterShapeParam1", Label.class).setText("MIN: ");
            emitterShapePane.findNiftyControl("EmitterShapeParam2", Label.class).setText("MAX: ");
            emitterShapePane.findNiftyControl("EmitterShapeParam0B", Button.class).getElement().hide();  
            emitterShapePane.findNiftyControl("EmitterShapeParam1T", TextField.class).getElement().show();
            emitterShapePane.findNiftyControl("EmitterShapeParam1T", TextField.class).setText("-1,-1,-1");
            emitterShapePane.findNiftyControl("EmitterShapeParam2T", TextField.class).getElement().show();
            emitterShapePane.findNiftyControl("EmitterShapeParam2T", TextField.class).setText("1,1,1");
        }else if(emitterShapeDropDownSelection.equals("EmitterPointShape")){
            effectParam=new Object[1];
            emitterShapePane.findNiftyControl("EmitterShapeParam0", Label.class).getElement().hide();  
            emitterShapePane.findNiftyControl("EmitterShapeParam1", Label.class).setText("Point: ");
            emitterShapePane.findNiftyControl("EmitterShapeParam2", Label.class).setText("");
            emitterShapePane.findNiftyControl("EmitterShapeParam0B", Button.class).getElement().hide();  
            emitterShapePane.findNiftyControl("EmitterShapeParam1T", TextField.class).getElement().show();
            emitterShapePane.findNiftyControl("EmitterShapeParam1T", TextField.class).setText("0,0,0");
            emitterShapePane.findNiftyControl("EmitterShapeParam2T", TextField.class).getElement().hide();            
        }else if(emitterShapeDropDownSelection.equals("EmitterSphereShape")){
            effectParam=new Object[2];
            emitterShapePane.findNiftyControl("EmitterShapeParam0", Label.class).getElement().hide();   
            emitterShapePane.findNiftyControl("EmitterShapeParam1", Label.class).setText("Center: ");
            emitterShapePane.findNiftyControl("EmitterShapeParam2", Label.class).setText("Radius: "); 
            emitterShapePane.findNiftyControl("EmitterShapeParam0B", Button.class).getElement().hide();  
            emitterShapePane.findNiftyControl("EmitterShapeParam1T", TextField.class).getElement().show();            
            emitterShapePane.findNiftyControl("EmitterShapeParam1T", TextField.class).setText("0,0,0");
            emitterShapePane.findNiftyControl("EmitterShapeParam2T", TextField.class).getElement().show();
            emitterShapePane.findNiftyControl("EmitterShapeParam2T", TextField.class).setText("1");
        }else{
            effectParam=new Object[1];             
            emitterShapePane.findNiftyControl("EmitterShapeParam0", Label.class).getElement().show();  
            emitterShapePane.findNiftyControl("EmitterShapeParam0", Label.class).setText("Mesh: ");
            emitterShapePane.findNiftyControl("EmitterShapeParam1", Label.class).setText("");
            emitterShapePane.findNiftyControl("EmitterShapeParam2", Label.class).setText(""); 
            emitterShapePane.findNiftyControl("EmitterShapeParam0B", Button.class).getElement().show();  
            emitterShapePane.findNiftyControl("EmitterShapeParam1T", TextField.class).getElement().hide();
            emitterShapePane.findNiftyControl("EmitterShapeParam2T", TextField.class).getElement().hide();
        }
    } 
    @NiftyEventSubscriber(id="EmitterShapeParam0B")
    public void onFindMeshButtonClicked(String id, ButtonClickedEvent event){
        String temp;
        try{temp = fileExplorer("Mesh");}catch(Exception e){return;}        
        String loc = "/Deck/Mesh/"+temp;
        effectParam[0] = loc;
        emitterShapePane.findNiftyControl("EmitterShapeParam0", Label.class).setText("Mesh: "+loc);
    }
    @NiftyEventSubscriber(id="EmitterShapeParam1T")
     public void onEffectShapeParam1FieldChanged(String id, TextFieldChangedEvent event){
        if(!vectorField)
            try{
                vectorField=true;
                String[] f = event.getText().split(",");
                float x = Float.parseFloat(f[0]);
                float y = Float.parseFloat(f[1]);
                float z = Float.parseFloat(f[2]);
                effectParam[0] = new Vector3f(x,y,z);
                event.getTextFieldControl().setText(x+","+y+","+z);                
                vectorField=false;
            }catch(NumberFormatException e){
                Vector3f tV = (Vector3f) effectParam[0];
                event.getTextFieldControl().setText(tV.getX()+" , "+tV.getY()+" , "+tV.getZ());
                vectorField=false;
            } 
    } 
    @NiftyEventSubscriber(id="EmitterShapeParam2T")
     public void onEffectShapeParam2FieldChanged(String id, TextFieldChangedEvent event){
        if(emitterShapeDropDownSelection.equals("EmitterPointShape")){return;            
        }else if(emitterShapeDropDownSelection.equals("EmitterSphereShape")){
             try{float f = Float.parseFloat(event.getText());effectParam[1]=f;
            }catch(NumberFormatException e){event.getTextFieldControl().setText(effectParam[1]+"");} 
        }else if(!vectorField){
            try{
                vectorField=true;
                String[] f = event.getText().split(",");
                float x = Float.parseFloat(f[0]);
                float y = Float.parseFloat(f[1]);
                float z = Float.parseFloat(f[2]);
                effectParam[1] = new Vector3f(x,y,z);
                event.getTextFieldControl().setText(x+","+y+","+z);                
                vectorField=false;
            }catch(NumberFormatException e){
                Vector3f tV = (Vector3f) effectParam[1];
                event.getTextFieldControl().setText(tV.getX()+" , "+tV.getY()+" , "+tV.getZ());
                vectorField=false;
            } 
        }
    } 
    
    @NiftyEventSubscriber(id="createNewEmitterShapeB")
    public void onCreateNewEmitterShapeButtonClicked(String id, ButtonClickedEvent event){
        if(emitterShapeDropDownSelection.equals("EmitterBoxShape")){
            selectedEffect.setShape(new EmitterBoxShape((Vector3f)effectParam[0],(Vector3f)effectParam[1]));
            effectsPane.findNiftyControl("EmitterShape", Label.class).setText("EmitterShape: EmitterBoxShape("+effectParam[0]+"|"+effectParam[1]+")");
        }else if(emitterShapeDropDownSelection.equals("EmitterPointShape")){
            selectedEffect.setShape(new EmitterPointShape((Vector3f)effectParam[0]));
            effectsPane.findNiftyControl("EmitterShape", Label.class).setText("EmitterShape: EmitterPointShape("+effectParam[0]+")");
        }else if(emitterShapeDropDownSelection.equals("EmitterSphereShape")){
            selectedEffect.setShape(new EmitterSphereShape((Vector3f)effectParam[0],(Float)effectParam[1]));
            effectsPane.findNiftyControl("EmitterShape", Label.class).setText("EmitterShape: EmitterSphereShape("+effectParam[0]+"|"+effectParam[1]+")");            
        }else {
            
             Node mesh = (Node) FlairDirector.getApp().getAssetManager().loadModel((String)effectParam[0]);
             java.util.ArrayList<com.jme3.scene.Mesh> list = new java.util.ArrayList<com.jme3.scene.Mesh>(); 
             for(com.jme3.scene.Spatial spat:mesh.getChildren()){
                list.add(((com.jme3.scene.Geometry)spat).getMesh());
             } 
             selectedEffect.setUserData("mesh", (String)effectParam[0]);
             if(emitterShapeDropDownSelection.equals("EmitterMeshConvexHullShape")){   
                selectedEffect.setShape(new EmitterMeshConvexHullShape(list));
                effectsPane.findNiftyControl("EmitterShape", Label.class).setText("EmitterShape: EmitterMeshConvexHullShape("+effectParam[0]+")");  
             }else if(emitterShapeDropDownSelection.equals("EmitterMeshFaceShape")){
                selectedEffect.setShape(new EmitterMeshFaceShape(list));
                effectsPane.findNiftyControl("EmitterShape", Label.class).setText("EmitterShape: EmitterMeshFaceShape("+effectParam[0]+")");  
            }else if(emitterShapeDropDownSelection.equals("EmitterMeshVertexShape")){
                selectedEffect.setShape(new EmitterMeshVertexShape(list));
                effectsPane.findNiftyControl("EmitterShape", Label.class).setText("EmitterShape: EmitterMeshVertexShape("+effectParam[0]+")");  
            }
        }
        emitterShapePane.hide();                
    }
    @NiftyEventSubscriber(id="cancelNewEmitterShapeB")
    public void onCancelEmitterShapeButtonClicked(String id, ButtonClickedEvent event){emitterShapePane.hide();}    
  
    /**
    * Creates a File Explorer to locate a specific sound
    * @param path
    * @return 
    */
    private String fileExplorer(String path){
       java.io.File root = new java.io.File("assets/Deck/"+path).getAbsoluteFile();
       //java.io.File root = new java.io.File("c:/");
       javax.swing.filechooser.FileSystemView fsv = new com.tools.flairEditor.Filters.SingleRootFileSystemView(root);
       javax.swing.JFileChooser fc = new javax.swing.JFileChooser(fsv);
        // Show open dialog; this method does not return until the dialog is closed
        fc.setDragEnabled(false);
        if(path.equals("Effects")){
            fc.addChoosableFileFilter(new com.tools.flairEditor.Filters.ImageFilter());
        }else if(path.equals("SoundEffects")){
             fc.addChoosableFileFilter(new com.tools.flairEditor.Filters.AudioFilter());
        }else if(path.equals("Mesh")){
            fc.addChoosableFileFilter(new com.tools.flairEditor.Filters.MeshFilter());
        }
        fc.setAcceptAllFileFilterUsed(false);        
        fc.showDialog(new javax.swing.JFrame(),"Load Asset");
        java.io.File selFile = fc.getSelectedFile();
        
        String[] relative = selFile.getPath().split(root.getName());
        return relative[1];
    } 
        
    @Override public void onStartScreen() {
        refreshSection1And2();
        refreshEffectList(); 
        refreshAudioList();        
        effectTab=section1Tab=animationTab=vectorField=false;//needed in order to properly reset        
        section("1");
        section("3");
        section("5");
        screen.layoutLayers();
    }
    
    @Override public void onEndScreen() {}
    private Object[] effectParam;//used for constructor params of new ParticleEmitter and new ParticleShape
    private Nifty nifty;
    private TextRenderer title;
    private Screen screen;
    private Element tL,tM,tR;//Tells Section
    private Element sect1,sect2,audio,effect,anim,stats;//Full Top Row Panes
    private Element newEffectsPane,effectsPane,audioPane,emitterShapePane,statsPane,specialStatsPane; //bottom left panes not needed now animPane
    private boolean effectTab=true,section1Tab=true,animationTab=true,vectorField=false;
    private String emitterShapeDropDownSelection,statsListBoxSelection;
    private CardSetter card;
    private ParticleEmitter selectedEffect;
    private String previousCardName;
    private AudioNode selectedAudio;    
}