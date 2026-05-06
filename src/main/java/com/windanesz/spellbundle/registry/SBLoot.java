package com.windanesz.spellbundle.registry;

import com.windanesz.spellbundle.Settings;
import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.integration.qualitytools.QTIntegration;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootCondition;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Class responsible for registering Spell Bundle's loot tables.
 */
@Mod.EventBusSubscriber
public class SBLoot {

    private static LootTable SB_RARE_SCROLLS;

    private SBLoot() {}

    /**
     * Called from preInit in the main mod class.
     */
    public static void preInit() {
        if (QTIntegration.getInstance().isEnabled()) {
            LootTableList.register(
                    new ResourceLocation(SpellBundle.MODID, "inject/rare_scrolls")
            );
        }
    }

    /**
     * Creates a new additive loot pool.
     * This is Forge-safe and does not mutate frozen pools.
     */
    private static LootPool getAdditive(String entryName, String poolName) {

        LootEntry entry = new LootEntryTable(
                new ResourceLocation(entryName),
                1,
                0,
                new LootCondition[0],
                SpellBundle.MODID + "_additive_entry"
        );

        return new LootPool(
                new LootEntry[]{entry},
                new LootCondition[0],
                new RandomValueRange(1),
                new RandomValueRange(0, 1),
                SpellBundle.MODID + "_" + poolName
        );
    }

    @SubscribeEvent
    public static void onTBLootTableLoadEvent(LootTableLoadEvent event) {

        if (!Settings.generalSettings.qualitytools_integration) {
            return;
        }

        String name = event.getName().toString();

        // Store reference to SpellBundle loot table
        if (name.equals(SpellBundle.MODID + ":inject/rare_scrolls")) {
            SB_RARE_SCROLLS = event.getTable();
            return;
        }

        // Inject safely into Ancient Spellcraft loot table
        if (name.equals("ancientspellcraft:subsets/rare_scrolls")) {

            try {

                LootPool additivePool = getAdditive(
                        SpellBundle.MODID + ":inject/rare_scrolls",
                        "spellbundle_injected"
                );

                event.getTable().addPool(additivePool);

                SpellBundle.logger.info(
                        "Successfully injected SpellBundle rare scrolls into Ancient Spellcraft loot table."
                );

            } catch (Exception e) {

                SpellBundle.logger.error(
                        "Failed to inject SpellBundle loot into Ancient Spellcraft.",
                        e
                );
            }
        }
    }
}
	private static LootTable RARE_SCROLLS;

	/**
	 * Called from the preInit method in the main mod class to register the custom dungeon loot.
	 */
	public static void preInit() {
		if (QTIntegration.getInstance().isEnabled()) {
			LootTableList.register(new ResourceLocation(SpellBundle.MODID, "inject/rare_scrolls"));
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

	@SubscribeEvent
	public static void onTBLootTableLoadEvent(LootTableLoadEvent event) {
		if (Settings.generalSettings.qualitytools_integration) {
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
