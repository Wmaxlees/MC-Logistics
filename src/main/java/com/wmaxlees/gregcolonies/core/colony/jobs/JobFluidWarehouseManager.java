package com.wmaxlees.gregcolonies.core.colony.jobs;

import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.core.colony.jobs.AbstractJob;
import com.wmaxlees.gregcolonies.core.entity.ai.workers.storage.EntityAIWorkFluidWarehouseManager;

public class JobFluidWarehouseManager
    extends AbstractJob<EntityAIWorkFluidWarehouseManager, JobFluidWarehouseManager> {
  /**
   * Instantiates the job for the crafter.
   *
   * @param entity the citizen who becomes a Sawmill
   */
  public JobFluidWarehouseManager(ICitizenData entity) {
    super(entity);
  }

  @Override
  public EntityAIWorkFluidWarehouseManager generateAI() {
    return new EntityAIWorkFluidWarehouseManager(this);
  }
}
