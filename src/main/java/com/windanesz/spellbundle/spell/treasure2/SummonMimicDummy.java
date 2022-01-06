package com.windanesz.spellbundle.spell.treasure2;

import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.spell.SpellDynamicMinion;
import net.minecraft.entity.monster.EntityZombie;

public class SummonMimicDummy extends SpellDynamicMinion<EntityZombie> {

	public SummonMimicDummy() {
		super(SpellBundle.MODID, "summon_mimic", EntityZombie::new);
	}
}
