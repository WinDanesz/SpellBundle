package com.windanesz.spellbundle.integration.trinkets;

import com.windanesz.spellbundle.Settings;
import com.windanesz.spellbundle.registry.SBItems;
import com.windanesz.wizardryutils.tools.WizardryUtilsTools;
import electroblob.wizardry.client.gui.GuiArcaneWorkbench;
import electroblob.wizardry.event.SpellCastEvent;
import electroblob.wizardry.item.IManaStoringItem;
import electroblob.wizardry.item.ISpellCastingItem;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.item.ItemWand;
import electroblob.wizardry.item.ItemWizardArmour;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.util.InventoryUtils;
import electroblob.wizardry.util.SpellModifiers;
import electroblob.wizardry.util.WandHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.attributes.MagicAttributes;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.mana.EntityManaConfig;

import java.util.UUID;

public class TrinketsEventHandler {

	public TrinketsEventHandler() {}

	private static final EntityManaConfig manaConfig = TrinketsConfig.SERVER.mana;

	public static TrinketsEventHandler INSTANCE = new TrinketsEventHandler();

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onSpellCastEventPre(SpellCastEvent.Pre event) {
		if (!Settings.generalSettings.trinkets_integration) {
			return;
		}

		if (!(event.getCaster() instanceof EntityPlayer)) {
			return;
		}

		if (event.getCaster() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getCaster();
			for (EnumHand hand : EnumHand.values()) {
				if (event.getSource() == SpellCastEvent.Source.WAND && player.getHeldItem(hand).getItem() instanceof ItemWand) {
					if (WandHelper.getCurrentCooldown(event.getCaster().getHeldItem(hand)) != 0) {
						event.setCanceled(true);
						return;
					}
				} else {
					if (player.getHeldItem(hand).getItem() instanceof ISpellCastingItem) {
						ItemStack stack = player.getHeldItem(hand);
						if (stack.getTagCompound() != null) {
							NBTTagCompound nbt = stack.getTagCompound();
							// 11 - int array
							if (nbt.hasKey("cooldown", 11) && nbt.hasKey("selectedSpell")) {
								if (WandHelper.getCurrentCooldown(event.getCaster().getHeldItem(hand)) != 0) {
									event.setCanceled(true);
									return;

								}
								//if (selectedSpellCooldown >= 0 && cooldowns.length > selectedSpellCooldown ? cooldowns[selectedSpellCooldown] : 0) {

							}
						}

						if (WandHelper.getCurrentCooldown(event.getCaster().getHeldItem(hand)) != 0) {
							event.setCanceled(true);
							return;

						}
					}
				}
			}

			// trying to make compat with other items

			float cost = event.getSpell().getCost() * event.getModifiers().get(SpellModifiers.COST);
			MagicStats stats = Capabilities.getMagicStats(event.getCaster());
			if (stats != null) {

				float currentMana = stats.getMana();
				if (currentMana < cost && !player.isCreative()) {
					WizardryUtilsTools.sendMessage(event.getCaster(), "You don't have enough mana (" + (int) cost + ")", true);
					event.setCanceled(true);
				}
			} else {
				return;
			}
			if (!event.getWorld().isRemote) {
				Capabilities.getMagicStats(event.getCaster(), true, (magic, rtn) -> magic.spendMana(cost));
			}

		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onSpellCastEventPre(SpellCastEvent.Tick event) {
		if (!Settings.generalSettings.trinkets_integration) {
			return;
		}

		if (!(event.getCaster() instanceof EntityPlayer)) {
			return;
		}

		if (event.getCount() % 20 == 0) {

			EntityPlayer player = (EntityPlayer) event.getCaster();
			//			if (event.getSource() == SpellCastEvent.Source.WAND && player.isHandActive() && player.getHeldItem(player.getActiveHand()).getItem() instanceof ItemWand) {
			//				if (WandHelper.getCurrentCooldown(event.getCaster().getHeldItem(event.getCaster().getActiveHand())) != 0) {
			//					return;
			//				}
			//			}

			float cost = ManaProgression.getOriginalCost(player);
			MagicStats stats = Capabilities.getMagicStats(event.getCaster());
			if (stats != null) {

				float currentMana = stats.getMana();
				if (currentMana < cost && !player.isCreative()) {
					WizardryUtilsTools.sendMessage(event.getCaster(), "You don't have enough mana (" + (int) cost + ")", true);
					event.setCanceled(true);
				}
			} else {
				return;
			}
			if (!event.getWorld().isRemote) {
				Capabilities.getMagicStats(event.getCaster(), true, (magic, rtn) -> magic.spendMana(cost));
			}

			event.getModifiers().set(SpellModifiers.COST, 0, true);
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onSpellCastEventPost(SpellCastEvent.Post event) {
		if (!Settings.generalSettings.trinkets_integration) {
			return;
		}

		if (!(event.getCaster() instanceof EntityPlayer)) {
			return;
		}

		if (event.getCaster() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getCaster();
			MagicStats stats = Capabilities.getMagicStats(event.getCaster());
			if (stats == null) {
				return;
			}

			float i = ManaProgression.getXpGainForSpellCast(player, event.getWorld(), event.getSpell(), event.getModifiers(), event.getCaster().getActiveHand(), 0);
			int level = ManaProgression.getLevel(player);

			ManaProgression.addXP(player, (int) i);
			int levelAfterCast = ManaProgression.getLevel(player);

			if (levelAfterCast > level && !event.getWorld().isRemote) {
				// Bump max mana
				//int currentManaBasedOnLevel = calculateCurrentMana(XpProgression.getMaxLevel(), level, (int) manaConfig.mana_max);
				double nextBonus = calculateCurrentMana(ManaProgression.getMaxLevel(), levelAfterCast, (int) manaConfig.mana_max) * 0.1;
				//int actualIncrease = nextBonus - currentManaBasedOnLevel;
				//double newBonusMana = (stats.getBonusMana() + actualIncrease);
				stats.setBonusMana(nextBonus);
			}
		}
		ManaProgression.setOriginalCost((EntityPlayer) event.getCaster(), (int) (event.getSpell().getCost() * event.getModifiers().get(SpellModifiers.COST)));
		event.getModifiers().set(SpellModifiers.COST, 0, true);
	}

	// Function to calculate XP based on the level
	public static int calculateNextBonusMana(int level, int maxLevel, int maxXP, int minXP, double decayFactor) {
		// Calculate linear interpolation between minXP and maxXP
		double interpolatedXP = minXP + ((maxXP - minXP) * (maxLevel - level)) / maxLevel;

		// Calculate the decay factor (inverted)
		double invertedDecay = (double) (level - 1) / (maxLevel - 1);

		// Calculate final XP using inverted decay
		double finalXP = interpolatedXP * invertedDecay;

		return (int) finalXP;
	}

	public static int calculateCurrentMana(int maxLevel, int currentLevel, int maxMana) {
		// Calculate the increment in mana for each level
		double increment = (double) maxMana / (maxLevel * (maxLevel + 1) / 2);

		// Calculate the current mana based on the level
		int currentMana = (int) (currentLevel * (currentLevel + 1) / 2 * increment);

		return Math.min(currentMana, maxMana); // Ensure currentMana does not exceed maxMana
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void attachAttributes(EntityJoinWorldEvent event) {
		if (!Settings.generalSettings.trinkets_integration) {
			return;
		}

		if (event.getEntity() instanceof EntityLivingBase) {
			final EntityLivingBase entity = (EntityLivingBase) event.getEntity();
			entity.getEntityAttribute(MagicAttributes.MAX_MANA).setBaseValue(10);
		}
	}

	private static final UUID ARMOR_HEAD_MODIFIER_UUID = java.util.UUID.fromString("58baa0b7-adca-4bfe-b8ef-1ed9e12a7451");
	private static final AttributeModifier ARMOR_HEAD_MODIFIER = new AttributeModifier(ARMOR_HEAD_MODIFIER_UUID, "Head Mana Regen", Settings.generalSettings.mana_regen_increase_per_slot, 1);

	private static final UUID ARMOR_CHEST_MODIFIER_UUID = java.util.UUID.fromString("d145af82-724b-42c6-8378-437f90e33b6f");
	private static final AttributeModifier ARMOR_CHEST_MODIFIER = new AttributeModifier(ARMOR_CHEST_MODIFIER_UUID, "Chest Mana Regen", Settings.generalSettings.mana_regen_increase_per_slot, 1);

	private static final UUID ARMOR_LEGS_MODIFIER_UUID = java.util.UUID.fromString("9cf08cfd-ba6d-4032-b1c9-48b97d4314fe");
	private static final AttributeModifier ARMOR_LEGS_MODIFIER = new AttributeModifier(ARMOR_LEGS_MODIFIER_UUID, "Legs Mana Regen", Settings.generalSettings.mana_regen_increase_per_slot, 1);

	private static final UUID ARMOR_FEET_MODIFIER_UUID = UUID.fromString("304acb3d-f7b7-48c4-97b0-e6b9ed4e2987");
	private static final AttributeModifier ARMOR_FEET_MODIFIER = new AttributeModifier(ARMOR_FEET_MODIFIER_UUID, "Feet Mana Regen", Settings.generalSettings.mana_regen_increase_per_slot, 1);

	private final UUID STORAGE_UPGRADE_UUID = UUID.fromString("6be88399-59cf-43ba-bf5a-910f5bb4a419");
	private final UUID CONDENSING_UPGRADE_UUID = UUID.fromString("bf5ffe8d-8acb-40ed-85ee-370ece058851");
	private final UUID REJUVENATION_UPGRADE_UUID = UUID.fromString("75a9d324-470c-4b3e-bccd-9b12ace6730b");

	@SubscribeEvent
	public void onEquipmentChange(LivingEquipmentChangeEvent event) {
		if (!Settings.generalSettings.trinkets_integration) {
			return;
		}

		if (!(event.getEntityLiving() instanceof EntityPlayer) || event.getEntityLiving().world.isRemote) {
			return;
		}

		if (event.getSlot() == EntityEquipmentSlot.MAINHAND || event.getSlot() == EntityEquipmentSlot.OFFHAND) {
			handleCondensingUpdate(event);
			handleStorageUpgrade(event);
			handleRejuvenationUpgrade(event);
		}

		if (event.getTo().getItem() instanceof ItemWizardArmour) {
			if (event.getSlot() == EntityEquipmentSlot.HEAD &&
					!event.getEntityLiving().getEntityAttribute(MagicAttributes.regen).hasModifier(ARMOR_HEAD_MODIFIER)) {
				event.getEntityLiving().getEntityAttribute(MagicAttributes.regen).applyModifier(ARMOR_HEAD_MODIFIER);
			} else if (event.getSlot() == EntityEquipmentSlot.CHEST &&
					!event.getEntityLiving().getEntityAttribute(MagicAttributes.regen).hasModifier(ARMOR_CHEST_MODIFIER)) {
				event.getEntityLiving().getEntityAttribute(MagicAttributes.regen).applyModifier(ARMOR_CHEST_MODIFIER);
			} else if (event.getSlot() == EntityEquipmentSlot.LEGS &&
					!event.getEntityLiving().getEntityAttribute(MagicAttributes.regen).hasModifier(ARMOR_LEGS_MODIFIER)) {
				event.getEntityLiving().getEntityAttribute(MagicAttributes.regen).applyModifier(ARMOR_LEGS_MODIFIER);
			} else if (event.getSlot() == EntityEquipmentSlot.FEET &&
					!event.getEntityLiving().getEntityAttribute(MagicAttributes.regen).hasModifier(ARMOR_FEET_MODIFIER)) {
				event.getEntityLiving().getEntityAttribute(MagicAttributes.regen).applyModifier(ARMOR_FEET_MODIFIER);
			}
		} else {
			if (event.getSlot() == EntityEquipmentSlot.HEAD &&
					event.getEntityLiving().getEntityAttribute(MagicAttributes.regen).hasModifier(ARMOR_HEAD_MODIFIER)) {
				event.getEntityLiving().getEntityAttribute(MagicAttributes.regen).removeModifier(ARMOR_HEAD_MODIFIER);
			} else if (event.getSlot() == EntityEquipmentSlot.CHEST &&
					event.getEntityLiving().getEntityAttribute(MagicAttributes.regen).hasModifier(ARMOR_CHEST_MODIFIER)) {
				event.getEntityLiving().getEntityAttribute(MagicAttributes.regen).removeModifier(ARMOR_CHEST_MODIFIER);
			} else if (event.getSlot() == EntityEquipmentSlot.LEGS &&
					event.getEntityLiving().getEntityAttribute(MagicAttributes.regen).hasModifier(ARMOR_LEGS_MODIFIER)) {
				event.getEntityLiving().getEntityAttribute(MagicAttributes.regen).removeModifier(ARMOR_LEGS_MODIFIER);
			} else if (event.getSlot() == EntityEquipmentSlot.FEET &&
					event.getEntityLiving().getEntityAttribute(MagicAttributes.regen).hasModifier(ARMOR_FEET_MODIFIER)) {
				event.getEntityLiving().getEntityAttribute(MagicAttributes.regen).removeModifier(ARMOR_FEET_MODIFIER);
			}
		}
	}

	private void handleStorageUpgrade(LivingEquipmentChangeEvent event) {
		if (WandHelper.getUpgradeLevel(event.getTo(), WizardryItems.storage_upgrade) > 0) {
			int lvl = WandHelper.getUpgradeLevel(event.getTo(), WizardryItems.storage_upgrade);

			if (!event.getEntityLiving().getEntityAttribute(MagicAttributes.MAX_MANA).hasModifier(new AttributeModifier(STORAGE_UPGRADE_UUID, "Max Mana Bonus", lvl * 0.05, 1))) {
				event.getEntityLiving().getEntityAttribute(MagicAttributes.MAX_MANA).applyModifier(
						new AttributeModifier(STORAGE_UPGRADE_UUID, "Max Mana Bonus", lvl * 0.1, 1));
			}
		} else {
			event.getEntityLiving().getEntityAttribute(MagicAttributes.MAX_MANA).removeModifier(STORAGE_UPGRADE_UUID);
		}
	}

	private void handleRejuvenationUpgrade(LivingEquipmentChangeEvent event) {
		if (WandHelper.getUpgradeLevel(event.getTo(), SBItems.rejuvenation_upgrade) > 0) {
			int lvl = WandHelper.getUpgradeLevel(event.getTo(), SBItems.rejuvenation_upgrade);

			if (!event.getEntityLiving().getEntityAttribute(MagicAttributes.regenCooldown).hasModifier(new AttributeModifier(REJUVENATION_UPGRADE_UUID, "Mana Rejuvenation Bonus", -lvl * 0.15, 1))) {
				event.getEntityLiving().getEntityAttribute(MagicAttributes.regenCooldown).applyModifier(
						new AttributeModifier(REJUVENATION_UPGRADE_UUID, "Mana Rejuvenation Bonus", -lvl * 0.15, 1));
			}
		} else {
			event.getEntityLiving().getEntityAttribute(MagicAttributes.regenCooldown).removeModifier(REJUVENATION_UPGRADE_UUID);
		}
	}

	private void handleCondensingUpdate(LivingEquipmentChangeEvent event) {
		if (WandHelper.getUpgradeLevel(event.getTo(), WizardryItems.condenser_upgrade) > 0) {
			int lvl = WandHelper.getUpgradeLevel(event.getTo(), WizardryItems.condenser_upgrade);

			if (!event.getEntityLiving().getEntityAttribute(MagicAttributes.regen).hasModifier(new AttributeModifier(CONDENSING_UPGRADE_UUID, "Mana Regen Rate Bonus", lvl * 0.25, 1))) {
				event.getEntityLiving().getEntityAttribute(MagicAttributes.regen).applyModifier(
						new AttributeModifier(CONDENSING_UPGRADE_UUID, "Mana Regen Rate Bonus", lvl * 0.25, 1));
			}
		} else {
			event.getEntityLiving().getEntityAttribute(MagicAttributes.regen).removeModifier(CONDENSING_UPGRADE_UUID);
		}
	}

//	@SideOnly(Side.CLIENT)
//	@SubscribeEvent
//	public void onToolTip(ItemTooltipEvent event) {
//		//		if (event.getItemStack().getItem() instanceof ItemWizardArmour) {
//		//			Wizardry.proxy.addMultiLineDescription(event.getToolTip(), TextFormatting.BLUE + I18n.format("tooltip.wizard_armor_mana"));
//		//		} else if (event.getItemStack().getItem() == WizardryItems.storage_upgrade) {
//		//			//			event.getToolTip().clear();
//		//			event.getToolTip().remove(event.getToolTip().size() - 3);
//		//			event.getToolTip().remove(event.getToolTip().size() - 2);
//		//			Wizardry.proxy.addMultiLineDescription(event.getToolTip(), I18n.format("item.spellbundle:storage_upgrade.desc"));
//		//		} else if (event.getItemStack().getItem() == WizardryItems.condenser_upgrade) {
//		//			//			event.getToolTip().clear();
//		//			event.getToolTip().remove(event.getToolTip().size() - 3);
//		//			event.getToolTip().remove(event.getToolTip().size() - 2);
//		//			Wizardry.proxy.addMultiLineDescription(event.getToolTip(), I18n.format("item.spellbundle:storage_upgrade.desc"));
//		//		} else if (event.getItemStack().getItem() == WizardryItems.siphon_upgrade) {
//		//			//			event.getToolTip().clear();
//		//			event.getToolTip().remove(event.getToolTip().size() - 3);
//		//			event.getToolTip().remove(event.getToolTip().size() - 2);
//		//			Wizardry.proxy.addMultiLineDescription(event.getToolTip(), I18n.format("item.spellbundle:siphon_upgrade.desc"));
//		//		}
//	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onGuiDrawForegroundEvent(GuiContainerEvent.DrawForeground event) {
		if (!Settings.generalSettings.trinkets_integration) {
			return;
		}

		if (!(event.getGuiContainer() instanceof GuiArcaneWorkbench)) {return;}

		EntityPlayerSP player = Minecraft.getMinecraft().player;
		int bookshelfOffset = -122;

		Minecraft mc = Minecraft.getMinecraft();
		mc.renderEngine.bindTexture(GuiArcaneWorkbench.texture);
		mc.fontRenderer.drawString("Level", 11, 20, 4210752);
		mc.fontRenderer.drawString(ManaProgression.getLevel(mc.player) + "/" + ManaProgression.getMaxLevel(), 12, 30, 4210752);
		double xpProgress = (double) ManaProgression.getXP(player) / ManaProgression.calculateXpRequiredForNextLevel(ManaProgression.getXP(player));
		String percentage = String.format("%.1f%%", xpProgress * 100);
		//mc.fontRenderer.drawString(percentage, 12, 40, 4210752);

		int iconVerticalPos = 0;
		;
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onLivingDeathEvent(LivingDeathEvent event) {
		if (!Settings.generalSettings.trinkets_integration) {
			return;
		}

		if (event.getSource().getTrueSource() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();

			for (ItemStack stack : InventoryUtils.getPrioritisedHotbarAndOffhand(player)) {
				if (stack.getItem() instanceof IManaStoringItem && WandHelper.getUpgradeLevel(stack, WizardryItems.siphon_upgrade) > 0) {
					int mana = 5 * WandHelper.getUpgradeLevel(stack, WizardryItems.siphon_upgrade) + player.world.rand.nextInt(5);
					if (ItemArtefact.isArtefactActive(player, WizardryItems.ring_siphoning)) {
						mana = (int) ((float) mana * 0.6F);
					}

					int finalMana = mana;
					Capabilities.getMagicStats(player, playerMana -> {
						playerMana.addMana(finalMana);
					});
					break;
				}
			}
		}

	}
}
