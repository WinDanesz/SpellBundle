package com.windanesz.spellbundle.client;

import com.windanesz.spellbundle.CommonProxy;
import com.windanesz.spellbundle.integration.treasure2.Treasure2Objects;
import com.windanesz.spellbundle.integration.treasure2.Treasure2Integration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	/**
	 * Called from preInit() in the main mod class to initialise the renderers.
	 */
	public void registerRenderers() {

		if (Treasure2Integration.getInstance().isEnabled()) {
			Treasure2Objects.registerRenderers();
		}

	}
}