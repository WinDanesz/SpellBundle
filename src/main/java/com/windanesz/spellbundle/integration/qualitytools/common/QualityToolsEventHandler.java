package com.windanesz.spellbundle.integration.qualitytools.common;

import com.windanesz.spellbundle.integration.qualitytools.QualityToolsUtils;
import com.windanesz.spellbundle.registry.SBItems;
import com.windanesz.wizardryutils.spell.SpellDynamicConjuration;
import electroblob.wizardry.event.SpellCastEvent;
import electroblob.wizardry.item.IConjuredItem;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.spell.SpellConjuration;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class QualityToolsEventHandler {

	public QualityToolsEventHandler() {}

	public static QualityToolsEventHandler INSTANCE = new QualityToolsEventHandler();

	@SubscribeEvent
	public void onSpellCastEvent(SpellCastEvent.Post event) {
		if (!event.getWorld().isRemote && event.getCaster() instanceof EntityPlayer &&
				(event.getSpell() instanceof SpellConjuration || event.getSpell() instanceof SpellDynamicConjuration)) {
			EntityPlayer player = (EntityPlayer) event.getCaster();
			if (ItemArtefact.isArtefactActive(player, SBItems.charm_spectral_hammer)) {
				// mainhand
				if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof IConjuredItem && !QualityToolsUtils.hasQuality(player.getHeldItem(EnumHand.MAIN_HAND))) {
					ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND).copy();
					QualityToolsUtils.generateQualityTag(stack, true, 30, true);
					player.setHeldItem(EnumHand.MAIN_HAND, stack);
				}
				// offhand
				if (player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof IConjuredItem && !QualityToolsUtils.hasQuality(player.getHeldItem(EnumHand.OFF_HAND))) {
					ItemStack stack = player.getHeldItem(EnumHand.OFF_HAND).copy();
					QualityToolsUtils.generateQualityTag(stack, true, 30, true);
					player.setHeldItem(EnumHand.OFF_HAND, stack);
				}

				// scan the hotbar
				for (int slotIndex = 0; slotIndex < 9; slotIndex++) {
					ItemStack stack = player.inventory.mainInventory.get(slotIndex);
					if (stack.getItem() instanceof IConjuredItem && !QualityToolsUtils.hasQuality(stack)) {
						ItemStack newStack = stack.copy();
						QualityToolsUtils.generateQualityTag(newStack, true, 30, true);
						player.inventory.mainInventory.set(slotIndex, newStack);
//						player.replaceItemInInventory(slotIndex, newStack);
					}
					slotIndex++;
				}

				// scan armor slots
				for (int slotIndex = 0; slotIndex < player.inventory.armorInventory.size(); slotIndex++) {
					ItemStack stack = player.inventory.armorInventory.get(slotIndex);
					if (stack.getItem() instanceof IConjuredItem && !QualityToolsUtils.hasQuality(stack)) {
						ItemStack newStack = stack.copy();
						QualityToolsUtils.generateQualityTag(newStack, true, 30, true);
						player.inventory.armorInventory.set(slotIndex, newStack);
				//		player.replaceItemInInventory(slotIndex, newStack);
					}
					slotIndex++;
				}
			}
		}
	}
}
