package com.windanesz.spellbundle.registry;

import com.windanesz.spellbundle.Settings;
import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.integration.pointer.PointerIntegration;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;

@GameRegistry.ObjectHolder(SpellBundle.MODID)
@Mod.EventBusSubscriber
public class SBRecipes {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void register(RegistryEvent.Register<IRecipe> event) {
		IForgeRegistryModifiable<IRecipe> registry = (IForgeRegistryModifiable<IRecipe>) event.getRegistry();

		if (PointerIntegration.getInstance().isEnabled() && Settings.generalSettings.remove_pointer_item) {
			if (registry.getValue(new ResourceLocation("pointer:pointer")) != null) {
				registry.remove(new ResourceLocation("pointer:pointer"));
			}
		}

		if (PointerIntegration.getInstance().isEnabled() && Settings.generalSettings.remove_portalgun_recipes) {
			if (registry.getValue(new ResourceLocation("portalgun:portalgun")) != null) {
				registry.remove(new ResourceLocation("portalgun:portalgun"));
			}
			if (registry.getValue(new ResourceLocation("portalgun:miniature_black_hole")) != null) {
				registry.remove(new ResourceLocation("portalgun:miniature_black_hole"));
			}
		}
	}
}
