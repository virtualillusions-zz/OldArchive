/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.systems.essence;

import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import com.spectre.app.SpectreAppState;
import com.spectre.app.SpectreApplicationState;
import com.spectre.scene.visual.components.InScenePiece;
import com.spectre.systems.essence.components.*;
import com.spectre.systems.essence.components.CharacterStatePiece.CharacterState;
import java.util.Iterator;
import java.util.Set;

/**
 * REDO ITS BAD TO ACCESS STRAIGHT FROM THE ENTITY DATA CLASS
 *
 * @author Kyle Williams
 */
public class EssenceSystem extends SpectreAppState {
/////////REMEMBER TO ADD MODIFIER IN INPUT SEGMENT 

    private EntitySet essenceSet;
    private EntityData ed;

    @Override
    public void SpectreAppState(SpectreApplicationState sAppState) {
        this.ed = sAppState.getEntityData();
        this.essenceSet = ed.getEntities(EssencePiece.class, InScenePiece.class);
    }

    @Override
    public void cleanUp() {
        this.essenceSet.release();
        this.essenceSet.applyChanges();
        remove(this.essenceSet);
    }

    @Override
    protected void spectreUpdate(float tpf) {
        if (essenceSet.applyChanges()) {
            add(essenceSet.getAddedEntities());
            remove(essenceSet.getRemovedEntities());
            add(essenceSet.getChangedEntities());//All I do is default components
        }
        essenceUpdate(tpf);
    }

    private void add(Set<Entity> addedEntities) {
        for (Iterator<Entity> it = addedEntities.iterator(); it.hasNext();) {
            EntityId id = it.next().getId();
            ed.setComponents(id, new BeginingHealthPiece(),
                    new HealthPiece(),
                    new ReplenishRatePiece(),
                    new FocusLevelPiece(),
                    new FocusPiece(),
                    new PowerPiece(),
                    new AccuracyPiece(),
                    new DexterityPiece(),
                    new ConstitutionPiece(),
                    new IncreaseFocusRatePiece(),
                    new CharacterStatePiece(),
                    new ModifierButtonPiece());
        }
    }

    private void remove(Set<Entity> removedEntities) {
        for (Iterator<Entity> it = removedEntities.iterator(); it.hasNext();) {
            EntityId id = it.next().getId();
            ed.removeComponent(id, BeginingHealthPiece.class);
            ed.removeComponent(id, HealthPiece.class);
            ed.removeComponent(id, ReplenishRatePiece.class);
            ed.removeComponent(id, FocusLevelPiece.class);
            ed.removeComponent(id, FocusPiece.class);
            ed.removeComponent(id, PowerPiece.class);
            ed.removeComponent(id, AccuracyPiece.class);
            ed.removeComponent(id, DexterityPiece.class);
            ed.removeComponent(id, ConstitutionPiece.class);
            ed.removeComponent(id, IncreaseFocusRatePiece.class);
            ed.removeComponent(id, CharacterStatePiece.class);
            ed.removeComponent(id, ModifierButtonPiece.class);
        }
    }

    private void essenceUpdate(float tpf) {
        for (Iterator<Entity> it = essenceSet.iterator(); it.hasNext();) {
            EntityId id = it.next().getId();
            int focus = getFocus(id);
            int focusLevel = getFocusLevel(id);
            if (focus < focusLevel) {
                float replenishRate = getReplenishRate(id);
                float cfiTime = getCurrentReplenishRate(id);
                if (cfiTime >= replenishRate) {
                    focus++;
                    cfiTime = 0;
                } else {
                    if (!getIsIncreaseFocusRate(id)) {
                        cfiTime += tpf * 1.5;
                    } else {
                        cfiTime += tpf;
                    }
                }
                ed.setComponents(id, new FocusPiece(focus),
                        new ReplenishRatePiece(replenishRate, cfiTime));
            }

            CharacterState cS = null;
            if (getHealth(id) <= getBeginingHealth(id) / 2) {
                cS = CharacterState.Injured;
            } else if (focus <= focusLevel) {
                cS = CharacterState.Tired;
            } else {
                cS = CharacterState.Healthy;
            }
            ed.setComponents(id, new CharacterStatePiece(cS));
        }
    }

    public int getAccuracy(EntityId id) {
        return ed.getComponent(id, AccuracyPiece.class).getAccuracy();
    }

    public int getBeginingHealth(EntityId id) {
        return ed.getComponent(id, BeginingHealthPiece.class).getBeginingHealth();
    }

    public int getConstitution(EntityId id) {
        return ed.getComponent(id, ConstitutionPiece.class).getConstitution();
    }

    public int getDexterity(EntityId id) {
        return ed.getComponent(id, DexterityPiece.class).getDexterity();
    }

    public int getFocusLevel(EntityId id) {
        return ed.getComponent(id, FocusLevelPiece.class).getFocusLevel();
    }

    public int getFocus(EntityId id) {
        return ed.getComponent(id, FocusPiece.class).getFocus();
    }

    public int getHealth(EntityId id) {
        return ed.getComponent(id, HealthPiece.class).getHealth();
    }

    public int getPower(EntityId id) {
        return ed.getComponent(id, PowerPiece.class).getPower();
    }

    public float getReplenishRate(EntityId id) {
        return ed.getComponent(id, ReplenishRatePiece.class).getReplenishRate();
    }

    public float getCurrentReplenishRate(EntityId id) {
        return ed.getComponent(id, ReplenishRatePiece.class).getCurrentReplenishRate();
    }

    public boolean getIsIncreaseFocusRate(EntityId id) {
        return ed.getComponent(id, IncreaseFocusRatePiece.class).isIncreaseFocusRate();
    }

    public float getIncreaseFocusRatePiece(EntityId id) {
        return ed.getComponent(id, IncreaseFocusRatePiece.class).getIncreasedRate();
    }

    public CharacterState getCharacterStatePiece(EntityId id) {
        return ed.getComponent(id, CharacterStatePiece.class).getType();
    }

    public boolean getModifierButtonPiece(EntityId id) {
        return ed.getComponent(id, ModifierButtonPiece.class).getModifierButton();
    }
}
