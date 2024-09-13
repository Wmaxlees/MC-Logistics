package com.wmaxlees.gregcolonies.core.client.gui.modules;

import static com.minecolonies.api.util.constant.WindowConstants.*;
import static org.jline.utils.AttributedStyle.WHITE;

import com.ldtteam.blockui.Pane;
import com.ldtteam.blockui.controls.Button;
import com.ldtteam.blockui.controls.ItemIcon;
import com.ldtteam.blockui.controls.Text;
import com.ldtteam.blockui.controls.TextField;
import com.ldtteam.blockui.views.ScrollingList;
import com.minecolonies.api.colony.buildings.views.IBuildingView;
import com.minecolonies.core.client.gui.AbstractModuleWindow;
import com.wmaxlees.gregcolonies.api.crafting.FluidStorage;
import com.wmaxlees.gregcolonies.api.items.ModItems;
import com.wmaxlees.gregcolonies.core.colony.buildings.moduleviews.FluidListModuleView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Predicate;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public class WindowFluidList extends AbstractModuleWindow {
  protected final ScrollingList resourceList;

  protected final IBuildingView building;

  private String filter = "";

  protected final boolean isInverted;

  protected List<FluidStorage> groupedFluidList;

  protected final List<FluidStorage> currentDisplayedList = new ArrayList<>();

  private int tick;

  public WindowFluidList(
      final String res, final IBuildingView building, final FluidListModuleView moduleView) {
    super(building, res);

    resourceList = window.findPaneOfTypeByID(LIST_RESOURCES, ScrollingList.class);
    window
        .findPaneOfTypeByID(DESC_LABEL, Text.class)
        .setText(Component.translatable(moduleView.getDesc().toLowerCase(Locale.US)));
    this.building = building;
    this.isInverted = moduleView.isInverted();
    this.id = moduleView.getId();

    groupedFluidList = new ArrayList<>(moduleView.getAllFluids().apply(building));

    window
        .findPaneOfTypeByID(INPUT_FILTER, TextField.class)
        .setHandler(
            input -> {
              final String newFilter = input.getText();
              if (!newFilter.equals(filter)) {
                filter = newFilter;
                this.tick = 10;
              }
            });
  }

  public void onButtonClicked(@NotNull final Button button) {
    super.onButtonClicked(button);
    if (Objects.equals(button.getID(), BUTTON_SWITCH)) {
      switchClicked(button);
    } else if (Objects.equals(button.getID(), BUTTON_RESET_DEFAULT)) {
      reset();
    }
  }

  @Override
  public void onOpened() {
    updateResources();
  }

  @Override
  public void onUpdate() {
    super.onUpdate();
    if (tick > 0 && --tick == 0) {
      updateResources();
    }
  }

  private void switchClicked(@NotNull final Button button) {
    final int row = resourceList.getListElementIndexByPane(button);
    final FluidStorage item = currentDisplayedList.get(row);
    final boolean on = button.getText().equals(Component.translatable(ON));
    final boolean add = (on && isInverted) || (!on && !isInverted);
    final FluidListModuleView module =
        building.getModuleViewMatching(FluidListModuleView.class, view -> view.getId().equals(id));

    if (add) {
      module.addFluid(item);
    } else {
      module.removeFluid(item);
    }

    resourceList.refreshElementPanes();
  }

  private void reset() {
    final FluidListModuleView module =
        building.getModuleViewMatching(FluidListModuleView.class, view -> view.getId().equals(id));

    module.clearFluids();

    resourceList.refreshElementPanes();
  }

  private void updateResources() {
    final Predicate<FluidStack> filterPredicate =
        stack ->
            filter.isEmpty()
                || stack
                    .getDisplayName()
                    .getString()
                    .toLowerCase(Locale.US)
                    .contains(filter.toLowerCase(Locale.US));
    currentDisplayedList.clear();
    for (final FluidStorage storage : groupedFluidList) {
      if (filterPredicate.test(storage.getFluidStack())) {
        currentDisplayedList.add(storage);
      }
    }

    applySorting(currentDisplayedList);

    updateResourceList();
  }

  protected void applySorting(final List<FluidStorage> displayedList) {
    displayedList.sort(
        (o1, o2) -> {
          boolean o1Allowed =
              building
                  .getModuleViewMatching(FluidListModuleView.class, view -> view.getId().equals(id))
                  .isAllowedFluid(o1);

          boolean o2Allowed =
              building
                  .getModuleViewMatching(FluidListModuleView.class, view -> view.getId().equals(id))
                  .isAllowedFluid(o2);

          if (!o1Allowed && o2Allowed) {
            return isInverted ? -1 : 1;
          } else if (o1Allowed && !o2Allowed) {
            return isInverted ? 1 : -1;
          } else {
            return 0;
          }
        });
  }

  protected void updateResourceList() {
    resourceList.enable();
    resourceList.show();

    resourceList.setDataProvider(
        new ScrollingList.DataProvider() {
          /**
           * The number of rows of the list.
           *
           * @return the number.
           */
          @Override
          public int getElementCount() {
            return currentDisplayedList.size();
          }

          /**
           * Inserts the elements into each row.
           *
           * @param index the index of the row/list element.
           * @param rowPane the parent Pane for the row, containing the elements to update.
           */
          @Override
          public void updateElement(final int index, @NotNull final Pane rowPane) {
            final FluidStack resource = currentDisplayedList.get(index).getFluidStack();
            final Text resourceLabel = rowPane.findPaneOfTypeByID(RESOURCE_NAME, Text.class);
            resourceLabel.setText(resource.getDisplayName());
            resourceLabel.setColors(WHITE);
            rowPane
                .findPaneOfTypeByID(RESOURCE_ICON, ItemIcon.class)
                .setItem(getTankFromFluid(resource));
            final boolean isAllowedItem =
                building
                    .getModuleViewMatching(
                        FluidListModuleView.class, view -> view.getId().equals(id))
                    .isAllowedFluid(new FluidStorage(resource, 1000));
            final Button switchButton = rowPane.findPaneOfTypeByID(BUTTON_SWITCH, Button.class);

            if ((isInverted && !isAllowedItem) || (!isInverted && isAllowedItem)) {
              switchButton.setText(Component.translatable(ON));
            } else {
              switchButton.setText(Component.translatable(OFF));
            }
          }
        });
  }

  private ItemStack getTankFromFluid(final FluidStack fluidStack) {
    ItemStack item = new ItemStack(ModItems.courierTank);
    if (item.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve().isEmpty()) {
      return item;
    }

    IFluidHandler handler =
        item.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve().get();
    handler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);

    return item;
  }
}
