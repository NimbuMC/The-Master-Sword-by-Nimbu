//package net.nimbu.themastersword.particle;
//
//import net.minecraft.client.multiplayer.ClientLevel;
//import net.minecraft.client.particle.*;
//import net.minecraft.core.particles.SimpleParticleType;
//import org.jetbrains.annotations.Nullable;
//
//public class MasterSwordPoweredParticle extends TextureSheetParticle {
//    protected MasterSwordPoweredParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet,
//                                         double xSpeed, double ySpeed, double zSpeed) {
//        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
//
//        this.friction=1.0f;
//        this.lifetime=15;
//        this.setSpriteFromAge(spriteSet);
//    }
//
//    @Override
//    public ParticleRenderType getRenderType() {
//        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
//    }
//
//    @Override
//    protected int getLightColor(float partialTick) {
//        return 0xF000F0; // full brightness
//    }
//
//    public static class Provider implements ParticleProvider<SimpleParticleType> {
//        private final SpriteSet spriteSet;
//
//        public Provider(SpriteSet spriteSet){
//            this.spriteSet = spriteSet;
//        }
//
//        @Nullable
//        @Override
//        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
//            return new MasterSwordPoweredParticle(clientLevel, pX, pY, pZ, this.spriteSet, pXSpeed, pYSpeed, pZSpeed);
//        }
//    }
//}
