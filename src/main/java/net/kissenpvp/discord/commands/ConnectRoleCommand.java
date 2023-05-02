package net.kissenpvp.discord.commands;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kissenpvp.core.api.database.savable.Savable;
import net.kissenpvp.proxy.api.base.ProxyKissen;
import net.kissenpvp.proxy.api.permission.ProxyPermissionImplementation;
import net.kissenpvp.proxy.api.user.rank.ProxyRank;
import net.kissenpvp.proxy.api.user.rank.ProxyRankImplementation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConnectRoleCommand extends ListenerAdapter {

    private final ProxyPermissionImplementation proxyPermissionImplementation = ProxyKissen.getInstance()
            .getImplementation(ProxyPermissionImplementation.class);
    private final ProxyRankImplementation rankImplementation = ProxyKissen.getInstance()
            .getImplementation(ProxyRankImplementation.class);


    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equalsIgnoreCase("connectrole")) {
            if (event.getOption("mcrank") == null) {
                event.reply("Du musst einen Rank angeben.").setEphemeral(true).queue();
                return;
            }
            String mcrank = event.getOption("mcrank").getAsString();
            if (!rankImplementation.getRank(mcrank).isPresent()) {
                event.reply("Dieser Rank existiert in Minecraft nicht.").setEphemeral(true).queue();
                return;
            }
            if (event.getOption("discord_roles") == null) {
                event.reply("Bitte gebe mindestens eine Discord Rolle an die verbunden werden soll")
                        .setEphemeral(true).queue();
                return;
            }
            String roles = event.getOption("discord_roles").getAsString();
            ProxyRank rank = rankImplementation.getRank(mcrank).get();
            Savable rankSavable = (Savable) rank;
            roles = roles.replace(" ", "");
            if(event.getGuild() == null) return;
            if (roles.contains(";")) {
                String[] roleStrings = roles.split(";");
                List<String> roleIDs = new ArrayList<>();
                Collections.addAll(roleIDs, roleStrings);
                rankSavable.getListNotNull("discord_roles").addAll(roleIDs);
                List<String> roleNames = new ArrayList<>();
                for (String roleID : roleIDs) {
                    Role role = event.getGuild().getRoleById(roleID);
                    if(role == null) {
                        event.reply("Rolle mit der ID konnte auf dem Discord Server nicht gefunden werden").queue();
                        return;
                    }
                    roleNames.add(role.getName());
                }
                event.reply("Du hast die Rollen " + roleNames + " zu " + rank.getName() + " hinzugefügt")
                        .setEphemeral(true).queue();
            } else {
                Role role = event.getGuild().getRoleById(roles);
                if(role == null) {
                    event.reply("Rolle mit der ID konnte auf dem Discord Server nicht gefunden werden").queue();
                    return;
                }
                rankSavable.getListNotNull("discord_roles").add(roles);
                event.reply("Du hast die Rolle " + role.getName() + " zu " + rank.getName() + " hinzugefügt")
                        .setEphemeral(true).queue();
            }
        }
    }
}
