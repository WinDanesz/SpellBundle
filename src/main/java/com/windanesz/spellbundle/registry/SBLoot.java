package com.windanesz.spellbundle.registry;

import com.windanesz.spellbundle.Settings;
import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.integration.Integration;
import electroblob.wizardry.Wizardry;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
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


	private static LootTable SB_RARE_SCROLLS;

	private SBLoot() {} // No instances!

	private static LootTable RARE_SCROLLS;

	/**
	 * Called from the preInit method in the main mod class to register the custom dungeon loot.
	 */
	public static void preInit() {
		LootTableList.register(new ResourceLocation(SpellBundle.MODID, "inject/rare_scrolls"));

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

	@SubscribeEvent
	public static void onTBLootTableLoadEvent(LootTableLoadEvent event) {
		if (Settings.generalSettings.trinkets_integration) {
			if (event.getName().toString().equals(SpellBundle.MODID + ":inject/rare_scrolls")) {
				SB_RARE_SCROLLS = event.getTable();
			}

			// Inject
			if (event.getName().toString().equals("ancientspellcraft" + ":subsets/rare_scrolls") && SB_RARE_SCROLLS != null) {
				LootPool targetPool = event.getTable().getPool("main");
				LootPool sourcePool = SB_RARE_SCROLLS.getPool("spellbundle");
				injectEntries(sourcePool, targetPool);
			}
		}
	}
}
