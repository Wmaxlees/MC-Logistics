package com.wmaxlees.gregcolonies.api.colony.buildings;

import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import net.minecraftforge.registries.RegistryObject;

public class ModBuildings {
  public static final String TOOLMAKER_ID = "toolmaker";

  public static RegistryObject<BuildingEntry> toolmaker;

  private ModBuildings() {
    throw new IllegalStateException(
        "Tried to initialize: ModBuildings but this is a Utility class.");
  }
}
