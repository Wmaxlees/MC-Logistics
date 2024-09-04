package com.wmaxlees.gregcolonies.core.client.gui.containers;

import static com.minecolonies.api.util.constant.TranslationConstants.WARNING_MAXIMUM_NUMBER_RECIPES;
import static com.minecolonies.api.util.constant.translation.BaseGameTranslationConstants.BASE_GUI_DONE;

import com.minecolonies.api.colony.IColonyManager;
import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.crafting.ModCraftingTypes;
import com.minecolonies.api.util.ItemStackUtils;
import com.minecolonies.core.Network;
import com.minecolonies.core.colony.buildings.views.AbstractBuildingView;
import com.minecolonies.core.network.messages.server.colony.building.worker.AddRemoveRecipeMessage;
import com.mojang.logging.LogUtils;
import com.wmaxlees.gregcolonies.api.inventory.container.ContainerCraftingPlayerDefined;
import com.wmaxlees.gregcolonies.api.inventory.container.slots.FakeSlot;
import com.wmaxlees.gregcolonies.api.util.constant.Constants;
import com.wmaxlees.gregcolonies.core.colony.buildings.moduleviews.PlayerDefinedCraftingModuleView;
import com.wmaxlees.gregcolonies.core.network.messages.inventory.InventoryAction;
import com.wmaxlees.gregcolonies.core.network.messages.inventory.InventoryActionMessage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

/** AbstractCrafting gui. */
public class WindowPlayerDefinedCrafting
    extends AbstractContainerScreen<ContainerCraftingPlayerDefined> {
  // Directly reference a slf4j logger
  private static final Logger LOGGER = LogUtils.getLogger();

  private static final ResourceLocation CRAFTING_TABLE_GUI_TEXTURES3X3 =
      new ResourceLocation(Constants.MINECOLONIES_MOD_ID, "textures/gui/crafting3x3.png");

  /** X offset of the button. */
  private static final int BUTTON_X_OFFSET = 1;

  /** Y offset of the button. */
  private static final int BUTTON_Y_POS = 170;

  /** Button width. */
  private static final int BUTTON_WIDTH = 150;

  /** Button height. */
  private static final int BUTTON_HEIGHT = 20;

  /** Color of the gui description. */
  private static final int GUI_COLOR = 4_210_752;

  /** X offset of the gui description. */
  private static final int X_OFFSET = 97;

  /** Y offset of the gui description. */
  private static final int Y_OFFSET = 8;

  /** Size of the crafting grid. */
  private static final int MAX_CRAFTING_GRID_SIZE = 9;

  /** The building the window belongs to. */
  private final AbstractBuildingView building;

  /** The module view. */
  private final PlayerDefinedCraftingModuleView module;

  /**
   * Create a crafting gui window.
   *
   * @param container the container.
   * @param playerInventory the player inv.
   * @param iTextComponent the display text component.
   */
  public WindowPlayerDefinedCrafting(
      final ContainerCraftingPlayerDefined container,
      final Inventory playerInventory,
      final Component iTextComponent) {
    super(container, playerInventory, iTextComponent);
    this.building =
        (AbstractBuildingView)
            IColonyManager.getInstance()
                .getBuildingView(playerInventory.player.level().dimension(), container.getPos());
    this.module = (PlayerDefinedCraftingModuleView) building.getModuleView(container.getModuleId());
  }

  @NotNull
  public AbstractBuildingView getBuildingView() {
    return building;
  }

  @Override
  protected void init() {
    super.init();
    final Component buttonDisplay =
        Component.translatable(
            module.canLearn(ModCraftingTypes.SMALL_CRAFTING.get())
                ? BASE_GUI_DONE
                : WARNING_MAXIMUM_NUMBER_RECIPES);
    /*
     * The button to click done after finishing the recipe.
     */
    final Button doneButton =
        Button.builder(buttonDisplay, this::onDoneClicked)
            .pos(leftPos + BUTTON_X_OFFSET, topPos + BUTTON_Y_POS)
            .size(BUTTON_WIDTH, BUTTON_HEIGHT)
            .build();
    this.addRenderableWidget(doneButton);
    if (!module.canLearn(ModCraftingTypes.SMALL_CRAFTING.get())) {
      doneButton.active = false;
    }
  }

  private void onDoneClicked(final Button button) {
    final List<ItemStorage> input = new LinkedList<>();

    for (int i = 0; i < MAX_CRAFTING_GRID_SIZE; i++) {
      final ItemStack stack = menu.craftMatrix.getItem(i);
      final ItemStack copy = stack.copy();
      ItemStackUtils.setSize(copy, 1);

      input.add(new ItemStorage(copy));
    }

    final ItemStack primaryOutput = menu.result.getItem(0).copy();

    if (!ItemStackUtils.isEmpty(primaryOutput)) {
      Network.getNetwork()
          .sendToServer(
              new AddRemoveRecipeMessage(
                  building,
                  input,
                  3,
                  primaryOutput,
                  new ArrayList<ItemStack>(),
                  false,
                  module.getProducer().getRuntimeID()));
    }
  }

  /** Draw the foreground layer for the GuiContainer (everything in front of the items) */
  @Override
  protected void renderLabels(
      @NotNull final GuiGraphics stack, final int mouseX, final int mouseY) {
    stack.drawString(
        this.font,
        Component.translatable("container.crafting").getString(),
        X_OFFSET,
        Y_OFFSET,
        GUI_COLOR,
        false);
  }

  /** Draws the background layer of this container (behind the items). */
  @Override
  protected void renderBg(
      @NotNull final GuiGraphics stack,
      final float partialTicks,
      final int mouseX,
      final int mouseY) {
    final ResourceLocation texture;
    texture = CRAFTING_TABLE_GUI_TEXTURES3X3;
    stack.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
  }

  @Override
  public void render(@NotNull final GuiGraphics stack, int x, int y, float z) {
    this.renderBackground(stack);
    super.render(stack, x, y, z);
    this.renderTooltip(stack, x, y);
  }

  @Override
  protected void slotClicked(
      @Nullable Slot slot, int slotIdx, int mouseButton, @NotNull ClickType clickType) {
    if (slot instanceof FakeSlot) {
      var action =
          mouseButton == 1
              ? InventoryAction.SPLIT_OR_PLACE_SINGLE
              : InventoryAction.PICKUP_OR_SET_DOWN;
      var p = new InventoryActionMessage(action, slotIdx);
      com.wmaxlees.gregcolonies.core.Network.getNetwork().sendToServer(p);

      return;
    }

    super.slotClicked(slot, slotIdx, mouseButton, clickType);
  }

  @Override
  public boolean mouseScrolled(double x, double y, double delta) {
    if (delta != 0) {
      if (getSlotUnderMouse() instanceof FakeSlot fakeSlot) {
        InventoryActionMessage p;
        if (delta > 0) {
          p = new InventoryActionMessage(InventoryAction.ROLL_UP, fakeSlot.getSlotIndex() + 1);
        } else {
          p = new InventoryActionMessage(InventoryAction.ROLL_DOWN, fakeSlot.getSlotIndex() + 1);
        }
        com.wmaxlees.gregcolonies.core.Network.getNetwork().sendToServer(p);
        return false;
      }
    }
    return super.mouseScrolled(x, y, delta);
  }
}
