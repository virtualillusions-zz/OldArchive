/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.spectre.director;

import com.spectre.deck.card.Card;
import com.spectre.deck.card.CardStats.CardRange;
import com.spectre.deck.card.CardStats.CardSeries;
import com.spectre.deck.card.CardStats.CardTrait;
import java.util.Date;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author Kyle Williams
 */
public final class EditorDirector {

    private static TopComponent flairDataEditorTopComponent;
    private static TopComponent flairVisualEditorTopComponent;
    //vert wastefull work around to a reoccuring problem however for now keep as is
    private static final String tempAnim = SceneDirector.getInstance().getAllAnimations().iterator().next();

    public static TopComponent getFliarDataEditorTopComponent() {
        if (flairDataEditorTopComponent == null) {
            flairDataEditorTopComponent = WindowManager.getDefault().findTopComponent("FlairDataEditorTopComponent");
        }
        return flairDataEditorTopComponent;
    }

    public static TopComponent getFlairVisualEditorTopComponent() {
        if (flairVisualEditorTopComponent == null) {
            flairVisualEditorTopComponent = WindowManager.getDefault().findTopComponent("FlairVisualEditorTopComponent");
        }
        return flairVisualEditorTopComponent;
    }

    public static Card createNewCard(final String name, final CardSeries series) {
        return new Card() {
            {
                lockData(false);
                getAnimation().setAnimationName(tempAnim);//fix to a reoccuring issue
                setData("cardName", name.toUpperCase());
                setData("dateModified", new Date().getTime());
                setData("cardSeries", series);
                setData("cardTrait", CardTrait.Enhance);
                setData("cardRange", CardRange.Misc);
                setData("maxUse", "1");
                setData("skillType", "instant");
                setData("skillTypeExtended", "user");
                setData("mineType", "Regular");
                setData("attackHoming", 0);
                setData("attakDamage", "Stat [Default]");
                setData("attackVelocity", 3);
                setData("attackCrawler", false);
                setData("attackPath", "Linear");
                setData("autoUse", false);
                setData("decreasePlayerKnockback", false);
                setData("shuffleSkills", false);
                setData("mpCost", "Stat [Default]");
                setData("skillCostIncrease", false);
                setData("StatusReset", false);
                setData("eraseSkill", "None");
                setData("mpIncrease", "Stat [Default]");
                setData("skillDescription", "New Card Skill");
                setData("A.O.E", false);
                setData("A.O.E_Radius", 3);
                setData("holdAndWait", false);
                setData("holdToPower", false);
                String s = "false:0:0";
                setData("hpUser", s);
                setData("hpOpponent", s);
                setData("speedUser", s);
                setData("speedOpponent", s);
                setData("immobolizeUser", s);
                setData("immobolizeOpponent", s);
                setData("paralyzeUser", s);
                setData("paralyzeOpponent", s);
                setData("mpLevelUser", s);
                setData("mpLevelOpponent", s);
                setData("increaseLockOnRangeUser", s);
                setData("increaseLockOnRangeOpponent", s);
                setData("mpUser", s);
                setData("mpOpponent", s);
                setData("confuseUser", s);
                setData("confuseOpponent", s);
                setData("invisibleUser", s);
                setData("invisibleOpponent", s);
                setData("dashUser", s);
                setData("dashOpponent", s);
                setData("jumpUser", s);
                setData("jumpOpponent", s);
                setData("defenseUser", s);
                setData("defenseOpponent", s);
                lockData(true);
            }
        };
    }
}
