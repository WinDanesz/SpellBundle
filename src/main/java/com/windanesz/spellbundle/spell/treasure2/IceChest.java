package com.windanesz.spellbundle.spell.treasure2;

import com.someguyssoftware.treasure2.block.AbstractChestBlock;
import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.integration.treasure2.common.IceChestTileEntity;
import com.windanesz.spellbundle.registry.SBBlocks;
import com.windanesz.spellbundle.registry.SBItems;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.SpellRay;
import electroblob.wizardry.util.BlockUtils;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.ParticleBuilder.Type;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class IceChest extends SpellRay {

	private static final String BLOCK_LIFETIME = "block_lifetime";

	public IceChest() {
		super(SpellBundle.MODID, "ice_chest", SpellActions.POINT, false);
	}

	@Override
	protected boolean onEntityHit(World world, Entity target, Vec3d hit, EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}

	@Override
	protected boolean onBlockHit(World world, BlockPos pos, EnumFacing side, Vec3d hit, EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {

		pos = pos.offset(side);

		if (world.isRemote) {
			ParticleBuilder.create(Type.FLASH).pos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5).scale(3)
					.clr(0.61f, 0.89f, 0.97f).spawn(world);
		}

		if (BlockUtils.canBlockBeReplaced(world, pos)) {

			if (!world.isRemote) {

				world.setBlockState(pos, SBBlocks.ice_chest.getDefaultState().withProperty(AbstractChestBlock.FACING, caster.getHorizontalFacing().getOpposite()));

				float potency = modifiers.get(SpellModifiers.POTENCY);
				int slotCount = 9;
				if (potency >= 1.0 + 0.15 * 6) {
					slotCount = 54;
				} else if (potency >= 1.0 + 0.15 * 5) {
					slotCount = 45;
				} else if (potency >= 1.0 + 0.15 * 4) {
					slotCount = 36;
				} else if (potency >= 1.0 + 0.15 * 3) {
					slotCount = 27;
				} else if (potency >= 1.0 + 0.15 * 2) {
					slotCount = 21;
				} else if (potency >= 1.0 + 0.15 * 1) {
					slotCount = 15;
				}

				if (world.getTileEntity(pos) instanceof IceChestTileEntity) {
					IceChestTileEntity tile = (IceChestTileEntity) world.getTileEntity(pos);
					// should never be null at this point, just adding to suppress the annoying warning
					if (tile != null) {
						if (caster instanceof EntityPlayer && ItemArtefact.isArtefactActive((EntityPlayer)caster, SBItems.charm_frozen_lock)) {
							tile.setAcceptsLocks(true);
						}
						tile.setNumberOfSlots(slotCount);
						tile.sendUpdates();
					}
				}
			}

			return true;
		}

		return false;
	}

	@Override
	protected boolean onMiss(World world, EntityLivingBase caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}

}
