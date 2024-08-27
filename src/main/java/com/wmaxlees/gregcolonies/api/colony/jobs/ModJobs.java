package com.wmaxlees.gregcolonies.api.colony.jobs;

import java.util.ArrayList;
import java.util.List;

import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.wmaxlees.gregcolonies.api.util.constant.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;

public final class ModJobs {
    public static final ResourceLocation TOOLMAKER_ID = new ResourceLocation(Constants.MINECOLONIES_MOD_ID, "toolmaker");

    public static RegistryObject<JobEntry> toolmaker;

    private ModJobs() {
        throw new IllegalStateException("Tried to initialize: ModJobs but this is a Utility class.");
    }

    public static List<ResourceLocation> jobs = new ArrayList<>() {
    };

    public static List<ResourceLocation> getJobs() {
        return jobs;
    }
}
