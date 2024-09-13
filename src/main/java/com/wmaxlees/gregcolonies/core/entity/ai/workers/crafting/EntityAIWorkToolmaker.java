package com.wmaxlees.gregcolonies.core.entity.ai.workers.crafting;

import static com.minecolonies.api.entity.ai.statemachine.states.AIWorkerState.*;
import static com.wmaxlees.gregcolonies.core.entity.ai.statemachine.states.AIWorkerState.*;

import com.minecolonies.core.entity.ai.workers.crafting.AbstractEntityAICrafting;
import com.wmaxlees.gregcolonies.core.colony.buildings.workerbuildings.BuildingToolmaker;
import com.wmaxlees.gregcolonies.core.colony.jobs.JobToolmaker;
import org.jetbrains.annotations.NotNull;

public class EntityAIWorkToolmaker
    extends AbstractEntityAICrafting<JobToolmaker, BuildingToolmaker> {
  /**
   * Constructor for the Toolmaker. Defines the tasks the toolmaker executes.
   *
   * @param job a toolmaker job to use.
   */
  public EntityAIWorkToolmaker(@NotNull final JobToolmaker job) {
    super(job);
    worker.setCanPickUpLoot(true);
  }

  @Override
  public Class<BuildingToolmaker> getExpectedBuildingClass() {
    return BuildingToolmaker.class;
  }
}
