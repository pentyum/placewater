package com.piggest.minecraft.bukkit.placewater;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class UseItemListener implements Listener {
	private Placewater pw = null;

	public UseItemListener(Placewater pw) {
		this.pw = pw;
	}

	@EventHandler
	public void onUsePickaxe(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.getMaterial() == Material.GOLDEN_PICKAXE) {
				Player player = event.getPlayer();
				player.sendMessage("你使用了金镐");
				BlockFace face = event.getBlockFace();
				Location loc = event.getClickedBlock().getLocation();
				loc.setX(loc.getX() + face.getModX());
				loc.setY(loc.getY() + face.getModY());
				loc.setZ(loc.getZ() + face.getModZ());
				BlockPlaceEvent place_water_event = new BlockPlaceEvent(loc.getBlock(), loc.getBlock().getState(), event.getClickedBlock(), event.getItem(), player, true, event.getHand());
				Bukkit.getServer().getPluginManager().callEvent(place_water_event);
				if (place_water_event.isCancelled() == false) {
					pw.place(player, loc);
				}
			}
		}
	}
}
