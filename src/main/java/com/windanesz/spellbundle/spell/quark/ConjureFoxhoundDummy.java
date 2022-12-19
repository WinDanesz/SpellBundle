package com.windanesz.spellbundle.spell.quark;

import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.integration.quark.QuarkIntegration;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ConjureFoxhoundDummy extends Spell {

	public static final String MINION_LIFETIME = "minion_lifetime";
	public static final String MINION_COUNT = "minion_count";
	public static final String SUMMON_RADIUS = "summon_radius";

	public ConjureFoxhoundDummy() {
		super(SpellBundle.MODID, "conjure_foxhound", SpellActions.POINT, false);
		this.soundValues(1, 1.1f, 0.2f);
		addProperties(MINION_LIFETIME, MINION_COUNT, SUMMON_RADIUS);
		this.setEnabled(false);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}

	/**
	 * Returns the disabled spell desc.
	 */
	protected String getDescriptionTranslationKey() { return QuarkIntegration.getInstance().getMissingSpellDesc(); }
}
