package net.nimbu.themastersword.particle;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.nimbu.themastersword.TheMasterSword;

import java.util.function.Supplier;

public class ModParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, TheMasterSword.MOD_ID);

//    public static final Supplier<SimpleParticleType> MASTER_SWORD_POWERED_PARTICLE =
//            PARTICLE_TYPES.register("powered_master_sword_particle", () -> new SimpleParticleType(false));

    public static final Supplier<SimpleParticleType> MASTER_SWORD_HIT_PARTICLE =
            PARTICLE_TYPES.register("master_sword_hit_particle", () -> new SimpleParticleType(false));

    public static void register(IEventBus eventBus){
        PARTICLE_TYPES.register(eventBus);
    }
}
