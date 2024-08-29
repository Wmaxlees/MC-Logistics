package com.wmaxlees.gregcolonies.api.util.constant;

import com.minecolonies.api.util.constant.IToolType;
import com.wmaxlees.gregcolonies.api.util.constant.translation.ToolTranslationConstants;
import net.minecraft.network.chat.Component;

public enum ToolType implements IToolType {
  HAMMER("hammer", true, Component.translatable(ToolTranslationConstants.TOOL_TYPE_HAMMER)),
  FILE("file", true, Component.translatable(ToolTranslationConstants.TOOL_TYPE_FILE));

  private final String name;
  private final boolean variableMaterials;
  private final Component displayName;

  private ToolType(
      final String name, final boolean variableMaterials, final Component displayName) {
    this.name = name;
    this.variableMaterials = variableMaterials;
    this.displayName = displayName;
  }

  public String getName() {
    return this.name;
  }

  public boolean hasVariableMaterials() {
    return variableMaterials;
  }

  @Override
  public Component getDisplayName() {
    return displayName;
  }
}
