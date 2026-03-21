package chrisrca.ancientanimations.mixin.client;

import chrisrca.ancientanimations.AncientAnimationsClient;
import chrisrca.ancientanimations.config.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(MinecraftClient.class)
public abstract class MinecraftMixin {

    @Shadow public ClientPlayerEntity player;
    private boolean hasSwungThisHold = false;

    @Inject(method = "stop", at = @At("HEAD"))
    public void onClose(CallbackInfo ci) {
        try {
            Config.save(AncientAnimationsClient.getInstance().config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void onTick(CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;
        if (!mc.options.attackKey.isPressed() && !mc.options.useKey.isPressed()) {
            hasSwungThisHold = false;
        } else if (!mc.options.attackKey.isPressed() && mc.options.useKey.isPressed()) {
            hasSwungThisHold = true;
        }
    }

    @Inject(method = "handleBlockBreaking", at = @At("HEAD"), cancellable = true)
    public void onHandleBlockBreaking(boolean breaking, CallbackInfo ci) {
        if (player == null || !breaking) return;

        if (AncientAnimationsClient.isSword(player.getMainHandStack().getItem())) {
            if (!player.isUsingItem()) return;

            MinecraftClient mc = MinecraftClient.getInstance();

            if (!hasSwungThisHold) {
                AncientAnimationsClient.trigger17Swing();
                hasSwungThisHold = true;
            } else {
                if (mc.crosshairTarget == null || mc.crosshairTarget.getType() != HitResult.Type.BLOCK) {
                    ci.cancel();
                    return;
                }
                AncientAnimationsClient.trigger17Swing();
                player.swingHand(Hand.MAIN_HAND);
            }
            ci.cancel();
            return;
        }

        if (AncientAnimationsClient.isEatingOrDrinking(player.getMainHandStack().getItem(), player.getMainHandStack()) && player.isUsingItem()) {
            MinecraftClient mc = MinecraftClient.getInstance();

            if (!hasSwungThisHold) {
                player.swingHand(Hand.MAIN_HAND);
                hasSwungThisHold = true;
            } else {
                if (mc.crosshairTarget == null || mc.crosshairTarget.getType() != HitResult.Type.BLOCK) {
                    ci.cancel();
                    return;
                }
                player.swingHand(Hand.MAIN_HAND);
            }
            ci.cancel();
        }

        if (AncientAnimationsClient.isDrawingBow(player.getMainHandStack().getItem(), player.getMainHandStack()) && player.isUsingItem()) {
            MinecraftClient mc = MinecraftClient.getInstance();

            if (!hasSwungThisHold) {
                player.swingHand(Hand.MAIN_HAND);
                hasSwungThisHold = true;
            } else {
                if (mc.crosshairTarget == null || mc.crosshairTarget.getType() != HitResult.Type.BLOCK) {
                    ci.cancel();
                    return;
                }
                player.swingHand(Hand.MAIN_HAND);
            }
            ci.cancel();
        }
    }
}