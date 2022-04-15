package com.windanesz.spellbundle.integration.waystones.client;

import com.windanesz.spellbundle.integration.waystones.WaystonesIntegration;
import net.blay09.mods.waystones.WarpMode;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

import java.util.List;

public class WaystonesClientObjects {

	public static void openGuiPlayerSelect(WaystonesIntegration instance, List<EntityPlayer> players, Object enumWarpMode, EnumHand hand, Object fromWaystoneEntry) {
		Minecraft.getMinecraft().displayGuiScreen(new GuiPlayerSelect(players, (WarpMode) enumWarpMode, hand, (WaystoneEntry) fromWaystoneEntry));
	}
}
