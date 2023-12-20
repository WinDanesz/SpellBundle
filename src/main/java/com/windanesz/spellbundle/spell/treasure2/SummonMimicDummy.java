package com.windanesz.spellbundle.spell.treasure2;

import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.spell.SpellDynamicMinion;
import com.windanesz.spellbundle.integration.treasure2.Treasure2Integration;
import net.minecraft.entity.monster.EntityZombie;

public class SummonMimicDummy extends SpellDynamicMinion<EntityZombie> {

	public SummonMimicDummy() {
		super(SpellBundle.MODID, "summon_mimic", EntityZombie::new);
	}

	/**
	 * Returns the disabled spell desc.
	 */
	protected String getDescriptionTranslationKey() { return Treasure2Integration.getInstance().getMissingSpellDesc(); }

}
