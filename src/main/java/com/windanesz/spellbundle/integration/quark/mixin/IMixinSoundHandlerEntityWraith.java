package com.windanesz.spellbundle.integration.quark.mixin;

import net.minecraft.network.datasync.DataParameter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import vazkii.quark.world.entity.EntityWraith;

@Mixin(EntityWraith.class)
public interface IMixinSoundHandlerEntityWraith
{

	@Accessor("IDLE_SOUND")
	public DataParameter<String> getIdleSound();

	@Accessor("HURT_SOUND")
	public DataParameter<String> getHurtSound();


	@Accessor("DEATH_SOUND")
	public DataParameter<String> getDeathSound();
}
