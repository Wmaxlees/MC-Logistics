package com.wmaxlees.gregcolonies.core.entity.ai.workers.crafting;

import com.minecolonies.core.entity.ai.workers.crafting.AbstractEntityAICrafting;
import com.wmaxlees.gregcolonies.core.colony.buildings.workerbuildings.BuildingToolPartSmith;
import com.wmaxlees.gregcolonies.core.colony.jobs.JobToolPartSmith;
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
