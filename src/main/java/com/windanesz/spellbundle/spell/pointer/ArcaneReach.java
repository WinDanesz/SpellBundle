package com.windanesz.spellbundle.spell.pointer;

import com.cleanroommc.pointer.EntityPlayerExpansion;
import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.wizardryutils.tools.WizardryUtilsTools;
import electroblob.wizardry.data.IStoredVariable;
import electroblob.wizardry.data.Persistence;
import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.SpellRay;
import electroblob.wizardry.util.Location;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.ParticleBuilder.Type;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ArcaneReach extends SpellRay {

	public static final IStoredVariable<Location> LOCATION = new IStoredVariable.StoredVariable<Location, NBTTagCompound>("arcaneHandBlockPos", Location::toNBT, Location::fromNBT, Persistence.ALWAYS);

	public ArcaneReach() {
		super(SpellBundle.MODID, "arcane_reach", SpellActions.POINT, false);
		WizardData.registerStoredVariables(LOCATION);
	}

	public static Location getLocation(EntityPlayer player) {
		WizardData data = WizardData.get(player);
		return data.getVariable(ArcaneReach.LOCATION);
	}

	public static void setLocation(EntityPlayer player, Location location) {
		WizardData data = WizardData.get(player);
		data.setVariable(ArcaneReach.LOCATION, location);
		data.sync();
	}

	@Override
	protected boolean onEntityHit(World world, Entity target, Vec3d hit, EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}

	@Override
	protected boolean onBlockHit(World world, BlockPos pos, EnumFacing side, Vec3d hit, EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {

		//pos = pos.offset(side);

		EntityPlayer player = (EntityPlayer) caster;

		if (world.isRemote) {
			ParticleBuilder.create(Type.FLASH).pos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5).scale(3)
					.clr(0.61f, 0.89f, 0.97f).spawn(world);
		}

		Location location = getLocation(player);

		if (location == null) {
			// no pointer - setting it

			if (!world.isRemote) {
				Location location1 = new Location(pos, player.dimension);
				setLocation(player, location1);
				WizardryUtilsTools.sendMessage(player, "spell." + this.getRegistryName() + ".stored_location", true);
			}
			return true;
		} else {
			// has pointer
			if (player.isSneaking()) {
				// remove pointer
				WizardryUtilsTools.sendMessage(player, "spell." + this.getRegistryName() + ".stored_location_removed", true);
				if (!world.isRemote) {
					setLocation(player, null);
				}
				return true;
			}

			if (location.dimension != player.dimension) {
				// other dimension
			} else {
				// same dimension - normal behaviour, accessing the pointer
				IBlockState state = world.getBlockState(location.pos);
				((EntityPlayerExpansion) player).setUsingPointer();
				if (!world.isRemote) {
					this.runRemoteRightClickRoutine(player, world, EnumHand.MAIN_HAND, location.pos, EnumFacing.NORTH, 1f, 1f, 1f);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	protected boolean onMiss(World world, EntityLivingBase caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
		EntityPlayer player = (EntityPlayer) caster;

		Location location = getLocation(player);

		if (location == null) {
			// no pointer
			WizardryUtilsTools.sendMessage(player, "spell." + this.getRegistryName() + ".no_stored_location", true);
			return false;
		} else {
			// has pointer
			if (player.isSneaking()) {
				// remove pointer
				WizardryUtilsTools.sendMessage(player, "spell." + this.getRegistryName() + ".stored_location_removed", true);
				if (!world.isRemote) {
					setLocation(player, null);
				}
				return true;
			}

			if (location.dimension != player.dimension) {
				// other dimension
			} else {
				// same dimension - normal behaviour, accessing the pointer
				IBlockState state = world.getBlockState(location.pos);
				((EntityPlayerExpansion) player).setUsingPointer();
				if (!world.isRemote) {
					this.runRemoteRightClickRoutine(player, world, EnumHand.MAIN_HAND, location.pos, EnumFacing.NORTH, 1f, 1f, 1f);
				}
				return true;
			}
		}
		return false;
	}

	private void runRemoteRightClickRoutine(EntityPlayer player, World world, EnumHand hand, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ) {
		PlayerInteractEvent.RightClickBlock event = ForgeHooks.onRightClickBlock(player, hand, pos, facing, ForgeHooks.rayTraceEyeHitVec(player, player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue() + 1.0D));
		if (!event.isCanceled()) {
			IBlockState state = world.getBlockState(pos);
			if (event.getUseBlock() != Event.Result.DENY) {
				state.getBlock().onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
			}

		}
	}

}
