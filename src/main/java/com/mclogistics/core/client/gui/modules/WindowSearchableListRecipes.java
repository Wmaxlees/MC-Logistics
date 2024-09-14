package com.mclogistics.core.client.gui.modules;

import static com.minecolonies.api.util.constant.WindowConstants.*;

import com.ldtteam.blockui.Pane;
import com.ldtteam.blockui.controls.*;
import com.ldtteam.blockui.views.ScrollingList;
import com.minecolonies.api.colony.buildings.views.IBuildingView;
import com.minecolonies.api.crafting.IRecipeStorage;
import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.util.constant.ToolType;
import com.minecolonies.api.util.constant.TranslationConstants;
import com.minecolonies.core.Network;
import com.minecolonies.core.client.gui.AbstractModuleWindow;
import com.minecolonies.core.colony.buildings.moduleviews.CraftingModuleView;
import com.minecolonies.core.network.messages.server.colony.building.worker.AddRemoveRecipeMessage;
import com.minecolonies.core.network.messages.server.colony.building.worker.ChangeRecipePriorityMessage;
import com.minecolonies.core.network.messages.server.colony.building.worker.ToggleRecipeMessage;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

/** BOWindow for the hiring or firing of a worker. */
public class WindowSearchableListRecipes extends AbstractModuleWindow {
  /** Id of the recipe list in the GUI. */
  private static final String RECIPE_LIST = "recipes";

  /** Id of the recipe status label in the GUI. */
  private static final String RECIPE_STATUS = "recipestatus";

  /** The output item icon. */
  private static final String OUTPUT_ICON = "output";

  /** The item icon of the resource. */
  private static final String RESOURCE = "res%d";

  private static final String FILTER = "filter";

  /** The view of the current module. */
  private final CraftingModuleView module;

  /** List of recipes which can be assigned. */
  private final ScrollingList recipeList;

  /** Button to access the crafting grid. */
  private static final String BUTTON_CRAFTING = "crafting";

  /** The recipe status. */
  private final Text recipeStatus;

  /** Life count. */
  private int lifeCount = 0;

  private String filterText = "";

  private List<IRecipeStorage> displayedRecipes = new ArrayList<>();

  /** Update delay. */
  private int tick;

  /**
   * The constructor of the window.
   *
   * @param view the building view.
   * @param name the layout file.
   */
  public WindowSearchableListRecipes(
      final IBuildingView view, final String name, final CraftingModuleView module) {
    super(view, name);
    this.module = module;
    recipeList = findPaneOfTypeByID(RECIPE_LIST, ScrollingList.class);
    recipeStatus = findPaneOfTypeByID(RECIPE_STATUS, Text.class);

    TextField filterInput = findPaneOfTypeByID(FILTER, TextField.class);
    filterInput.setHandler(
        h -> {
          final String newFilter = h.getText();
          if (!newFilter.equals(filterText)) {
            filterText = newFilter;
            tick = 10;
          }
        });

    displayedRecipes = module.getRecipes();

    findPaneOfTypeByID(BUTTON_CRAFTING, ButtonImage.class)
        .setVisible(module.isRecipeAlterationAllowed());
    findPaneOfTypeByID("recipestatus", Text.class).setVisible(module.isRecipeAlterationAllowed());

    super.registerButton(BUTTON_CRAFTING, this::craftingClicked);
    super.registerButton(BUTTON_REMOVE, this::removeClicked);
    super.registerButton(BUTTON_FORWARD, this::forwardClicked);
    super.registerButton(BUTTON_BACKWARD, this::backwardClicked);
    super.registerButton(BUTTON_TOGGLE, this::toggleRecipe);
  }

  /**
   * Recipe toggle.
   *
   * @param button the clicked button.
   */
  private void toggleRecipe(final Button button) {
    final int row = recipeList.getListElementIndexByPane(button);
    module.toggle(row);
    Network.getNetwork()
        .sendToServer(
            new ToggleRecipeMessage(buildingView, row, module.getProducer().getRuntimeID()));
  }

  /**
   * Backwards clicked in the button.
   *
   * @param button the clicked button.
   */
  private void backwardClicked(final Button button) {
    final boolean shift =
        InputConstants.isKeyDown(mc.getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT);
    final int row = recipeList.getListElementIndexByPane(button);
    module.switchOrder(row, row + 1, shift);
    Network.getNetwork()
        .sendToServer(
            new ChangeRecipePriorityMessage(
                buildingView, row, false, module.getProducer().getRuntimeID(), shift));
    recipeList.refreshElementPanes();
  }

  /**
   * Forward clicked.
   *
   * @param button the clicked button.
   */
  private void forwardClicked(final Button button) {
    final boolean shift =
        InputConstants.isKeyDown(mc.getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT);
    final int row = recipeList.getListElementIndexByPane(button);
    module.switchOrder(row, row - 1, shift);
    Network.getNetwork()
        .sendToServer(
            new ChangeRecipePriorityMessage(
                buildingView, row, true, module.getProducer().getRuntimeID(), shift));
    recipeList.refreshElementPanes();
  }

  /**
   * On remove recipe clicked.
   *
   * @param button the clicked button.
   */
  private void removeClicked(final Button button) {
    final int row = recipeList.getListElementIndexByPane(button);
    final IRecipeStorage data = module.getRecipes().get(row);
    Network.getNetwork()
        .sendToServer(
            new AddRemoveRecipeMessage(
                buildingView, true, data, module.getProducer().getRuntimeID()));
  }

  /** If crafting is clicked this happens. Override if needed. */
  public void craftingClicked() {
    if (!module.isRecipeAlterationAllowed()) {
      // This should never happen, because the button is hidden. But if someone glitches into the
      // interface, stop him here.
      return;
    }

    module.openCraftingGUI();
  }

  @Override
  public void onOpened() {
    refreshListView();
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

  @Override
  public void onUpdate() {
    super.onUpdate();
    if (!Screen.hasShiftDown()) {
      lifeCount++;
    }
    recipeStatus.setText(
        Component.translatable(
            TranslationConstants.RECIPE_STATUS,
            module.getRecipes().size(),
            module.getMaxRecipes()));
    window.findPaneOfTypeByID(RECIPE_LIST, ScrollingList.class).refreshElementPanes();

    if (tick > 0 && --tick == 0) {
      updateRecipeList();
    }
  }

  private void updateRecipeList() {
    final Predicate<ItemStack> filterPredicate =
        stack ->
            filterText.isEmpty()
                || stack
                    .getDescriptionId()
                    .toLowerCase(Locale.US)
                    .contains(filterText.toLowerCase(Locale.US))
                || stack
                    .getHoverName()
                    .getString()
                    .toLowerCase(Locale.US)
                    .contains(filterText.toLowerCase(Locale.US));

    displayedRecipes = new ArrayList<>();
    for (final IRecipeStorage storage : module.getRecipes()) {
      if (filterPredicate.test(storage.getPrimaryOutput())) {
        displayedRecipes.add(storage);
      }
    }

    refreshListView();
  }

  private void refreshListView() {
    recipeList.enable();
    recipeList.show();

    // Creates a dataProvider for the homeless recipeList.
    recipeList.setDataProvider(
        new ScrollingList.DataProvider() {
          @Override
          public int getElementCount() {
            return displayedRecipes.size();
          }

          @Override
          public void updateElement(final int index, @NotNull final Pane rowPane) {
            @NotNull final IRecipeStorage recipe = displayedRecipes.get(index);
            final ItemIcon icon = rowPane.findPaneOfTypeByID(OUTPUT_ICON, ItemIcon.class);
            List<ItemStack> displayStacks = recipe.getRecipeType().getOutputDisplayStacks();
            icon.setItem(
                displayStacks.get((lifeCount / LIFE_COUNT_DIVIDER) % (displayStacks.size())));

            if (!module.isRecipeAlterationAllowed()) {
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

            if (module.isDisabled(recipe)) {
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
}
