package com.wmaxlees.gregcolonies.core.entity.ai.workers.crafting;

import static com.minecolonies.api.entity.ai.statemachine.states.AIWorkerState.*;
import static com.minecolonies.api.util.constant.Constants.TICKS_SECOND;
import static com.wmaxlees.gregcolonies.core.entity.ai.statemachine.states.AIWorkerState.*;

import com.minecolonies.api.entity.ai.statemachine.AITarget;
import com.minecolonies.api.entity.ai.statemachine.states.IAIState;
import com.minecolonies.api.util.WorldUtil;
import com.minecolonies.core.entity.ai.workers.crafting.AbstractEntityAICrafting;
import com.wmaxlees.gregcolonies.core.colony.buildings.modules.ToolmakerToolsModule;
import com.wmaxlees.gregcolonies.core.colony.buildings.modules.ToolmakerWorkordersModule;
import com.wmaxlees.gregcolonies.core.colony.buildings.workerbuildings.BuildingToolmaker;
import com.wmaxlees.gregcolonies.core.colony.jobs.JobToolmaker;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EntityAIWorkToolmaker
    extends AbstractEntityAICrafting<JobToolmaker, BuildingToolmaker> {
  /**
   * Constructor for the Toolmaker. Defines the tasks the toolmaker executes.
   *
   * @param job a toolmaker job to use.
   */
  @SuppressWarnings("unchecked")
  public EntityAIWorkToolmaker(@NotNull final JobToolmaker job) {
    super(job);
    super.registerTargets(new AITarget<IAIState>(CRAFT_TOOL, this::craftTools, TICKS_SECOND));
    worker.setCanPickUpLoot(true);
  }

  @Override
  public Class<BuildingToolmaker> getExpectedBuildingClass() {
    return BuildingToolmaker.class;
  }

  @Override
  protected IAIState decide() {
    worker.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
    if (walkToBuilding()) {
      return START_WORKING;
    }

    final IAIState craftState = getNextCraftingState();
    if (craftState != START_WORKING && !WorldUtil.isPastTime(world, 6000)) {
      return craftState;
    }

    if (wantInventoryDumped()) {
      // Wait to dump before continuing.
      return getState();
    }

    // TODO(Wmaxlees): Implement this

    return IDLE;
  }

  private ResourceLocation getNextCraftableTool() {
    Set<ResourceLocation> workOrders =
        building.getFirstModuleOccurance(ToolmakerWorkordersModule.class).getWorkorders();
    Set<ResourceLocation> toolsToKeepStocked =
        building.getFirstModuleOccurance(ToolmakerToolsModule.class).getToolsToKeepStocked();

    // TODO(Wmaxlees): Implement this
    for (Iterator<ResourceLocation> it = workOrders.iterator(); it.hasNext(); ) {
      return it.next();
    }

    for (Iterator<ResourceLocation> it = toolsToKeepStocked.iterator(); it.hasNext(); ) {
      return it.next();
    }

    return null;
  }

  private IAIState craftTools() {
    return IDLE;
  }
}
