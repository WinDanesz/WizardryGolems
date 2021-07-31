package com.windanesz.wizardrygolems.entity.living;

import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityFireGolemMinion extends EntityGolemBaseMinion implements IFireGolem {
	public EntityFireGolemMinion(World world) {
		super(world);
		setImmuneToFire(true);
	}

	@Override
	public void onDeath(DamageSource cause) {
		onDeathDelegateFireGolem(this);
		super.onDeath(cause);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		fireGolemUpdate(this);
	}
}
