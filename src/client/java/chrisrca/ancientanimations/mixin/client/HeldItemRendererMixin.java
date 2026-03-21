package chrisrca.ancientanimations.mixin.client;

import chrisrca.ancientanimations.AncientAnimationsClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {

    @Inject(method = "applyEquipOffset", at = @At("HEAD"), cancellable = true)
    private void fixEquipOffsetForSword(MatrixStack matrices, Arm arm, float equipProgress, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;

        ItemStack stack = (arm == mc.player.getMainArm())
                ? mc.player.getMainHandStack()
                : mc.player.getOffHandStack();

        if (AncientAnimationsClient.isTool(stack.getItem())) {
            var cfg = AncientAnimationsClient.getInstance().config;
            int i = (arm == Arm.RIGHT) ? 1 : -1;

            matrices.translate(
                    (float) i * 0.56F + (float) cfg.itemPosX,
                    -0.52F + (float) cfg.itemPosY,
                    -0.72F + (float) cfg.itemPosZ
            );

            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float) cfg.itemRotX));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) cfg.itemRotY));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) cfg.itemRotZ));

            float s = (float) cfg.itemScale;
            matrices.scale(s, s, s);

            ci.cancel();
        }
    }

    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemDisplayContext;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;I)V", ordinal = 1))
    private void apply17SwingBeforeRender(
            AbstractClientPlayerEntity player,
            float tickDelta,
            float pitch,
            Hand hand,
            float swingProgress,
            ItemStack item,
            float equipProgress,
            MatrixStack matrices,
            OrderedRenderCommandQueue queue,
            int light,
            CallbackInfo ci
    ) {
        if (!AncientAnimationsClient.isSword(item.getItem())) return;
        if (!player.isUsingItem()) return;
        if (item.getUseAction() != UseAction.BLOCK) return;
        if (AncientAnimationsClient.swing17 == 0f && !AncientAnimationsClient.swing17InProgress()) return;

        float partialTicks = MinecraftClient.getInstance().getRenderTickCounter().getTickProgress(false);
        float swing = AncientAnimationsClient.getSwing17Progress(partialTicks);
        float arc = MathHelper.sin((float) Math.sqrt(swing) * (float) Math.PI);

        var cfg = AncientAnimationsClient.getInstance().config;

        matrices.translate((float)(arc * cfg.swingTransX), 0f, 0f);
        matrices.translate(0f, (float)(arc * cfg.swingTransY), 0f);
        matrices.translate(0f, 0f, (float)(arc * cfg.swingTransZ));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)(arc * cfg.swingRotY)));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float)(arc * cfg.swingRotX)));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)(arc * cfg.swingRotZ)));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)(arc * cfg.swingRotY2)));
    }

    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemDisplayContext;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;I)V", ordinal = 1))
    private void applyEatPunchAnimation(
            AbstractClientPlayerEntity player,
            float tickDelta,
            float pitch,
            Hand hand,
            float swingProgress,
            ItemStack item,
            float equipProgress,
            MatrixStack matrices,
            OrderedRenderCommandQueue queue,
            int light,
            CallbackInfo ci
    ) {
        if (!AncientAnimationsClient.isEatingOrDrinking(item.getItem(), item)) return;
        if (!player.isUsingItem()) return;
        if (player.getHandSwingProgress(tickDelta) <= 0f) return;

        float swing = player.getHandSwingProgress(tickDelta);
        float arc = MathHelper.sin((float) Math.sqrt(swing) * (float) Math.PI);

        matrices.translate(arc * -0.4f, 0f, 0f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(arc * -20.0f));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(arc * -80.0f));
    }

    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemDisplayContext;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;I)V", ordinal = 1))
    private void applyBowPunchAnimation(
            AbstractClientPlayerEntity player,
            float tickDelta,
            float pitch,
            Hand hand,
            float swingProgress,
            ItemStack item,
            float equipProgress,
            MatrixStack matrices,
            OrderedRenderCommandQueue queue,
            int light,
            CallbackInfo ci
    ) {
        if (!AncientAnimationsClient.isDrawingBow(item.getItem(), item)) return;
        if (!player.isUsingItem()) return;

        float swing = player.getHandSwingProgress(tickDelta);
        if (swing <= 0f) return;

        float tx = -0.4F * MathHelper.sin((float) Math.sqrt(swing) * (float) Math.PI);
        float ty =  0.2F * MathHelper.sin((float) Math.sqrt(swing) * (float) Math.PI * 2.0F);
        float tz = -0.2F * MathHelper.sin(swing * (float) Math.PI);
        matrices.translate(tx, ty, tz);

        float f  = MathHelper.sin(swing * swing * (float) Math.PI);
        float f1 = MathHelper.sin((float) Math.sqrt(swing) * (float) Math.PI);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f  * 20.0F));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(f1 * 20.0F));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(f1 * -80.0F));
    }
}