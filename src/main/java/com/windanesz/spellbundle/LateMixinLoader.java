package com.windanesz.spellbundle;

import com.google.common.collect.Lists;
import net.minecraftforge.fml.common.Loader;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.List;

@SuppressWarnings("unused")
public class LateMixinLoader implements ILateMixinLoader {

	@Override
	public List<String> getMixinConfigs() {
		return Lists.newArrayList("quark.mixins.json", "bop.mixins.json", "trinkets.mixins.json");
	}

	@Override
	public boolean shouldMixinConfigQueue(String mixinConfig) {

		switch (mixinConfig) {
			case "quark.mixins.json":
				return Settings.generalSettings.quark_integration && Loader.isModLoaded("quark");
			case "bop.mixins.json":
				return Settings.generalSettings.bop_integration && Loader.isModLoaded("biomesoplenty");
			case "trinkets.mixins.json":
				return Settings.generalSettings.trinkets_integration && Loader.isModLoaded("xat");
		}

		return false;
	}
}
