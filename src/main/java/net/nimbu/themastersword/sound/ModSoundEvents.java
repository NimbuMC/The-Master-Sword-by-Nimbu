package net.nimbu.themastersword.sound;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.nimbu.themastersword.TheMasterSword;

import java.util.function.Supplier;

public class ModSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, TheMasterSword.MOD_ID);

    public static final Supplier<SoundEvent> MASTER_SWORD_HIT = registerSoundEvent("master_sword_hit");
    public static final Supplier<SoundEvent> MASTER_SWORD_TRANSFORM = registerSoundEvent("master_sword_transform");
    public static final Supplier<SoundEvent> MASTER_SWORD_BREAK = registerSoundEvent("master_sword_break");

    private static Supplier<SoundEvent> registerSoundEvent(String name){
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(TheMasterSword.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus){
        SOUND_EVENTS.register(eventBus);
    }
}
