package com.windanesz.spellbundle.integration.biomesoplenty.common;

import electroblob.wizardry.Wizardry;
import electroblob.wizardry.tileentity.TileEntityTimer;
import electroblob.wizardry.util.EntityUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nullable;
import java.util.UUID;

public class TileEntityTimerWithOwner extends TileEntityTimer {
	private UUID casterUUID = null;

	public TileEntityTimerWithOwner(int maxTimer) {
		super(maxTimer);
	}

	public void sync() {
		this.world.markAndNotifyBlock(this.pos, (Chunk) null, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
	}

	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		if (tagCompound.hasUniqueId("casterUUID")) {
			this.casterUUID = tagCompound.getUniqueId("casterUUID");
		}

	}

	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		if (this.casterUUID != null) {
			tagCompound.setUniqueId("casterUUID", this.casterUUID);
		}

		return tagCompound;
	}

	@Nullable
	public EntityLivingBase getCaster() {
		Entity entity = EntityUtils.getEntityByUUID(this.world, this.casterUUID);
		if (entity != null && !(entity instanceof EntityLivingBase)) {
			Wizardry.logger.warn("{} has a non-living owner!", this);
			entity = null;
		}

		return (EntityLivingBase) entity;
	}

	public void setCaster(@Nullable EntityLivingBase caster) {
		this.casterUUID = caster == null ? null : caster.getUniqueID();
	}
}
