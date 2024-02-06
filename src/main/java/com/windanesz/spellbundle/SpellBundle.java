package com.windanesz.spellbundle;

import com.windanesz.spellbundle.capability.SummonedCreatureData;
import com.windanesz.spellbundle.client.SBGuiHandler;
import com.windanesz.spellbundle.command.CommandRecallAlly;
import com.windanesz.spellbundle.integration.Integration;
import com.windanesz.spellbundle.integration.baubles.BaublesIntegration;
import com.windanesz.spellbundle.integration.biomesoplenty.BiomesOPlentyIntegration;
import com.windanesz.spellbundle.integration.pointer.PointerIntegration;
import com.windanesz.spellbundle.integration.portalgun.PortalGunIntegration;
import com.windanesz.spellbundle.integration.qualitytools.QTIntegration;
import com.windanesz.spellbundle.integration.quark.QuarkIntegration;
import com.windanesz.spellbundle.integration.treasure2.Treasure2Integration;
import com.windanesz.spellbundle.integration.waystones.WaystonesIntegration;
import com.windanesz.spellbundle.network.SBPacketHandler;
import com.windanesz.spellbundle.registry.SBBlocks;
import com.windanesz.spellbundle.registry.SBLoot;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Logger;

import java.util.Random;

@Mod(modid = SpellBundle.MODID, name = SpellBundle.NAME, version = "@VERSION@", acceptedMinecraftVersions = "[@MCVERSION@]",
		dependencies = "required-after:mixinbooter;required-after:ebwizardry@[@WIZARDRY_VERSION@,4.4);required-after:wizardryutils")
public class SpellBundle {

	public static final String MODID = "spellbundle";
	public static final String NAME = "Spell Bundle";

	public static final Random rand = new Random();

	public static Logger logger;

	// The instance of wizardry that Forge uses.
	@Mod.Instance(SpellBundle.MODID)
	public static SpellBundle instance;

	// Location of the proxy code, used by Forge.
	@SidedProxy(clientSide = "com.windanesz.spellbundle.client.ClientProxy", serverSide = "com.windanesz.spellbundle.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();

		// content mods
		WaystonesIntegration.getInstance().init();
		Treasure2Integration.getInstance().init();
		PointerIntegration.getInstance().init();
		QuarkIntegration.getInstance().init();
		QTIntegration.getInstance().init();
		PortalGunIntegration.getInstance().init();
		BiomesOPlentyIntegration.getInstance().init();

		proxy.registerRenderers();

		BaublesIntegration.init();

		// Capabilities
		SummonedCreatureData.register();



		// Register things that don't have registries
		SBBlocks.registerTileEntities();

		// Loot
		SBLoot.preInit();

	}

	@EventHandler
	public void init(FMLInitializationEvent event) {

		MinecraftForge.EVENT_BUS.register(instance);
		proxy.registerParticles();
		proxy.init();
		SBPacketHandler.initPackets();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new SBGuiHandler());

	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		Integration.setDisables();
	}

	@EventHandler
	public void serverStartup(FMLServerStartingEvent event) {
		if (WaystonesIntegration.getInstance().isEnabled()) {
			event.registerServerCommand(new CommandRecallAlly());
		}
	}
}
