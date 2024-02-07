package com.windanesz.spellbundle.integration.biomesoplenty.common;

import biomesoplenty.common.block.BlockBOPBramblePlant;
import com.windanesz.spellbundle.SpellBundle;
import electroblob.wizardry.util.AllyDesignationSystem;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockConjuredBOPBramble extends BlockBOPBramblePlant implements ITileEntityProvider {

	public BlockConjuredBOPBramble() {
		super();
		this.setRegistryName(SpellBundle.MODID, "conjured_bramble");
	}

	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileEntityTimerWithOwner(1200);
	}

	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if (entityIn instanceof EntityLivingBase && worldIn.getTileEntity(pos) instanceof TileEntityTimerWithOwner) {
			EntityLivingBase caster = ((TileEntityTimerWithOwner) worldIn.getTileEntity(pos)).getCaster();
			if (entityIn == caster || AllyDesignationSystem.isAllied(caster, (EntityLivingBase) entityIn)) {
				return;
			}
			entityIn.attackEntityFrom(DamageSource.CACTUS, 1.0F);
		}
	}

}
