package com.windanesz.spellbundle.integration.portalgun.common;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class PortalHolderBlock extends BlockContainer {
	protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.9D, 0.9D, 0.1875D);
	protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.8125D, 0.9D, 1.0D, 0.9D);
	protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.8125D, 0.0D, 0.0D, 0.9D, 1.0D, 0.9D);
	protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.1875D, 1.0D, 0.9D);
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyBool UPPER = PropertyBool.create("upper");

	public PortalHolderBlock() {
		super(Material.GROUND, MapColor.AIR);
		setRegistryName("portalholder");
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(UPPER, false));
		setHardness(0.5f);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		state = state.getActualState(source, pos);
		EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);

		switch (enumfacing) {
			case EAST:
			default:
				return WEST_AABB;
			case SOUTH:
				return NORTH_AABB;
			case WEST:
				return EAST_AABB;
			case NORTH:
				return SOUTH_AABB;
		}
	}

	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Items.AIR;
	}

	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(UPPER, Boolean.valueOf(false));
	}

	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(UPPER, (meta & 4) != 0).withProperty(FACING, EnumFacing.byHorizontalIndex(meta & 3));
	}

	public int getMetaFromState(IBlockState state) {
		int i = 0;
		i = i | state.getValue(FACING).getHorizontalIndex();
		if (state.getValue(UPPER)) {
			i |= 4;
		}

		return i;
	}

	@Override
	public int tickRate(World worldIn) {
		return super.tickRate(worldIn);
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {FACING, UPPER});
	}

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return super.canRenderInLayer(state, layer);
	}

	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
		return false;
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TilePortalHolderBlock();
	}
}
