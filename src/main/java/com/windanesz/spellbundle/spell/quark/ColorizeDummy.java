package com.windanesz.spellbundle.spell.quark;

import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.integration.quark.QuarkIntegration;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ColorizeDummy extends Spell {

	public static final String CONSUME_RUNE = "consume_rune";

	public ColorizeDummy() {
		super(SpellBundle.MODID, "colorize", SpellActions.IMBUE, true);
		addProperties(CONSUME_RUNE);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}


	@Override
	public boolean canBeCastBy(EntityLiving npc, boolean override) { return false; }

	@Override
	public boolean canBeCastBy(TileEntityDispenser dispenser) { return false; }

	/**
	 * Returns the disabled spell desc.
	 */
	protected String getDescriptionTranslationKey() { return QuarkIntegration.getInstance().getMissingSpellDesc(); }
}
