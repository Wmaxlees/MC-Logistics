package com.wmaxlees.gregcolonies.core.entity.ai.workers.custom;

import com.minecolonies.core.entity.ai.workers.AbstractEntityAISkill;
import com.wmaxlees.gregcolonies.core.colony.buildings.workerbuildings.BuildingMachinist;
import com.wmaxlees.gregcolonies.core.colony.jobs.JobMachinist;
import org.jetbrains.annotations.NotNull;

public class EntityAIWorkMachinist extends AbstractEntityAISkill<JobMachinist, BuildingMachinist> {
  /**
   * Sets up some important skeleton stuff for every ai.
   *
   * @param job the job class
   */
  public EntityAIWorkMachinist(@NotNull JobMachinist job) {
    super(job);
  }

  @Override
  public Class<BuildingMachinist> getExpectedBuildingClass() {
    return BuildingMachinist.class;
  }
}
