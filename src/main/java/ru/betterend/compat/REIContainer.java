package ru.betterend.compat;

import me.shedaniel.rei.plugin.containers.CraftingContainerInfoWrapper;
import me.shedaniel.rei.server.ContainerInfoHandler;
import ru.betterend.client.gui.EndStoneSmelterScreenHandler;
import ru.betterend.recipe.AlloyingRecipe;

public class REIContainer implements Runnable {

	@Override
	public void run() {
		ContainerInfoHandler.registerContainerInfo(AlloyingRecipe.ID, CraftingContainerInfoWrapper.create(EndStoneSmelterScreenHandler.class));
	}

}
