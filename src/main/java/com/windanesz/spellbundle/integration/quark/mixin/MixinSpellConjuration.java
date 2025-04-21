package com.windanesz.spellbundle.integration.quark.mixin;

import electroblob.wizardry.spell.SpellConjuration;
import electroblob.wizardry.util.SpellModifiers;
import electroblob.wizardry.spell.Spell;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.windanesz.spellbundle.integration.quark.util.ElementalTagUtil;

@Mixin(SpellConjuration.class)
public class MixinSpellConjuration {

	@Inject(method = "addItemExtras", at = @At("RETURN"), remap = false)
	protected void mixinAddItemExtras(EntityPlayer caster, ItemStack stack, SpellModifiers modifiers, CallbackInfo ci) {
		Spell spell = (Spell) (Object) this;
		ElementalTagUtil.applyElementalTags(stack, spell);
	}
}
