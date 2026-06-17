package net.nimbu.themastersword.items;

import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.nimbu.themastersword.TheMasterSword;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TheMasterSword.MOD_ID);

    public static final DeferredItem<Item> MASTER_SWORD = ITEMS.register("master_sword",
            () -> new SwordItem(Tiers.DIAMOND, new Item.Properties().attributes(SwordItem.createAttributes(Tiers.DIAMOND, 4, -2.4F))));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
