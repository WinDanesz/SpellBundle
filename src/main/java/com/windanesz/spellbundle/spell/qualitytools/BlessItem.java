package com.windanesz.spellbundle.spell.qualitytools;

import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.integration.qualitytools.QualityToolsUtils;
import com.windanesz.spellbundle.registry.SBItems;
import com.windanesz.wizardryutils.tools.WizardryUtilsTools;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class BlessItem extends Spell {

	public BlessItem() {
		super(SpellBundle.MODID, "bless_item", SpellActions.IMBUE, false);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		if (!caster.getHeldItem(getOtherHand(hand)).isEmpty()) {
			ItemStack itemStackToBless = caster.getHeldItem(getOtherHand(hand)).copy();
			if (QualityToolsUtils.hasQuality(itemStackToBless) && QualityToolsUtils.BAD_QUALITY_COLORS.contains(QualityToolsUtils.getQualityColor(itemStackToBless))) {
				if (!world.isRemote) {
					if (ItemArtefact.isArtefactActive(caster, SBItems.amulet_reforging)) {
						QualityToolsUtils.generateQualityTag(itemStackToBless, true, 30, true);
						caster.setHeldItem(getOtherHand(hand), itemStackToBless);
					} else {
						QualityToolsUtils.addNormalQuality(itemStackToBless);
						caster.setHeldItem(getOtherHand(hand), itemStackToBless);
					}
				}
				return true;
			} else {
				WizardryUtilsTools.sendMessage(caster, "spell." + this.getRegistryName() + ".item_does_not_have_negative_quality", false);
			}

		} else {
			WizardryUtilsTools.sendMessage(caster, "spell." + this.getRegistryName() + ".no_item", false);
		}
		return false;
	}

	@Override
	public boolean canBeCastBy(EntityLiving npc, boolean override) {return false;}

	@Override
	public boolean canBeCastBy(TileEntityDispenser dispenser) {return false;}

	public static EnumHand getOtherHand(EnumHand hand) {
		return hand == EnumHand.MAIN_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
	}

}
