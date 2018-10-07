package com.piggest.minecraft.bukkit.placewater;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.economy.Economy;

public class Placewater extends JavaPlugin {
	private boolean use_vault = true;
	private Economy economy = null;
	private ConfigurationSection price = null;
	private FileConfiguration config = null;
	private final UseItemListener item_listener = new UseItemListener(this);

	private boolean initVault() {
		boolean hasNull = false;
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager()
				.getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			if ((economy = economyProvider.getProvider()) == null) {
				hasNull = true;
			}
		}
		return !hasNull;
	}

	@Override
	public void onEnable() {
		saveDefaultConfig();
		config = getConfig();
		use_vault = config.getBoolean("use-vault");
		price = config.getConfigurationSection("price");
		if (use_vault == true) {
			getLogger().info("使用Vault");
			if (!initVault()) {
				getLogger().severe("初始化Vault失败,请检测是否已经安装Vault插件和经济插件");
				return;
			}
		} else {
			getLogger().info("不使用Vault");
		}

		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(item_listener, this);

	}

	private void fill_water(Location loc) {
		loc.getBlock().breakNaturally();
		loc.getBlock().setType(Material.WATER);
	}

	public void place(Player player, Location loc) {
		String world_name = player.getWorld().getName();
		int world_price = 0;
		if (price.getKeys(false).contains(world_name)) {
			world_price = price.getInt(world_name);
		} else {
			world_price = price.getInt("other");
		}
		if (use_vault == true) {
			if (economy.has(player, world_price)) {
				economy.withdrawPlayer(player, world_price);
				player.sendMessage("已扣除" + world_price);
				fill_water(loc);
			} else {
				player.sendMessage("你的金钱不够");
			}
		} else {
			fill_water(loc);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("placewater")) {
			if (args.length == 0) { // 放水
				if (!(sender instanceof Player)) { // 如果sender与Player类不匹配
					sender.sendMessage("这个指令只能让玩家使用。");
				} else {
					Player player = (Player) sender;
					Location loc = player.getLocation();
					BlockPlaceEvent place_water_event = new BlockPlaceEvent(loc.getBlock(), loc.getBlock().getState(), null, null, player, true, null);
					Bukkit.getServer().getPluginManager().callEvent(place_water_event);
					if (place_water_event.isCancelled() == false) {
						place(player, loc);
					}
				}
				return true;
			} else if (args[0].equalsIgnoreCase("setprice")) { // 设置价格
				int newprice = 0;
				String world_name = "world";
				if (args.length == 3) {
					world_name = args[1];
				} else if (args.length == 2) {
					if (!(sender instanceof Player)) { // 如果sender与Player类不匹配
						sender.sendMessage("在控制台使用该命令必须指定世界名称");
						return true;
					} else {
						Player player = (Player) sender;
						world_name = player.getWorld().getName();
					}
				}
				if (sender.hasPermission("placewater.changeprice")) {
					try {
						newprice = Integer.parseInt(args[args.length - 1]);
					} catch (Exception e) {
						sender.sendMessage("请输入整数");
						return true;
					}
					price.set(world_name, newprice);
					config.set("price", price);
					saveConfig();
					sender.sendMessage("价格设置成功");
				} else {
					sender.sendMessage("你没有权限设置价格");
				}
				return true;
			}
		}
		return false;
	}

	/*
	 * @Override public List<String> onTabComplete(CommandSender sender, Command
	 * command, String alias, String[] args) {
	 * 
	 * }
	 */

}
