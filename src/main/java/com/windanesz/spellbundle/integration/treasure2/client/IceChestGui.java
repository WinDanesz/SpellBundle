/**
 *
 */

package com.windanesz.spellbundle.integration.treasure2.client;

import com.someguyssoftware.treasure2.tileentity.ITreasureChestTileEntity;
import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.integration.treasure2.common.IceChestContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * CLIENT SIDE ONLY!
 */
public class IceChestGui extends GuiContainer {

	// This is the resource location for the background image for the GUI
	private static Map<Integer, ResourceLocation> textures;

	private int cols;
	private int rows;
	private int invSize;

	static {
		textures = new HashMap<>();
		textures.put(9, new ResourceLocation(SpellBundle.MODID, "textures/gui/container/ice_chest/9.png"));
		textures.put(15, new ResourceLocation(SpellBundle.MODID, "textures/gui/container/ice_chest/15.png"));
		textures.put(21, new ResourceLocation(SpellBundle.MODID, "textures/gui/container/ice_chest/21.png"));
		textures.put(27, new ResourceLocation(SpellBundle.MODID, "textures/gui/container/ice_chest/27.png"));
		textures.put(36, new ResourceLocation(SpellBundle.MODID, "textures/gui/container/ice_chest/36.png"));
		textures.put(45, new ResourceLocation(SpellBundle.MODID, "textures/gui/container/ice_chest/45.png"));
		textures.put(54, new ResourceLocation(SpellBundle.MODID, "textures/gui/container/ice_chest/54.png"));
	}

	private final ITreasureChestTileEntity tileEntity;

	/**
	 * NOTE can pass anything into the ChestGui (GuiContainer) as long as the
	 * player's inventory and the container's inventory is present. NOTE both can be
	 * IInventory - doesn't have to be TileEntity
	 *
	 * @param invPlayer
	 * @param tileEntity
	 */
	public IceChestGui(InventoryPlayer invPlayer, ITreasureChestTileEntity tileEntity) {
		super(new IceChestContainer(invPlayer, (IInventory) tileEntity));
		this.tileEntity = tileEntity;
		this.invSize = tileEntity.getSizeInventory();
		this.cols = invSize <= 27 ? invSize / 3 : 9;
		this.rows = invSize <= 27 ? 3 : 3 + ((invSize - 27) / 9);
		// Set the width and height of the gui. Should match the size of the texture!

		int offset = 0;
		int y = 167;
		if (this.invSize > 27) {
			offset = ((this.invSize - 27 ) / 9) * 18;
		}
		y+= offset;
		xSize = 176;
		ySize = y;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see net.minecraft.client.gui.inventory.GuiContainer#
	 * drawGuiContainerBackgroundLayer(float, int, int)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		// Bind the image texture of our custom container
		Minecraft.getMinecraft().getTextureManager().bindTexture(textures.get(invSize != 0 ? invSize : 9));
		// Draw the image
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	// draw the foreground for the GUI - rendered after the slots, but before the
	// dragged items and tooltips
	// renders relative to the top left corner of the background
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		final int LABEL_XPOS = 5;
		final int LABEL_YPOS = 5;
		fontRenderer.drawString(tileEntity.getDisplayName().getUnformattedText(), LABEL_XPOS, LABEL_YPOS,
				Color.darkGray.getRGB());
	}

	/**
	 *
	 */
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
}
