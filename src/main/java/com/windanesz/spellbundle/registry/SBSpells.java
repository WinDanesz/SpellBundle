package com.windanesz.spellbundle.registry;

import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.integration.waystones.WaystonesIntegration;
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

	private SBSpells() {} // no instances

	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T placeholder() { return null; }

	public static final Spell warp = placeholder();
	public static final Spell mass_warp = placeholder();
	public static final Spell summon_ally = placeholder();

	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public static void register(RegistryEvent.Register<Spell> event) {

		IForgeRegistry<Spell> registry = event.getRegistry();

		//1.0.0 Spells
		if (WaystonesIntegration.getInstance().isEnabled()) {
			registry.register(new Warp());
			registry.register(new MassWarp());
			registry.register(new SummonAlly());
		} else {
			registry.register(new WarpDummy());
			registry.register(new MassWarpDummy());
			registry.register(new SummonAllyDummny());
		}

	}
}
