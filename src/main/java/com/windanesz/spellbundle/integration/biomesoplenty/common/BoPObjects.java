package com.windanesz.spellbundle.integration.biomesoplenty.common;

import biomesoplenty.api.block.BOPBlocks;
import com.windanesz.spellbundle.registry.SBEntities;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.tileentity.TileEntityThorns;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class BoPObjects {

	private BoPObjects() {}

	public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
		IForgeRegistry<EntityEntry> registry = event.getRegistry();
		registry.register(SBEntities.createEntry(EntityWaspMinion.class, "wasp_minion", SBEntities.TrackingType.LIVING).build());
	}

	public static void registerBlocks(IForgeRegistry<Block> registry) {
		registry.register(new BlockConjuredBOPBramble());

	}

	public static void registerSmeltingRecipes(RegistryEvent.Register<IRecipe> event) {
		FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(Item.getItemFromBlock(BOPBlocks.log_1), 1, 5), new ItemStack(WizardryItems.crystal_shard), 0.1f);
	}

	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityTimerWithOwner.class, new ResourceLocation("spellbundle", "player_save_timed_with_owner"));

	}
}