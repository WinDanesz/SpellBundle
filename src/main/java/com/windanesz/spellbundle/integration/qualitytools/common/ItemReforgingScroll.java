package com.windanesz.spellbundle.integration.qualitytools.common;

import com.tmtravlr.qualitytools.QualityToolsMod;
import com.windanesz.spellbundle.integration.qualitytools.QualityToolsUtils;
import com.windanesz.spellbundle.item.ItemRareScroll;
import com.windanesz.wizardryutils.tools.WizardryUtilsTools;
import electroblob.wizardry.client.DrawingUtils;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.Random;

public class ItemReforgingScroll extends ItemRareScroll {

	public ItemReforgingScroll() {
		super();
		this.setCreativeTab(QualityToolsMod.tabQualityTools);
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase entityLiving, int count) {
		if (!entityLiving.world.isRemote) {return;}

		World world = entityLiving.world;
		Random rand = entityLiving.world.rand;
		double posX = entityLiving.posX;
		double posY = entityLiving.posY;
		double posZ = entityLiving.posZ;

		if (world.getTotalWorldTime() % 3 == 0) {
			ParticleBuilder.create(ParticleBuilder.Type.SPARKLE, rand, posX + rand.nextDouble() * 0.5d * (rand.nextBoolean() ? 1 : -1), posY,
							posZ + rand.nextDouble() * 0.5d * (rand.nextBoolean() ? 1 : -1), 0.03, true).vel(0, 0.3, 0).clr(0x09ebe7)
					.time(20 + rand.nextInt(50)).spawn(world);

			ParticleBuilder.create(ParticleBuilder.Type.SPARKLE, rand, posX + rand.nextDouble() * 0.5d * (rand.nextBoolean() ? 1 : -1), posY,
							posZ + rand.nextDouble() * 0.5d * (rand.nextBoolean() ? 1 : -1), 0.03, true).vel(0, 0.3, 0).clr(0x0af21d)
					.time(20 + rand.nextInt(50)).spawn(world);

			ParticleBuilder.create(ParticleBuilder.Type.SPARKLE, rand, posX + rand.nextDouble() * 0.2d * (rand.nextBoolean() ? 1 : -1), posY,
							posZ + rand.nextDouble() * 0.2d * (rand.nextBoolean() ? 1 : -1), 0.03, true).spin(0.7, 0.05).vel(0, 0.3, 0).clr(0x0af2df)
					.time(20 + rand.nextInt(50)).spawn(world);
		}

		// horizontal particle on the floor, always visible
		ParticleBuilder.create(ParticleBuilder.Type.FLASH)
				.pos(entityLiving.posX, entityLiving.posY + 0.101, entityLiving.posZ)
				.face(EnumFacing.UP)
				.clr(DrawingUtils.mix(0x02baae, 0x6b6a69, 0.5f))
				.collide(false)
				.scale(2.3F)
				.time(10)
				.spawn(world);

	}

	/**
	 * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using
	 * the Item before the action is complete.
	 */
	@Override
	public ItemStack onItemUseFinish(ItemStack scrollStack, World world, EntityLivingBase entityLiving) {
		ItemStack stack = scrollStack.copy();

		if (entityLiving instanceof EntityPlayer && !world.isRemote) {
			EntityPlayer player = (EntityPlayer) entityLiving;
			ItemStack offhandStack = player.getHeldItemOffhand();
			if (offhandStack.isEmpty()) {
				WizardryUtilsTools.sendMessage(player, "item" + this.getRegistryName() + "needs_offhand_item", false);
			} else {
				ItemStack stackToReforge = player.getHeldItemOffhand().copy();
				if (QualityToolsUtils.generateQualityTag(stackToReforge, true, 30, true)) {
					player.setHeldItem(EnumHand.OFF_HAND, stackToReforge);
					stack.shrink(1);
				}
			}

		}

		return stack;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {return SpellActions.IMBUE;}

	/**
	 * Called when the equipped item is right-clicked.
	 */
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		playerIn.setActiveHand(handIn);
		return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack scrollStack, World world, EntityLivingBase entityLiving, int timeLeft) {
		// this is not getting called
		if (entityLiving instanceof EntityPlayer && !world.isRemote) {
			((EntityPlayer) entityLiving).sendStatusMessage(new TextComponentTranslation("item." + this.getRegistryName() + ".needs_offhand_item"), true);
			((EntityPlayer) entityLiving).getCooldownTracker().setCooldown(this, 40);
		}

	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {return 40;}
}
