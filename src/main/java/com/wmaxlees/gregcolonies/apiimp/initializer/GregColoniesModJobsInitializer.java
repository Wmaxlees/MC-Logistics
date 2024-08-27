package com.wmaxlees.gregcolonies.apiimp.initializer;

import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.apiimp.initializer.ModJobsInitializer;
import com.minecolonies.core.colony.jobs.views.CrafterJobView;
import com.wmaxlees.gregcolonies.api.colony.jobs.ModJobs;
import com.wmaxlees.gregcolonies.api.util.constant.Constants;
import com.wmaxlees.gregcolonies.core.colony.jobs.JobToolmaker;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public final class GregColoniesModJobsInitializer {
    public void RegisterJobs() {
        ModJobs.toolmaker = register(ModJobsInitializer.DEFERRED_REGISTER, ModJobs.TOOLMAKER_ID.getPath(),
                () -> new JobEntry.Builder()
                        .setJobProducer(JobToolmaker::new)
                        .setJobViewProducer(() -> CrafterJobView::new)
                        .setRegistryName(ModJobs.TOOLMAKER_ID)
                        .createJobEntry());
    }

    /**
     * Register a job at the deferred registry and store the job token in the job
     * list.
     * 
     * @param deferredRegister the registry,
     * @param path             the path.
     * @param supplier         the supplier of the entry.
     * @return the registry object.
     */
    private static RegistryObject<JobEntry> register(final DeferredRegister<JobEntry> deferredRegister,
            final String path, final Supplier<JobEntry> supplier) {
        com.minecolonies.api.colony.jobs.ModJobs.jobs.add(new ResourceLocation(Constants.MINECOLONIES_MOD_ID, path));
        return deferredRegister.register(path, supplier);
    }
}
