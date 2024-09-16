package com.mclogistics.core.colony.jobs;

import com.mclogistics.core.entity.ai.workers.storage.EntityAIWorkItemWarehouseManager;
import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.core.colony.jobs.AbstractJob;

public class JobItemWarehouseManager
    extends AbstractJob<EntityAIWorkItemWarehouseManager, JobItemWarehouseManager> {
  public JobItemWarehouseManager(ICitizenData entity) {
    super(entity);
  }

  @Override
  public EntityAIWorkItemWarehouseManager generateAI() {
    return new EntityAIWorkItemWarehouseManager(this);
  }
}
