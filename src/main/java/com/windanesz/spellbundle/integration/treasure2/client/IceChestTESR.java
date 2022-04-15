package com.windanesz.spellbundle.integration.treasure2.client;

import com.someguyssoftware.treasure2.client.model.StandardChestModel;
import com.someguyssoftware.treasure2.client.render.tileentity.TreasureChestTileEntityRenderer;
import com.windanesz.spellbundle.SpellBundle;
import net.minecraft.util.ResourceLocation;

public class IceChestTESR extends TreasureChestTileEntityRenderer {

	public IceChestTESR() {
		super("wood-chest", new StandardChestModel());
		setTexture(new ResourceLocation(SpellBundle.MODID + ":textures/entity/ice_chest.png"));

	}
}
