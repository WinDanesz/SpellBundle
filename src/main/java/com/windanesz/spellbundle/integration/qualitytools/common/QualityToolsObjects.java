package com.windanesz.spellbundle.integration.qualitytools.common;

import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.wizardryutils.registry.ItemRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;

public class QualityToolsObjects {

	public static void registerItems(RegistryEvent.Register<Item> event) {
		ItemRegistry.registerItem(event.getRegistry(), "reforging_scroll", SpellBundle.MODID, new ItemReforgingScroll());
	}

}
