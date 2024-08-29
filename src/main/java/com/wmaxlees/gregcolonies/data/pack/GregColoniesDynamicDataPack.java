package com.wmaxlees.gregcolonies.data.pack;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import com.gregtechceu.gtceu.api.addon.AddonFinder;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.wmaxlees.gregcolonies.api.util.constant.Constants;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import net.minecraft.SharedConstants;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.Nullable;

public class GregColoniesDynamicDataPack implements PackResources {
  protected static final ObjectSet<String> SERVER_DOMAINS = new ObjectOpenHashSet<>();
  protected static final Map<ResourceLocation, byte[]> DATA = new HashMap<>();

  private final String name;

  static {
    SERVER_DOMAINS.addAll(Sets.newHashSet(Constants.MOD_ID, "minecraft", "forge", "c"));
  }

  public GregColoniesDynamicDataPack(String name) {
    this(
        name,
        AddonFinder.getAddons().stream().map(IGTAddon::addonModId).collect(Collectors.toSet()));
  }

  public GregColoniesDynamicDataPack(String name, Collection<String> domains) {
    this.name = name;
    SERVER_DOMAINS.addAll(domains);
  }

  public static void clearServer() {
    DATA.clear();
  }

  public static void addRecipe(FinishedRecipe recipe) {
    JsonObject recipeJson = recipe.serializeRecipe();
    ResourceLocation recipeId = recipe.getId();
    DATA.put(getRecipeLocation(recipeId), recipeJson.toString().getBytes(StandardCharsets.UTF_8));
  }

  public static void addCustomRecipe(FinishedRecipe recipe) {
    JsonObject recipeJson = recipe.serializeRecipe();
    ResourceLocation recipeId = recipe.getId();
    DATA.put(
        getCustomCrafterRecipeLocation(recipeId),
        recipeJson.toString().getBytes(StandardCharsets.UTF_8));
  }

  @Nullable
  @Override
  public IoSupplier<InputStream> getRootResource(String... elements) {
    return null;
  }

  @Override
  public IoSupplier<InputStream> getResource(PackType type, ResourceLocation location) {
    if (type == PackType.SERVER_DATA) {
      var byteArray = DATA.get(location);
      if (byteArray != null) return () -> new ByteArrayInputStream(byteArray);
      else return null;
    } else {
      return null;
    }
  }

  @Override
  public void listResources(
      PackType packType, String namespace, String path, ResourceOutput resourceOutput) {
    if (packType == PackType.SERVER_DATA) {
      if (!path.endsWith("/")) path += "/";
      final String finalPath = path;
      DATA.keySet().stream()
          .filter(Objects::nonNull)
          .filter(loc -> loc.getPath().startsWith(finalPath))
          .forEach(
              (id) -> {
                IoSupplier<InputStream> resource = this.getResource(packType, id);
                if (resource != null) {
                  resourceOutput.accept(id, resource);
                }
              });
    }
  }

  @Override
  public Set<String> getNamespaces(PackType type) {
    return type == PackType.SERVER_DATA ? SERVER_DOMAINS : Set.of();
  }

  @Nullable
  @Override
  public <T> T getMetadataSection(MetadataSectionSerializer<T> metaReader) {
    if (metaReader == PackMetadataSection.TYPE) {
      return (T)
          new PackMetadataSection(
              Component.literal("GregColonies dynamic data"),
              SharedConstants.getCurrentVersion().getPackVersion(PackType.SERVER_DATA));
    }
    return null;
  }

  @Override
  public String packId() {
    return this.name;
  }

  @Override
  public void close() {
    // NOOP
  }

  public static ResourceLocation getRecipeLocation(ResourceLocation recipeId) {
    return new ResourceLocation(
        recipeId.getNamespace(), String.join("", "recipes/", recipeId.getPath(), ".json"));
  }

  public static ResourceLocation getCustomCrafterRecipeLocation(ResourceLocation recipeId) {
    return new ResourceLocation(
        recipeId.getNamespace(), String.join("", "crafterrecipes/", recipeId.getPath(), ".json"));
  }
}
