package com.windanesz.spellbundle;

import com.windanesz.spellbundle.command.CommandRecallAlly;
import com.windanesz.spellbundle.integration.baubles.BaublesIntegration;
import com.windanesz.spellbundle.integration.waystones.WaystonesIntegration;
import com.windanesz.spellbundle.network.SBPacketHandler;
import com.windanesz.spellbundle.registry.SBLoot;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

import java.util.Random;

@Mod(modid = SpellBundle.MODID, name = SpellBundle.NAME, version = "@VERSION@", acceptedMinecraftVersions = "[@MCVERSION@]",
		dependencies = "required-after:ebwizardry@[@WIZARDRY_VERSION@,4.4)")
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

		proxy.registerRenderers();

		BaublesIntegration.init();

		// content mods
		WaystonesIntegration.getInstance().init();

		// Loot
		SBLoot.preInit();

	}

	@EventHandler
	public void init(FMLInitializationEvent event) {

		MinecraftForge.EVENT_BUS.register(instance);
		proxy.registerParticles();
		proxy.init();
		SBPacketHandler.initPackets();
	}

	@EventHandler
	public void serverStartup(FMLServerStartingEvent event) {
		if (WaystonesIntegration.getInstance().isEnabled()) {
			event.registerServerCommand(new CommandRecallAlly());
		}
	}
}
