package com.windanesz.spellbundle.registry;

import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.integration.Integration;
import electroblob.wizardry.Wizardry;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class responsible for registering Spell Bundle's loot tables. Also handles loot injection.
 *
 * @author WinDanesz
 */
@Mod.EventBusSubscriber
public class SBLoot {

	private SBLoot() {} // No instances!

	/**
	 * Called from the preInit method in the main mod class to register the custom dungeon loot.
	 */
	public static void preInit() { }

	@SubscribeEvent
	public static void onLootTableLoadEvent(LootTableLoadEvent event) {
		/////////////// Artefact injection ///////////////
		// Uncommon

		if (event.getName().getNamespace().equals(Wizardry.MODID)) {

			Pattern p = Pattern.compile(".*subsets/(.*)_artefacts");
			Matcher m = p.matcher(event.getName().getPath());

			if (m.find()) {

				String poolName = m.group(1) + "_artefacts";
				EnumRarity rarity = EnumRarity.valueOf(m.group(1).toUpperCase());

				List<Item> inject = new ArrayList<>();
				List<Item> finalInject = inject;
				Integration.getRegistry().forEach((s, integration) -> {
					if (integration.isEnabled()) {
						for (Item artefact : integration.getArtefacts()) {
							if (artefact.getRarity(new ItemStack(artefact)) == rarity) {
								finalInject.add(artefact);
							}
						}
					}
				});

				if (!inject.isEmpty()) {

					int index = 0;
					for (Item item : inject) {
						SpellBundle.logger.debug("Injecting loot entry item " + item.getRegistryName().toString() + " to " + poolName + " Wizardry loot table.");
						event.getTable().getPool(poolName).addEntry(new LootEntryItem(item, 1, 0, new LootFunction[0], new LootCondition[0],
								item.getRegistryName().getPath() + "_" + index));
					}
				}
			}
		}
	}

	/**
	 * Injects every element of sourcePool into targetPool
	 */
	private static void injectEntries(LootPool sourcePool, LootPool targetPool) {
		// Accessing {@link net.minecraft.world.storage.loot.LootPool.lootEntries}
		if (sourcePool != null && targetPool != null) {
			List<LootEntry> lootEntries = ObfuscationReflectionHelper.getPrivateValue(LootPool.class, sourcePool, "field_186453_a");

			for (LootEntry entry : lootEntries) {
				targetPool.addEntry(entry);
			}
		} else {
			SpellBundle.logger.warn("Attempted to inject to null pool source or target.");
		}

	}

	private static LootPool getAdditive(String entryName, String poolName) {
		return new LootPool(new LootEntry[] {getAdditiveEntry(entryName, 1)}, new LootCondition[0],
				new RandomValueRange(1), new RandomValueRange(0, 1), SpellBundle.MODID + "_" + poolName);
	}

	private static LootEntryTable getAdditiveEntry(String name, int weight) {
		return new LootEntryTable(new ResourceLocation(name), weight, 0, new LootCondition[0],
				SpellBundle.MODID + "_additive_entry");
	}

}
