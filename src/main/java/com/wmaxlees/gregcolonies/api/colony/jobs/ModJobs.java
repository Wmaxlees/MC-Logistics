package com.wmaxlees.gregcolonies.api.colony.jobs;

import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.wmaxlees.gregcolonies.api.util.constant.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;

public final class ModJobs {
  public static final ResourceLocation TOOL_PART_SMITH_ID =
      new ResourceLocation(Constants.MOD_ID, "toolpartsmith");
  public static final ResourceLocation TOOLMAKER_ID =
      new ResourceLocation(Constants.MOD_ID, "toolmaker");
  public static final ResourceLocation MACHINIST_ID =
      new ResourceLocation(Constants.MOD_ID, "machinist");
  public static final ResourceLocation FLUID_COURIER_ID =
      new ResourceLocation(Constants.MOD_ID, "fluidcourier");

  public static RegistryObject<JobEntry> toolpartsmith;
  public static RegistryObject<JobEntry> toolmaker;
  public static RegistryObject<JobEntry> machinist;
  public static RegistryObject<JobEntry> fluidcourier;

  private ModJobs() {
    throw new IllegalStateException("Tried to initialize: ModJobs but this is a Utility class.");
  }
}
