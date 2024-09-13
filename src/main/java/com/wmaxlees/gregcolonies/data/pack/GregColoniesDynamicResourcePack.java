package com.wmaxlees.gregcolonies.data.pack;

import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.gregtechceu.gtceu.api.addon.AddonFinder;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.wmaxlees.gregcolonies.api.util.constant.Constants;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public class GregColoniesDynamicResourcePack implements PackResources {
  protected static final ObjectSet<String> CLIENT_DOMAINS = new ObjectOpenHashSet<>();

  @ApiStatus.Internal
  public static final ConcurrentMap<ResourceLocation, byte[]> DATA = new ConcurrentHashMap<>();

  private final String name;

  static {
    CLIENT_DOMAINS.addAll(Sets.newHashSet(Constants.MOD_ID, "minecraft", "forge", "c"));
  }

  public GregColoniesDynamicResourcePack(String name) {
    this(
        name,
        AddonFinder.getAddons().stream().map(IGTAddon::addonModId).collect(Collectors.toSet()));
  }

  public GregColoniesDynamicResourcePack(String name, Collection<String> domains) {
    this.name = name;
    CLIENT_DOMAINS.addAll(domains);
  }

  public static void clearClient() {
    DATA.clear();
  }

  public static void addItemModel(ResourceLocation loc, JsonElement obj) {
    ResourceLocation l = getItemModelLocation(loc);
    DATA.put(l, obj.toString().getBytes(StandardCharsets.UTF_8));
  }

  public static void addItemModel(ResourceLocation loc, Supplier<JsonElement> obj) {
    addItemModel(loc, obj.get());
  }

  @Nullable
  @Override
  public IoSupplier<InputStream> getRootResource(String... elements) {
    return null;
  }

  @Override
  public IoSupplier<InputStream> getResource(PackType type, ResourceLocation location) {
    if (type == PackType.CLIENT_RESOURCES) {
      if (DATA.containsKey(location)) return () -> new ByteArrayInputStream(DATA.get(location));
    }
    return null;
  }

  @Override
  public void listResources(
      PackType packType, String namespace, String path, ResourceOutput resourceOutput) {
    if (packType == PackType.CLIENT_RESOURCES) {
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
    return type == PackType.CLIENT_RESOURCES ? CLIENT_DOMAINS : Set.of();
  }

  @Nullable
  @Override
  public <T> T getMetadataSection(MetadataSectionSerializer<T> metaReader) {
    if (metaReader == PackMetadataSection.TYPE) {
      return (T)
          new PackMetadataSection(
              Component.literal("GregColonies dynamic assets"),
              SharedConstants.getCurrentVersion().getPackVersion(PackType.CLIENT_RESOURCES));
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

  public static ResourceLocation getItemModelLocation(ResourceLocation itemId) {
    return new ResourceLocation(
        itemId.getNamespace(), String.join("", "models/item/", itemId.getPath(), ".json"));
  }
}
