package com.windanesz.spellbundle.spell.biomesoplenty;

import biomesoplenty.api.block.BOPBlocks;
import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.wizardryutils.tools.WizardryUtilsTools;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.SpellRay;
import electroblob.wizardry.util.BlockUtils;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class HotSpring extends SpellRay {

	public HotSpring() {
		super(SpellBundle.MODID, "hot_spring", SpellActions.POINT, false);
		hitLiquids = true;
		ignoreUncollidables = false;
	}

	@Override
	protected boolean onEntityHit(World world, Entity target, Vec3d hit, EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}

	@Override
	protected boolean onBlockHit(World world, BlockPos pos, EnumFacing side, Vec3d hit, EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		if (caster != null) {
			if (world.getBlockState(pos).getBlock() == Blocks.WATER && BlockUtils.canPlaceBlock(caster, world, pos)) {
				for (EnumFacing facing : EnumFacing.values()) {
					if (world.getBlockState(pos.offset(facing)).getBlock() == Blocks.WATER) {
						WizardryUtilsTools.sendMessage(caster, "spell.spellbundle:hot_spring.water_nearby", true);
						return false;
					}
				}
				world.setBlockState(pos, BOPBlocks.hot_spring_water.getDefaultState());
				return true;
			}

		}
		WizardryUtilsTools.sendMessage(caster, "spell.spellbundle:hot_spring.no_water", true);
		return false;
	}

	@Override
	protected boolean onMiss(World world, EntityLivingBase caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}

}
