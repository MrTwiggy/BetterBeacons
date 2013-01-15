package com.pheenixm.betterbeacons;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterBeaconsPlugin extends JavaPlugin 
{
	BetterBeaconsManager manager; //The BetterBeacons manager
	public static Logger log;
	public static HashMap<Integer,BetterBeaconsProperties> Better_Beacons_Properties; //Map of properties for all tiers

    public static final String PLUGIN_NAME = "BetterBeacons";
    public static final String VERSION = "0.1";
    public static final String PLUGIN_PREFIX = PLUGIN_NAME + " " + VERSION + ": "; 
    public static final String BETTER_BEACONS_SAVES_DIRECTORY = "OreGinSaves";
    public static final int TICKS_PER_SECOND = 20; 

	public static int UPDATE_CYCLE; //Update time in ticks
	public static int SAVE_CYCLE; //The time between periodic saves in minutes
	public static boolean CITADEL_ENABLED; //Whether the plugin 'Citadel' is enabled on this server

	public static final String CITADEL_NAME = "Citadel"; //The plugin name for 'Citadel'

    
    public void onEnable() 
    {
    	initializeBetterBeaconsProperties();
    	
		if(properPluginsLoaded())
		{
			log = this.getLogger();
			log.info(PLUGIN_NAME+" v"+VERSION+" enabled!");
			getConfig().options().copyDefaults(true);
			manager = new BetterBeaconsManager(this);
		}
		else
		{
		BetterBeaconsPlugin.sendConsoleMessage("The Citadel config value is not correct for loaded plugins! Disabling OreGin now!");
		getServer().getPluginManager().disablePlugin(this);
		}

    }
    
	public void onDisable() 
	{
		log.info(PLUGIN_NAME+" v"+VERSION+" disabled!");
	}
	

	/**
	 * Initializes the default BetterBeaconsProperties from config
	 */
	@SuppressWarnings("unchecked")
	public void initializeBetterBeaconsProperties()
	{
		Better_Beacons_Properties = new HashMap<Integer,BetterBeaconsProperties>();

		//Load general config values
		BetterBeaconsPlugin.UPDATE_CYCLE = getConfig().getInt("general.update_cycle");
		BetterBeaconsPlugin.CITADEL_ENABLED = getConfig().getBoolean("general.citadel_enabled");
		BetterBeaconsPlugin.SAVE_CYCLE = getConfig().getInt("general.save_cycle");

		Material fuel_type = Material.valueOf("fuel_type");
		int fuel_amount= getConfig().getInt("fuel_amount");
		
		Better_Beacons_Properties.put(0, new BetterBeaconsProperties(fuel_amount, fuel_type));
		
		BetterBeaconsPlugin.sendConsoleMessage("Config values successfully loaded!");
		saveConfig();
	}
	
	/**
	 * Returns the BetterBeacons Saves file
	 */
	public File getBetterBeaconsSavesFile()
	{
		return new File(getDataFolder(), BETTER_BEACONS_SAVES_DIRECTORY + ".txt");
	}

	/**
	 * Returns whether the proper plugins are loaded based on config values
	 */
	public boolean properPluginsLoaded()
	{
		return ( (getServer().getPluginManager().getPlugin(CITADEL_NAME) != null && BetterBeaconsPlugin.CITADEL_ENABLED)
				|| (getServer().getPluginManager().getPlugin(CITADEL_NAME) == null && !BetterBeaconsPlugin.CITADEL_ENABLED));
	}


	/**
	 * Sends a message to the console with appropriate prefix
	 */
	public static void sendConsoleMessage(String message)
	{
		Bukkit.getLogger().info(BetterBeaconsPlugin.PLUGIN_PREFIX + message);
	}

}
