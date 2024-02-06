package com.windanesz.spellbundle.registry;

import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.integration.Integration;
import com.windanesz.spellbundle.integration.portalgun.PortalGunIntegration;
import com.windanesz.spellbundle.integration.qualitytools.QTIntegration;
import com.windanesz.spellbundle.integration.qualitytools.common.QualityToolsObjects;
import com.windanesz.spellbundle.integration.quark.QuarkIntegration;
import com.windanesz.spellbundle.integration.treasure2.Treasure2Integration;
import com.windanesz.spellbundle.integration.treasure2.common.Treasure2Objects;
import com.windanesz.spellbundle.integration.waystones.WaystonesIntegration;
import com.windanesz.spellbundle.integration.waystones.common.WaystonesObjects;
import com.windanesz.spellbundle.item.ItemArtefactSB;
import com.windanesz.spellbundle.item.ItemCharmWishingWell;
import com.windanesz.spellbundle.item.ItemPortalWand;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.registry.WizardryTabs;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

@ObjectHolder(SpellBundle.MODID)
@Mod.EventBusSubscriber
public final class SBItems {

	private SBItems() {} // No instances!

	// Looking for the rest of the items? Items with hard-dependencies are registered at com.windanesz.spellbundle.integration

	public static final Item ring_warpstone = placeholder();
	public static final Item ring_key = placeholder();
	public static final Item charm_frozen_lock = placeholder();
	public static final Item spectral_lock = placeholder();

	// Waystones
	public static final Item bound_waystone = placeholder();

	// Quark
	public static final Item ring_wraith = placeholder();
	public static final Item charm_spirit_guide = placeholder();

	// Quality Tools
	public static final Item reforging_scroll = placeholder();
	public static final Item amulet_reforging = placeholder();
	public static final Item charm_spectral_hammer = placeholder();

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		// Artefacts

		// Waystones Mod
		registerItem(registry, "ring_warpstone", new ItemArtefactSB(EnumRarity.EPIC, ItemArtefact.Type.RING, WaystonesIntegration.getInstance()), WaystonesIntegration.getInstance());
		registerItem(registry, "ring_key", new ItemArtefactSB(EnumRarity.RARE, ItemArtefact.Type.RING, Treasure2Integration.getInstance()), Treasure2Integration.getInstance());

		// Treasure2! Mod
		registerItem(registry, "charm_frozen_lock", new ItemArtefactSB(EnumRarity.RARE, ItemArtefact.Type.CHARM, Treasure2Integration.getInstance()), Treasure2Integration.getInstance());
		registerItem(registry, "charm_wishing_well", new ItemCharmWishingWell(EnumRarity.UNCOMMON, ItemArtefact.Type.CHARM, Treasure2Integration.getInstance()), Treasure2Integration.getInstance());

		// Treasure2 Optional items
		if (Treasure2Integration.getInstance().isEnabled()) {
			Treasure2Objects.registerItems(event);
		}

		// Waystones Optional items
		if (WaystonesIntegration.getInstance().isEnabled()) {
			WaystonesObjects.registerItems(event);
		}

		registerItem(registry, "ring_wraith", new ItemArtefactSB(EnumRarity.EPIC, ItemArtefact.Type.RING, QuarkIntegration.getInstance()), QuarkIntegration.getInstance());
		registerItem(registry, "charm_spirit_guide", new ItemArtefactSB(EnumRarity.RARE, ItemArtefact.Type.CHARM, QuarkIntegration.getInstance()), QuarkIntegration.getInstance());

		// Treasure2! Mod
		registerItem(registry, "amulet_reforging", new ItemArtefactSB(EnumRarity.EPIC, ItemArtefact.Type.AMULET, QTIntegration.getInstance()), QTIntegration.getInstance());
		registerItem(registry, "charm_spectral_hammer", new ItemCharmWishingWell(EnumRarity.RARE, ItemArtefact.Type.CHARM, QTIntegration.getInstance()), QTIntegration.getInstance());

		// QualityTools Optional items
		if (QTIntegration.getInstance().isEnabled()) {
			QualityToolsObjects.registerItems(event);
		}

		// PortalGun mod items
		registerItem(registry, "charm_portals", new ItemPortalWand(EnumRarity.EPIC, ItemArtefact.Type.CHARM, PortalGunIntegration.getInstance()), PortalGunIntegration.getInstance());
	}

	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T placeholder() { return null; }

	/**
	 * Registers artefacts into the item registry, also handles loot injection to the Wizardry artefact loot tables if the integration is enabled.
	 * Categorization happens based on EnumRarity (Uncommon/Rare/Epic) - the standard Wizardry artefact rarities.
	 */
	public static void registerItem(IForgeRegistry<Item> registry, String name, ItemArtefact item, Integration integration) {
		registerItem(registry, name, item, false);
		integration.addArtefact(item);
	}

	// below registry methods are courtesy of EB
	public static void registerItem(IForgeRegistry<Item> registry, String name, Item item) {
		registerItem(registry, name, item, false);
	}

	// below registry methods are courtesy of EB
	public static void registerItem(IForgeRegistry<Item> registry, String name, String modid, Item item) {
		registerItem(registry, name, modid, item, false);
	}

	public static void registerItem(IForgeRegistry<Item> registry, String name, Item item, boolean setTabIcon) {
		item.setRegistryName(SpellBundle.MODID, name);
		item.setTranslationKey(item.getRegistryName().toString());
		registry.register(item);

		if (setTabIcon && item.getCreativeTab() instanceof WizardryTabs.CreativeTabSorted) {
			((WizardryTabs.CreativeTabSorted) item.getCreativeTab()).setIconItem(new ItemStack(item));
		}

		if (item.getCreativeTab() instanceof WizardryTabs.CreativeTabListed) {
			((WizardryTabs.CreativeTabListed) item.getCreativeTab()).order.add(item);
		}
	}

	public static void registerItem(IForgeRegistry<Item> registry, String modid, String name, Item item, boolean setTabIcon) {
		item.setRegistryName(modid, name);
		item.setTranslationKey(item.getRegistryName().toString());
		registry.register(item);

		if (setTabIcon && item.getCreativeTab() instanceof WizardryTabs.CreativeTabSorted) {
			((WizardryTabs.CreativeTabSorted) item.getCreativeTab()).setIconItem(new ItemStack(item));
		}

		if (item.getCreativeTab() instanceof WizardryTabs.CreativeTabListed) {
			((WizardryTabs.CreativeTabListed) item.getCreativeTab()).order.add(item);
		}
	}

	public static void registerItemBlock(IForgeRegistry<Item> registry, Block block) {
		Item itemblock = new ItemBlock(block).setRegistryName(block.getRegistryName());
		registry.register(itemblock);
	}

	public static void registerItemBlock(IForgeRegistry<Item> registry, Block block, Item itemblock) {
		itemblock.setRegistryName(block.getRegistryName());
		registry.register(itemblock);
	}
}