package com.windanesz.spellbundle.spell.treasure2;

import com.someguyssoftware.treasure2.entity.monster.BoundSoulEntity;
import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.spell.SpellDynamicMinion;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class SummonBoundSoul extends SpellDynamicMinion<BoundSoulEntity> {

	private static final String MIN_DISTANCE = "minimum_distance";
	public SummonBoundSoul() {
		super(SpellBundle.MODID, "summon_bound_soul", BoundSoulEntity::new);
		addProperties(MIN_DISTANCE);
	}

	@Override
	protected boolean extraConditions(BlockPos pos, @Nullable EntityLivingBase caster, SpellModifiers modifier, World world) {
		if (!EntityUtils.getEntitiesWithinRadius(getProperty(MIN_DISTANCE).floatValue(), pos.getX(), pos.getY(), pos.getZ(), world,
				BoundSoulEntity.class).isEmpty()) {

			if (caster instanceof EntityPlayer && !world.isRemote) {
				((EntityPlayer) caster).sendStatusMessage(new TextComponentTranslation("spell.spellbundle:summon_bound_soul.minion_distance_not_met"), true);
			}
			return false;
		}

		return true;
	}

	@Override
	protected void addMinionExtras(BoundSoulEntity minion, BlockPos pos, @Nullable EntityLivingBase caster, SpellModifiers modifiers, int alreadySpawned) {
		super.addMinionExtras(minion, pos, caster, modifiers, alreadySpawned);

		// set the home position
		minion.setHomePosAndDistance(pos, 10);
	}
}
