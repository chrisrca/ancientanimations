package chrisrca.ancientanimations.mixin.client;

import chrisrca.ancientanimations.AncientAnimationsClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Inject(at = @At("HEAD"), method = "getActiveItemSpeedMultiplier", cancellable = true)
    private void noSlowOnSword(CallbackInfoReturnable<Float> cir) {
        ClientPlayerEntity self = (ClientPlayerEntity)(Object)this;
        // Prevent block slowdown
        if (AncientAnimationsClient.isSword(self.getMainHandStack().getItem()) && self.isUsingItem()) {
            cir.setReturnValue(1.0f);
        }
    }
}