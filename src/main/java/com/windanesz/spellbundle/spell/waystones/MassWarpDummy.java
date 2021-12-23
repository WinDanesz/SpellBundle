package com.windanesz.spellbundle.spell.waystones;

import com.windanesz.spellbundle.SpellBundle;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class MassWarpDummy extends WarpDummy {

	public MassWarpDummy() {
		super(SpellBundle.MODID, "mass_warp", SpellActions.SUMMON, false);
		this.setEnabled(false);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}

	/**
	 * Returns the translation key for this spell's description.
	 */
	protected String getDescriptionTranslationKey() {
		return "spell." + this.getRegistryName().toString() + ".desc_disabled";
	}
}
