package com.windanesz.spellbundle.integration.treasure2.common;

import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureChestBlock;
import com.someguyssoftware.treasure2.chest.TreasureChestType;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.ITreasureChestTileEntity;
import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.client.SBGuiHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class IceChestBlock extends TreasureChestBlock {
	public IceChestBlock(String modID, String name, Class<? extends ITreasureChestTileEntity> te, TreasureChestType type, Rarity rarity) {
		super(modID, name, te, type, rarity);
	}

	public IceChestBlock(String modID, String name, Material material, Class<? extends ITreasureChestTileEntity> te, TreasureChestType type, Rarity rarity) {
		super(modID, name, material, te, type, rarity);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		AbstractTreasureChestTileEntity te = (AbstractTreasureChestTileEntity) worldIn.getTileEntity(pos);

		// exit if on the client
		if (WorldInfo.isClientSide(worldIn)) {
			return true;
		}

		boolean isLocked = false;
		// determine if chest is locked
		if (te.hasLocks()) {
			isLocked = true;
		}

		// open the chest
		if (!isLocked) {
			playerIn.openGui(SpellBundle.instance, SBGuiHandler.ICE_CHEST, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}

		return true;
	}

	/**
	 * Same as super
	 */
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		Treasure.logger.debug("Breaking block....!");

		TileEntity tileEntity = worldIn.getTileEntity(pos);
		AbstractTreasureChestTileEntity te = null;
		if (tileEntity instanceof AbstractTreasureChestTileEntity) {
			te = (AbstractTreasureChestTileEntity)tileEntity;
		}

		if (te != null) {
			// unlocked!
			if (!te.hasLocks()) {
				if (WorldInfo.isServerSide(worldIn)) {
					/*
					 * spawn inventory items
					 */
					InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) te);

					/*
					 * create chest item
					 */
					ItemStack chestItem = new ItemStack(Item.getItemFromBlock(this), 1);

					/*
					 * write the properties to the nbt
					 */
					if (!chestItem.hasTagCompound()) {
						chestItem.setTagCompound(new NBTTagCompound());
					}
					te.writeToNBT(chestItem.getTagCompound());

					/*
					 * spawn chest item
					 */
					Treasure.logger.debug("Item being created from chest -> {}", chestItem.getItem().getRegistryName());

					// not spawning this as an item as ice chests can only be picked up when locked
					//InventoryHelper.spawnItemStack(worldIn, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), chestItem);
				}
			} else {

				/*
				 * spawn chest item
				 */

				if (WorldInfo.isServerSide(worldIn)) {
					ItemStack chestItem = new ItemStack(Item.getItemFromBlock(this), 1);

					// give the chest a tag compound
					//					Treasure.logger.debug("[BreakingBlock]Saving chest items:");

					NBTTagCompound nbt = new NBTTagCompound();
					nbt = te.writeToNBT(nbt);
					chestItem.setTagCompound(nbt);

					InventoryHelper.spawnItemStack(worldIn, (double) pos.getX(), (double) pos.getY(),
							(double) pos.getZ(), chestItem);

					// TEST log all items in item
					//					NonNullList<ItemStack> items = NonNullList.<ItemStack>withSize(27, ItemStack.EMPTY);
					//					ItemStackHelper.loadAllItems(chestItem.getTagCompound(), items);
					//					for (ItemStack stack : items) {
					//						Treasure.logger.debug("[BreakingBlock] item in chest item -> {}", stack.getDisplayName());
					//					}
				}
			}

			// remove the tile entity
			worldIn.removeTileEntity(pos);
		}
		else {
			// default to regular block break;
			super.breakBlock(worldIn, pos, state);
		}
	}

}
