package org.ricey_yam.skywiper.client.mixin;

import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SignText;
import org.ricey_yam.skywiper.client.utils.mixin_accessor.SignBlockEntityAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SignBlockEntity.class)
public abstract class SignBlockEntityMixin implements SignBlockEntityAccessor {
    @Shadow
    private SignText frontText;
    @Shadow
    private SignText backText;

    @Unique
    @Override
    public SignText getFrontText(){
        return this.frontText;
    }

    @Unique
    @Override
    public SignText getBackText(){
        return this.backText;
    }
}
