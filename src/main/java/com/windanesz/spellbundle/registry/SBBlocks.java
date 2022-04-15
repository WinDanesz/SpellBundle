package com.windanesz.spellbundle.registry;

import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.integration.treasure2.common.Treasure2Objects;
import com.windanesz.spellbundle.integration.treasure2.Treasure2Integration;
import net.minecraft.block.Block;
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
	}

	/** Called from the preInit method in the main mod class to register all the tile entities. */
	public static void registerTileEntities(){
		if (Treasure2Integration.getInstance().isEnabled()) {
			Treasure2Objects.registerTileEntities();
		}
	}

}
