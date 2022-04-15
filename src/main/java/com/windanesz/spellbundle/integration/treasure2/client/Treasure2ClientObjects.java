package com.windanesz.spellbundle.integration.treasure2.client;

import com.someguyssoftware.treasure2.tileentity.ITreasureChestTileEntity;
import com.windanesz.spellbundle.integration.treasure2.common.IceChestTileEntity;
import com.windanesz.spellbundle.integration.treasure2.common.Treasure2Objects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Treasure2ClientObjects {

	// Client only
	@SideOnly(Side.CLIENT)
	public static void registerRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(IceChestTileEntity.class, new IceChestTESR());
	}

	public static Object getIceChestGui(int id, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		ITreasureChestTileEntity chestTileEntity = null;

		// NOTE could pass in the different bg textures here
		if ((chestTileEntity = Treasure2Objects.getChestTileEntity(tileEntity)) == null) { return null; }
		return new IceChestGui(player.inventory, chestTileEntity);
	}
}
