package net.nimbu.themastersword.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.nimbu.themastersword.items.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemRenderMixin {


    @Inject(
            method = "render",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderMixin(
            ItemStack stack,
            ItemDisplayContext displayContext,
            boolean leftHanded,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int light,
            int overlay,
            BakedModel model,
            CallbackInfo ci
    ) {
        if (stack.is(ModItems.MASTER_SWORD.get())) {
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            itemRenderer.render(Items.IRON_SWORD.getDefaultInstance(), displayContext, leftHanded, poseStack, buffer, 0xF000F0, OverlayTexture.NO_OVERLAY, model);
            ci.cancel();
        }
    }
}
