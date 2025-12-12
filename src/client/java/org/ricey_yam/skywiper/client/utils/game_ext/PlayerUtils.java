package org.ricey_yam.skywiper.client.utils.game_ext;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.ricey_yam.skywiper.client.utils.game_ext.block.BlockUtils;

public class PlayerUtils {
    /// 获取玩家选中的方块ID
    public static BlockHitResult getBlockHit(){
        var client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) {
            return null;
        }
        HitResult hitResult = client.crosshairTarget;
        if (hitResult == null || hitResult.getType() != HitResult.Type.BLOCK) {
            return null;
        }
        return (BlockHitResult) hitResult;
    }

    /// 获取玩家选中的方块信息
    public static String getSelectedBlockID() {
        var selectedBlock = getSelectedBlock();
        return BlockUtils.getBlockID(selectedBlock);
    }
    public static BlockPos getSelectedBlockPos() {
        var blockHit = getBlockHit();
        if (blockHit != null) {
            return blockHit.getBlockPos();
        }
        else return null;
    }
    public static Block getSelectedBlock() {
        var client = MinecraftClient.getInstance();
        var blockHit = getBlockHit();
        if (client.world != null) {
            if (blockHit != null) {
                return client.world.getBlockState(blockHit.getBlockPos()).getBlock();
            }
        }
        return null;
    }

}
