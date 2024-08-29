package com.wmaxlees.gregcolonies.core.colony.buildings.workerbuildings;

import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.colony.requestsystem.token.IToken;
import com.minecolonies.core.colony.buildings.AbstractBuilding;
import com.minecolonies.core.colony.buildings.modules.AbstractCraftingBuildingModule;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;

public class BuildingToolPartSmith extends AbstractBuilding {
  private static final String TOOLPARTSMITH = "toolpartsmith";

  /**
   * The constructor of the building.
   *
   * @param c the colony
   * @param l the position
   */
  public BuildingToolPartSmith(@NotNull final IColony c, final BlockPos l) {
    super(c, l);
  }

  @NotNull
  @Override
  public String getSchematicName() {
    return TOOLPARTSMITH;
  }

  @Override
  public int getMaxBuildingLevel() {
    return 1;
    // return GTValues.ALL_TIERS.length;
  }

  public static class CraftingModule extends AbstractCraftingBuildingModule.Custom {
    /**
     * Create a new module.
     *
     * @param jobEntry the entry of the job.
     */
    public CraftingModule(final JobEntry jobEntry) {
      super(jobEntry);
    }

    @Override
    public boolean addRecipe(IToken<?> token) {
      // Only custom recipes
      return false;
    }
  }
}
