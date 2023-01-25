package com.windanesz.wizardrygolems.entity.living;

import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract class EntityEarthGolemMinion extends EntityGolemBaseMinion implements IEarthGolem {
	public EntityEarthGolemMinion(World world) {
		super(world);
	}

	@Override
	public void onDeath(DamageSource cause) {
		onGolemUpdate(this);
		super.onDeath(cause);
	}
}
