package com.windanesz.spellbundle.client;

import com.windanesz.spellbundle.CommonProxy;
import com.windanesz.spellbundle.integration.treasure2.Treasure2Integration;
import com.windanesz.spellbundle.integration.treasure2.client.Treasure2ClientObjects;
import com.windanesz.spellbundle.integration.waystones.WaystonesIntegration;
import com.windanesz.spellbundle.integration.waystones.client.WaystonesClientObjects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	/**
	 * Called from preInit() in the main mod class to initialise the renderers.
	 */
	public void registerRenderers() {

		if (Treasure2Integration.getInstance().isEnabled()) {
			Treasure2ClientObjects.registerRenderers();
		}
	}

	@Override
	public void openGuiPlayerSelect(List<EntityPlayer> players, Object enumWarpMode, EnumHand hand, Object fromWaystoneEntry) {
		if (WaystonesIntegration.getInstance().isEnabled()) {
			WaystonesClientObjects.openGuiPlayerSelect(((WaystonesIntegration) WaystonesIntegration.getInstance()), players, enumWarpMode, hand, fromWaystoneEntry);
		}
	}
}