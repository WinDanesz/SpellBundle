package com.windanesz.spellbundle.registry;

import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.integration.qualitytools.QTIntegration;
import com.windanesz.spellbundle.integration.qualitytools.common.enchantments.EnchantmentMagicallyWeaken;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import javax.annotation.Nonnull;

@ObjectHolder(SpellBundle.MODID)
@Mod.EventBusSubscriber
public final class SBEnchantments {

	private SBEnchantments() {}

	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T placeholder(){ return null; }

	public static final Enchantment magically_weakened = placeholder();
	@SubscribeEvent
	public static void register(RegistryEvent.Register<Enchantment> event) {

		if (QTIntegration.getInstance().isEnabled()) {
			event.getRegistry().register(new EnchantmentMagicallyWeaken());
		}
	}
}
