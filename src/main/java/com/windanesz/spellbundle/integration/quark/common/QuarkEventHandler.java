package com.windanesz.spellbundle.integration.quark.common;

import com.windanesz.spellbundle.integration.quark.mixin.IMixinSoundHandlerEntityWraith;
import com.windanesz.spellbundle.registry.SBItems;
import com.windanesz.wizardryutils.capability.SummonedCreatureData;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.util.BlockUtils;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.quark.world.entity.EntityWraith;
import vazkii.quark.world.feature.Wraiths;

public class QuarkEventHandler {

	public QuarkEventHandler() {}

//	private static final DataParameter<String> IDLE_SOUND = EntityDataManager.createKey(EntityWraith.class, DataSerializers.STRING);
//	private static final DataParameter<String> HURT_SOUND = EntityDataManager.createKey(EntityWraith.class, DataSerializers.STRING);
//	private static final DataParameter<String> DEATH_SOUND = EntityDataManager.createKey(EntityWraith.class, DataSerializers.STRING);

	public static QuarkEventHandler INSTANCE = new QuarkEventHandler();

	@SubscribeEvent
	public void onLivingDeathEvent(LivingDeathEvent event) {
		if (!event.getEntity().world.isRemote && event.getSource().getTrueSource() instanceof EntityPlayer && event.getEntity().world.rand.nextInt(10) < 3) {
			if (ItemArtefact.isArtefactActive((EntityPlayer) event.getSource().getTrueSource(), SBItems.ring_wraith)) {
				if (event.getEntity() instanceof EntityLivingBase) {
					EntityLivingBase deadEntity = (EntityLivingBase) event.getEntity();
					BlockPos pos = BlockUtils.findNearbyFloorSpace(deadEntity, 2, 2);
					if (pos != null) {

						EntityWraith wraith = new EntityWraith(event.getEntityLiving().world);
						wraith.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
						SummonedCreatureData minionData = SummonedCreatureData.get(wraith);
						ResourceLocation dead = EntityList.getKey(deadEntity);
						for (String sound : Wraiths.validWraithSounds) {
							String pattern = ".*" + dead.getPath() + ".*";
							String[] split = sound.split("\\|");
							if (split[0].matches(pattern)) {
								//noinspection ConstantConditions
								//if (wraith instanceof IMixinSoundHandlerEntityWraith) {
								wraith.getDataManager().set(((IMixinSoundHandlerEntityWraith) wraith).getIdleSound(), split[0]);
								wraith.getDataManager().set(((IMixinSoundHandlerEntityWraith) wraith).getHurtSound(), split[1]);
								wraith.getDataManager().set(((IMixinSoundHandlerEntityWraith) wraith).getDeathSound(), split[2]);
								//}
								break;
							}
						}

						if (minionData != null) {
							minionData.setCaster((EntityLivingBase) event.getSource().getTrueSource());
							minionData.setLifetime(600);
							minionData.setFollowOwner(true);
						}
						deadEntity.world.spawnEntity(wraith);
					}
				}
			}
		}
	}
}
