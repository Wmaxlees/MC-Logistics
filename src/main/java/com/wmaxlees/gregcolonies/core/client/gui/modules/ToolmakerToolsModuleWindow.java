package com.wmaxlees.gregcolonies.core.client.gui.modules;

import static com.wmaxlees.gregcolonies.api.util.constant.WindowConstants.*;

import com.ldtteam.blockui.Pane;
import com.ldtteam.blockui.controls.Button;
import com.ldtteam.blockui.controls.ItemIcon;
import com.ldtteam.blockui.controls.Text;
import com.ldtteam.blockui.views.ScrollingList;
import com.minecolonies.api.colony.buildings.views.IBuildingView;
import com.minecolonies.core.client.gui.AbstractModuleWindow;
import com.wmaxlees.gregcolonies.api.items.ModTags;
import com.wmaxlees.gregcolonies.api.util.constant.Constants;
import com.wmaxlees.gregcolonies.core.colony.buildings.moduleviews.ToolmakerToolsModuleView;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class ToolmakerToolsModuleWindow extends AbstractModuleWindow {
  /** The resource string. */
  private static final String RESOURCE_STRING = ":gui/layouthuts/layouttoolmakertools.xml";

  /** The stations module view. */
  private final ToolmakerToolsModuleView module;

  private final ScrollingList toolList;

  /**
   * Constructor for the window of the worker building.
   *
   * @param building class extending
   */
  public ToolmakerToolsModuleWindow(
      final IBuildingView building, final ToolmakerToolsModuleView module) {
    super(building, Constants.MOD_ID + RESOURCE_STRING);

    toolList = this.window.findPaneOfTypeByID("toollist", ScrollingList.class);

    this.module = module;

    registerButton(BUTTON_REMOVE_WORKORDER, this::addWorkorderClicked);
  }

  @Override
  public void onOpened() {
    super.onOpened();
    updateToolList();
  }

  private void updateToolList() {
    toolList.enable();
    toolList.show();

    toolList.setDataProvider(
        new ScrollingList.DataProvider() {
          /**
           * The number of rows of the list.
           *
           * @return the number.
           */
          @Override
          public int getElementCount() {
            int buildingLevel = module.getBuildingView().getBuildingLevel();
            int size = ForgeRegistries.ITEMS.tags().getTag(ModTags.toolsmithLV).size();
            if (buildingLevel > 1) {
              // TODO(Wmaxlees): Add other tags here based on level
            }
            return size;
          }

          /**
           * Inserts the elements into each row.
           *
           * @param index the index of the row/list element.
           * @param rowPane the parent Pane for the row, containing the elements to update.
           */
          @Override
          public void updateElement(final int index, @NotNull final Pane rowPane) {
            final ItemStack item =
                new ItemStack(
                    (Item)
                        ForgeRegistries.ITEMS.tags().getTag(ModTags.toolsmithLV).stream()
                            .toArray()[index]);
            rowPane.findPaneOfTypeByID("toolName", Text.class).setText(item.getHoverName());
            rowPane.findPaneOfTypeByID("toolIcon", ItemIcon.class).setItem(item);
          }
        });
  }

  private void addWorkorderClicked(@NotNull final Button button) {}
}
