package com.windanesz.spellbundle.integration.quark.common;

import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.integration.Integration;
import com.windanesz.spellbundle.integration.quark.QuarkIntegration;
import com.windanesz.spellbundle.registry.SBPotions;
import com.windanesz.spellbundle.spell.quark.Colorize;
import com.windanesz.spellbundle.spell.quark.CurseOfEvil;
import com.windanesz.spellbundle.spell.quark.CurseOfHaunting;
import com.windanesz.wizardryutils.capability.SummonedCreatureData;
import com.windanesz.wizardryutils.spell.SpellDynamicMinion;
import electroblob.wizardry.potion.Curse;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.BlockUtils;
import electroblob.wizardry.util.EntityUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.IForgeRegistry;
import vazkii.quark.world.entity.EntityFoxhound;
import vazkii.quark.world.entity.EntityWraith;

import java.util.List;

public class QuarkObjects {

	public static void registerPotions(IForgeRegistry<Potion> registry) {
		SBPotions.registerPotion(registry, "curse_of_haunting", new Curse(false, 0xc6ff01,
				new ResourceLocation(SpellBundle.MODID, "textures/gui/potion_icons/curse_of_haunting.png")) {
			@Override
			public void performEffect(EntityLivingBase target, int strength) {
				if (!target.world.isRemote && target.ticksExisted % 60 == 0 && target.world.rand.nextInt(10) == 0) {
					// summon a wraith
					BlockPos pos = BlockUtils.findNearbyFloorSpace(target, 5, 5);
					if (pos != null) {

						List<EntityWraith> wraithList = EntityUtils.getEntitiesWithinRadius(15f, target.posX, target.posY, target.posZ, target.world, EntityWraith.class);
						if (!wraithList.isEmpty() && wraithList.size() >= 3) {
							return;
						}

						EntityWraith wraith = new EntityWraith(target.world);
						wraith.setPosition(pos.getX(), pos.getY(), pos.getZ());

						SummonedCreatureData minionData = SummonedCreatureData.get(wraith);
						if (minionData != null) {
							wraith.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
							minionData.setCaster(null);
						}

						target.world.spawnEntity(wraith);
					}
				}
			}

			@Override
			public boolean isReady(int duration, int amplifier) {
				return true;
			}
		});
	}

	public static void registerSpells(IForgeRegistry<Spell> registry) {
		Integration instance = QuarkIntegration.getInstance();

		// these had to be moved because of the class reference
		registry.register(instance.addSpell(new CurseOfHaunting()));
		registry.register(instance.addSpell(new SpellDynamicMinion<>(SpellBundle.MODID, "conjure_foxhound", EntityFoxhound::new)));
		registry.register(instance.addSpell(new SpellDynamicMinion<>(SpellBundle.MODID, "conjure_wraith", EntityWraith::new)));
		registry.register(instance.addSpell(new CurseOfEvil()));
		registry.register(instance.addSpell(new Colorize()));
	}
}
