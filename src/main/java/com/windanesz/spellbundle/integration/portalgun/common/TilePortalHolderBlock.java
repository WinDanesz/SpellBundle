package com.windanesz.spellbundle.integration.portalgun.common;

import electroblob.wizardry.block.BlockReceptacle;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.util.BlockUtils;
import electroblob.wizardry.util.ParticleBuilder;
import me.ichun.mods.portalgun.common.tileentity.TilePortalBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TilePortalHolderBlock extends TileEntity implements ITickable {

	private int element = 0;
	public int color = BlockReceptacle.PARTICLE_COLOURS.get(Element.MAGIC)[0];;
	public TilePortalHolderBlock() {

	}

	public void setElement(Element element) {
		this.element = element.ordinal();
		this.color = BlockReceptacle.PARTICLE_COLOURS.get(element)[0];

	}

	@Override
	public void update() {
		if (world.getTotalWorldTime() % 20 == 0) {
			boolean f = true;
			for (BlockPos p : BlockUtils.getBlockSphere(pos, 1)) {
				if (world.getTileEntity(p) instanceof TilePortalBase) {
					f = false;
				}
			}
			if (f) {
				world.setBlockToAir(pos);
			}
		}
		if (world.isRemote && world.getTotalWorldTime() % 5 == 0) {
			int i;
			double speed;

			for (i = 0; i < 3; ++i) {
				speed = (double) (world.rand.nextBoolean() ? 1 : -1) * (0.05 + 0.02 * world.rand.nextDouble());
				ParticleBuilder.create(ParticleBuilder.Type.DUST).time(20).scale(1f).pos(pos.getX() + 0.5, pos.getY() + world.rand.nextFloat(), pos.getZ() + 0.5).clr(color).spin(+0.5, speed).spawn(world);
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("element", this.element);
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		setElement(Element.values()[compound.getInteger("element")]);
		super.readFromNBT(compound);
	}

	public final NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
	}

	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
	}
}
