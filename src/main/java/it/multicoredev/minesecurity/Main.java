package it.multicoredev.minesecurity;

import it.mineblock.mbcore.spigot.Chat;
import it.mineblock.mbcore.spigot.ConfigManager;
import it.mineblock.mbcore.spigot.config.Configuration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

public class Main extends JavaPlugin {
    static final SimpleDateFormat DATE = new SimpleDateFormat("dd-MM-yyyy");
    static final SimpleDateFormat TIME = new SimpleDateFormat("HH:mm:ss");
    static String address;
    static int port;
    static List<String> kickMsgs;
    static Plugin plugin;
    static Logger logger;

    public void onEnable() {
        plugin = this;
        ConfigManager configManager = new ConfigManager();
        Configuration config = configManager.autoloadConfig(this, "MineSecurity", getResource("config.yml"), new File(getDataFolder(), "config.yml"), "config.yml");

        address = config.getString("proxy-address");
        port = config.getInt("proxy-port");
        kickMsgs = config.getStringList("kick-messages");

        logger = new Logger();

        getServer().getPluginManager().registerEvents(new OnPlayerJoin(), this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equals("msreload") && sender.hasPermission("minesecurity.reload")) {
            onEnable();
            Chat.send("&2Minesecurity reloaded!", sender, true);
        }
        return true;
    }
}