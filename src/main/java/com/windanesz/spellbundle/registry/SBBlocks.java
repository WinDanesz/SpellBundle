package com.windanesz.spellbundle.registry;

import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.integration.biomesoplenty.BiomesOPlentyIntegration;
import com.windanesz.spellbundle.integration.biomesoplenty.common.BoPObjects;
import com.windanesz.spellbundle.integration.portalgun.PortalGunIntegration;
import com.windanesz.spellbundle.integration.portalgun.common.PortalGunObjects;
import com.windanesz.spellbundle.integration.treasure2.Treasure2Integration;
import com.windanesz.spellbundle.integration.treasure2.common.Treasure2Objects;
import electroblob.wizardry.registry.WizardryItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

@GameRegistry.ObjectHolder(SpellBundle.MODID)
@Mod.EventBusSubscriber
public class SBBlocks {

	public static final Block ice_chest = placeholder();
	public static final Block portalholder = placeholder();
	public static final Block conjured_bramble = placeholder();

	private SBBlocks() {} // no instances

	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T placeholder() { return null; }

	public static void registerBlock(IForgeRegistry<Block> registry, String name, Block block) {
		block.setRegistryName(SpellBundle.MODID, name);
		block.setTranslationKey(block.getRegistryName().toString());
		registry.register(block);
	}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();

		if (Treasure2Integration.getInstance().isEnabled()) {
			Treasure2Objects.registerBlocks(registry);
		}
		if (PortalGunIntegration.getInstance().isEnabled()) {
			PortalGunObjects.registerBlocks(registry);
		}
		if (BiomesOPlentyIntegration.getInstance().isEnabled()) {
			BoPObjects.registerBlocks(registry);
		}
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		if (BiomesOPlentyIntegration.getInstance().isEnabled()) {
			BoPObjects.registerSmeltingRecipes(event);
		}
	}

	/** Called from the preInit method in the main mod class to register all the tile entities. */
	public static void registerTileEntities(){
		if (Treasure2Integration.getInstance().isEnabled()) {
			Treasure2Objects.registerTileEntities();
		}
		if (PortalGunIntegration.getInstance().isEnabled()) {
			PortalGunObjects.registerTileEntities();
		}
	}

}
