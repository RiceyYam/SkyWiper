package org.ricey_yam.skywiper.client.utils.mixin_accessor;

import net.minecraft.block.entity.SignText;

public interface SignBlockEntityAccessor {
    SignText getFrontText();
    SignText getBackText();
}
