package com.windanesz.spellbundle.registry;

import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.integration.Integration;
import com.windanesz.spellbundle.integration.treasure2.Treasure2Integration;
import com.windanesz.spellbundle.integration.waystones.WaystonesIntegration;
import com.windanesz.spellbundle.spell.treasure2.IceChest;
import com.windanesz.spellbundle.spell.treasure2.IceChestDummy;
import com.windanesz.spellbundle.spell.treasure2.Knock;
import com.windanesz.spellbundle.spell.treasure2.KnockDummy;
import com.windanesz.spellbundle.spell.treasure2.Lock;
import com.windanesz.spellbundle.spell.treasure2.LockDummy;
import com.windanesz.spellbundle.spell.treasure2.SummonBoundSoul;
import com.windanesz.spellbundle.spell.treasure2.SummonBoundSoulDummy;
import com.windanesz.spellbundle.spell.treasure2.SummonMimic;
import com.windanesz.spellbundle.spell.treasure2.SummonMimicDummy;
import com.windanesz.spellbundle.spell.waystones.MassWarp;
import com.windanesz.spellbundle.spell.waystones.MassWarpDummy;
import com.windanesz.spellbundle.spell.waystones.SummonAlly;
import com.windanesz.spellbundle.spell.waystones.SummonAllyDummny;
import com.windanesz.spellbundle.spell.waystones.Warp;
import com.windanesz.spellbundle.spell.waystones.WarpDummy;
import electroblob.wizardry.spell.Spell;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

@ObjectHolder(SpellBundle.MODID)
@EventBusSubscriber
public final class SBSpells {

	// Waystones mod
	public static final Spell warp = placeholder();
	public static final Spell mass_warp = placeholder();
	public static final Spell summon_ally = placeholder();

	// Treasure2 mod
	public static final Spell knock = placeholder();
	public static final Spell lock = placeholder();
	public static final Spell summon_bound_soul = placeholder();
	public static final Spell summon_mimic = placeholder();
	public static final Spell ice_chest = placeholder();

	private SBSpells() {} // no instances

	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T placeholder() { return null; }

	/**
	 * Spells cannot reference classes from other mods here!
	 * If the spell has dependencies (most spells do...), don't include them in the constructor here.
	 * @param event
	 */
	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public static void register(RegistryEvent.Register<Spell> event) {

		IForgeRegistry<Spell> registry = event.getRegistry();

		//	Waystones spells
		{
			Integration instance = WaystonesIntegration.getInstance();
			if (instance.isEnabled()) {
				registry.register(instance.addSpell(new Warp()));
				registry.register(instance.addSpell(new MassWarp()));
				registry.register(instance.addSpell(new SummonAlly()));
			} else {
				registry.register(instance.addSpell(new WarpDummy()));
				registry.register(instance.addSpell(new MassWarpDummy()));
				registry.register(instance.addSpell(new SummonAllyDummny()));
			}
		}
		// Treasure2 spells
		{
			Integration instance = Treasure2Integration.getInstance();
			if (instance.isEnabled()) {
				registry.register(instance.addSpell(new Knock()));
				registry.register(instance.addSpell(new Lock()));
				registry.register(instance.addSpell(new SummonBoundSoul()));
				registry.register(instance.addSpell(new SummonMimic()));
				registry.register(instance.addSpell(new IceChest()));

			} else {
				registry.register(instance.addSpell(new KnockDummy()));
				registry.register(instance.addSpell(new LockDummy()));
				registry.register(instance.addSpell(new SummonBoundSoulDummy()));
				registry.register(instance.addSpell(new SummonMimicDummy()));
				registry.register(instance.addSpell(new IceChestDummy()));
			}
		}
	}
}
