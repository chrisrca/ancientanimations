package chrisrca.ancientanimations.mixin.client;

import chrisrca.ancientanimations.AncientAnimationsClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(at = @At("HEAD"), method = "use", cancellable = true)
    public void onUseItem(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack stack = user.getStackInHand(hand);
        if (AncientAnimationsClient.isSword(stack.getItem())) {
            user.setCurrentHand(hand);
            cir.setReturnValue(ActionResult.CONSUME);
        }
    }

    @Inject(at = @At("HEAD"), method = "getMaxUseTime", cancellable = true)
    public void onGetMaxUseTime(ItemStack stack, LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        if (AncientAnimationsClient.isSword(stack.getItem())) {
            cir.setReturnValue(72000);
        }
    }

    @Inject(at = @At("HEAD"), method = "getUseAction", cancellable = true)
    public void onGetUseAction(ItemStack stack, CallbackInfoReturnable<UseAction> cir) {
        if (AncientAnimationsClient.isSword(stack.getItem())) {
            cir.setReturnValue(UseAction.BLOCK);
        }
    }
}