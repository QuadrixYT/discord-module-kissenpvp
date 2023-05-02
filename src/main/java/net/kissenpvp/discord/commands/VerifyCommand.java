package net.kissenpvp.discord.commands;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kissenpvp.core.api.database.savable.Savable;
import net.kissenpvp.core.api.networking.client.entitiy.PlayerNotFoundException;
import net.kissenpvp.proxy.api.base.ProxyKissen;
import net.kissenpvp.proxy.api.networking.client.entity.ProxyPlayerClient;
import net.kissenpvp.proxy.api.user.rank.ProxyRank;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VerifyCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getGuild() == null) return;
        if (event.getMember() == null) return;
        if (event.getInteraction().getName().equalsIgnoreCase("verify")) {
            if (event.getOption("username") == null) {
                event.reply("Du musst einen Username angeben.").setEphemeral(true).queue();
                return;
            }
            String username = event.getOption("username").getAsString();
            try {
                ProxyPlayerClient proxyPlayerClient = ProxyKissen.getInstance().getPlayer(username);
                if (!proxyPlayerClient.isConnected()) {
                    event.reply("Du musst in Minecraft online sein.").setEphemeral(true).queue();
                    return;
                }
                Optional<ProxyRank> proxyRank = proxyPlayerClient.getRank().getSource();
                List<String> roleNames = new ArrayList<>();
                if (proxyRank.isPresent()) {
                    if (proxyRank.get() instanceof Savable proxyRankSavable) {
                        for (String s : proxyRankSavable.getListNotNull("discord_roles")) {
                            s = s.replace(" ", "");
                            Role role = event.getGuild().getRoleById(s);
                            if (role == null) continue;
                            roleNames.add(role.getName());
                            event.getGuild().addRoleToMember(event.getMember().getUser(), role).queue();
                        }
                        if (roleNames.size() > 0) {
                            event.reply("Du hast erfolgreich die Rollen " + roleNames + " erhalten.")
                                    .setEphemeral(true).queue();
                        } else {
                            event.reply("Mit deinem Rank sind noch keine Rollen auf Discord verknüpft.")
                                    .setEphemeral(true).queue();
                        }
                    } else {
                        event.reply("Du bist bisher noch nicht registriert auf dem Minecraft Server.")
                                .setEphemeral(true).queue();
                    }
                }
            } catch (PlayerNotFoundException exception) {
                event.reply("Es wurde kein Spieler mit diesem Namen gefunden.")
                        .setEphemeral(true).queue();
            }
        }
    }
}
