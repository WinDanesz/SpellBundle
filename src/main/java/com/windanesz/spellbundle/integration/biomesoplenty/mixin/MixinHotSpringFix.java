package com.windanesz.spellbundle.integration.biomesoplenty.mixin;

import biomesoplenty.common.fluids.blocks.BlockHotSpringWaterFluid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BlockHotSpringWaterFluid.class)
public class MixinHotSpringFix {

	/**
	 * @author WinDanesz
	 * @reason This fixes the broken regeneration effect granted by hot spring water
	 */
	@Overwrite
	public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
		if (entity instanceof EntityLivingBase) {
			if (entity.ticksExisted % 20 == 0) {
				((EntityLivingBase) entity).heal(0.5f);
			}
		}
	}
}