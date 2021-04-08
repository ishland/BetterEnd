package ru.betterend.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import ru.betterend.util.MHelper;

@Environment(EnvType.CLIENT)
public class ParticleSnowflake extends SpriteBillboardParticle {
	private int ticks;
	private double preVX;
	private double preVY;
	private double preVZ;
	private double nextVX;
	private double nextVY;
	private double nextVZ;

	protected ParticleSnowflake(ClientLevel world, double x, double y, double z, double r, double g, double b,
			SpriteSet sprites) {
		super(world, x, y, z, r, g, b);
		setSprite(sprites);

		this.maxAge = MHelper.randRange(150, 300, random);
		this.scale = MHelper.randRange(0.05F, 0.2F, random);
		this.setColorAlpha(0F);

		preVX = random.nextGaussian() * 0.015;
		preVY = random.nextGaussian() * 0.015;
		preVZ = random.nextGaussian() * 0.015;

		nextVX = random.nextGaussian() * 0.015;
		nextVY = random.nextGaussian() * 0.015;
		nextVZ = random.nextGaussian() * 0.015;
	}

	@Override
	public void tick() {
		ticks++;
		if (ticks > 200) {
			preVX = nextVX;
			preVY = nextVY;
			preVZ = nextVZ;
			nextVX = random.nextGaussian() * 0.015;
			nextVY = random.nextGaussian() * 0.015;
			nextVZ = random.nextGaussian() * 0.015;
			if (random.nextInt(4) == 0) {
				nextVY = Math.abs(nextVY);
			}
			ticks = 0;
		}
		double delta = (double) ticks / 200.0;

		if (this.age <= 40) {
			this.setColorAlpha(this.age / 40F);
		} else if (this.age >= this.maxAge - 40) {
			this.setColorAlpha((this.maxAge - this.age) / 40F);
		}

		if (this.age >= this.maxAge) {
			this.markDead();
		}

		this.velocityX = Mth.lerp(delta, preVX, nextVX);
		this.velocityY = Mth.lerp(delta, preVY, nextVY);
		this.velocityZ = Mth.lerp(delta, preVZ, nextVZ);

		super.tick();
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Environment(EnvType.CLIENT)
	public static class FactorySnowflake implements ParticleFactory<SimpleParticleType> {

		private final SpriteSet sprites;

		public FactorySnowflake(SpriteSet sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z,
				double vX, double vY, double vZ) {
			return new ParticleSnowflake(world, x, y, z, 1, 1, 1, sprites);
		}
	}
}