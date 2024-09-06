package com.wmaxlees.gregcolonies.api.colony.buildings;

import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import net.minecraftforge.registries.RegistryObject;

public class ModBuildings {
  public static final String TOOL_PART_SMITH_ID = "toolpartsmith";
  public static final String TOOLMAKER_ID = "toolmaker";
  public static final String MACHINIST_ID = "machinist";

  public static RegistryObject<BuildingEntry> toolpartsmith;
  public static RegistryObject<BuildingEntry> toolmaker;
  public static RegistryObject<BuildingEntry> machinist;

  private ModBuildings() {
    throw new IllegalStateException(
        "Tried to initialize: ModBuildings but this is a Utility class.");
  }
}
