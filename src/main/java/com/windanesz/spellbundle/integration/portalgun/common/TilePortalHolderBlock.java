package com.windanesz.spellbundle.integration.portalgun.common;

import electroblob.wizardry.block.BlockReceptacle;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.util.BlockUtils;
import electroblob.wizardry.util.ParticleBuilder;
import me.ichun.mods.portalgun.common.tileentity.TilePortalBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TilePortalHolderBlock extends TileEntity implements ITickable {

	public TilePortalHolderBlock() {

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

			for (i = 0; i < 1; ++i) {
				speed = (double) (world.rand.nextBoolean() ? 1 : -1) * (0.05 + 0.02 * world.rand.nextDouble());
				ParticleBuilder.create(ParticleBuilder.Type.FLASH).time(20).scale(1f).pos(pos.getX() + 0.5, pos.getY() + world.rand.nextFloat(), pos.getZ() + 0.5).clr(BlockReceptacle.PARTICLE_COLOURS.get(Element.FIRE)[0]).spin(+0.5, speed).spawn(world);
			}
		}
	}
}
