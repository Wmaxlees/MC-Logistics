package com.wmaxlees.gregcolonies.core.client.gui.modules;

import static com.wmaxlees.gregcolonies.api.util.constant.WindowConstants.*;

import com.ldtteam.blockui.controls.Button;
import com.minecolonies.api.colony.buildings.views.IBuildingView;
import com.minecolonies.core.client.gui.AbstractModuleWindow;
import com.wmaxlees.gregcolonies.api.util.constant.Constants;
import com.wmaxlees.gregcolonies.core.colony.buildings.moduleviews.ToolmakerWorkordersModuleView;
import org.jetbrains.annotations.NotNull;

public class ToolmakerWorkordersModuleWindow extends AbstractModuleWindow {
  /** The resource string. */
  private static final String RESOURCE_STRING = ":gui/layouthuts/layouttoolmakerworkorders.xml";

  /** The stations module view. */
  private final ToolmakerWorkordersModuleView module;

  /**
   * Constructor for the window of the worker building.
   *
   * @param building class extending
   */
  public ToolmakerWorkordersModuleWindow(
      final IBuildingView building, final ToolmakerWorkordersModuleView module) {
    super(building, Constants.MOD_ID + RESOURCE_STRING);
    super.registerButton(BUTTON_REMOVE_WORKORDER, this::removeWorkorderClicked);
    this.module = module;
  }

  private void removeWorkorderClicked(@NotNull final Button button) {}
}
