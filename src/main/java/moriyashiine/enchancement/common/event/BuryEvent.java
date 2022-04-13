package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.common.component.entity.BuryComponent;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import moriyashiine.enchancement.common.registry.ModSoundEvents;
import moriyashiine.enchancement.common.registry.ModTags;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BuryEvent implements UseEntityCallback {
	@Override
	public ActionResult interact(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
		if (!entity.getType().isIn(ModTags.EntityTypes.CANNOT_BURY) && entity instanceof LivingEntity living) {
			ItemStack stack = player.getStackInHand(hand);
			if (EnchantmentHelper.getLevel(ModEnchantments.BURY, stack) > 0) {
				BuryComponent buryComponent = ModEntityComponents.BURY.get(living);
				if (buryComponent.getBuryPos() == null) {
					BlockPos down = entity.getBlockPos().down();
					BlockState state = world.getBlockState(down);
					if (state.isIn(ModTags.Blocks.BURIABLE) && state.isFullCube(world, down)) {
						if (!world.isClient) {
							world.playSoundFromEntity(null, entity, ModSoundEvents.ENTITY_GENERIC_BURY, entity.getSoundCategory(), 1, 1);
							stack.damage(1, player, stackUser -> stackUser.sendToolBreakStatus(hand));
						} else {
							BlockStateParticleEffect particle = new BlockStateParticleEffect(ParticleTypes.BLOCK, state);
							for (int i = 0; i < 24; i++) {
								world.addParticle(particle, entity.getParticleX(1), entity.getRandomBodyY(), entity.getParticleZ(1), 0, 0, 0);
							}
						}
						buryComponent.setBuryPos(down);
						return ActionResult.success(world.isClient);
					}
				}
			}
		}
		return ActionResult.PASS;
	}
}
