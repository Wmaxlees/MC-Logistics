package com.wmaxlees.gregcolonies.core.client.gui.modules;

import static com.minecolonies.api.util.constant.WindowConstants.*;

import com.ldtteam.blockui.Pane;
import com.ldtteam.blockui.controls.*;
import com.ldtteam.blockui.views.ScrollingList;
import com.minecolonies.api.colony.buildings.views.IBuildingView;
import com.minecolonies.api.crafting.IRecipeStorage;
import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.util.constant.ToolType;
import com.minecolonies.core.client.gui.modules.WindowListRecipes;
import com.minecolonies.core.colony.buildings.moduleviews.CraftingModuleView;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

public class SearchableWindowListRecipes extends WindowListRecipes {
  /** Id of the recipe list in the GUI. */
  private static final String RECIPE_LIST = "recipes";

  /** The output item icon. */
  private static final String OUTPUT_ICON = "output";

  /** The item icon of the resource. */
  private static final String RESOURCE = "res%d";

  CraftingModuleView moduleView;

  /** The filter for the resource list. */
  private String filter = "";

  /** Update delay. */
  private int tick;

  /** Grouped list after applying the current temporary filter. */
  protected final List<IRecipeStorage> currentDisplayedList = new ArrayList<>();

  /** List of recipes which can be assigned. */
  private final ScrollingList recipeList;

  /**
   * The constructor of the window.
   *
   * @param view the building view.
   * @param name the layout file.
   * @param module
   */
  public SearchableWindowListRecipes(IBuildingView view, String name, CraftingModuleView module) {
    super(view, name, module);

    moduleView = module;

    recipeList = findPaneOfTypeByID(RECIPE_LIST, ScrollingList.class);

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

  @Override
  public void onUpdate() {
    super.onUpdate();
    if (tick > 0 && --tick == 0) {
      updateRecipes();
    }
  }

  /** Update the recipe list. */
  private void updateRecipes() {
    final Predicate<IRecipeStorage> filterPredicate =
        recipe ->
            filter.isEmpty()
                || recipe
                    .getPrimaryOutput()
                    .getDescriptionId()
                    .toLowerCase(Locale.US)
                    .contains(filter.toLowerCase(Locale.US))
                || recipe
                    .getPrimaryOutput()
                    .getHoverName()
                    .getString()
                    .toLowerCase(Locale.US)
                    .contains(filter.toLowerCase(Locale.US));

    currentDisplayedList.clear();
    for (final IRecipeStorage storage : moduleView.getRecipes()) {
      if (filterPredicate.test(storage)) {
        currentDisplayedList.add(storage);
      }
    }

    applySorting(currentDisplayedList);

    updateRecipeList();
  }

  protected void applySorting(final List<IRecipeStorage> displayedList) {
    displayedList.sort(
        Comparator.comparing(
            o -> o.getPrimaryOutput().getHoverName().getString().toLowerCase(Locale.US)));
  }

  /** Updates the resource list in the GUI with the info we need. */
  protected void updateRecipeList() {
    recipeList.enable();
    recipeList.show();

    recipeList.setDataProvider(
        new ScrollingList.DataProvider() {
          @Override
          public int getElementCount() {
            return currentDisplayedList.size();
          }

          @Override
          public void updateElement(final int index, @NotNull final Pane rowPane) {
            @NotNull final IRecipeStorage recipe = currentDisplayedList.get(index);
            final ItemIcon icon = rowPane.findPaneOfTypeByID(OUTPUT_ICON, ItemIcon.class);
            List<ItemStack> displayStacks = recipe.getRecipeType().getOutputDisplayStacks();
            icon.setItem(displayStacks.get((1 / LIFE_COUNT_DIVIDER) % (displayStacks.size())));

            if (!moduleView.isRecipeAlterationAllowed()) {
              final Button removeButton = rowPane.findPaneOfTypeByID(BUTTON_REMOVE, Button.class);
              if (removeButton != null) {
                removeButton.setVisible(false);
              }
            }

            final Text intermediate = rowPane.findPaneOfTypeByID("intermediate", Text.class);
            intermediate.setVisible(false);
            if (recipe.getRequiredTool() != ToolType.NONE) {
              intermediate.setText(recipe.getRequiredTool().getDisplayName());
              intermediate.setVisible(true);
            } else if (recipe.getIntermediate() != Blocks.AIR) {
              intermediate.setText(recipe.getIntermediate().getName());
              // intermediate.setVisible(true);
            }

            if (moduleView.isDisabled(recipe)) {
              rowPane.findPaneOfTypeByID("gradient", Gradient.class).setVisible(true);
              rowPane
                  .findPaneOfTypeByID(BUTTON_TOGGLE, Button.class)
                  .setText(Component.translatable("com.minecolonies.coremod.gui.recipe.enable"));
            } else {
              rowPane.findPaneOfTypeByID("gradient", Gradient.class).setVisible(false);
              rowPane
                  .findPaneOfTypeByID(BUTTON_TOGGLE, Button.class)
                  .setText(Component.translatable("com.minecolonies.coremod.gui.recipe.disable"));
            }

            // Some special recipes might not include all necessary air blocks.
            if (recipe.getInput().size() < 4) {
              for (int i = 0; i < 9; i++) {
                if (i < recipe.getInput().size()) {
                  rowPane
                      .findPaneOfTypeByID(String.format(RESOURCE, i + 1), ItemIcon.class)
                      .setItem(getStackWithCount(recipe.getInput().get(i)));
                } else {
                  rowPane
                      .findPaneOfTypeByID(String.format(RESOURCE, i + 1), ItemIcon.class)
                      .setItem(ItemStack.EMPTY);
                }
              }
            } else if (recipe.getInput().size() == 4) {
              rowPane
                  .findPaneOfTypeByID(String.format(RESOURCE, 1), ItemIcon.class)
                  .setItem(getStackWithCount(recipe.getInput().get(0)));
              rowPane
                  .findPaneOfTypeByID(String.format(RESOURCE, 2), ItemIcon.class)
                  .setItem(getStackWithCount(recipe.getInput().get(1)));
              rowPane
                  .findPaneOfTypeByID(String.format(RESOURCE, 3), ItemIcon.class)
                  .setItem(ItemStack.EMPTY);
              rowPane
                  .findPaneOfTypeByID(String.format(RESOURCE, 4), ItemIcon.class)
                  .setItem(getStackWithCount(recipe.getInput().get(2)));
              rowPane
                  .findPaneOfTypeByID(String.format(RESOURCE, 5), ItemIcon.class)
                  .setItem(getStackWithCount(recipe.getInput().get(3)));
              for (int i = 6; i < 9; i++) {
                rowPane
                    .findPaneOfTypeByID(String.format(RESOURCE, i + 1), ItemIcon.class)
                    .setItem(ItemStack.EMPTY);
              }
            } else {
              for (int i = 0; i < recipe.getInput().size(); i++) {
                rowPane
                    .findPaneOfTypeByID(String.format(RESOURCE, i + 1), ItemIcon.class)
                    .setItem(getStackWithCount(recipe.getInput().get(i)));
              }
            }
          }
        });
  }

  /**
   * Setup the stack with count.
   *
   * @param storage the storage to get it from.
   * @return the stack with the set count.
   */
  private ItemStack getStackWithCount(final ItemStorage storage) {
    final ItemStack displayItem = storage.getItemStack();
    displayItem.setCount(storage.getAmount());
    return displayItem;
  }
}
