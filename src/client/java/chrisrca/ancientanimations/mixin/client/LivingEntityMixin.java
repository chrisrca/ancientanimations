package chrisrca.ancientanimations.mixin.client;

import chrisrca.ancientanimations.AncientAnimationsClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
        if (!(((Object) this) instanceof LivingEntity living)) return;

        ItemStack mainHand = living.getMainHandStack();
        ItemStack offHand  = living.getOffHandStack();

        boolean activeIsSword = AncientAnimationsClient.isSword(this.activeItemStack.getItem());

        // If either hand holds a shield, don't trigger animation
        boolean shieldPresent = mainHand.isOf(Items.SHIELD) || offHand.isOf(Items.SHIELD);
        if (shieldPresent) return;

        if (activeIsSword && this.isUsingItem()) {
            cir.setReturnValue(true);
        }
    }
}