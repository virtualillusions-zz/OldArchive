package de.lessvoid.nifty.controls.dropdown;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.dropdown.controller.DropDownControl;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class CreateDropDownControl extends ControlAttributes {
  public CreateDropDownControl() {
    setId(NiftyIdCreator.generate());
    setName("dropDownControl");
  }

  public CreateDropDownControl(final String id) {
    setId(id);
    setName("dropDownControl");
  }

  public DropDownControl create(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    nifty.addControl(screen, parent, getStandardControl());
    nifty.addControlsWithoutStartScreen();
    return parent.findControl(attributes.get("id"), DropDownControl.class);
  }
}
