package com.windanesz.spellbundle;

import com.google.common.collect.Lists;
import net.minecraftforge.fml.common.Loader;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.List;

@SuppressWarnings("unused")
public class LateMixinLoader implements ILateMixinLoader {

	@Override
	public List<String> getMixinConfigs() {
		return Lists.newArrayList("quark.mixins.json");
	}

	@Override
	public boolean shouldMixinConfigQueue(String mixinConfig) {

		switch (mixinConfig) {
			case "quark.mixins.json":
				return Loader.isModLoaded("quark");
		}

		return false;
	}
}
