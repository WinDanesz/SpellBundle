package com.windanesz.spellbundle.client;

import com.windanesz.spellbundle.integration.treasure2.client.Treasure2ClientObjects;
import com.windanesz.spellbundle.integration.treasure2.common.Treasure2Objects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class SBGuiHandler implements IGuiHandler {

	/**
	 * Incrementable index for the gui ID
	 */
	private static int nextGuiId = 0;

	public static final int ICE_CHEST = nextGuiId++;

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {

		if (id == ICE_CHEST) {
			return Treasure2Objects.getIceChestContainer(id, player, world, x, y, z);
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {

		if (id == ICE_CHEST) {
			return Treasure2ClientObjects.getIceChestGui(id, player, world, x, y, z);
		}

		return null;
	}

}


