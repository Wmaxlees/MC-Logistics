package com.wmaxlees.gregcolonies.apiimp.initializer;

import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.core.colony.jobs.views.CrafterJobView;
import com.minecolonies.core.colony.jobs.views.DefaultJobView;
import com.wmaxlees.gregcolonies.api.colony.jobs.ModJobs;
import com.wmaxlees.gregcolonies.api.util.constant.Constants;
import com.wmaxlees.gregcolonies.core.colony.jobs.JobFluidWarehouseManager;
import com.wmaxlees.gregcolonies.core.colony.jobs.JobMachinist;
import com.wmaxlees.gregcolonies.core.colony.jobs.JobToolPartSmith;
import com.wmaxlees.gregcolonies.core.colony.jobs.JobToolmaker;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;

public final class GregColoniesModJobsInitializer {
  public static final DeferredRegister<JobEntry> DEFERRED_REGISTER =
      DeferredRegister.create(
          new ResourceLocation(Constants.MINECOLONIES_MOD_ID, "jobs"), Constants.MOD_ID);

  static {
    ModJobs.toolmaker =
        DEFERRED_REGISTER.register(
            ModJobs.TOOLMAKER_ID.getPath(),
            () ->
                new JobEntry.Builder()
                    .setJobProducer(JobToolmaker::new)
                    .setJobViewProducer(() -> CrafterJobView::new)
                    .setRegistryName(ModJobs.TOOLMAKER_ID)
                    .createJobEntry());

    ModJobs.toolpartsmith =
        DEFERRED_REGISTER.register(
            ModJobs.TOOL_PART_SMITH_ID.getPath(),
            () ->
                new JobEntry.Builder()
                    .setJobProducer(JobToolPartSmith::new)
                    .setJobViewProducer(() -> CrafterJobView::new)
                    .setRegistryName(ModJobs.TOOL_PART_SMITH_ID)
                    .createJobEntry());

    ModJobs.machinist =
        DEFERRED_REGISTER.register(
            ModJobs.MACHINIST_ID.getPath(),
            () ->
                new JobEntry.Builder()
                    .setJobProducer(JobMachinist::new)
                    .setJobViewProducer(() -> CrafterJobView::new)
                    .setRegistryName(ModJobs.MACHINIST_ID)
                    .createJobEntry());

    ModJobs.fluidwarehousemanager =
        DEFERRED_REGISTER.register(
            ModJobs.FLUID_WAREHOUSE_MANAGER_ID.getPath(),
            () ->
                new JobEntry.Builder()
                    .setJobProducer(JobFluidWarehouseManager::new)
                    .setJobViewProducer(() -> DefaultJobView::new)
                    .setRegistryName(ModJobs.FLUID_WAREHOUSE_MANAGER_ID)
                    .createJobEntry());

    // REMOVE THESE ONCE SOUNDS ARE NOT RELIANT ON THIS ARRAY
    com.minecolonies.api.colony.jobs.ModJobs.jobs.add(ModJobs.MACHINIST_ID);
    com.minecolonies.api.colony.jobs.ModJobs.jobs.add(ModJobs.TOOLMAKER_ID);
    com.minecolonies.api.colony.jobs.ModJobs.jobs.add(ModJobs.TOOL_PART_SMITH_ID);
    com.minecolonies.api.colony.jobs.ModJobs.jobs.add(ModJobs.FLUID_WAREHOUSE_MANAGER_ID);
  }
}
