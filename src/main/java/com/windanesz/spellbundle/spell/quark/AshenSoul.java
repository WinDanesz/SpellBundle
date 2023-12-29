package com.windanesz.spellbundle.spell.quark;

import com.windanesz.spellbundle.SpellBundle;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import vazkii.quark.base.sounds.QuarkSounds;
import com.windanesz.wizardryutils.tools.WizardryUtilsTools;
import vazkii.quark.misc.entity.EntitySoulPowder;

public class AshenSoul extends Spell {


	public AshenSoul() {
		super(SpellBundle.MODID, "ashen_soul", SpellActions.SUMMON, false);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		if(!world.isRemote) {

			if (caster.world.provider.getDimensionType() != DimensionType.NETHER) {
				WizardryUtilsTools.sendMessage(caster, "spell.spellbundle:ashen_soul.wrong_dimension", true);
				return false;
			}
			BlockPos blockpos = caster.getEntityWorld().findNearestStructure("Fortress", caster.getPosition(), false);

			if(blockpos != null) {
				EntitySoulPowder entity = new EntitySoulPowder(world, blockpos.getX(), blockpos.getZ());
				Vec3d look = caster.getLookVec();
				entity.setPosition(caster.posX + look.x * 2, caster.posY + 0.25, caster.posZ + look.z * 2);
				world.spawnEntity(entity);

				world.playSound(null, caster.posX, caster.posY, caster.posZ, QuarkSounds.ITEM_SOUL_POWDER_SPAWN, SoundCategory.PLAYERS, 1F, 1F);
				return true;
			}
		}
		return false;
	}


	@Override
	public boolean canBeCastBy(EntityLiving npc, boolean override) { return false; }

	@Override
	public boolean canBeCastBy(TileEntityDispenser dispenser) { return false; }

}
