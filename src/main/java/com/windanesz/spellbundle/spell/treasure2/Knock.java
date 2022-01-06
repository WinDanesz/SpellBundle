package com.windanesz.spellbundle.spell.treasure2;

import com.someguyssoftware.treasure2.block.AbstractChestBlock;
import com.someguyssoftware.treasure2.block.ITreasureChestProxy;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.item.LockItem;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;
import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.registry.SBItems;
import electroblob.wizardry.constants.Constants;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.SpellRay;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;
import java.util.List;

public class Knock extends SpellRay {

	private static final String MIN_TIER = "min_tier";

	public Knock() {
		super(SpellBundle.MODID, "knock", SpellActions.POINT, false);
		addProperties(MIN_TIER);
	}

	protected Knock(String modid, String name, EnumAction action, boolean isContinuous) {
		super(modid, name, action, isContinuous);
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

			// max tier this spell can knock
			int minTier = getProperty(MIN_TIER).intValue();

			// The maximum harvest level as determined by the potency multiplier. The + 0.5f is so that
			// weird float processing doesn't incorrectly round it down.
			int maxTier = minTier + ((int) ((modifiers.get(SpellModifiers.POTENCY) - 1) / Constants.POTENCY_INCREASE_PER_TIER -0.2f));

			// can't open locks better than RARE (=3) wih just potency
			maxTier = Math.min(3, maxTier);

			// +1 tier from ring artefact, looping so that it can stack with two rings...
			for (ItemArtefact artefact : ItemArtefact.getActiveArtefacts((EntityPlayer) caster, ItemArtefact.Type.RING)) {
				if (artefact == SBItems.ring_key) {
					maxTier++;
				}
			}

			// artefact that bumps maxTier by one, allowing to potentially knock Epic locks with potency high enough for RARE rarity

			// Based on com.someguyssoftware.treasure2.item.KeyItem.onItemUse
			// Author: Mark Gottschling
			BlockPos chestPos = pos;
			// determine if block at pos is a treasure chest
			Block block = world.getBlockState(chestPos).getBlock();
			if (block instanceof ITreasureChestProxy) {
				chestPos = ((ITreasureChestProxy) block).getChestPos(chestPos);
				block = world.getBlockState(chestPos).getBlock();
			}

			if (block instanceof AbstractChestBlock) {
				// get the tile entity
				TileEntity tileEntity = world.getTileEntity(chestPos);
				if (tileEntity == null || !(tileEntity instanceof AbstractTreasureChestTileEntity)) {
					// Null or incorrect TileEntity
					return false;
				}
				AbstractTreasureChestTileEntity chestTileEntity = (AbstractTreasureChestTileEntity) tileEntity;

				// if chest has no locks
				if (!chestTileEntity.hasLocks()) { return false; }

				List<LockState> lockStates = chestTileEntity.getLockStates();

				// probably can't happen but better safe..
				if (lockStates == null || lockStates.isEmpty()) { return false; }

				boolean unlocked = false;

				for (LockState lockState : lockStates) {
					if (lockState.getLock() != null && lockState.getLock().getRarity().ordinal() <= maxTier) {
						LockItem lock = lockState.getLock();
						unlocked = true;

						// remove the lock
						lockState.setLock(null);

						// update the client
						chestTileEntity.sendUpdates();

						// spawn the lock
						if (TreasureConfig.KEYS_LOCKS.enableLockDrops) {
							InventoryHelper.spawnItemStack(world, (double)chestPos.getX(), (double)chestPos.getY(), (double)chestPos.getZ(), new ItemStack(lock));
						}

						((WorldServer) world).spawnParticle(EnumParticleTypes.SPELL_WITCH, chestPos.getX() + lockState.getSlot().getXOffset(), chestPos.getY() + lockState.getSlot().getYOffset(), chestPos.getZ() + lockState.getSlot().getZOffset(), 24, 0.0D, 0.1D, 0.0D, 0.1D);
						break;
					}
				}

				if (!unlocked) {
					((EntityPlayer) caster).sendStatusMessage(new TextComponentTranslation("spell.spellbundle:knock.weak_spell"), true);
				}
				return true;
			}
		}
		return true;
}

	@Override
	protected boolean onMiss(World world, @Nullable EntityLivingBase caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}
}
