package com.piggest.minecraft.bukkit.placewater;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class UseItem_listener implements Listener {
	private Placewater pw = null;

	public UseItem_listener(Placewater pw) {
		this.pw = pw;
	}

	@EventHandler
	public void onUsePickaxe(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.getMaterial() == Material.GOLDEN_PICKAXE) {
				Player player = event.getPlayer();
				player.sendMessage("你使用了金镐");
				if (pw.place_eco(player) == true) {
					BlockFace face = event.getBlockFace();
					Block block = event.getClickedBlock();
					Location loc = block.getLocation();
					loc.add(face.getModX(), face.getModY(), face.getModZ());
					block = loc.getBlock();
					BlockState old_state = block.getState();
					block.setType(Material.WATER);
					BlockPlaceEvent place_water_event = new BlockPlaceEvent(loc.getBlock(), old_state,
							event.getClickedBlock(), event.getItem(), player, true, event.getHand());
					Bukkit.getServer().getPluginManager().callEvent(place_water_event);
				}
			}
		}
	}
}
