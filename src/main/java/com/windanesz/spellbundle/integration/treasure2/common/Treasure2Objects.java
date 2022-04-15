package com.windanesz.spellbundle.integration.treasure2.common;

import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.chest.TreasureChestTypes;
import com.someguyssoftware.treasure2.enums.Category;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.item.KeyItem;
import com.someguyssoftware.treasure2.item.LockItem;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.tileentity.ITreasureChestTileEntity;
import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.registry.SBBlocks;
import com.windanesz.spellbundle.registry.SBItems;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import static com.someguyssoftware.treasure2.Treasure.logger;

public class Treasure2Objects {

	private Treasure2Objects() {}

	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(new LockItem(SpellBundle.MODID, "spectral_lock", new KeyItem[] {TreasureItems.JEWELLED_KEY}) {
			@Override
			public boolean onEntityItemUpdate(EntityItem entityItem) {
				entityItem.setDead();
				return false;
			}
		}
				.setCategory(Category.ELEMENTAL)
				.setRarity(Rarity.SCARCE));

		SBItems.registerItemBlock(event.getRegistry(), SBBlocks.ice_chest, new IceChestItemBlock(SBBlocks.ice_chest));
	}

	public static void registerBlocks(IForgeRegistry<Block> registry) {
		// Ice Chest
		// standard chest bounds
		AxisAlignedBB vanilla = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
		AxisAlignedBB[] stdChestBounds = new AxisAlignedBB[4];
		stdChestBounds[0] = vanilla; // S
		stdChestBounds[1] = vanilla; // W
		stdChestBounds[2] = vanilla; // N
		stdChestBounds[3] = vanilla; // E

		// Not using SBBlocks#registerBlock as the superclass of the chest block already sets the registryName
		registry.register(new IceChestBlock(SpellBundle.MODID, "ice_chest", IceChestTileEntity.class,
				TreasureChestTypes.STANDARD, Rarity.COMMON).setBounds(stdChestBounds).setHardness(2.5F));

	}

	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(IceChestTileEntity.class, new ResourceLocation(SpellBundle.MODID, "ice_chest_tile"));
	}

	public static Object getIceChestContainer(int id, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

		if (tileEntity instanceof ITreasureChestTileEntity) {
			ITreasureChestTileEntity chestTileEntity = (ITreasureChestTileEntity) tileEntity;
			return new IceChestContainer(player.inventory, chestTileEntity);
		}

		return null;
	}

	public static ITreasureChestTileEntity getChestTileEntity(TileEntity tileEntity) {
		ITreasureChestTileEntity chestTileEntity = (tileEntity instanceof ITreasureChestTileEntity) ? (ITreasureChestTileEntity) tileEntity : null;
		if (chestTileEntity == null) {
			logger.warn("Umm, GUI handler error - wrong tile entity.");
			return null;
		}
		return chestTileEntity;
	}

	public static Block getWishingWellBlockState() {
		return TreasureBlocks.WISHING_WELL_BLOCK;
	}
}
