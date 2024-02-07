package com.windanesz.spellbundle.spell.biomesoplenty;

import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.integration.biomesoplenty.BiomesOPlentyIntegration;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.SpellRay;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ConvulsiveCurseDummy extends SpellRay {

	public ConvulsiveCurseDummy() {
		super(SpellBundle.MODID, "convulsive_curse", SpellActions.POINT, false);
		this.soundValues(0.7F, 1.0F, 0.4F);
		this.addProperties(EFFECT_DURATION);
	}

	/**
	 * Returns the disabled spell desc.
	 */
	protected String getDescriptionTranslationKey() {return BiomesOPlentyIntegration.getInstance().getMissingSpellDesc();}

	@Override
	public boolean canBeCastBy(EntityLiving npc, boolean override) {
		return false;
	}

	@Override
	protected boolean onEntityHit(World world, Entity entity, Vec3d vec3d,
			@Nullable EntityLivingBase entityLivingBase, Vec3d vec3d1, int i, SpellModifiers spellModifiers) {
		return false;
	}

	@Override
	protected boolean onBlockHit(World world, BlockPos blockPos, EnumFacing enumFacing, Vec3d vec3d,
			@Nullable EntityLivingBase entityLivingBase, Vec3d vec3d1, int i, SpellModifiers spellModifiers) {
		return false;
	}

	@Override
	protected boolean onMiss(World world, @Nullable EntityLivingBase entityLivingBase, Vec3d vec3d, Vec3d vec3d1, int i, SpellModifiers spellModifiers) {
		return false;
	}
}
