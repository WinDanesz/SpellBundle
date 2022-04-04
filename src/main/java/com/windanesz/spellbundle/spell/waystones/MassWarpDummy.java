package com.windanesz.spellbundle.spell.waystones;

import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.integration.waystones.WaystonesIntegration;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class MassWarpDummy extends WarpDummy {

	public MassWarpDummy() {
		super(SpellBundle.MODID, "mass_warp", SpellActions.SUMMON, false);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}

	/**
	 * Returns the disabled spell desc.
	 */
	protected String getDescriptionTranslationKey() { return WaystonesIntegration.getInstance().getMissingSpellDesc(); }
}
