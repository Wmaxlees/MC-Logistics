package com.mclogistics.api.colony.jobs;

import com.mclogistics.api.util.constant.Constants;
import com.mclogistics.core.colony.jobs.JobFluidWarehouseManager;
import com.mclogistics.core.colony.jobs.JobMachinist;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.core.colony.jobs.views.CrafterJobView;
import com.minecolonies.core.colony.jobs.views.DefaultJobView;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ModJobs {
  public static final ResourceLocation MACHINIST_ID =
      new ResourceLocation(Constants.MOD_ID, "machinist");
  public static final ResourceLocation FLUID_WAREHOUSE_MANAGER_ID =
      new ResourceLocation(Constants.MOD_ID, "fluidwarehousemanager");

  public static RegistryObject<JobEntry> machinist;
  public static RegistryObject<JobEntry> fluidwarehousemanager;

  private ModJobs() {
    throw new IllegalStateException("Tried to initialize: ModJobs but this is a Utility class.");
  }

  public static final DeferredRegister<JobEntry> DEFERRED_REGISTER =
      DeferredRegister.create(
          new ResourceLocation(Constants.MINECOLONIES_MOD_ID, "jobs"), Constants.MOD_ID);

  static {
    machinist =
        DEFERRED_REGISTER.register(
            MACHINIST_ID.getPath(),
            () ->
                new JobEntry.Builder()
                    .setJobProducer(JobMachinist::new)
                    .setJobViewProducer(() -> CrafterJobView::new)
                    .setRegistryName(ModJobs.MACHINIST_ID)
                    .createJobEntry());

    fluidwarehousemanager =
        DEFERRED_REGISTER.register(
            FLUID_WAREHOUSE_MANAGER_ID.getPath(),
            () ->
                new JobEntry.Builder()
                    .setJobProducer(JobFluidWarehouseManager::new)
                    .setJobViewProducer(() -> DefaultJobView::new)
                    .setRegistryName(ModJobs.FLUID_WAREHOUSE_MANAGER_ID)
                    .createJobEntry());

    // REMOVE THESE ONCE SOUNDS ARE NOT RELIANT ON THIS ARRAY
    com.minecolonies.api.colony.jobs.ModJobs.jobs.add(ModJobs.MACHINIST_ID);
    com.minecolonies.api.colony.jobs.ModJobs.jobs.add(ModJobs.FLUID_WAREHOUSE_MANAGER_ID);
  }
}
