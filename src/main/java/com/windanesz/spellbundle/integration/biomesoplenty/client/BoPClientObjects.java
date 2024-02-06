package com.windanesz.spellbundle.integration.biomesoplenty.client;

import biomesoplenty.common.entities.RenderWasp;
import com.windanesz.spellbundle.integration.biomesoplenty.common.EntityWaspMinion;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BoPClientObjects {

	// Client only
	@SideOnly(Side.CLIENT)
	public static void registerRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(EntityWaspMinion.class, RenderWasp::new);
	}

}
