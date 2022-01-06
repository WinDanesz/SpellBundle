package com.windanesz.spellbundle.spell.treasure2;

import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.spell.SpellDynamicMinion;
import net.minecraft.entity.monster.EntityZombie;

public class SummonBoundSoulDummy extends SpellDynamicMinion<EntityZombie> {

	private static final String MIN_DISTANCE = "minimum_distance";
	public SummonBoundSoulDummy() {
		super(SpellBundle.MODID, "summon_bound_soul", EntityZombie::new);
		addProperties(MIN_DISTANCE);
	}

	/**
	 * Returns the disabled spell desc.
	 */
	protected String getDescriptionTranslationKey() { return "spell.spellbundle:treasure2_missing"; }

}
