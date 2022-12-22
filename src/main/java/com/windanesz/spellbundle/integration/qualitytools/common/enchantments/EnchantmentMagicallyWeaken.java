package com.windanesz.spellbundle.integration.qualitytools.common.enchantments;

import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.integration.qualitytools.QualityToolsUtils;
import electroblob.wizardry.enchantment.Imbuement;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class EnchantmentMagicallyWeaken extends Enchantment implements Imbuement {

	public static final String PREVIOUS_QUALITY_TAG = "PreviousQuality";

	public EnchantmentMagicallyWeaken() {
		// Setting enchantment type to null stops the book appearing in the creative inventory
		super(Enchantment.Rarity.COMMON, null, new EntityEquipmentSlot[]{
				EntityEquipmentSlot.MAINHAND,
				EntityEquipmentSlot.OFFHAND,
				EntityEquipmentSlot.HEAD,
				EntityEquipmentSlot.CHEST,
				EntityEquipmentSlot.LEGS,
				EntityEquipmentSlot.FEET
		});
		setRegistryName(SpellBundle.MODID, "magically_weakened");
	}

	@Override
	public void onImbuementRemoval(ItemStack stack) {
		// restoring the hold quality of the item
		if (stack.hasTagCompound()) {
			NBTTagCompound nbt = stack.getTagCompound();
			//noinspection DataFlowIssue
			if (nbt.hasKey(PREVIOUS_QUALITY_TAG)) {
				nbt.setTag(QualityToolsUtils.QUALITY_TAG, nbt.getCompoundTag(PREVIOUS_QUALITY_TAG));
			}
		}
	}

	@Override
	public boolean canApply(ItemStack p_92089_1_){
		return false;
	}

	@Override
	public String getName(){
		return "enchantment." + this.getRegistryName();
	}

	/**
	 * Returns the maximum level that the enchantment can have.
	 */
	// Here, enchantment level is the damage multiplier of the spell used to apply the enchantment, i.e. with an
	// non-sorcerer wand it is level 1, an apprentice sorcerer wand is level 2, and so on. Note that basic sorcerer
	// wands can't
	// cast the imbue weapon spell, so level 2 is actually for apprentice wands.
	@Override
	public int getMaxLevel(){
		return 1;
	}

	@Override
	public boolean isAllowedOnBooks(){
		return false;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack){
		return false;
	}

}
