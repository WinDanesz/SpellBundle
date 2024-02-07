package com.windanesz.spellbundle.item;

import com.windanesz.spellbundle.integration.Integration;
import com.windanesz.spellbundle.spell.portalgun.TwinPortals;
import electroblob.wizardry.client.DrawingUtils;
import electroblob.wizardry.item.IManaStoringItem;
import electroblob.wizardry.item.IWorkbenchItem;
import electroblob.wizardry.item.ItemCrystal;
import electroblob.wizardry.registry.WizardryItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemPortalWand extends ItemArtefactSB implements IManaStoringItem, IWorkbenchItem {

	public ItemPortalWand(EnumRarity rarity, Type type, Integration integration) {
		super(rarity, type, integration);
		setMaxDamage(800);
	}
	public void setDamage(ItemStack stack, int damage) {
	}

	@Override
	public void setMana(ItemStack stack, int mana) {
		super.setDamage(stack, this.getManaCapacity(stack) - mana);
	}

	@Override
	public int getMana(ItemStack stack) {
		return this.getManaCapacity(stack) - this.getDamage(stack);
	}

	@Override
	public int getManaCapacity(ItemStack stack) {
		return this.getMaxDamage(stack);
	}

	@Override
	public int getSpellSlotCount(ItemStack itemStack) {
		return 0;
	}

	@Override
	public boolean onApplyButtonPressed(EntityPlayer player, Slot centre, Slot crystals, Slot upgrade, Slot[] spellBooks) {
		boolean changed = false;

		int chargeDepleted;
		if (crystals.getStack() != ItemStack.EMPTY && !this.isManaFull(centre.getStack())) {
			chargeDepleted = this.getManaCapacity(centre.getStack()) - this.getMana(centre.getStack());
			int manaPerItem = crystals.getStack().getItem() instanceof IManaStoringItem ? ((IManaStoringItem)crystals.getStack().getItem()).getMana(crystals.getStack()) : (crystals.getStack().getItem() instanceof ItemCrystal ? 100 : 10);
			if (crystals.getStack().getItem() == WizardryItems.crystal_shard) {
				manaPerItem = 10;
			}

			if (crystals.getStack().getItem() == WizardryItems.grand_crystal) {
				manaPerItem = 400;
			}

			if (crystals.getStack().getCount() * manaPerItem < chargeDepleted) {
				this.rechargeMana(centre.getStack(), crystals.getStack().getCount() * manaPerItem);
				crystals.decrStackSize(crystals.getStack().getCount());
			} else {
				this.setMana(centre.getStack(), this.getManaCapacity(centre.getStack()));
				crystals.decrStackSize((int)Math.ceil((double)chargeDepleted / (double)manaPerItem));
			}

			changed = true;
		}

		return changed;
	}

	@Override
	public boolean showTooltip(ItemStack itemStack) {
		return false;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		return TwinPortals.onItemRightClick(world, player, hand, this);
	}

	public int getRGBDurabilityForDisplay(ItemStack stack) {
		return DrawingUtils.mix(16747518, 9318116, (float)this.getDurabilityForDisplay(stack));
	}
}
