package org.ricey_yam.skywiper.client.utils.skyblock.entity.player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkbPlayerStatus {
    private StatusValue health;
    private StatusValue maxHealth;

    private StatusValue mana;
    private StatusValue maxMana;

    private StatusValue defense;

    public SkbPlayerStatus(){
        this.health = StatusValue.getInstancePool().borrowInstance();
        this.maxHealth = StatusValue.getInstancePool().borrowInstance();
        this.mana = StatusValue.getInstancePool().borrowInstance();
        this.maxMana = StatusValue.getInstancePool().borrowInstance();
        this.defense = StatusValue.getInstancePool().borrowInstance();
    }
}
