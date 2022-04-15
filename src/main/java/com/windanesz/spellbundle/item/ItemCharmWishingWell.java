package com.windanesz.spellbundle.item;

import com.windanesz.spellbundle.integration.Integration;
import com.windanesz.spellbundle.integration.treasure2.Treasure2Integration;
import com.windanesz.spellbundle.integration.treasure2.common.Treasure2Objects;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ItemCharmWishingWell extends ItemArtefactSB {

	public ItemCharmWishingWell(EnumRarity rarity, Type type, Integration integration) {
		super(rarity, type, integration);
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem) {

		if (Treasure2Integration.getInstance().isEnabled()) {

			int mossyStoneBlocks = 0;
			List<BlockPos> blockPosList = new ArrayList<>();

			if (entityItem.ticksExisted % 10 == 0) {
				if (entityItem.isInWater()) {

					World world = entityItem.world;
					BlockPos pos = entityItem.getPosition();
					if (world.getBlockState(pos.north()).getBlock() == Blocks.MOSSY_COBBLESTONE) {
						mossyStoneBlocks++;
						blockPosList.add(pos.north());
					}
					if (world.getBlockState(pos.south()).getBlock() == Blocks.MOSSY_COBBLESTONE) {
						mossyStoneBlocks++;
						blockPosList.add(pos.south());
					}
					if (world.getBlockState(pos.east()).getBlock() == Blocks.MOSSY_COBBLESTONE) {
						mossyStoneBlocks++;
						blockPosList.add(pos.east());
					}
					if (world.getBlockState(pos.west()).getBlock() == Blocks.MOSSY_COBBLESTONE) {
						mossyStoneBlocks++;
						blockPosList.add(pos.west());
					}

					if (world.getBlockState(pos.north().west()).getBlock() == Blocks.MOSSY_COBBLESTONE) {
						mossyStoneBlocks++;
						blockPosList.add(pos.north().west());
					}
					if (world.getBlockState(pos.south().west()).getBlock() == Blocks.MOSSY_COBBLESTONE) {
						mossyStoneBlocks++;
						blockPosList.add(pos.south().west());
					}
					if (world.getBlockState(pos.north().east()).getBlock() == Blocks.MOSSY_COBBLESTONE) {
						mossyStoneBlocks++;
						blockPosList.add(pos.north().east());
					}
					if (world.getBlockState(pos.south().east()).getBlock() == Blocks.MOSSY_COBBLESTONE) {
						mossyStoneBlocks++;
						blockPosList.add(pos.south().east());
					}


					if (mossyStoneBlocks == 8) {
						if (!entityItem.world.isRemote) {

						Block wellBlock = Treasure2Objects.getWishingWellBlockState();
						blockPosList.forEach(currPos -> world.setBlockState(currPos, wellBlock.getDefaultState()));

						// kill the item
						entityItem.setItem(ItemStack.EMPTY);

						} else {
							// spawn particles on the client
							blockPosList.forEach(currPos -> ItemDye.spawnBonemealParticles(world, currPos.up(), 0));
						}
					}
				}
			}
		}
		return super.onEntityItemUpdate(entityItem);
	}
}
