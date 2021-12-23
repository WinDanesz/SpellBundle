package com.windanesz.spellbundle.spell.waystones;

import com.windanesz.spellbundle.Settings;
import com.windanesz.spellbundle.SpellBundle;
import electroblob.wizardry.data.IStoredVariable;
import electroblob.wizardry.data.Persistence;
import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.SpellRay;
import electroblob.wizardry.util.SpellModifiers;
import net.blay09.mods.waystones.WaystoneManager;
import net.blay09.mods.waystones.block.BlockWaystone;
import net.blay09.mods.waystones.block.TileWaystone;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Optional;

public class Warp extends SpellRay {

	protected static final String BOUND_WAYSTONE_TAG = "boundWaystone";

	public static final IStoredVariable<NBTTagCompound> BOUND_WAYSTONE = IStoredVariable.StoredVariable.ofNBT(BOUND_WAYSTONE_TAG, Persistence.ALWAYS).setSynced();

	public Warp() {
		super(SpellBundle.MODID, "warp", SpellActions.SUMMON, false);
		WizardData.registerStoredVariables(BOUND_WAYSTONE);
	}

	protected Warp(String modid, String name, EnumAction action, boolean isContinuous) {
		super(modid, name, action, isContinuous);
	}

	public boolean warp(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		if (!caster.isSneaking()) {
			Optional<WaystoneEntry> waystone = getBoundWaystone(caster);

			if (waystone.isPresent()) {

				boolean dimensionWarp = waystone.get().getDimensionId() != caster.getEntityWorld().provider.getDimension();
				if (dimensionWarp) {
					if (!world.isRemote) {
						caster.sendStatusMessage(new TextComponentTranslation("spell." + this.getUnlocalisedName() + ".wrong_dimension"), true);
					}
					return false;
				}

				caster.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 40));
				if (!world.isRemote) {
					teleportEntity(caster, waystone.get());
				}
				return true;
			} else {
				if (!world.isRemote) { caster.sendStatusMessage(new TextComponentTranslation("spell." + this.getUnlocalisedName() + ".undefined"), true); }
			}
		}
		return false;
	}

	@Override
	protected boolean onEntityHit(World world, Entity target, Vec3d hit,
			@Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		if (caster instanceof EntityPlayer) {
			return warp(world, (EntityPlayer) caster, EnumHand.MAIN_HAND, ticksInUse, modifiers);
		}
		return false;
	}

	@Override
	protected boolean onBlockHit(World world, BlockPos pos, EnumFacing side, Vec3d hit,
			@Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		if (caster instanceof EntityPlayer) {

			if (caster.isSneaking()) {
				TileEntity tileEntity = world.getTileEntity(pos);
				if (tileEntity instanceof TileWaystone) {
					TileWaystone tileWaystone = ((TileWaystone) tileEntity).getParent();

					if (!world.isRemote) {
						WaystoneEntry waystone = new WaystoneEntry(tileWaystone);

						Optional<WaystoneEntry> oldWaystone = setBoundWaystone((EntityPlayer) caster, waystone);

						if (oldWaystone.isPresent()) {
							((EntityPlayer) caster).sendStatusMessage(new TextComponentTranslation("spell." + SpellBundle.MODID + ":warp.remember_with_old", waystone.getName(), oldWaystone.get().getName()), true);
						} else {
							((EntityPlayer) caster).sendStatusMessage(new TextComponentTranslation("spell." + SpellBundle.MODID + ":warp.remember", waystone.getName()), true);
						}
						return true;
					}
				}
			} else {
				return warp(world, (EntityPlayer) caster, EnumHand.MAIN_HAND, ticksInUse, modifiers);
			}
		}

		return true;
	}

	@Override
	protected boolean onMiss(World world, @Nullable EntityLivingBase caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
		if (caster instanceof EntityPlayer) {
			return warp(world, (EntityPlayer) caster, EnumHand.MAIN_HAND, ticksInUse, modifiers);
		}
		return false;
	}

	public static Optional<WaystoneEntry> getBoundWaystone(EntityPlayer player) {
		if (player != null) {
			WizardData data = WizardData.get(player);
			NBTTagCompound compound = data.getVariable(BOUND_WAYSTONE);
			if (compound != null && compound.hasKey(BOUND_WAYSTONE_TAG)) {
				WaystoneEntry waystone = WaystoneEntry.read(compound.getCompoundTag(BOUND_WAYSTONE_TAG));
				return Optional.of(waystone);
			}
		}

		return Optional.empty();
	}

	/**
	 * Convenience method to set the bound waystone for players, also returns the previously stored entry.
	 *
	 * @param player   player to update the stored waystone for
	 * @param waystone new waystone entry
	 * @return the previously stored waystone entry, if the player had any, Optional.empty() otherwise or the player is null
	 */
	protected static Optional<WaystoneEntry> setBoundWaystone(EntityPlayer player, WaystoneEntry waystone) {
		Optional<WaystoneEntry> ret = Optional.empty();
		if (player != null && !player.world.isRemote) {
			WizardData data = WizardData.get(player);
			NBTTagCompound compound = data.getVariable(BOUND_WAYSTONE);
			if (compound != null && compound.hasKey(BOUND_WAYSTONE_TAG)) {
				WaystoneEntry oldWaystone = WaystoneEntry.read(compound.getCompoundTag(BOUND_WAYSTONE_TAG));
				ret = Optional.of(oldWaystone);
			}

			NBTTagCompound tagCompound = new NBTTagCompound();
			tagCompound.setTag(BOUND_WAYSTONE_TAG, waystone.writeToNBT());
			data.setVariable(BOUND_WAYSTONE, tagCompound);
			data.sync();
		}

		return ret;
	}

	protected static boolean teleportEntity(EntityLivingBase entity, WaystoneEntry waystone) {
		if (entity instanceof EntityPlayer) {
			WaystoneManager.teleportToWaystone((EntityPlayer) entity, waystone);
		} else if (isEntityAllowed(entity)) {
			EnumFacing facing = entity.world.getBlockState(waystone.getPos()).getValue(BlockWaystone.FACING);
			BlockPos pos = waystone.getPos().offset(facing);
			entity.setPositionAndUpdate(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
		}
		return false;
	}

	private static boolean isEntityAllowed(EntityLivingBase entityLivingBase) {
		String registryname = EntityList.getKey(entityLivingBase).toString();

		return !Arrays.stream(Settings.spellTweaksSettings.warp_entity_blacklist).anyMatch(s -> s.equals(registryname));
	}

}
