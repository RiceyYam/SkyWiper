package org.ricey_yam.skywiper.client.utils.skyblock.entity;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.ricey_yam.skywiper.client.utils.format.LongUtils;
import org.ricey_yam.skywiper.client.utils.format.pattern.GamePattern;
import org.ricey_yam.skywiper.client.utils.game_ext.EntityUtils;
import org.ricey_yam.skywiper.client.utils.pool.IPoolable;
import org.ricey_yam.skywiper.client.utils.pool.InstancePool;

import java.util.regex.Pattern;

@Getter
@Setter
public class SkbEntity implements IPoolable {
    @Getter
    private final static InstancePool<SkbEntity> instancePool = new InstancePool<>(200,SkbEntity.class);
    private final static Pattern ENTITY_INFO_PATTERN = Pattern.compile(
            "\\S*\\s+\\S\\s+(?<name>\\D+?)\\s+(?<health>\\S+)/(?<maxHealth>\\S+)❤"
    );

    private ArmorStandEntity armorStandEntity;
    private LivingEntity boundEntity;
    private String name;
    private long maxHealth;
    private long health;

    @Override
    public void reset(){
        armorStandEntity = null;
        boundEntity = null;
        name = "";
        maxHealth = 0;
        health = 0;
    }

    @Override
    public void update(Object... args) {
        try{
            this.armorStandEntity = (ArmorStandEntity) args[0];
            refreshBoundEntity();
        }
        catch (Exception e){
            e.printStackTrace();
            boundEntity = null;
            System.out.println("转换隐形盔甲架绑定实体时出现错误！" + e.getMessage());
        }
    }

    @Override
    public void release() {
        instancePool.returnInstance(this);
    }

    private void refreshBoundEntity(){
        this.boundEntity = EntityUtils.findNearestEntity(armorStandEntity, LivingEntity.class,2,e -> !(e instanceof ArmorStandEntity) && e != armorStandEntity&& !(e instanceof PlayerEntity));
        if(boundEntity != null){
            var infoStr = GamePattern.cleanColorSymbol(EntityUtils.getEntityDisplayName(armorStandEntity));
            if (!infoStr.isEmpty()) {
                var infoMatcher = ENTITY_INFO_PATTERN.matcher(infoStr);
                if(infoMatcher.find()){
                    this.name = infoMatcher.group("name").trim();
                    this.health = LongUtils.parseHumanReadableNumber(infoMatcher.group("health"));
                    this.maxHealth = LongUtils.parseHumanReadableNumber(infoMatcher.group("maxHealth"));
                }
            }
        }
    }
    private void getHealthValue(){

    }
}
