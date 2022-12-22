package com.windanesz.spellbundle.integration.waystones.common;

import net.blay09.mods.waystones.PlayerWaystoneHelper;
import net.blay09.mods.waystones.WaystoneConfig;
import net.blay09.mods.waystones.WaystoneManager;
import net.blay09.mods.waystones.Waystones;
import net.blay09.mods.waystones.block.BlockWaystone;
import net.blay09.mods.waystones.block.TileWaystone;
import net.blay09.mods.waystones.item.ItemBoundScroll;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

import static net.blay09.mods.waystones.item.ItemWarpStone.lastTimerUpdate;

public class ItemBoundWarpstone extends ItemBoundScroll {

	public ItemBoundWarpstone() {
		super();
		this.setMaxStackSize(1);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack itemStack, World world, EntityLivingBase entity) {

		if (!world.isRemote && entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;

			WaystoneEntry boundTo = getBoundTo(player, itemStack);
			if (boundTo != null) {
				double distance = entity.getDistance(boundTo.getPos().getX(), boundTo.getPos().getY(), boundTo.getPos().getZ());
				if (distance <= 2) {
					return itemStack;
				}

				TileWaystone tileWaystone = WaystoneManager.getWaystoneInWorld(boundTo);
				if (tileWaystone != null) {
					((BlockWaystone) Waystones.blockWaystone).activateWaystone(player, world, tileWaystone);
					if (WaystoneManager.teleportToWaystone(player, boundTo)) {
					}
				}
			}
		}

		return itemStack;
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		ItemStack itemStack = player.getHeldItem(hand);
		NBTTagCompound tagCompound = itemStack.getTagCompound();
		if (tagCompound != null && tagCompound.hasKey("WaystonesBoundTo")) {
			return EnumActionResult.PASS;
		} else {
			if (PlayerWaystoneHelper.canUseWarpStone(player)) {
				EnumActionResult result = super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
				if (result == EnumActionResult.SUCCESS) {
					player.getCooldownTracker().setCooldown(this, WaystoneConfig.general.warpStoneCooldown * 20);
				}
				return result;
			}
			return EnumActionResult.PASS;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean showDurabilityBar(ItemStack itemStack) {
		return getDurabilityForDisplay(itemStack) > 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public double getDurabilityForDisplay(ItemStack stack) {
		EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();
		if (player == null) {
			return 0.0;
		}

		long timeLeft = PlayerWaystoneHelper.getLastWarpStoneUse(player);
		long timeSince = (System.currentTimeMillis() - lastTimerUpdate);
		float percentage = (float) timeSince / timeLeft;
		return 1.0 - (double) (Math.max(0, Math.min(1, percentage)));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack itemStack) {
		return PlayerWaystoneHelper.canUseWarpStone(FMLClientHandler.instance().getClientPlayerEntity());
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		super.addInformation(stack, world, tooltip, flag);
		EntityPlayer player = Minecraft.getMinecraft().player;
		if (player == null) {
			return;
		}
		long timeLeft = PlayerWaystoneHelper.getLastWarpStoneUse(player);
		long timeSince = System.currentTimeMillis() - lastTimerUpdate;
		int secondsLeft = (int) ((timeLeft - timeSince) / 1000);
		if (secondsLeft > 0) {
			tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.waystones:cooldownLeft", secondsLeft));
		}
	}

}
