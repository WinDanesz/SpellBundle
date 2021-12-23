package com.windanesz.spellbundle.spell.waystones;

import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.client.gui.GuiPlayerSelect;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import net.blay09.mods.waystones.WarpMode;
import net.blay09.mods.waystones.block.TileWaystone;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class SummonAlly extends Warp {

	public SummonAlly() {
		super(SpellBundle.MODID, "summon_ally", SpellActions.SUMMON, true);
	}

	public boolean warp(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		if (!caster.isSneaking()) {
			Optional<WaystoneEntry> waystone = getBoundWaystone(caster);

			if (waystone.isPresent()) {
				if (caster.getDistance(waystone.get().getPos().getX(), waystone.get().getPos().getY(), waystone.get().getPos().getZ()) > 5) {
					caster.sendStatusMessage(new TextComponentTranslation("spell." + SpellBundle.MODID + ":summon_ally.distance_from_waystone_too_big", waystone.get().getName()), true);
					return false;
				}

				if (world.isRemote) {
					ParticleBuilder.create(ParticleBuilder.Type.SPARKLE)
							.clr(0xe24bfd)
							.fade(0, 0, 0)
							.spin(2, 0.07f)
							.time((caster.isHandActive() ? (caster.getItemInUseMaxCount() - caster.getItemInUseCount()) : 10))
							.pos(waystone.get().getPos().getX() + 0.5, waystone.get().getPos().getY() + 0.5, waystone.get().getPos().getZ() + 0.5)
							.scale(1.2f)
							.spawn(world);
				}

				return true;
			}
		}

		return false;
	}

	@Override
	protected boolean onEntityHit(World world, Entity target, Vec3d hit,
			@Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		if (caster instanceof EntityPlayer) {
			return warp(world, (EntityPlayer) caster, EnumHand.MAIN_HAND, ticksInUse, modifiers);
		}
		return false;
	}

	@Override
	protected boolean onBlockHit(World world, BlockPos pos, EnumFacing side, Vec3d hit,
			@Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		if (caster instanceof EntityPlayer) {

			if (caster.isSneaking()) {
				TileEntity tileEntity = world.getTileEntity(pos);
				if (tileEntity instanceof TileWaystone) {
					TileWaystone tileWaystone = ((TileWaystone) tileEntity).getParent();

					if (!world.isRemote) {
						WaystoneEntry waystone = new WaystoneEntry(tileWaystone);

						Optional<WaystoneEntry> oldWaystone = setBoundWaystone((EntityPlayer) caster, waystone);

						if (oldWaystone.isPresent()) {
							((EntityPlayer) caster).sendStatusMessage(new TextComponentTranslation("spell." + SpellBundle.MODID + ":warp.remember_with_old", waystone.getName(), oldWaystone.get().getName()), true);
						} else {
							((EntityPlayer) caster).sendStatusMessage(new TextComponentTranslation("spell." + SpellBundle.MODID + ":warp.remember", waystone.getName()), true);
						}
						return true;
					}
				} else {
					// select player
					Optional<WaystoneEntry> waystone = getBoundWaystone((EntityPlayer) caster);

					if (waystone.isPresent()) {
						if (world.isRemote) {
							List<EntityPlayer> players = caster.world.playerEntities;
							Minecraft.getMinecraft().displayGuiScreen(new GuiPlayerSelect(players, WarpMode.WARP_SCROLL, EnumHand.MAIN_HAND, waystone.get()));
						}
					} else {
						if (!world.isRemote) {
							((EntityPlayer) caster).sendStatusMessage(new TextComponentTranslation("spell.spellbundle:summon_ally.undefined"), true);
						}
					}
				}
			} else {
				return warp(world, (EntityPlayer) caster, EnumHand.MAIN_HAND, ticksInUse, modifiers);
			}
		}

		return true;
	}
}