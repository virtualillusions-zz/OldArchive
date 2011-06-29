package de.lessvoid.nifty.controls.checkbox;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * A CheckboxControl.
 * @author void
 */
public class CheckboxControl implements Controller {
  private Element element;
  private Screen screen;
  private boolean checked;
  private FocusHandler focusHandler;

  public void bind(
      final Nifty niftyParam,
      final Screen screenParam,
      final Element elementParam,
      final Properties propertiesParam,
      final ControllerEventListener listenerParam,
      final Attributes controlDefinitionAttributes) {
    element = elementParam;
    screen = screenParam;
    checked = true;
  }

  public void onStartScreen() {
    focusHandler = screen.getFocusHandler();
  }

  public void onFocus(final boolean getFocus) {
  }

  public void inputEvent(final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyInputEvent.NextInputElement) {
      focusHandler.getNext(element).setFocus();
    } else if (inputEvent == NiftyInputEvent.PrevInputElement) {
      focusHandler.getPrev(element).setFocus();
    } else if (inputEvent == NiftyInputEvent.Activate) {
      onClick();
    }
  }

  public boolean onClick() {
    checked = !checked;
    Element selectImage = element.findElementByName("select");
    if (checked) {
      selectImage.show();
    } else {
      selectImage.hide();
    }
    return true;
  }
}
