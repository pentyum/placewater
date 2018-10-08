package com.piggest.minecraft.bukkit.placewater;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class Placewater_listener implements Listener {
	private Placewater pw = null;

	public Placewater_listener(Placewater pw) {
		this.pw = pw;
	}

	@EventHandler
	public void on_placewater(BlockPlaceEvent event) {
		if (event.getBlockPlaced().getType() == Material.WATER
				&& event.getItemInHand().getType() == Material.GOLDEN_PICKAXE) {
			if (event.getBlockReplacedState().getType() == Material.END_PORTAL_FRAME) {
				event.setCancelled(true);
			}
		}
	}
}
