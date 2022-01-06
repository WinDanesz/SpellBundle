package com.windanesz.spellbundle.integration.treasure2.ice_chest;

import com.someguyssoftware.treasure2.block.TreasureChestBlock;
import com.someguyssoftware.treasure2.item.TreasureChestItemBlock;
import com.someguyssoftware.treasure2.tileentity.ITreasureChestTileEntity;
import electroblob.wizardry.Wizardry;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class IceChestItemBlock extends TreasureChestItemBlock {
	/**
	 * @param block
	 */
	public IceChestItemBlock(Block block) {
		super(block);
	}


	/**
	 *
	 */
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		// from super.super()
		this.block.addInformation(stack, worldIn, tooltip, flagIn);
		// get the block
		TreasureChestBlock tb = (TreasureChestBlock)getBlock();
		ITreasureChestTileEntity te = tb.getTileEntity();

		// chest info
		tooltip.add(Wizardry.proxy.translate("tooltip.label.max_locks", TextFormatting.DARK_BLUE + String.valueOf(tb.getChestType().getMaxLocks())));
		tooltip.add(Wizardry.proxy.translate("tooltip.label.container_size", TextFormatting.DARK_GREEN + String.valueOf(te.getNumberOfSlots())));
	}
}
