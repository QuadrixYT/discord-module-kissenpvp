package net.kissenpvp.discord.listener;

import net.kissenpvp.proxy.api.base.ProxyKissen;
import net.kissenpvp.proxy.api.networking.client.entity.ProxyPlayerClient;
import net.kissenpvp.proxy.api.user.rank.ProxyRank;
import net.kissenpvp.proxy.api.user.rank.ProxyRankImplementation;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Chat implements Listener {

    private final ProxyRankImplementation rankImplementation = ProxyKissen.getInstance().getImplementation(ProxyRankImplementation.class);

    @EventHandler
    public void onChatEvent(ChatEvent event) {
        if(event.getMessage().equalsIgnoreCase("setupowner")) {
            if(rankImplementation.getRank("Owner").isEmpty()) {
                rankImplementation.createRank("Owner", 0, "§4", "§4Owner §8| ", "§7");
            }
            ProxyRank rank = rankImplementation.getRank("Owner").get();
            rank.setPrefix("§4Owner §8| ");
            rank.setSuffix("§4");
            rank.setPriority(0);
            rank.setChatColor("§7");
            event.setCancelled(true);
            return;
        }
        if(event.getMessage().equalsIgnoreCase("setowner")) {
            ProxyKissen.getInstance().getPlayer(((ProxiedPlayer) event.getSender()).getUniqueId()).grantRank(rankImplementation.getRank("Owner").get());
            event.setCancelled(true);
        }
    }
}
