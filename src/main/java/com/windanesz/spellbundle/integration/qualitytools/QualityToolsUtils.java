package com.windanesz.spellbundle.integration.qualitytools;

import com.tmtravlr.qualitytools.config.ConfigLoader;
import com.tmtravlr.qualitytools.config.QualityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;

import java.util.Arrays;
import java.util.List;

public class QualityToolsUtils {

	/**
	 * This is just an arbitrary list as a best effort, since qualities doesn't have proper categorization
	 **/
	public static final List<TextFormatting> BAD_QUALITY_COLORS = Arrays.asList(TextFormatting.RED, TextFormatting.DARK_RED, TextFormatting.DARK_GRAY, TextFormatting.YELLOW);
	public static final String QUALITY_TAG = "Quality";
	public static final String COLOR_TAG = "Color";

	/**
	 *
	 * @param stack The stack to apply the quality to
	 * @param skipNormal If true, no items will be selected with the normal quality. Normal is basically a regular item, but it still has the "Quality" nbt tag,
	 *                   but it won't display any properties
	 * @param attempts The number of attempts to try to pick a chosen quality tyle (good or bad)
	 * @param goodQuality If true, the method will try to pick a positive quality (based on quality colors), if false it will try to pick a negative quality
	 *                    Might fail to choose the chosen type if there are many qualities and the attempts count is low
	 * @return the stack with a quality applied
	 */
	public static boolean generateQualityTag(ItemStack stack, boolean skipNormal, int attempts, boolean goodQuality) {
		if (stack != null && !stack.isEmpty() && (stack.getItem().getItemStackLimit(stack) == 1 || ConfigLoader.allowStackableItems)) {
			for (QualityType qualityType : ConfigLoader.qualityTypes.values()) {
				if (qualityType != null && qualityType.itemMatches(stack)) {

					for (int i = 0; i < attempts; i++) {
						qualityType.generateQualityTag(stack, skipNormal);
						if (stack.hasTagCompound() && stack.getTagCompound().hasKey(QUALITY_TAG)) {
							NBTTagCompound quality = stack.getTagCompound().getCompoundTag(QUALITY_TAG);

							if (!quality.isEmpty() && quality.hasKey(COLOR_TAG)) {
								String color = quality.getString(COLOR_TAG);
								TextFormatting ft = TextFormatting.getValueByName(color);
								if (goodQuality) {
									// look for a good quality
									if (!BAD_QUALITY_COLORS.contains(ft)) {
										return true;
									}
								} else {
									// look for a bad quality
									if (BAD_QUALITY_COLORS.contains(ft)) {
										return true;
									}
								}
							}
						}
					}
				}
			}
		}

		return false;
	}

	public static boolean hasQuality(ItemStack stack) {
		//noinspection DataFlowIssue
		return stack.hasTagCompound() && stack.getTagCompound().hasKey(QUALITY_TAG);
	}

	public static TextFormatting getQualityColor(ItemStack stack) {
		//noinspection DataFlowIssue
		return hasQuality(stack) && stack.getTagCompound().getCompoundTag(QUALITY_TAG).hasKey(COLOR_TAG) ?
				TextFormatting.getValueByName(stack.getTagCompound().getCompoundTag(QUALITY_TAG).getString(COLOR_TAG)) : TextFormatting.WHITE;
	}

	public static ItemStack addNormalQuality(ItemStack stack) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		NBTTagCompound nbt = stack.getTagCompound();
		nbt.setTag(QUALITY_TAG, new NBTTagCompound());
		stack.setTagCompound(nbt);
		return stack;
	}
}
