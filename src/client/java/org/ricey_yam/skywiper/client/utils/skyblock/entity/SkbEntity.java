package org.ricey_yam.skywiper.client.utils.skyblock.entity;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import org.ricey_yam.skywiper.client.utils.game_ext.EntityUtils;
import org.ricey_yam.skywiper.client.utils.pool.IPoolable;
import org.ricey_yam.skywiper.client.utils.pool.InstancePool;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.regex.Pattern;

@Getter
@Setter
public class SkbEntity implements IPoolable {
    @Getter
    private final static InstancePool<SkbEntity> instancePool = new InstancePool<>(200,SkbEntity.class);
    private final static Pattern ENTITY_INFO_PATTERN = Pattern.compile("");

    private ArmorStandEntity armorStandEntity;
    private LivingEntity boundEntity;
    private String name;
    private int maxHealth;
    private int health;

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
        this.boundEntity = EntityUtils.findNearestEntity(armorStandEntity, LivingEntity.class,2,e -> !(e instanceof ArmorStandEntity) && e != armorStandEntity);
        if(boundEntity != null){
            var temp0 = EntityUtils.getEntityDisplayName(armorStandEntity);
            if (temp0 != null) {
                var temp1 = temp0.split(" ",3)[2];
                var temp2 = temp1.split("❤")[0];
                var entityInfo = temp2.split(" ");
                var nameBuilder = new StringBuilder();
                var lastIndex = entityInfo.length - 1;
                for (int i = 0; i < lastIndex; i++) {
                    nameBuilder.append(entityInfo[i]).append(i == lastIndex - 1 ? "" : " ");
                }
                this.name = nameBuilder.toString();
                var healthInfo = entityInfo[lastIndex].split("/");
                this.health = Integer.parseInt(healthInfo[0].replaceAll(",",""));
                this.maxHealth = Integer.parseInt(healthInfo[1].replaceAll(",",""));
            }
        }
    }
}
