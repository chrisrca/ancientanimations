package chrisrca.ancientanimations.mixin.client;

import chrisrca.ancientanimations.AncientAnimationsClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow protected ItemStack activeItemStack;
    @Shadow public abstract boolean isUsingItem();

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(at = @At("HEAD"), method = "isBlocking", cancellable = true)
    public void makeFakeBlockingOnSword(CallbackInfoReturnable<Boolean> cir) {
        if (AncientAnimationsClient.isSword(this.activeItemStack.getItem()) && this.isUsingItem()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(at = @At("RETURN"), method = "getHandSwingDuration", cancellable = true)
    public void onGetHandSwingDuration(CallbackInfoReturnable<Integer> cir) {
        AncientAnimationsClient instance = AncientAnimationsClient.getInstance();
        if (instance == null || instance.config == null) return;
        double mult = instance.config.swingSpeedMultiplier;
        if (mult == 1.0) return;
        int original = cir.getReturnValue();
        int modified = (int) Math.max(1, Math.round(original * mult));
        cir.setReturnValue(modified);
    }
}