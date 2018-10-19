package it.multicoredev.minesecurity;

import it.mineblock.mbcore.spigot.Chat;
import org.bukkit.Bukkit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

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