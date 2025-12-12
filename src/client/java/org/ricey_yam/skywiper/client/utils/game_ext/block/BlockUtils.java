package org.ricey_yam.skywiper.client.utils.game_ext.block;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.*;
import java.util.function.Predicate;

public class BlockUtils {
    /// 搜索最近的方块的位置
    public static BlockPos findNearestBlock(LivingEntity livingEntity, int radius, Predicate<BlockPos> predicate) {
        if(livingEntity == null) return null;
        var startPos = livingEntity.getBlockPos();
        var world = livingEntity.getWorld();
        Queue<BlockPos> queue = new LinkedList<>();
        Set<BlockPos> visited = new HashSet<>();

        queue.add(startPos);
        visited.add(startPos);

        int[][] directions = {{0, 1, 0}, {0, -1, 0}, {1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1}};

        while (!queue.isEmpty()) {
            BlockPos currentPos = queue.poll();
            if (predicate.test(currentPos)) {
                return currentPos;
            }
            if (currentPos.getManhattanDistance(startPos) > radius) {
                continue;
            }
            for (int[] dir : directions) {
                BlockPos neighborPos = currentPos.add(dir[0], dir[1], dir[2]);
                if (!visited.contains(neighborPos)) {
                    visited.add(neighborPos);
                    queue.add(neighborPos);
                }
            }
        }

        return null;
    }

    /// 扫描附近符合要求的全部方块位置
    public static List<BlockPos> scanAllBlock(LivingEntity livingEntity, int radius,Predicate<BlockPos> predicate) {
        var entityPos = livingEntity.getBlockPos();
        var result = new ArrayList<BlockPos>();
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    var pos = entityPos.add(x, y, z);
                    if(predicate.test(pos)){
                        result.add(pos);
                    }
                }
            }
        }
        return result;
    }
    /// 获取方块ID
    public static String getBlockID(BlockPos pos) {
        var world = MinecraftClient.getInstance().world;
        if (world == null || pos == null) {
            return null;
        }
        Block block = world.getBlockState(pos).getBlock();
        Identifier blockId = Registries.BLOCK.getId(block);
        return blockId.toString();
    }
    public static String getBlockID(Block block) {
        if(block == null) return null;
        Identifier blockId = Registries.BLOCK.getId(block);
        return blockId.toString();
    }
}
