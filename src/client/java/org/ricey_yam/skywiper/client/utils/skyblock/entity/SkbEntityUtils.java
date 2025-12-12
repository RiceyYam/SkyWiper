package org.ricey_yam.skywiper.client.utils.skyblock.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import org.ricey_yam.skywiper.client.utils.game_ext.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class SkbEntityUtils {
    public static List<SkbEntity> scanAllSkbEntities(LivingEntity entity, int boxSize, Predicate<SkbEntity> filter){
        var skbEntities = new ArrayList<SkbEntity>();
        var entities = EntityUtils.scanAllEntity(entity, ArmorStandEntity.class,boxSize, e -> {
            var bse = SkbEntity.getInstancePool().borrowInstance(e);
            if(bse == null) return false;
            if(bse.getBoundEntity() == null){
                bse.release();
                return false;
            }
            var r = filter.test(bse);
            if(r) skbEntities.add(bse);
            else bse.release();
            return r;
        });
        return skbEntities;
    }
    public static List<SkbEntity> scanAllSkbEntities(LivingEntity entity, int boxSize){
        return scanAllSkbEntities(entity,boxSize,e -> true);
    }

    public static SkbEntity findNearestSkbEntity(LivingEntity entity,int boxSize, Predicate<SkbEntity> filter){
        var skbEntities = scanAllSkbEntities(entity,boxSize, filter);
        if(skbEntities.isEmpty()) return null;
        var nearestSkbEntity = skbEntities.get(0);
        float minR = boxSize * 2;
        for(var skbEntity : skbEntities){
            var boundEntity = skbEntity.getBoundEntity();
            if(boundEntity == null) continue;
            if(boundEntity.distanceTo(entity) < minR){
                minR = boundEntity.distanceTo(entity);
                nearestSkbEntity = skbEntity;
            }
        }
        return nearestSkbEntity;
    }
    public static SkbEntity findNearestSkbEntity(LivingEntity entity,int boxSize){
        return findNearestSkbEntity(entity,boxSize,e -> true);
    }
}
