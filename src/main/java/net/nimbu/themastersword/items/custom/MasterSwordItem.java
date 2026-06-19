package net.nimbu.themastersword.items.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.nimbu.themastersword.component.ModDataComponents;
import net.nimbu.themastersword.items.ModTiers;
import net.nimbu.themastersword.particle.ModParticleTypes;
import net.nimbu.themastersword.sound.ModSoundEvents;

public class MasterSwordItem extends SwordItem {
    public MasterSwordItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    private static final float rechargeRate = 240;

    @Override
    public int getBarColor(ItemStack stack) {
        Boolean isRecharging = stack.getOrDefault(ModDataComponents.RECHARGING, false);
        float stackMaxDamage = (float) this.getMaxDamage(stack);

        float f = Math.max(0.0F, (stackMaxDamage - (float) stack.getDamageValue()) / stackMaxDamage);
        float hueStart = 0.9F; // purple
        float hueEnd = 0.5F; // green

        float sat = 1.0f;
        if(isRecharging){
            assert Minecraft.getInstance().level != null;
            long gameTime = Minecraft.getInstance().level.getGameTime();

            sat = 0.5F + 0.5F * Mth.sin(gameTime * 0.5F);
        }

        float hue = Mth.lerp(f, hueStart, hueEnd);
        return Mth.hsvToRgb(hue, sat, 1.0F);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (level.isClientSide()) return;

        Boolean isRecharging = stack.getOrDefault(ModDataComponents.RECHARGING, false);
        Boolean activeFrame = entity.tickCount % 20 == 0; //activates every 20 frames

        if (isRecharging && entity.tickCount % rechargeRate == 0){
            //recharge
            stack.setDamageValue(stack.getDamageValue()-1);
            if (stack.getDamageValue()==0) { //recharges fully every 20 minutes
                repairItem(stack, entity);
            }
        }

        if (activeFrame){ //only power up/down if charged, only check every second

            boolean hostileNearby = !level.getEntitiesOfClass(
                    Monster.class,
                    entity.getBoundingBox().inflate(18).move(new Vec3(0,6,0)),
                    LivingEntity::isAlive
            ).isEmpty();
            boolean isPowered = stack.getOrDefault(ModDataComponents.POWERED, false);
            if (hostileNearby && !isPowered && !isRecharging){
                powerUp(stack, entity, isSelected);}
            else if (!hostileNearby && isPowered){
                powerDown(stack, entity);}
        }
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        if(stack.getOrDefault(ModDataComponents.POWERED, false)){// && !stack.getOrDefault(ModDataComponents.RECHARGING, false)){


            if (attacker instanceof Player player){ //prevent audio spamming
                float attackStrength = player.getAttackStrengthScale(0.0F);
                if (attackStrength<0.5) {return super.hurtEnemy(stack, target, attacker);}
            }

            ServerLevel level = (ServerLevel) attacker.level();
            if (!level.isClientSide()) {
//                level.playSound(
//                        null,
//                        attacker.getX(), attacker.getY(), attacker.getZ(),
//                        ModSoundEvents.MASTER_SWORD_HIT.get(),
//                        SoundSource.PLAYERS,
//                        1.0F,
//                        1.0f
//                );

                level.sendParticles(
                        ModParticleTypes.MASTER_SWORD_HIT_PARTICLE.get(),
                        target.getX(),
                        target.getY() + target.getBbHeight() * 0.5,
                        target.getZ(),
                        1,     // count
                        0.0,    // x spread
                        0.0,    // y spread
                        0.0,    // z spread
                        0.0    // speed
                );
            }


        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!isMinimumDurability(stack)) { //dont allow sword to permanently break
            super.postHurtEnemy(stack, target, attacker);

            if (isMinimumDurability(stack)){ //if sword has been damaged enough to no longer take damage, it must break
                this.breakItem(stack, attacker);
            }
        }
    }

    private void powerUp(ItemStack stack, Entity holder, boolean isSelected){
        modifyAttackDamage(stack, 11);

        //Set to power up
        stack.set(ModDataComponents.POWERED, true);

        //Audio
        if(isSelected) { //only play audio if held in hand
            Level level = holder.level();

            level.playSound(
                    null,
                    holder.getX(),
                    holder.getY(),
                    holder.getZ(),
                    ModSoundEvents.MASTER_SWORD_TRANSFORM.get(),
                    SoundSource.PLAYERS,
                    1.0F,
                    1.0F
            );
        }
    }
    private void powerDown(ItemStack stack, Entity holder){
        modifyAttackDamage(stack, 7);

        //Set to normal
        stack.set(ModDataComponents.POWERED, false);
    }

    private void breakItem(ItemStack stack, LivingEntity holder){

        modifyAttackDamage(stack, 0);

        //Set to recharge
        stack.set(ModDataComponents.RECHARGING, true);

        //Audio
        Level level = holder.level();

        level.playSound(
                null,
                holder.getX(),
                holder.getY(),
                holder.getZ(),
                ModSoundEvents.MASTER_SWORD_BREAK.get(),
                SoundSource.PLAYERS,
                1.0F,
                1.0F
        );
    }
    private void repairItem(ItemStack stack, Entity holder){

        modifyAttackDamage(stack, 7);

        //Stop recharging
        stack.set(ModDataComponents.RECHARGING, false);

        //Durability fix
        stack.setDamageValue(0);

        //Audio
        Level level = holder.level();

        level.playSound(
                null,
                holder.getX(),
                holder.getY(),
                holder.getZ(),
                SoundEvents.ITEM_BREAK,
                SoundSource.PLAYERS,
                1.0F,
                1.0F
        );
    }

    private static void modifyAttackDamage(ItemStack stack, int newDamage){
        ItemAttributeModifiers old = stack.get(DataComponents.ATTRIBUTE_MODIFIERS);

        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();

        for (ItemAttributeModifiers.Entry e : old.modifiers()) {
            if (e.attribute() != Attributes.ATTACK_DAMAGE) {
                builder.add(e.attribute(), e.modifier(), e.slot());
            }
            else{
                builder.add(
                        Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(
                                ResourceLocation.withDefaultNamespace("base_attack_damage"),
                                newDamage + ModTiers.Master.getAttackDamageBonus(),
                                AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.MAINHAND
                );
            }
        }
        stack.set(DataComponents.ATTRIBUTE_MODIFIERS, builder.build());
    }

    public static boolean isMinimumDurability(ItemStack swordStack){
        return swordStack.getDamageValue()>=swordStack.getMaxDamage()-1;
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        return super.getDefaultAttributeModifiers(stack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged; //stops animation occurring when powering on and repairing
    }


}
