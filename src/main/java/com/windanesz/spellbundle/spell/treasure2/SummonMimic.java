package com.windanesz.spellbundle.spell.treasure2;

import com.someguyssoftware.treasure2.entity.monster.WoodMimicEntity;
import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.spell.SpellDynamicMinion;

public class SummonMimic extends SpellDynamicMinion<WoodMimicEntity> {

	public SummonMimic() {
		super(SpellBundle.MODID, "summon_mimic", WoodMimicEntity::new);
	}
}
