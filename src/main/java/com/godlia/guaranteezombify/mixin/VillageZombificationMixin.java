package com.godlia.guaranteezombify.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.logging.Logger;

@Mixin(VillagerEntity.class)
public class VillageZombificationMixin {
    @Unique
    Logger LOGGER = Logger.getLogger("GuaranteeZombify");
    @Inject(method = "onDeath" ,at = @At("HEAD"), cancellable = true)
    public void onDeath(DamageSource source, CallbackInfo ci) {
        LOGGER.info("GODLIA Villager died");
        //spawn a zombievillager with same trades as the villagers
        if(source.getAttacker() instanceof ZombieEntity) {
            VillagerEntity villager = (VillagerEntity) (Object) this;
            ZombieVillagerEntity zombieVillager = new ZombieVillagerEntity(EntityType.ZOMBIE_VILLAGER, villager.getWorld());
            zombieVillager.refreshPositionAndAngles(villager.getX(), villager.getY(), villager.getZ(), villager.getYaw(), villager.getPitch());
            zombieVillager.setVillagerData(villager.getVillagerData());
            zombieVillager.setXp(villager.getExperience());
            zombieVillager.setOfferData(villager.getOffers().toNbt());
            zombieVillager.setGossipData(villager.getGossip().serialize(NbtOps.INSTANCE));
            villager.getWorld().spawnEntity(zombieVillager);
            ci.cancel();
        }
    }

}
