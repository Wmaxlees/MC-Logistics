package com.wmaxlees.gregcolonies.data.pack;

import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;

public class GregColoniesPackSource implements RepositorySource {

  private final String name;
  private final PackType type;
  private final Pack.Position position;
  private final Function<String, PackResources> resources;

  public GregColoniesPackSource(
      String name,
      PackType type,
      Pack.Position position,
      Function<String, PackResources> resources) {
    this.name = name;
    this.type = type;
    this.position = position;
    this.resources = resources;
  }

  @Override
  public void loadPacks(Consumer<Pack> onLoad) {
    onLoad.accept(
        Pack.readMetaAndCreate(
            name,
            Component.literal(name),
            true,
            resources::apply,
            type,
            position,
            PackSource.BUILT_IN));
  }
}
