package com.windanesz.spellbundle.network.packet;

import io.netty.buffer.ByteBuf;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import static com.windanesz.spellbundle.command.CommandRecallAlly.COMMAND_RECALL_MESSAGE;

/**
 * <b>[Client -> Server]</b>
 */
public class PacketRecallCommand implements IMessageHandler<PacketRecallCommand.Message, IMessage> {

	@Override
	public IMessage onMessage(PacketRecallCommand.Message message, MessageContext ctx) {

		// Just to make sure that the side is correct
		if (ctx.side.isServer()) {

			// this is the sender's context, not the receiver player
			final EntityPlayerMP sender = ctx.getServerHandler().player;

			sender.getServerWorld().addScheduledTask(() -> {

				int playerID = (message.playerID);
				Entity entity = sender.world.getEntityByID(playerID);
				if (entity instanceof EntityPlayer) {

					EntityPlayer receiver = (EntityPlayer) entity;
					ITextComponent iTextComponent = new TextComponentTranslation("gui.spellbundle:summon_ally.click_here", message.waystone.getName());
					iTextComponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + COMMAND_RECALL_MESSAGE + " " + receiver.getUniqueID() + " " + sender.getUniqueID()));
					receiver.sendMessage(iTextComponent);
				}

			});
		}

		return null;
	}

	public static class Message implements IMessage {

		private int playerID;
		private WaystoneEntry waystone;

		// This constructor is required otherwise you'll get errors (used somewhere in fml through reflection)
		public Message() {
		}

		public Message(int entityId, WaystoneEntry waystone) {
			this.playerID = entityId;
			this.waystone = waystone;
		}

		@Override
		public void fromBytes(ByteBuf buf) {
			// The order is important
			this.playerID = buf.readInt();
			waystone = WaystoneEntry.read(buf);
		}

		@Override
		public void toBytes(ByteBuf buf) {
			buf.writeInt(playerID);
			waystone.write(buf);
		}
	}
}
