package mcjty.enigma.commands;

import mcjty.enigma.progress.Progress;
import mcjty.enigma.progress.ProgressHolder;
import mcjty.enigma.snapshot.SnapshotTools;
import mcjty.enigma.varia.BlockPosDim;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CmdSnapshot extends CommandBase {
    @Override
    public String getName() {
        return "e_snapshot";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "e_snapshot <file>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            ITextComponent component = new TextComponentString(TextFormatting.RED + "File missing!");
            if (sender instanceof EntityPlayer) {
                ((EntityPlayer) sender).sendStatusMessage(component, false);
            } else {
                sender.sendMessage(component);
            }
            return;
        }
        String fn = args[0];

        World world = sender.getEntityWorld();
        Set<Pair<Integer,ChunkPos>> chunkPosSet = new HashSet<>();
        Progress progress = ProgressHolder.getProgress(world);
        for (BlockPosDim posDim : progress.getNamedPositions()) {
            chunkPosSet.add(Pair.of(posDim.getDimension(), new ChunkPos(posDim.getPos())));
            chunkPosSet.add(Pair.of(posDim.getDimension(), new ChunkPos(posDim.getPos().east(16))));
            chunkPosSet.add(Pair.of(posDim.getDimension(), new ChunkPos(posDim.getPos().west(16))));
            chunkPosSet.add(Pair.of(posDim.getDimension(), new ChunkPos(posDim.getPos().south(16))));
            chunkPosSet.add(Pair.of(posDim.getDimension(), new ChunkPos(posDim.getPos().north(16))));
        }

        List<Chunk> chunks = new ArrayList<>();
        for (Pair<Integer, ChunkPos> pair : chunkPosSet) {
            World w = DimensionManager.getWorld(pair.getKey());
            if (w == null) {
                w = world.getMinecraftServer().getWorld(pair.getKey());
            }
            Chunk chunk = w.getChunkFromChunkCoords(pair.getRight().x, pair.getRight().z);
            if (chunk != null) {
                chunks.add(chunk);
            }
        }

        try {
            File dataDir = new File(((WorldServer) world).getChunkSaveLocation(), "enigma");
            dataDir.mkdirs();
            File file = new File(dataDir, fn);
            SnapshotTools.makeChunkSnapshot(world, chunks, file);
            ITextComponent component = new TextComponentString(TextFormatting.GREEN + "Made a snapshot in '" + fn + "' for " + chunks.size() + " chunks");
            if (sender instanceof EntityPlayer) {
                ((EntityPlayer) sender).sendStatusMessage(component, false);
            } else {
                sender.sendMessage(component);
            }
        } catch (IOException e) {
            ITextComponent component = new TextComponentString(TextFormatting.RED + "Error writing snapshot to '" + fn + "'!");
            if (sender instanceof EntityPlayer) {
                ((EntityPlayer) sender).sendStatusMessage(component, false);
            } else {
                sender.sendMessage(component);
            }
        }
    }
}
