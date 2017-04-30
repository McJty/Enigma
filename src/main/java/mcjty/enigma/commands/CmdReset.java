package mcjty.enigma.commands;

import mcjty.enigma.Enigma;
import mcjty.enigma.code.RootScope;
import mcjty.lib.compat.CompatCommandBase;
import mcjty.lib.tools.ChatTools;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class CmdReset extends CompatCommandBase {
    @Override
    public String getName() {
        return "e_reset";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "e_reset";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        Enigma.logger.info("Reset status:");
        RootScope.reset(server.getEntityWorld());
        ChatTools.addChatMessage(sender, new TextComponentString(TextFormatting.RED + "Total Enigma State Reset!"));
    }
}