package com.windanesz.spellbundle.integration.trinkets.common;

import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.integration.trinkets.TrinketsIntegration;
import com.windanesz.spellbundle.item.ItemArtefactWithAttribute;
import com.windanesz.wizardryutils.registry.ItemRegistry;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.item.ItemWandUpgrade;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import xzeroair.trinkets.attributes.MagicAttributes;

import java.util.UUID;

import static com.windanesz.spellbundle.registry.SBItems.createAttributeModifierMultimap;

public class TrinketsObjects {

	public static void registerItems(RegistryEvent.Register<Item> event) {
		ItemRegistry.registerItem(event.getRegistry(), "rejuvenation_upgrade", SpellBundle.MODID, new ItemWandUpgrade());

		if (TrinketsIntegration.getInstance().isEnabled()) {
			ItemRegistry.registerItem(event.getRegistry(), "ring_bonus_mana", SpellBundle.MODID, new ItemArtefactWithAttribute(EnumRarity.UNCOMMON, ItemArtefact.Type.RING, TrinketsIntegration.getInstance(),
					createAttributeModifierMultimap(UUID.fromString("af27418f-4677-4845-b0b9-3440e02fa158"), "Max Mana Bonus", 25, 0, MagicAttributes.MAX_MANA)));
		}
	}

}
