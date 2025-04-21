package com.windanesz.spellbundle.integration.quark.mixin;

import com.windanesz.spellbundle.integration.quark.util.ElementalTagUtil;
import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.registry.WizardryEnchantments;
import electroblob.wizardry.spell.FreezingWeapon;
import electroblob.wizardry.spell.ImbueWeapon;
import electroblob.wizardry.util.InventoryUtils;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FreezingWeapon.class)
public class MixinFreezingWeapon {

	@Inject(at = @At("HEAD"), method = "cast", remap = false)
	public void mixinCast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers, CallbackInfoReturnable<Boolean> cir){

		// Won't work if the weapon already has the enchantment
		if(WizardData.get(caster) != null
				&& WizardData.get(caster).getImbuementDuration(WizardryEnchantments.freezing_weapon) <= 0){

			for(ItemStack stack : InventoryUtils.getPrioritisedHotbarAndOffhand(caster)){

				if((ImbueWeapon.isSword(stack) || ImbueWeapon.isBow(stack))
						&& !EnchantmentHelper.getEnchantments(stack).containsKey(WizardryEnchantments.freezing_weapon)){
					ElementalTagUtil.applyElementalTags(stack, (FreezingWeapon) (Object) this);
					break;
				}
			}
		}
	}
}
