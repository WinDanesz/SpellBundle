package com.windanesz.spellbundle.command;

import com.windanesz.spellbundle.registry.SBSpells;
import com.windanesz.spellbundle.spell.waystones.Warp;
import electroblob.wizardry.item.ISpellCastingItem;
import net.blay09.mods.waystones.WaystoneManager;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.Optional;

public class CommandRecallAlly extends CommandBase {

	public static final String COMMAND_RECALL_MESSAGE = "spellbundle_recall_accept_message";

	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public boolean checkPermission(final MinecraftServer server, final ICommandSender sender) {
		return true;
	}

	@Override
	public String getName() {
		return COMMAND_RECALL_MESSAGE;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return null;
	}

	public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
		if (args.length != 2) {
			throw new WrongUsageException(this.getUsage(sender), new Object[0]);
		}
		if (!(sender instanceof EntityPlayerMP)) {
			throw new WrongUsageException(this.getUsage(sender), new Object[0]);
		}
		final EntityPlayer allyPlayer = getPlayer(server, sender, args[0]);
		final EntityPlayer casterPlayer = getPlayer(server, sender, args[1]);
		if (casterPlayer != null) {
			if (casterPlayer.dimension != allyPlayer.dimension) {
				casterPlayer.sendStatusMessage(new TextComponentTranslation("spell.spellbundle:summon_ally.ally_wrong_dimension"), true);
				allyPlayer.sendStatusMessage(new TextComponentTranslation("spell.spellbundle:summon_ally.caster_wrong_dimension"), true);
			} else {

				if (casterPlayer.isHandActive()) {
					ItemStack stack = casterPlayer.getActiveItemStack();
					if (stack.getItem() instanceof ISpellCastingItem && ((ISpellCastingItem) stack.getItem()).getCurrentSpell(stack) == SBSpells.summon_ally) {

						Optional<WaystoneEntry> waystone = Warp.getBoundWaystone(casterPlayer);
						waystone.ifPresent(waystoneEntry -> WaystoneManager.teleportToWaystone(allyPlayer, waystoneEntry));
					}
				}
			}
		} else {
			allyPlayer.sendStatusMessage(new TextComponentTranslation("spell.spellbundle:summon_ally.not_casting"), true);
		}
	}

	public boolean isUsernameIndex(final String[] args, final int index) {
		return index == 0;
	}
}
