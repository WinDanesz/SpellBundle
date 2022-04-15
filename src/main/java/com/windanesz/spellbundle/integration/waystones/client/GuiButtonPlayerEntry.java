package com.windanesz.spellbundle.integration.waystones.client;

import net.blay09.mods.waystones.WarpMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;

public class GuiButtonPlayerEntry extends GuiButton {

	private final EntityPlayer plyr;

	public GuiButtonPlayerEntry(int id, int x, int y, EntityPlayer plyr, WarpMode mode) {
		super(id, x, y, plyr.getName());
		this.plyr = plyr;
		enabled = true;
	}

	public EntityPlayer getTargetPlayer() {
		return plyr;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		super.drawButton(mc, mouseX, mouseY, partialTicks);
		GlStateManager.color(1f, 1f, 1f, 1f);
	}
}