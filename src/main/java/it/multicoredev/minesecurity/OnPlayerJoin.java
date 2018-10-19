package it.multicoredev.minesecurity;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class OnPlayerJoin implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerLoginEvent event) {
        String ip = event.getRealAddress().getHostAddress();
        String port = event.getHostname();

        if(!Main.address.equalsIgnoreCase(ip) || !port.endsWith(":" + Main.port)) {
            Main.logger.add(event.getPlayer().getName() + " tried to login from an external bungeecord. (" + ip + ":" + port.split(":")[1] + ")", "warning");
            StringBuilder builder = new StringBuilder();
            for(String line : Main.kickMsgs) {
                builder.append(line).append("\n&r");
            }
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(builder.toString());
            }
    }
}
