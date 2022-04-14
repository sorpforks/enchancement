package moriyashiine.enchancement.mixin.berserk;

import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
	@Inject(method = "finishUsing", at = @At("HEAD"))
	private void enchancement$berserk(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
		if (EnchantmentHelper.getLevel(ModEnchantments.BERSERK, stack) > 0) {
			user.damage(DamageSource.WITHER, 2);
			if (user.isSneaking()) {
				ModEntityComponents.BERSERK.maybeGet(user).ifPresent(berserkComponent -> berserkComponent.setPreventRegenerationTicks(100));
			}
		}
	}

	@Inject(method = "getMaxUseTime", at = @At("HEAD"), cancellable = true)
	private void enchancement$berserkUseTime(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		if (EnchantmentHelper.getLevel(ModEnchantments.BERSERK, stack) > 0) {
			cir.setReturnValue(20);
		}
	}

	@Inject(method = "getUseAction", at = @At("HEAD"), cancellable = true)
	private void enchancement$berserkUseAction(ItemStack stack, CallbackInfoReturnable<UseAction> cir) {
		if (EnchantmentHelper.getLevel(ModEnchantments.BERSERK, stack) > 0) {
			cir.setReturnValue(UseAction.BOW);
		}
	}

	@Inject(method = "use", at = @At("HEAD"), cancellable = true)
	private void enchancement$berserk(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
		ItemStack stack = user.getStackInHand(hand);
		if (EnchantmentHelper.getLevel(ModEnchantments.BERSERK, stack) > 0) {
			user.setCurrentHand(hand);
			cir.setReturnValue(TypedActionResult.consume(stack));
		}
	}
}