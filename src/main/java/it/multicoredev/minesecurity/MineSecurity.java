package it.multicoredev.minesecurity;

import it.multicoredev.mclib.yaml.Configuration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

/**
 * Copyright © 2018-2020 by Lorenzo Magni
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

public class MineSecurity extends JavaPlugin implements Listener {
    private String address;
    private int port;
    private String kickMsg;
    private Logger logger;

    public void onEnable() {
        Configuration config = new Configuration(new File(getDataFolder(), "config.yml"), getResource("config.yml"));

        try {
            if (!getDataFolder().exists() || !getDataFolder().isDirectory()) {
                if (!getDataFolder().mkdir()) throw new IOException();
            }

            config.autoload();
        } catch (IOException e) {
            e.printStackTrace();
            onDisable();
            return;
        }

        getServer().getPluginManager().registerEvents(this, this);

        address = config.getString("proxy-address");
        port = config.getInt("proxy-port");
        kickMsg = config.getString("kick-msg");

        logger = new Logger(this);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerLoginEvent event) {
        String ip = event.getRealAddress().getHostAddress();
        String port = event.getHostname();

        if (!address.equalsIgnoreCase(ip) || !port.endsWith(":" + port)) {
            logger.log(event.getPlayer().getName() + " tried to login from an external bungeecord. (" + ip + ":" + port.split(":")[1] + ")");

            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(kickMsg);
        }
    }
}