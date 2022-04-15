package com.windanesz.spellbundle.integration.waystones.client;

import com.windanesz.spellbundle.network.SBPacketHandler;
import com.windanesz.spellbundle.network.packet.PacketRecallCommand;
import net.blay09.mods.waystones.WarpMode;
import net.blay09.mods.waystones.client.gui.GuiButtonRemoveWaystone;
import net.blay09.mods.waystones.client.gui.GuiButtonSortWaystone;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.List;

public class GuiPlayerSelect extends GuiScreen {

	private final WarpMode warpMode;
	private final WaystoneEntry fromWaystone;
	private List<EntityPlayer> players;
	private GuiButton btnPrevPage;
	private GuiButton btnNextPage;
	private int pageOffset;
	private int headerY;
	private int buttonsPerPage;

	public GuiPlayerSelect(List<EntityPlayer> players, WarpMode warpMode, EnumHand hand, @Nullable WaystoneEntry fromWaystone) {
		this.players = players;
		this.warpMode = warpMode;
		this.fromWaystone = fromWaystone;
	}

	@Override
	public void initGui() {
		btnPrevPage = new GuiButton(0, width / 2 - 100, height / 2 + 40, 95, 20, I18n.format("gui.waystones:warpStone.previousPage"));
		buttonList.add(btnPrevPage);

		btnNextPage = new GuiButton(1, width / 2 + 5, height / 2 + 40, 95, 20, I18n.format("gui.waystones:warpStone.nextPage"));
		buttonList.add(btnNextPage);

		updateList();
	}

	public void updateList() {
		final int maxContentHeight = (int) (height * 0.8f);
		final int headerHeight = 40;
		final int footerHeight = 25;
		final int entryHeight = 25;
		final int maxButtonsPerPage = (maxContentHeight - headerHeight - footerHeight) / entryHeight;

		buttonsPerPage = Math.max(4, Math.min(maxButtonsPerPage, players.size()));
		final int contentHeight = headerHeight + buttonsPerPage * entryHeight + footerHeight;
		headerY = height / 2 - contentHeight / 2;

		btnPrevPage.enabled = pageOffset > 0;
		btnNextPage.enabled = pageOffset < (players.size() - 1) / buttonsPerPage;

		buttonList.removeIf(button -> button instanceof GuiButtonPlayerEntry || button instanceof GuiButtonSortWaystone || button instanceof GuiButtonRemoveWaystone);

		int id = 2;
		int y = headerHeight;
		for (int i = 0; i < buttonsPerPage; i++) {
			int entryIndex = pageOffset * buttonsPerPage + i;
			if (entryIndex >= 0 && entryIndex < players.size()) {
				GuiButtonPlayerEntry btnWaystone = new GuiButtonPlayerEntry(id, width / 2 - 100, headerY + y, players.get(entryIndex), warpMode);

				// can't click self
				if (players.get(entryIndex).getName().equals(Minecraft.getMinecraft().player.getName())) {
					btnWaystone.enabled = false;
				}

				buttonList.add(btnWaystone);
				id++;

				y += 22;
			}
		}

		btnPrevPage.y = headerY + headerHeight + buttonsPerPage * 22 + (players.size() > 0 ? 10 : 0);
		btnNextPage.y = headerY + headerHeight + buttonsPerPage * 22 + (players.size() > 0 ? 10 : 0);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button == btnNextPage) {
			pageOffset = GuiScreen.isShiftKeyDown() ? (players.size() - 1) / buttonsPerPage : pageOffset + 1;
			updateList();
		} else if (button == btnPrevPage) {
			pageOffset = GuiScreen.isShiftKeyDown() ? 0 : pageOffset - 1;
			updateList();
		} else if (button instanceof GuiButtonPlayerEntry) {
			// Packet building
			IMessage msg = new PacketRecallCommand.Message(((GuiButtonPlayerEntry) button).getTargetPlayer().getEntityId(), fromWaystone);
			SBPacketHandler.net.sendToServer(msg);

			// packet sent, close GUI
			mc.displayGuiScreen(null);
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawWorldBackground(0);
		super.drawScreen(mouseX, mouseY, partialTicks);
		GL11.glColor4f(1f, 1f, 1f, 1f);
		drawCenteredString(fontRenderer, I18n.format("gui.spellbundle:summon_ally.select_player"), width / 2, headerY + (fromWaystone != null ? 20 : 0), 0xFFFFFF);

		if (players.size() == 0) {
			drawCenteredString(fontRenderer, TextFormatting.RED + I18n.format("gui.spellbundle:summon_ally.no_player"), width / 2, height / 2 - 20, 0xFFFFFF);
		}
	}
}
