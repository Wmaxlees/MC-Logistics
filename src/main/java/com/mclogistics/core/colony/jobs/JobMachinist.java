package com.mclogistics.core.colony.jobs;

import com.mclogistics.core.entity.ai.workers.custom.EntityAIWorkMachinist;
import com.minecolonies.api.client.render.modeltype.ModModelTypes;
import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.core.colony.jobs.AbstractJobCrafter;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class JobMachinist extends AbstractJobCrafter<EntityAIWorkMachinist, JobMachinist> {
  public JobMachinist(final ICitizenData entity) {
    super(entity);
  }

  @NotNull
  @Override
  public ResourceLocation getModel() {
    return ModModelTypes.BLACKSMITH_ID;
  }

  @NotNull
  @Override
  public EntityAIWorkMachinist generateAI() {
    return new EntityAIWorkMachinist(this);
  }
}
