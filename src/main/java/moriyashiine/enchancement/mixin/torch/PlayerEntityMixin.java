/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.torch;

import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
	@Unique
	private static boolean torch = false;

	@ModifyVariable(method = "getProjectileType", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/RangedWeaponItem;getHeldProjectiles()Ljava/util/function/Predicate;"))
	private Predicate<ItemStack> enchancement$torchHeld(Predicate<ItemStack> value, ItemStack stack) {
		if (EnchancementUtil.hasEnchantment(ModEnchantments.TORCH, stack)) {
			value = projectile -> projectile.isOf(Items.TORCH);
		}
		return value;
	}

	@ModifyVariable(method = "getProjectileType", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/RangedWeaponItem;getProjectiles()Ljava/util/function/Predicate;"))
	private Predicate<ItemStack> enchancement$torch(Predicate<ItemStack> value, ItemStack stack) {
		if (EnchancementUtil.hasEnchantment(ModEnchantments.TORCH, stack)) {
			value = projectile -> false;
		}
		return value;
	}

	@Inject(method = "getProjectileType", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;<init>(Lnet/minecraft/item/ItemConvertible;)V"))
	private void enchancement$torch(ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
		torch = EnchancementUtil.hasEnchantment(ModEnchantments.TORCH, stack);
	}

	@ModifyArg(method = "getProjectileType", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;<init>(Lnet/minecraft/item/ItemConvertible;)V"))
	private ItemConvertible enchancement$torch(ItemConvertible value) {
		if (torch) {
			return Items.TORCH;
		}
		return value;
	}
}
