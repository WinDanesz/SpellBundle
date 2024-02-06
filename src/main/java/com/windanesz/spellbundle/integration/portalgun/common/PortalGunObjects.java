package com.windanesz.spellbundle.integration.portalgun.common;

import com.windanesz.spellbundle.SpellBundle;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class PortalGunObjects {

	private PortalGunObjects() {}

	public static void registerBlocks(IForgeRegistry<Block> registry) {
		registry.register(new PortalHolderBlock().setHardness(1));
	}

	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(TilePortalHolderBlock.class, new ResourceLocation(SpellBundle.MODID, "blockholder"));
	}
}
