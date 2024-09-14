package com.mclogistics.core.colony.jobs;

import com.mclogistics.core.entity.ai.workers.storage.EntityAIWorkFluidWarehouseManager;
import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.core.colony.jobs.AbstractJob;

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
