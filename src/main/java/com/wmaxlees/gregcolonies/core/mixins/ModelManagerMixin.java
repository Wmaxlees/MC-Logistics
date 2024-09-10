package com.wmaxlees.gregcolonies.core.mixins;

import com.wmaxlees.gregcolonies.api.util.constant.Logger;
import com.wmaxlees.gregcolonies.client.renderer.item.ToolHeadItemRenderer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.fml.ModLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ModelManager.class)
public abstract class ModelManagerMixin {
  @Inject(method = "reload", at = @At(value = "HEAD"))
  private void gregcolonies$loadDynamicModels(
      PreparableReloadListener.PreparationBarrier preparationBarrier,
      ResourceManager resourceManager,
      ProfilerFiller preparationsProfiler,
      ProfilerFiller reloadProfiler,
      Executor backgroundExecutor,
      Executor gameExecutor,
      CallbackInfoReturnable<CompletableFuture<Void>> cir) {
    if (!ModLoader.isLoadingStateValid()) return;

    long startTime = System.currentTimeMillis();
    ToolHeadItemRenderer.reinitModels();
    Logger.InfoLog("GregColonies Model loading took {}ms", System.currentTimeMillis() - startTime);
  }
}
