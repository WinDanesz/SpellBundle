package com.windanesz.spellbundle.spell.biomesoplenty;

import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.integration.biomesoplenty.common.TileEntityTimerWithOwner;
import com.windanesz.spellbundle.registry.SBBlocks;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.SpellRay;
import electroblob.wizardry.util.BlockUtils;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Briarburst extends SpellRay {
	private static final String BLOCK_LIFETIME = "block_lifetime";

	public Briarburst() {
		super(SpellBundle.MODID, "briarburst", SpellActions.POINT, true);
		addProperties(BLOCK_LIFETIME);
	}

	@Override
	protected boolean onEntityHit(World world, Entity target, Vec3d hit, EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		BlockPos pos = target.getPosition().offset(EnumFacing.byIndex((Math.max(2, world.rand.nextInt(6)))), Math.max(1,  (int) (target.width / 2)));
		spawnBrambleParticles(world, pos);

		if (ticksInUse % 4 == 0 && BlockUtils.canBlockBeReplaced(world, pos) && caster.getPosition() != pos && caster.getPosition().up() != pos) {
			spawnBramble(world, pos, modifiers, caster);
		}
		return true;
	}

	@Override
	protected boolean onBlockHit(World world, BlockPos pos, EnumFacing side, Vec3d hit, EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		if (caster != null && caster.getDistance(pos.getX(), pos.getY(), pos.getZ()) > 2) {
			pos = pos.offset(side);
			if (world.getBlockState(pos).getBlock() == SBBlocks.conjured_bramble) {
				pos = pos.up();
			}
			spawnBrambleParticles(world, pos);

			if (ticksInUse % 4 == 0 && BlockUtils.canBlockBeReplaced(world, pos) && caster.getPosition() != pos && caster.getPosition().up() != pos) {
				spawnBramble(world, pos, modifiers, caster);
				BlockPos abovePos = pos.up();
				spawnBramble(world, abovePos, modifiers, caster);
			}
		}
		return true;
	}

	@Override
	protected boolean onMiss(World world, EntityLivingBase caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}

	private void spawnBrambleParticles(World world, BlockPos pos) {
		if (world.isRemote) {
			ParticleBuilder.create(ParticleBuilder.Type.LEAF).pos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5).scale(3.0F).clr(0x9c4b22).spawn(world);
		}
	}

	private void spawnBramble(World world, BlockPos pos, SpellModifiers modifiers, EntityLivingBase caster) {
		if (!world.isRemote) {
			world.setBlockState(pos, SBBlocks.conjured_bramble.getDefaultState());
			if (world.getTileEntity(pos) instanceof TileEntityTimerWithOwner) {
				((TileEntityTimerWithOwner) world.getTileEntity(pos)).setLifetime((int) (this.getProperty(BLOCK_LIFETIME).floatValue() * modifiers.get(WizardryItems.duration_upgrade)));
				if (caster != null) {
					((TileEntityTimerWithOwner) world.getTileEntity(pos)).setCaster(caster);
				}
			}
		}
	}

	@Override
	public boolean canBeCastBy(EntityLiving npc, boolean override) {
		return true;
	}
}
