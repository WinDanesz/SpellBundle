//package com.windanesz.spellbundle.integration.quark.mixin;
//
//import electroblob.wizardry.enchantment.Imbuement;
//import net.minecraft.item.ItemStack;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import com.windanesz.spellbundle.integration.quark.util.ElementalTagUtil;
//
//@Mixin(Imbuement.class)
//public class MixinImbuement {
// Doesn't work because the method is private
//
//	@Inject(method = "removeImbuements", at = @At("HEAD"), remap = false)
//	private static void mixinRemoveImbuements(ItemStack stack, CallbackInfo ci) {
//		ElementalTagUtil.restoreOriginalColor(stack); // Restore original color when imbuements are removed
//	}
//}
