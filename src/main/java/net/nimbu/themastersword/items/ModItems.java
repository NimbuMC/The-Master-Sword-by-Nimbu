package net.nimbu.themastersword.items;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.nimbu.themastersword.TheMasterSword;
import net.nimbu.themastersword.items.custom.MasterSwordItem;

public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TheMasterSword.MOD_ID);

    public static final DeferredItem<Item> MASTER_SWORD = ITEMS.register("master_sword",
            () -> new MasterSwordItem(ModTiers.Master,
                    new Item.Properties()
                            .attributes(SwordItem.createAttributes(ModTiers.Master, 7, -2.2F))
                            .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false)
                            .rarity(Rarity.EPIC)));

//    public static final DeferredItem<Item> BROKEN_MASTER_SWORD = ITEMS.register("broken_master_sword",
//            () -> new MasterSwordItem(ModTiers.Master,
//                    new Item.Properties()
//                            .attributes(SwordItem.createAttributes(ModTiers.Master, 0, -2.2F))
//                            .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false)
//                            .rarity(Rarity.EPIC)));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }

}
