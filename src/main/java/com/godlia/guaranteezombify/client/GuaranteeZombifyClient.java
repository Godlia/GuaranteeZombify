package com.godlia.guaranteezombify.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.passive.VillagerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.mojang.text2speech.Narrator.LOGGER;

@Mixin(VillagerEntity.class)
public class GuaranteeZombifyClient implements ClientModInitializer {
    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {

    }

    @Inject(method = "onDeath" , at = @At("HEAD"), cancellable = true)
    public void onDeath(DamageSource source, CallbackInfo ci) {
        LOGGER.info("GODLIA Villager died");
        //spawn a zombievillager with same trades as the villagers
        if(source.getAttacker() instanceof ZombieEntity) {
            VillagerEntity villager = (VillagerEntity) (Object) this;
            ZombieVillagerEntity zombieVillager = new ZombieVillagerEntity(EntityType.ZOMBIE_VILLAGER, villager.getWorld());
            zombieVillager.refreshPositionAndAngles(villager.getX(), villager.getY(), villager.getZ(), villager.getYaw(), villager.getPitch());
            zombieVillager.setVillagerData(villager.getVillagerData());
            zombieVillager.setXp(villager.getExperience());
            villager.getWorld().spawnEntity(zombieVillager);

            ci.cancel();

        }
    }
}
