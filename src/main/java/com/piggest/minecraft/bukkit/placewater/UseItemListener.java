package com.piggest.minecraft.bukkit.placewater;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class UseItemListener implements Listener {
	private Placewater pw = null;
	public UseItemListener(Placewater pw){
		this.pw = pw;
	}
	@EventHandler
	public void onUsePickaxe(PlayerInteractEvent event) {
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			if(event.getMaterial() == Material.GOLDEN_PICKAXE){
				Player player = event.getPlayer();
				BlockFace face = event.getBlockFace();
				Location loc = event.getClickedBlock().getLocation();
				loc.setX(loc.getX()+face.getModX());
				loc.setY(loc.getY()+face.getModY());
				loc.setZ(loc.getZ()+face.getModZ());
				player.sendMessage("你使用了金镐放水");
				pw.place(player, loc);
			}
		}
	}
}
