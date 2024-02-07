package com.windanesz.spellbundle.spell.portalgun;

import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.integration.portalgun.common.PortalHolderBlock;
import com.windanesz.spellbundle.integration.portalgun.common.TilePortalHolderBlock;
import com.windanesz.spellbundle.item.ItemPortalWand;
import com.windanesz.spellbundle.registry.SBBlocks;
import electroblob.wizardry.block.BlockReceptacle;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.item.ItemWand;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.SpellRay;
import electroblob.wizardry.util.BlockUtils;
import electroblob.wizardry.util.SpellModifiers;
import me.ichun.mods.portalgun.common.entity.EntityPortalProjectile;
import me.ichun.mods.portalgun.common.portal.PortalGunHelper;
import me.ichun.mods.portalgun.common.portal.info.PortalInfo;
import me.ichun.mods.portalgun.common.tileentity.TilePortalBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.UUID;
import java.util.stream.Collectors;

public class TwinPortals extends SpellRay {

	public TwinPortals() {
		super(SpellBundle.MODID, "twin_portals", SpellActions.POINT, true);
	}

	@Override
	protected boolean onEntityHit(World world, Entity target, Vec3d hit, EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}

	@Override
	protected boolean onBlockHit(World world, BlockPos pos, EnumFacing side, Vec3d hit, EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		if (caster != null) {
			boolean isTypeA = !caster.isSneaking();
			int color = BlockReceptacle.PARTICLE_COLOURS.get(Element.MAGIC)[0];
			if (caster.getHeldItemMainhand().getItem() instanceof ItemWand) {
				color = BlockReceptacle.PARTICLE_COLOURS.get(((ItemWand) caster.getHeldItemMainhand().getItem()).element)[0];
			} else if (caster.getHeldItemOffhand().getItem() instanceof ItemWand) {
				color = BlockReceptacle.PARTICLE_COLOURS.get(((ItemWand) caster.getHeldItemOffhand().getItem()).element)[0];
			}
			PortalInfo info = (new PortalInfo()).setInfo(caster.getUniqueID().toString(), caster.getName(), isTypeA).setColour(color);

			EnumFacing lookEF = EnumFacing.getFacingFromVector((float) caster.getLookVec().x, 0.0F, (float) caster.getLookVec().z);
			if (!world.isRemote && PortalGunHelper.spawnPortal(world, pos, side, lookEF, info, 3, 3, false)) {
			//	EntityHelper.playSoundAtEntity(caster, isTypeA ? SoundIndex.pg_wpn_portal_gun_fire_blue : SoundIndex.pg_wpn_portal_gun_fire_red, caster.getSoundCategory(), 0.2F, 1.0F + (caster.getRNG().nextFloat() - caster.getRNG().nextFloat()) * 0.1F);
				if (world.getTileEntity(pos.offset(side)) instanceof TilePortalBase) {
					if (world.getTileEntity(pos.offset(side)) != null) {world.getTileEntity(pos.offset(side)).validate();}
					if (caster instanceof EntityPlayer) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	protected boolean onMiss(World world, EntityLivingBase caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
		if (!world.isRemote  && caster != null && caster.rotationPitch > -50 && caster.rotationPitch < 50) {

			boolean X = caster.getHorizontalFacing() == EnumFacing.NORTH || caster.getHorizontalFacing() == EnumFacing.SOUTH;
			BlockPos here = new BlockPos(origin.add(direction.scale(3)));
			Element element = Element.MAGIC;
			if (caster.getHeldItemMainhand().getItem() instanceof ItemWand) {
				element = ((ItemWand) caster.getHeldItemMainhand().getItem()).element;
			} else if (caster.getHeldItemOffhand().getItem() instanceof ItemWand) {
				element = ((ItemWand) caster.getHeldItemOffhand().getItem()).element;
			}

			for (BlockPos pos : BlockUtils.getBlockSphere(new BlockPos(here.getX(), here.getY(), here.getZ()), 1.5).stream().filter(p -> X ? p.getZ() == here.getZ() : p.getX() == here.getX()).collect(Collectors.toList())) {
				if (world.isAirBlock(pos) || BlockUtils.canBlockBeReplaced(world, pos)) {
					world.setBlockState(pos, SBBlocks.portalholder.getDefaultState().withProperty(PortalHolderBlock.FACING, caster.getHorizontalFacing().getOpposite()));
					if (world.getTileEntity(pos) instanceof TilePortalHolderBlock) {
						((TilePortalHolderBlock) world.getTileEntity(pos)).setElement(element);
						((TilePortalHolderBlock) world.getTileEntity(pos)).markDirty();
						world.markAndNotifyBlock(pos, (Chunk)null, world.getBlockState(pos), world.getBlockState(pos), 3);

					}
				}
			}
		}
		return false;
	}


	public static ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand, ItemPortalWand item) {
		ItemStack stack = player.getHeldItem(hand);

		if (item.getMana(stack) >= 100) {
			UUID channelId = stack.hasTagCompound() ? stack.getTagCompound().getCompoundTag("UUID").getUniqueId("UUID") : UUID.randomUUID();
			PortalInfo info = new PortalInfo().setInfo(channelId.toString(), channelId.toString(), player.isSneaking()).setColour(0x02d991);
			if (!world.isRemote) {
				player.getEntityWorld().spawnEntity(new EntityPortalProjectile(player.getEntityWorld(), player, 1, 2, info, 25));
			}
			//	EntityHelper.playSoundAtEntity(player, player.isSneaking() ? SoundIndex.pg_wpn_portal_gun_fire_blue : SoundIndex.pg_wpn_portal_gun_fire_red, player.getSoundCategory(), 0.2F, 1.0F + (player.getRNG().nextFloat() - player.getRNG().nextFloat()) * 0.1F);
			NBTTagCompound nbt = stack.getTagCompound();
			if (nbt == null) {
				nbt = new NBTTagCompound();
			}

			nbt.setTag("UUID", new NBTTagCompound());
			nbt.getCompoundTag("UUID").setUniqueId("UUID", channelId);

			stack.setTagCompound(nbt);
			item.consumeMana(stack, 100, player);
			player.getCooldownTracker().setCooldown(item, 40);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
	}
}
