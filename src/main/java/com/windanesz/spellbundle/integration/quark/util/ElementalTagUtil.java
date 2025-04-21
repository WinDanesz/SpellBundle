package com.windanesz.spellbundle.integration.quark.util;

import electroblob.wizardry.constants.Element;
import electroblob.wizardry.spell.Spell;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;

import java.util.HashMap;
import java.util.Map;

public class ElementalTagUtil {

	private static final Map<Element, Integer> ELEMENT_COLOR_MAP = new HashMap<>();

	// Static constants for reused strings
	private static final String QUARK_RUNE_COLOR = "Quark:RuneColor";
	private static final String QUARK_RUNE_ATTACHED = "Quark:RuneAttached";
	private static final String SPELLBUNDLE_ORIGINAL_COLOR = "SpellBundle:originalColor";

	static {
		ELEMENT_COLOR_MAP.put(Element.MAGIC, TextFormatting.GRAY.getColorIndex());
		ELEMENT_COLOR_MAP.put(Element.FIRE, TextFormatting.DARK_RED.getColorIndex());
		ELEMENT_COLOR_MAP.put(Element.ICE, TextFormatting.AQUA.getColorIndex());
		ELEMENT_COLOR_MAP.put(Element.LIGHTNING, TextFormatting.DARK_AQUA.getColorIndex());
		ELEMENT_COLOR_MAP.put(Element.NECROMANCY, TextFormatting.DARK_PURPLE.getColorIndex());
		ELEMENT_COLOR_MAP.put(Element.EARTH, TextFormatting.DARK_GREEN.getColorIndex());
		ELEMENT_COLOR_MAP.put(Element.SORCERY, TextFormatting.GREEN.getColorIndex());
		ELEMENT_COLOR_MAP.put(Element.HEALING, TextFormatting.YELLOW.getColorIndex());
	}

	public static void applyElementalTags(ItemStack stack, Spell spell) {
		applyElementalTags(stack, spell.getElement());
	}

	public static void applyElementalTags(ItemStack stack, Element element) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		NBTTagCompound tag = stack.getTagCompound();
		if (tag != null) {
			if (tag.hasKey(QUARK_RUNE_COLOR) && !tag.hasKey(SPELLBUNDLE_ORIGINAL_COLOR)) {
				// Save the original color if it exists and hasn't been saved yet
				int existingColor = tag.getInteger(QUARK_RUNE_COLOR);
				tag.setInteger(SPELLBUNDLE_ORIGINAL_COLOR, existingColor);
			}
			tag.setBoolean(QUARK_RUNE_ATTACHED, true);
			int color = ELEMENT_COLOR_MAP.getOrDefault(element, TextFormatting.WHITE.getColorIndex());
			tag.setInteger(QUARK_RUNE_COLOR, color);
		}
	}

	public static void restoreOriginalColor(ItemStack stack) {
		if (stack.hasTagCompound()) {
			NBTTagCompound tag = stack.getTagCompound();
			if (tag != null) {
				if (tag.hasKey(SPELLBUNDLE_ORIGINAL_COLOR)) {
					// Restore the original color
					int originalColor = tag.getInteger(SPELLBUNDLE_ORIGINAL_COLOR);
					tag.setInteger(QUARK_RUNE_COLOR, originalColor);
					tag.removeTag(SPELLBUNDLE_ORIGINAL_COLOR); // Remove the temporary originalColor tag
				} else {
					// Remove all colorations if no original color is present
					tag.removeTag(QUARK_RUNE_COLOR);
					tag.removeTag(QUARK_RUNE_ATTACHED);
				}
			}
		}
	}
}
