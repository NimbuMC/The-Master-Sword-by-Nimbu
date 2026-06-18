package net.nimbu.themastersword;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.nimbu.themastersword.component.ModDataComponents;
import net.nimbu.themastersword.items.custom.MasterSwordItem;
import net.nimbu.themastersword.items.ModItems;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = TheMasterSword.MOD_ID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = TheMasterSword.MOD_ID, value = Dist.CLIENT)
public class TheMasterSwordClient {

    public static BakedModel CUSTOM_GUI_MODEL = null;

    public TheMasterSwordClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(ModItems.MASTER_SWORD.asItem(), ResourceLocation.withDefaultNamespace("broken"),
                    (stack, level, entity, seed) ->
                            stack.getOrDefault(ModDataComponents.RECHARGING, false) ? 1.0F : 0.0F
            );

            ItemProperties.register(ModItems.MASTER_SWORD.asItem(), ResourceLocation.withDefaultNamespace("powered"),
                    (stack, level, entity, seed) ->
                            stack.getOrDefault(ModDataComponents.POWERED, false) ? 1.0F : 0.0F
            );
        });
    }
}
