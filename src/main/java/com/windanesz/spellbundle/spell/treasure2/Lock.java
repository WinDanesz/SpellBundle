package com.windanesz.spellbundle.spell.treasure2;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.AbstractChestBlock;
import com.someguyssoftware.treasure2.block.ITreasureChestProxy;
import com.someguyssoftware.treasure2.item.LockItem;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;
import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.registry.SBItems;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.SpellRay;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class Lock extends SpellRay {

	public Lock() {
		super(SpellBundle.MODID, "lock", SpellActions.POINT, false);
	}

	@Override
	protected boolean onEntityHit(World world, Entity target, Vec3d hit,
			@Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}

	@Override
	protected boolean onBlockHit(World world, BlockPos pos, EnumFacing side, Vec3d hit,
			@Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		if (caster instanceof EntityPlayer && !world.isRemote) {

			BlockPos chestPos = pos;
			// determine if block at pos is a treasure chest
			Block block = world.getBlockState(chestPos).getBlock();
			if (block instanceof ITreasureChestProxy) {
				chestPos = ((ITreasureChestProxy) block).getChestPos(chestPos);
				block = world.getBlockState(chestPos).getBlock();
			}

			// based on com.someguyssoftware.treasure2.item.LockItem - Author: Mark Gottschling onJan 10, 2018
			if (block instanceof AbstractChestBlock) {
				// get the tile entity
				AbstractTreasureChestTileEntity tileEntity = (AbstractTreasureChestTileEntity) world.getTileEntity(chestPos);

				try {
					// handle the lock
					// NOTE don't use the return boolean as the locked flag here, as the chest is
					// already locked and if the method was
					// unsuccessful it could state the chest is unlocked.
					boolean lockedAdded = false;
					LockItem lock = (LockItem) SBItems.spectral_lock;
					// add the lock to the first lockstate that has an available slot
					for (LockState lockState : tileEntity.getLockStates()) {
						if (lockState != null && lockState.getLock() == null) {
							lockState.setLock(lock);
							tileEntity.sendUpdates();
							// decrement item in hand
							lockedAdded = true;
							break;
						}
					}
					return lockedAdded;

				} catch (Exception e) {
					Treasure.LOGGER.error("error: ", e);
				}
			}
		}
		return true;
}

	@Override
	protected boolean onMiss(World world, @Nullable EntityLivingBase caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}
}
