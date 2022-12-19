package com.windanesz.spellbundle.spell.quark;

import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.wizardryutils.tools.WizardryUtilsTools;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.InventoryUtils;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.quark.misc.feature.ColorRunes;
import vazkii.quark.misc.item.ItemRune;

public class Colorize extends Spell {

	public static final String CONSUME_RUNE = "consume_rune";

	public Colorize() {
		super(SpellBundle.MODID, "colorize", SpellActions.IMBUE, true);
		addProperties(CONSUME_RUNE);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		ItemStack runeStack = getFirstRune(caster);
		if (runeStack != ItemStack.EMPTY) {
			if (!caster.getHeldItem(getOtherHand(hand)).isEmpty()) {

				if (!world.isRemote) {
					ItemStack itemStackToColorize = caster.getHeldItem(getOtherHand(hand)).copy();

					if (EnchantmentHelper.getEnchantments(itemStackToColorize).isEmpty()) {
						WizardryUtilsTools.sendMessage(caster, "spell.spellbundle:colorize.item_not_enchanted", false);
						return false;
					}

					ItemNBTHelper.setBoolean(itemStackToColorize, ColorRunes.TAG_RUNE_ATTACHED, true);
					ItemNBTHelper.setInt(itemStackToColorize, ColorRunes.TAG_RUNE_COLOR, runeStack.getItemDamage());
					caster.setHeldItem(getOtherHand(hand), itemStackToColorize);

					// consume the rune
					if (getProperty(CONSUME_RUNE).intValue() == 1) {
						runeStack.shrink(1);
					}
				}

				return true;

			} else {
				WizardryUtilsTools.sendMessage(caster, "spell.spellbundle:colorize.no_item", false);
			}
		} else {
			WizardryUtilsTools.sendMessage(caster, "spell.spellbundle:colorize.no_rune", false);
		}

		return false;
	}

	@Override
	public boolean canBeCastBy(EntityLiving npc, boolean override) {return false;}

	@Override
	public boolean canBeCastBy(TileEntityDispenser dispenser) {return false;}

	public static ItemStack getFirstRune(EntityPlayer player) {
		for (ItemStack stack : InventoryUtils.getHotbar(player)) {
			if (stack.getItem() instanceof ItemRune) {
				return stack;
			}
		}
		return ItemStack.EMPTY;
	}

	public static EnumHand getOtherHand(EnumHand hand) {
		return hand == EnumHand.MAIN_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
	}

	protected String getDescriptionTranslationKey() {
		if (getProperty(CONSUME_RUNE).intValue() == 1) {
			return "spell.spellbundle:colorize_consume_rune.desc";
		} else {return super.getDescriptionTranslationKey();}
	}
}
