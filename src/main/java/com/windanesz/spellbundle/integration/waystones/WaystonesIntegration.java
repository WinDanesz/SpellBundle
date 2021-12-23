package com.windanesz.spellbundle.integration.waystones;

import com.windanesz.spellbundle.Settings;
import com.windanesz.spellbundle.integration.Integration;
import net.minecraft.item.EnumRarity;
import net.minecraftforge.fml.common.Loader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WaystonesIntegration extends Integration {

	// singleton instance
	private static final WaystonesIntegration instance = new WaystonesIntegration();
	private static final String modId = "waystones";

	// only used for loot table initialization
	private static final List<EnumRarity> LOOT_TABLE_RARITY_TYPES = new ArrayList<>(Collections.singletonList(EnumRarity.EPIC));

	private boolean isLoaded;

	/************* Standard methods *************/

	private WaystonesIntegration() {}

	public static Integration getInstance() { return instance; }

	@Override
	public String getModid() { return modId; }

	@Override
	public void init() {
		isLoaded = Loader.isModLoaded(getModid());

		if (!isEnabled()) { return; }

		// init stuff
		initCustom();

		// register compat instance
		Integration.register(getModid(), getInstance());
	}

	@Override
	public boolean isEnabled() {
		return Settings.generalSettings.waystones_integration && isLoaded;
	}

	/************* Standard methods *************/

	private void initCustom() {

	}

	@Override
	public List<EnumRarity> getArtefactTypeList() {
		return LOOT_TABLE_RARITY_TYPES;
	}
}
