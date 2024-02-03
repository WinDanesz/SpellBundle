package com.windanesz.spellbundle.spell.waystones;

import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.registry.SBItems;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;

public class MassWarp extends Warp {

	public MassWarp() {
		super(SpellBundle.MODID, "mass_warp", SpellActions.SUMMON, true);
	}

	@Override
	public boolean warp(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		if (ticksInUse < 80) {
			return true;
		} else if (ticksInUse == 80) {
			if (!caster.isSneaking()) {
				Optional<WaystoneEntry> waystone = getBoundWaystone(caster);

				if (waystone.isPresent()) {
					boolean dimensionWarp = waystone.get().getDimensionId() != caster.getEntityWorld().provider.getDimension();
					if (dimensionWarp) {
						if (!world.isRemote) {
							caster.sendStatusMessage(new TextComponentTranslation("spell." + this.getUnlocalisedName() + ".wrong_dimension"), true);
						}
						return false;
					}

					float range = getProperty(RANGE).floatValue();
					boolean hasArtefact = ItemArtefact.isArtefactActive(caster, SBItems.ring_warpstone);

					for (EntityLivingBase entity : EntityUtils.getEntitiesWithinRadius(range, caster.posX, caster.posY, caster.posZ, world, EntityLivingBase.class)) {

						if (entity instanceof EntityPlayer) {
							entity.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 60));
							if (!world.isRemote) { teleportEntity(entity, waystone.get()); }
						} else if (hasArtefact) {
							// this only actually happens if the entity is not blacklisted
							teleportEntity(entity, waystone.get());
						}
					}
					return true;
				} else {
					if (!world.isRemote) {
						caster.sendStatusMessage(new TextComponentTranslation("spell." + this.getUnlocalisedName() + ".undefined"), true);
					}
				}
			}
		}
		return false;
	}

	@Override
	protected void spawnParticleRay(World world, Vec3d origin, Vec3d direction, @Nullable EntityLivingBase caster, double distance) {
		ParticleBuilder.create(ParticleBuilder.Type.DUST).clr(0xe24bfd).vel(0, 0.1, 0).fade(1f, 1f, 1f).spin(2f, 0.015f).time(40).entity(caster).scale(1.2f).spawn(world);
		ParticleBuilder.create(ParticleBuilder.Type.DUST).clr(0xe24bfd).vel(0, 0.1, 0).fade(1f, 1f, 1f).spin(2f, -0.015f).time(40).entity(caster).scale(1.2f).spawn(world);
		if (caster != null) {
			ParticleBuilder.create(ParticleBuilder.Type.SPARKLE)
					.clr(0xe24bfd)
					.fade(0, 0, 0)
					.spin(2, 0.07f)
					.time((caster.isHandActive() ? (caster.getItemInUseMaxCount() - caster.getItemInUseCount()) : 10))
					.pos(0, caster.height / 2, 0)
					.entity(caster)
					.scale(1.2f)
					.spawn(world);
		}
	}
}
