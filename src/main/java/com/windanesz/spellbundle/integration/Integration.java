package com.windanesz.spellbundle.integration;

import net.minecraft.item.EnumRarity;

import java.util.LinkedHashMap;
import java.util.List;

public abstract class Integration {

	public static LinkedHashMap<String, Integration> INTEGRATIONS = new LinkedHashMap<>();

	public abstract void init();

	public abstract String getModid();

	public abstract boolean isEnabled();

	public static void register(String modid, Integration instance) {
		INTEGRATIONS.put(modid, instance);
	}

	public static LinkedHashMap<String, Integration> getRegistry() {
		return INTEGRATIONS;
	}

	public abstract List<EnumRarity> getArtefactTypeList();

}
