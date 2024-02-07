package com.windanesz.spellbundle.spell.biomesoplenty;

import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.integration.biomesoplenty.BiomesOPlentyIntegration;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.SpellRay;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class HotSpringDummy extends SpellRay {

	public HotSpringDummy() {
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
		return false;
	}

	@Override
	protected boolean onMiss(World world, EntityLivingBase caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}

	/**
	 * Returns the disabled spell desc.
	 */
	protected String getDescriptionTranslationKey() {return BiomesOPlentyIntegration.getInstance().getMissingSpellDesc();}
}
