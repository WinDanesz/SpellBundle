package com.windanesz.spellbundle.registry;

import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.integration.Integration;
import com.windanesz.spellbundle.integration.biomesoplenty.BiomesOPlentyIntegration;
import com.windanesz.spellbundle.integration.biomesoplenty.common.EntityWaspMinion;
import com.windanesz.spellbundle.integration.pointer.PointerIntegration;
import com.windanesz.spellbundle.integration.portalgun.PortalGunIntegration;
import com.windanesz.spellbundle.integration.qualitytools.QTIntegration;
import com.windanesz.spellbundle.integration.quark.QuarkIntegration;
import com.windanesz.spellbundle.integration.quark.common.QuarkObjects;
import com.windanesz.spellbundle.integration.treasure2.Treasure2Integration;
import com.windanesz.spellbundle.integration.waystones.WaystonesIntegration;
import com.windanesz.spellbundle.spell.biomesoplenty.Briarburst;
import com.windanesz.spellbundle.spell.biomesoplenty.BriarburstDummy;
import com.windanesz.spellbundle.spell.biomesoplenty.ConvulsiveCurse;
import com.windanesz.spellbundle.spell.biomesoplenty.ConvulsiveCurseDummy;
import com.windanesz.spellbundle.spell.biomesoplenty.HotSpring;
import com.windanesz.spellbundle.spell.biomesoplenty.HotSpringDummy;
import com.windanesz.spellbundle.spell.biomesoplenty.WaspSwarmDummy;
import com.windanesz.spellbundle.spell.pointer.ArcaneReach;
import com.windanesz.spellbundle.spell.pointer.ArcaneReachDummy;
import com.windanesz.spellbundle.spell.portalgun.TwinPortals;
import com.windanesz.spellbundle.spell.portalgun.TwinPortalsDummy;
import com.windanesz.spellbundle.spell.qualitytools.BlessItem;
import com.windanesz.spellbundle.spell.qualitytools.BlessItemDummy;
import com.windanesz.spellbundle.spell.qualitytools.WeakenEquipment;
import com.windanesz.spellbundle.spell.qualitytools.WeakenEquipmentDummy;
import com.windanesz.spellbundle.spell.quark.AshenSoulDummy;
import com.windanesz.spellbundle.spell.quark.ColorizeDummy;
import com.windanesz.spellbundle.spell.quark.ConjureFoxhoundDummy;
import com.windanesz.spellbundle.spell.quark.ConjureWraithDummy;
import com.windanesz.spellbundle.spell.quark.CurseOfEvilDummy;
import com.windanesz.spellbundle.spell.quark.CurseOfHauntingDummy;
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
import electroblob.wizardry.spell.SpellMinion;
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

	// Pointer mod
	public static final Spell arcane_reach = placeholder();

	// Quality Tools
	public static final Spell bless_item = placeholder();
	public static final Spell weaken_equipment = placeholder();

	// PortalGun mod
	public static final Spell twin_portals = placeholder();

	// BoP
	public static final Spell wasp_swarm = placeholder();
	public static final Spell briarburst = placeholder();
	public static final Spell hot_spring = placeholder();
	public static final Spell convulsive_curse = placeholder();

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
		// Pointer mod spells
		{
			Integration instance = PointerIntegration.getInstance();
			if (instance.isEnabled()) {
				registry.register(instance.addSpell(new ArcaneReach()));
			} else {
				registry.register(instance.addSpell(new ArcaneReachDummy()));
			}
		}
		// Quark mod spells
		{
			Integration instance = QuarkIntegration.getInstance();
			if (instance.isEnabled()) {
				QuarkObjects.registerSpells(registry);
			} else {
				registry.register(instance.addSpell(new CurseOfHauntingDummy()));
				registry.register(instance.addSpell(new ConjureFoxhoundDummy()));
				registry.register(instance.addSpell(new ConjureWraithDummy()));
				registry.register(instance.addSpell(new CurseOfEvilDummy()));
				registry.register(instance.addSpell(new ColorizeDummy()));
				registry.register(instance.addSpell(new AshenSoulDummy()));
			}
		}
		// Quality Tools mod spells
		{
			Integration instance = QTIntegration.getInstance();
			if (instance.isEnabled()) {
				registry.register(instance.addSpell(new BlessItem()));
				registry.register(instance.addSpell(new WeakenEquipment()));
			} else {
				registry.register(instance.addSpell(new BlessItemDummy()));
				registry.register(instance.addSpell(new WeakenEquipmentDummy()));
			}
		}

		// PortalGun mod spells
		{
			Integration instance = PortalGunIntegration.getInstance();
			if (instance.isEnabled()) {
				registry.register(instance.addSpell(new TwinPortals()));
			} else {
				registry.register(instance.addSpell(new TwinPortalsDummy()));
			}
		}

		// Biomes O' Plenty  spells
		{
			Integration instance = BiomesOPlentyIntegration.getInstance();
			if (instance.isEnabled()) {
				registry.register(instance.addSpell(new SpellMinion<>(SpellBundle.MODID, "wasp_swarm", EntityWaspMinion::new)));
				registry.register(instance.addSpell(new Briarburst()));
				registry.register(instance.addSpell(new HotSpring()));
				registry.register(instance.addSpell(new ConvulsiveCurse()));
			} else {
				registry.register(instance.addSpell(new WaspSwarmDummy()));
				registry.register(instance.addSpell(new BriarburstDummy()));
				registry.register(instance.addSpell(new ConvulsiveCurseDummy()));
				registry.register(instance.addSpell(new HotSpringDummy()));
			}
		}
	}
}
