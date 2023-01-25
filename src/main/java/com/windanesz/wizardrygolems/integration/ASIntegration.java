package com.windanesz.wizardrygolems.integration;

import net.minecraftforge.fml.common.Loader;

public final class ASIntegration {

	public static final String AS_MOD_ID = "ancientspellcraft";

	private static boolean loaded;

	public static void init() {
		loaded = Loader.isModLoaded(AS_MOD_ID);
	}

	public static void spawnMushroom() {

	}
}
