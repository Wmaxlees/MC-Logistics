package com.wmaxlees.gregcolonies.core.colony.buildings.workerbuildings;

import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.colony.requestsystem.token.IToken;
import com.minecolonies.api.compatibility.Compatibility;
import com.minecolonies.api.crafting.IGenericRecipe;
import com.minecolonies.api.util.CraftingUtils;
import com.minecolonies.api.util.OptionalPredicate;
import com.minecolonies.core.colony.buildings.AbstractBuilding;
import com.minecolonies.core.colony.buildings.modules.AbstractCraftingBuildingModule;
import com.gregtechceu.gtceu.api.GTValues;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import java.util.Optional;

import static com.wmaxlees.gregcolonies.api.util.constant.TagConstants.CRAFTING_TOOLSMITH;

public class BuildingToolmaker extends AbstractBuilding {
    private static final String TOOLMAKER = "toolmaker";

    /**
     * The constructor of the building.
     *
     * @param c the colony
     * @param l the position
     */
    public BuildingToolmaker(@NotNull final IColony c, final BlockPos l) {
        super(c, l);
    }

    @NotNull
    @Override
    public String getSchematicName() {
        return TOOLMAKER;
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
