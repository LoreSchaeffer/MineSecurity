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

/**
 * Copyright © 2018 by Lorenzo Magni
 * This file is part of MineSecurity.
 * MineSecurity is under "The 3-Clause BSD License", you can find a copy <a href="https://opensource.org/licenses/BSD-3-Clause">here</a>.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

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