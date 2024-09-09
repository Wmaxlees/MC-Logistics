package com.wmaxlees.gregcolonies.core.entity.ai.workers.storage;

import com.minecolonies.core.entity.ai.workers.AbstractEntityAIBasic;
import com.wmaxlees.gregcolonies.core.colony.buildings.workerbuildings.BuildingFluidWarehouse;
import com.wmaxlees.gregcolonies.core.colony.jobs.JobFluidWarehouseManager;
import org.jetbrains.annotations.NotNull;

public class EntityAIWorkFluidWarehouseManager
    extends AbstractEntityAIBasic<JobFluidWarehouseManager, BuildingFluidWarehouse> {
  /**
   * Sets up some important skeleton stuff for every ai.
   *
   * @param job the job class
   */
  public EntityAIWorkFluidWarehouseManager(@NotNull JobFluidWarehouseManager job) {
    super(job);
  }

  @Override
  public Class<BuildingFluidWarehouse> getExpectedBuildingClass() {
    return BuildingFluidWarehouse.class;
  }
}
