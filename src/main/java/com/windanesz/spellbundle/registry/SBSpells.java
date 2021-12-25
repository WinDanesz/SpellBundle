package com.windanesz.spellbundle.registry;

import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.integration.Integration;
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

	public static final Spell warp = placeholder();
	public static final Spell mass_warp = placeholder();
	public static final Spell summon_ally = placeholder();

	private SBSpells() {} // no instances

	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T placeholder() { return null; }

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
	}
}
