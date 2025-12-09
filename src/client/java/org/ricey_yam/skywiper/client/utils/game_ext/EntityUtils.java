package org.ricey_yam.skywiper.client.utils.game_ext;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.ricey_yam.skywiper.client.utils.client.ClientUtils;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

public class EntityUtils {
    public static <T extends Entity> List<T> scanAllEntity(LivingEntity entity, Class<T> targetEntityClass, int boxSize, Predicate<? super T> predicate){
        if(entity == null) return null;
        var world = entity.getWorld();
        if(world == null) return null;
        var minPos = entity.getBlockPos().add(-boxSize, -boxSize, -boxSize);
        var maxPos = entity.getBlockPos().add(boxSize, boxSize, boxSize);
        var minVec3 = new Vec3d(minPos.getX(), minPos.getY(), minPos.getZ());
        var maxVec3 = new Vec3d(maxPos.getX(), maxPos.getY(), maxPos.getZ());
        return world.getEntitiesByClass(targetEntityClass,new Box(minVec3,maxVec3),predicate);
    }

    public static <T extends Entity> T findNearestEntity(LivingEntity entity,Class<T> targetEntityClass,int boxSize,Predicate<? super T> predicate){
        var targets = scanAllEntity(entity,targetEntityClass,boxSize ,predicate);
        if(targets.isEmpty()) return null;
        var nearestEntity = targets.get(0);
        float minR = boxSize * 2;
        for(var target : targets){
            if(target.distanceTo(entity) < minR){
                minR = target.distanceTo(entity);
                nearestEntity = target;
            }
        }
        return nearestEntity;
    }
    public static <T extends Entity> T findNearestEntity(LivingEntity entity,Class<T> targetEntityClass,int boxSize){
        return findNearestEntity(entity,targetEntityClass,boxSize,e -> true);
    }

    public static String getEntityID(Entity entity){
        if(entity == null) return null;
        var i = Registries.ENTITY_TYPE.getId(entity.getType());
        return i.toString();
    }

    public static LivingEntity getEntityByUUID(UUID uuid){
        var world = ClientUtils.getWorld();
        if(world == null) return null;
        for (var entity : world.getEntitiesByClass(LivingEntity.class, Box.from(Vec3d.ZERO).expand(30000), e -> true)) {
            if (entity.getUuid().equals(uuid)) {
                return entity;
            }
        }
        return null;
    }

    public static String getEntityDisplayName(LivingEntity entity) {
        var mc = ClientUtils.getClient();
        if (mc.world == null) return null;

        if (entity.hasCustomName()) {
            return Objects.requireNonNull(entity.getCustomName()).getString().trim();
        }

        var scoreboard = mc.world.getScoreboard();
        var team = scoreboard.getTeam(entity.getNameForScoreboard());
        if (team != null) {
            var teamPrefix = team.getPrefix();
            var teamSuffix = team.getSuffix();
            var baseName = entity.getName();
            return Text.empty().append(teamPrefix).append(baseName).append(teamSuffix).getString().trim();
        }

        return entity.getName().getString().trim();
    }
}
