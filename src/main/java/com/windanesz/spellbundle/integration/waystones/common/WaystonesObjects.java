package com.windanesz.spellbundle.integration.waystones.common;

import com.windanesz.spellbundle.registry.SBItems;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;

public class WaystonesObjects {

	public static void registerItems(RegistryEvent.Register<Item> event) {
		SBItems.registerItem(event.getRegistry(), "bound_warpstone", new ItemBoundWarpstone());
	}
}
