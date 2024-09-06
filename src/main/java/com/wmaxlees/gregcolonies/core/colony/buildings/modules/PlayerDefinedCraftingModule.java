package com.wmaxlees.gregcolonies.core.colony.buildings.modules;

import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.crafting.IGenericRecipe;
import com.minecolonies.api.crafting.ModCraftingTypes;
import com.minecolonies.api.crafting.registry.CraftingType;
import com.minecolonies.core.colony.buildings.modules.AbstractCraftingBuildingModule;
import com.wmaxlees.gregcolonies.api.util.constant.BuildingConstants;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class PlayerDefinedCraftingModule extends AbstractCraftingBuildingModule {
  /**
   * Create a new module.
   *
   * @param jobEntry the entry of the job.
   */
  public PlayerDefinedCraftingModule(JobEntry jobEntry) {
    super(jobEntry);
  }

  @Override
  public boolean canLearn(CraftingType type) {
    return type == ModCraftingTypes.SMALL_CRAFTING.get()
        || type == ModCraftingTypes.LARGE_CRAFTING.get();
  }

  @Override
  public Set<CraftingType> getSupportedCraftingTypes() {
    return Set.of(ModCraftingTypes.SMALL_CRAFTING.get(), ModCraftingTypes.LARGE_CRAFTING.get());
  }

  @Override
  public boolean isRecipeCompatible(@NotNull IGenericRecipe recipe) {
    return true;
  }

  @Override
  public @NotNull String getId() {
    return BuildingConstants.MODULE_MACHINIST;
  }
}
