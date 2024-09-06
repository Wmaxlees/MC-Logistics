package com.wmaxlees.gregcolonies.apiimp.initializer;

import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.apiimp.initializer.ModJobsInitializer;
import com.minecolonies.core.colony.jobs.views.CrafterJobView;
import com.wmaxlees.gregcolonies.api.colony.jobs.ModJobs;
import com.wmaxlees.gregcolonies.api.util.constant.Constants;
import com.wmaxlees.gregcolonies.core.colony.jobs.JobMachinist;
import com.wmaxlees.gregcolonies.core.colony.jobs.JobToolPartSmith;
import com.wmaxlees.gregcolonies.core.colony.jobs.JobToolmaker;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;

public final class GregColoniesModJobsInitializer {
  public void RegisterJobs() {
    ModJobs.toolmaker =
        register(
            ModJobs.TOOLMAKER_ID.getPath(),
            () ->
                new JobEntry.Builder()
                    .setJobProducer(JobToolmaker::new)
                    .setJobViewProducer(() -> CrafterJobView::new)
                    .setRegistryName(ModJobs.TOOLMAKER_ID)
                    .createJobEntry());

    ModJobs.toolpartsmith =
        register(
            ModJobs.TOOL_PART_SMITH_ID.getPath(),
            () ->
                new JobEntry.Builder()
                    .setJobProducer(JobToolPartSmith::new)
                    .setJobViewProducer(() -> CrafterJobView::new)
                    .setRegistryName(ModJobs.TOOL_PART_SMITH_ID)
                    .createJobEntry());

    ModJobs.machinist =
        register(
            ModJobs.MACHINIST_ID.getPath(),
            () ->
                new JobEntry.Builder()
                    .setJobProducer(JobMachinist::new)
                    .setJobViewProducer(() -> CrafterJobView::new)
                    .setRegistryName(ModJobs.MACHINIST_ID)
                    .createJobEntry());
  }

  /**
   * Register a job at the deferred registry and store the job token in the job list.
   *
   * @param path the path.
   * @param supplier the supplier of the entry.
   * @return the registry object.
   */
  private static RegistryObject<JobEntry> register(
      final String path, final Supplier<JobEntry> supplier) {
    com.minecolonies.api.colony.jobs.ModJobs.jobs.add(
        new ResourceLocation(Constants.MINECOLONIES_MOD_ID, path));
    return ModJobsInitializer.DEFERRED_REGISTER.register(path, supplier);
  }
}
