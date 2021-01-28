package com.djb.martial_cultivation.capabilities.skills.base;

import com.djb.martial_cultivation.Main;
import com.djb.martial_cultivation.capabilities.skills.CultivationSkill;
import com.djb.martial_cultivation.capabilities.skills.CultivationSkillSubType;
import com.djb.martial_cultivation.capabilities.skills.CultivationSkillType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

public class BasicQiEnhanceSkill extends CultivationSkill {
    private final ResourceLocation textureLocation = new ResourceLocation(Main.MOD_ID, "textures/skill/" + this.skillId + ".png");
    public static final String skillId = "basic_qi_enhance_skill";

    public BasicQiEnhanceSkill() {
        super(
            CultivationSkillType.BASE,
            CultivationSkillSubType.QI_ENHANCEMENT,
            skillId);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();

        nbt.put("base", super.serializeNBT());

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT((CompoundNBT) nbt.get("base"));
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return this.textureLocation;
    }

    @Override
    public String getSkillId() {
        return skillId;
    }
}
