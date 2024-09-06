package com.wmaxlees.gregcolonies.core.entity.ai.workers.crafting;

import static com.minecolonies.api.entity.ai.statemachine.states.AIWorkerState.IDLE;
import static com.minecolonies.api.entity.ai.statemachine.states.AIWorkerState.START_WORKING;
import static com.minecolonies.api.util.constant.Constants.TICKS_SECOND;
import static com.wmaxlees.gregcolonies.core.entity.ai.statemachine.states.AIWorkerState.CRAFT_TOOL;

import com.minecolonies.api.entity.ai.statemachine.AITarget;
import com.minecolonies.api.entity.ai.statemachine.states.IAIState;
import com.minecolonies.api.util.WorldUtil;
import com.minecolonies.core.entity.ai.workers.crafting.AbstractEntityAICrafting;
import com.wmaxlees.gregcolonies.core.colony.buildings.modules.ToolmakerToolsModule;
import com.wmaxlees.gregcolonies.core.colony.buildings.modules.ToolmakerWorkordersModule;
import com.wmaxlees.gregcolonies.core.colony.buildings.workerbuildings.BuildingToolPartSmith;
import com.wmaxlees.gregcolonies.core.colony.jobs.JobToolPartSmith;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EntityAIWorkToolPartSmith
    extends AbstractEntityAICrafting<JobToolPartSmith, BuildingToolPartSmith> {
  /**
   * Constructor for the Toolmaker. Defines the tasks the toolmaker executes.
   *
   * @param job a toolmaker job to use.
   */
  public EntityAIWorkToolPartSmith(@NotNull final JobToolPartSmith job) {
    super(job);
    worker.setCanPickUpLoot(true);
  }

  @Override
  public Class<BuildingToolPartSmith> getExpectedBuildingClass() {
    return BuildingToolPartSmith.class;
  }
}
