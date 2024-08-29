package com.wmaxlees.gregcolonies.core.colony.jobs;

import com.minecolonies.api.client.render.modeltype.ModModelTypes;
import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.core.colony.jobs.AbstractJobCrafter;
import com.wmaxlees.gregcolonies.core.entity.ai.workers.crafting.EntityAIWorkToolPartSmith;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class JobToolPartSmith
    extends AbstractJobCrafter<EntityAIWorkToolPartSmith, JobToolPartSmith> {
  /**
   * Create a toolmaker job.
   *
   * @param entity the toolmaker.
   */
  public JobToolPartSmith(final ICitizenData entity) {
    super(entity);
  }

  /**
   * Get the RenderBipedCitizen.Model to use when the Citizen performs this job role.
   *
   * @return Model of the citizen.
   */
  @NotNull
  @Override
  public ResourceLocation getModel() {
    // TODO(Wmaxlees): Update this to something that makes sense.
    return ModModelTypes.BLACKSMITH_ID;
  }

  /**
   * Generate your AI class to register.
   *
   * @return your personal AI instance.
   */
  @NotNull
  @Override
  public EntityAIWorkToolPartSmith generateAI() {
    return new EntityAIWorkToolPartSmith(this);
  }
}
