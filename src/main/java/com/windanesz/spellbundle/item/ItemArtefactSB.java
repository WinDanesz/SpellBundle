package com.windanesz.spellbundle.item;

import com.windanesz.spellbundle.integration.Integration;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.item.ItemArtefact;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Same as ItemArtefact, but with an extended tooltip when the supported mod of this item is not present.
 */

public class ItemArtefactSB extends ItemArtefact {

	private Integration integration;

	public ItemArtefactSB(EnumRarity rarity, Type type, Integration integration) {
		super(rarity, type);
		this.integration = integration;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, net.minecraft.client.util.ITooltipFlag advanced){
		if (integration.isEnabled()) {
			super.addInformation(stack, world, tooltip, advanced);
		} else {
			Wizardry.proxy.addMultiLineDescription(tooltip, "item." + this.getRegistryName() + ".desc");
			// If we are here, the {@link com.windanesz.spellbundle.integration.Integration.setDisables} method enforces this to be disabled during postInit, so no need to check this.enabled (it's private anyways...)
			// Adding a desc with the missing mod thing
			String modName = Wizardry.proxy.translate("integration.spellbundle:" + integration.getModid(), new Style().setColor(TextFormatting.YELLOW));
			tooltip.add(Wizardry.proxy.translate("item.spellbundle:generic.disabled_with_modname", new Style().setColor(TextFormatting.RED), modName));
		}



	}
}
