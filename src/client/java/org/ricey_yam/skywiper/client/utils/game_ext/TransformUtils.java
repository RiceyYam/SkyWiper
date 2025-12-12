package org.ricey_yam.skywiper.client.utils.game_ext;

import baritone.api.utils.Rotation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class TransformUtils {
    public static Rotation getRotation(Vec3d vec3dForm, Vec3d vec3dTo) {
        var diff = vec3dTo.subtract(vec3dForm);
        var distance = diff.length();
        var xzDistance = Math.sqrt(diff.x * diff.x + diff.z * diff.z);

        float pitch = (float) Math.toDegrees(-Math.atan2(diff.y, xzDistance));
        float yaw = (float) Math.toDegrees(Math.atan2(diff.z, diff.x)) - 90.0F;

        return new Rotation(yaw, pitch).normalize();
    }

    public static float getDistance(BlockPos pos1, BlockPos pos2) {
        var x = pos1.getX() - pos2.getX();
        var y = pos1.getY() - pos2.getY();
        var z = pos1.getZ() - pos2.getZ();
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public static float to180(float yaw) {
        yaw %= 360f;
        if (yaw >= 180f) yaw -= 360f;
        if (yaw < -180f) yaw += 360f;
        return yaw;
    }

    public static float lerp(float start, float target, float progress, boolean isYaw) {
        if (isYaw) {
            start = to180(start);
            target = to180(target);
            float delta = to180(target - start);
            return to180(start + delta * progress);
        }
        return start + (target - start) * progress;
    }

    public static float slerp(float start, float target, float progress, boolean isYaw) {
        if (isYaw) {
            start = to180(start);
            target = to180(target);
            double delta = Math.toRadians(to180(target - start));
            double startRad = Math.toRadians(start);
            double targetRad = Math.toRadians(target);

            double sinDelta = Math.sin(delta);
            double interpolatedRad;
            if (Math.abs(sinDelta) < 1e-6) {
                interpolatedRad = startRad;
            } else {
                interpolatedRad = (Math.sin((1 - progress) * delta) * startRad
                        + Math.sin(progress * delta) * targetRad) / sinDelta;
            }
            return to180((float) Math.toDegrees(interpolatedRad));
        }
        return lerp(start, target, progress, false);
    }

    public static BlockPos getBlocksAheadInLookDir(LivingEntity livingEntity,float distance) {
        if (livingEntity == null) {
            return (BlockPos) BlockPos.ZERO;
        }

        var eyePos = livingEntity.getEyePos();

        var lookDir = getPlayerLookDirection(livingEntity);

        var targetVec = eyePos.add(lookDir.x * 8, lookDir.y * 8, lookDir.z * 8);

        int blockX = (int) Math.floor(targetVec.x);
        int blockY = (int) Math.floor(targetVec.y);
        int blockZ = (int) Math.floor(targetVec.z);

        return new BlockPos(blockX, blockY, blockZ);
    }

    private static Vec3d getPlayerLookDirection(LivingEntity livingEntity) {
        float yaw = livingEntity.getYaw();
        float pitch = livingEntity.getPitch();

        float yawRad = (float) Math.toRadians(yaw);
        float pitchRad = (float) Math.toRadians(pitch);

        double x = -Math.sin(yawRad) * Math.cos(pitchRad);
        double y = -Math.sin(pitchRad);
        double z = Math.cos(yawRad) * Math.cos(pitchRad);

        return new Vec3d(x, y, z).normalize();
    }

    public static Vec3d getEntityCenterPos(Entity entity) {
        if (entity == null) {
            return Vec3d.ZERO;
        }
        var boundingBox = entity.getBoundingBox();
        double centerX = (boundingBox.minX + boundingBox.maxX) / 2.0;
        double centerY = (boundingBox.minY + boundingBox.maxY) / 2.0;
        double centerZ = (boundingBox.minZ + boundingBox.maxZ) / 2.0;
        return new Vec3d(centerX, centerY, centerZ);
    }
}
