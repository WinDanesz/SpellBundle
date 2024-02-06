package com.windanesz.spellbundle.integration.biomesoplenty.common;

import com.windanesz.spellbundle.registry.SBEntities;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.registries.IForgeRegistry;

public class BoPObjects {

	private BoPObjects() {}

	public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
		IForgeRegistry<EntityEntry> registry = event.getRegistry();
		registry.register(SBEntities.createEntry(EntityWaspMinion.class, "wasp_minion", SBEntities.TrackingType.LIVING).build());
	}
}
