package it.multicoredev.minesecurity;

import it.mineblock.mbcore.spigot.Chat;
import org.bukkit.Bukkit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

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

public class Logger {
    private String filename;
    private File log;

    public Logger() {
        init(new Date());
    }

    private void init(Date date) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            filename = Main.DATE.format(date) + ".log";
            log = new File(Main.plugin.getDataFolder(), filename);

            if(!log.exists()) {
                try {
                    if(!log.createNewFile()) {
                        Chat.getLogger("IOException, cannot create log", "severe");
                        return;
                    }
                } catch (IOException e) {
                    Chat.getLogger("IOException, cannot create log", "severe");
                    e.printStackTrace();
                }
            }
        });
    }

    void add(String msg, String mode, String server, String username, boolean logToConsole) {
        Date date = new Date();
        init(date);

        if(logToConsole) {
            Chat.getLogger(msg, mode);
        }

        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            String time = Main.TIME.format(date);
            String message = Chat.getDecolored(msg);

            if((server == null || server.isEmpty()) && (username == null || username.isEmpty())) {
                message = "[" + time + "] - " + message;
            }
            else if(server == null || server.isEmpty()) {
                message = "[" + time + "] - " + username + " -> " + message;
            } else if(username == null || username.isEmpty()) {
                message = "[" + time + "] - " + server + " -> " + message;
            } else {
                message = "[" + time + "] - " + server + " : " + username + " -> " + message;
            }

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(log, true));
                writer.append(message).append("\n");
                writer.close();
            } catch (IOException e) {
                Chat.getLogger("IOException, cannot write log", "severe");
                e.printStackTrace();
            }
        });
    }

    void add(String msg, String mode) {
        add(msg, mode, null, null, true);
    }
}