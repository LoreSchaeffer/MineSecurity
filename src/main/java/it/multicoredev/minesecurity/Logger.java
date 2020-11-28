package it.multicoredev.minesecurity;

import it.multicoredev.mbcore.spigot.Chat;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

public class Logger {
    private static final SimpleDateFormat DATE = new SimpleDateFormat("dd-MM-yyyy");
    private static final SimpleDateFormat TIME = new SimpleDateFormat("HH:mm:ss");
    private final Plugin plugin;
    private String filename;
    private File dir;
    private File log;

    public Logger(Plugin plugin) {
        this.plugin = plugin;
        dir = new File(plugin.getDataFolder(), "logs");
    }

    private void init(Date date) {
        if (!dir.exists() || !dir.isDirectory()) {
            if (!dir.mkdir()) Chat.info("&4Cannot create log directory.");
        }

        filename = DATE.format(date) + ".log";
        log = new File(dir, filename);

        if (!log.exists()) {
            try {
                if (!log.createNewFile()) {
                    Chat.severe("&4Cannot create log file.");
                }
            } catch (IOException e) {
                Chat.severe("&4" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    void log(String msg) {
        Date date = new Date();
        init(date);

        Chat.info(msg);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String time = TIME.format(date);
            String message = Chat.getDiscolored(msg);

            message = "[" + time + "] - " + message;

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(log, true));
                writer.append(message).append("\n");
                writer.flush();
                writer.close();
            } catch (IOException e) {
                Chat.severe("&4" + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}