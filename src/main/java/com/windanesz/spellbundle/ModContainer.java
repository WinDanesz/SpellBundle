package com.windanesz.spellbundle;

import com.google.common.eventbus.EventBus;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

public class ModContainer extends DummyModContainer
{
	public ModContainer() {
		super(new ModMetadata());
		ModMetadata meta = this.getMetadata();
		meta.modId = "spellbundlecore";
		meta.name = "Spell Bundle Coremod";
		meta.description = "Spell Bundle Coremod";
		meta.version = "1.12.2-1.0.0";
		meta.authorList.add("WinDanesz");
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
	}
}