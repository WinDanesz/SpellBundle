package com.windanesz.spellbundle.integration.treasure2.common;

import com.someguyssoftware.treasure2.item.LockItem;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;

public class IceChestTileEntity extends AbstractTreasureChestTileEntity {

	private static final String NUMBER_OF_SLOTS = "number_of_slots";
	private static final String ACCEPTS_LOCKS = "accepts_locks";

	private static final int maxSlots = 54;
	private int slotCount = 0;
	private boolean acceptsLocks = false;

	public IceChestTileEntity() {
		super();
		setCustomName(I18n.translateToLocal("tile.spellbundle:display.ice_chest.name"));
	}

	/**
	 * @return the numberOfSlots, 9 by default. Number of slots depends on spell potency
	 */
	@Override
	public int getNumberOfSlots() {
		return this.slotCount == 0 ? 54 : this.slotCount;
	}

	@Override
	public void setNumberOfSlots(int numberOfSlots) {
		this.slotCount = numberOfSlots;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		if (!hasLocks() && getItems().size() > index) {
			return getItems().get(index);
		}
		return ItemStack.EMPTY;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		if (slotCount != 0) {
			nbt.setInteger(NUMBER_OF_SLOTS, slotCount);
		}
		nbt.setBoolean(ACCEPTS_LOCKS, acceptsLocks);
		return super.writeToNBT(nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		if (nbt.hasKey(NUMBER_OF_SLOTS)) {
			slotCount = nbt.getInteger(NUMBER_OF_SLOTS);
		}
		if (nbt.hasKey(ACCEPTS_LOCKS)) {
			acceptsLocks = nbt.getBoolean(ACCEPTS_LOCKS);
		}

		super.readFromNBT(nbt);
	}

	@Override
	public void readFromItemStackNBT(NBTTagCompound nbt) {
		if (nbt.hasKey(NUMBER_OF_SLOTS)) {
			slotCount = nbt.getInteger(NUMBER_OF_SLOTS);
		}
		super.readFromItemStackNBT(nbt);
	}

	public boolean isAcceptsLocks() {
		return acceptsLocks;
	}

	public void setAcceptsLocks(boolean acceptsLocks) {
		setCustomName(I18n.translateToLocal("tile.spellbundle:display.ice_chest_lockable.name"));
		this.acceptsLocks = acceptsLocks;
	}

	@Override
	public void update() {
		if (!this.world.isRemote) {
			// dump the lock
			if (!isAcceptsLocks() && !hasLocks()) {
				for (LockState state : getLockStates()) {
					if (state != null && state.getLock() != null) {

						LockItem lock = state.getLock();

						// remove the lock
						state.setLock(null);

						// update the client
						this.sendUpdates();
						InventoryHelper.spawnItemStack(world, (double) this.pos.getX(), this.pos.getY(), this.pos.getZ(), new ItemStack(lock));
					}
				}
			}
		}
		super.update();
	}

	//	/**
	//	 * Hack to make the artefact work
	//	 *
	//	 * @return
	//	 */
	//	@Override
	//	public List<LockState> getLockStates() {
	//		if (this.world != null && !this.world.isRemote) {
	//			boolean lockable = false;
	//			boolean triesLocking = false;
	//			for (EntityPlayer player : EntityUtils.getEntitiesWithinRadius(15, this.pos.getX(), this.pos.getY(), this.pos.getZ(), this.world, EntityPlayer.class)) {
	//				if (ItemArtefact.isArtefactActive(player, SBItems.charm_frozen_lock)) {
	//					lockable = true;
	//					break;
	//				}
	//				if (player.isSneaking() && (player.getHeldItemMainhand().getItem() instanceof LockItem || player.getHeldItemOffhand().getItem() instanceof LockItem)) {
	//					triesLocking = true;
	//				}
	//			}
	//			if (lockable) {
	//				return super.getLockStates();
	//			} else if (triesLocking) {
	//				List<LockState> dummy = Arrays.asList(null);
	//				return dummy;
	//			}
	//			else {
	//				return super.getLockStates();
	//			}
	//		}
	//
	//		return super.getLockStates();
	//	}
}
