package net.kissenpvp.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.kissenpvp.core.api.config.ConfigurationImplementation;
import net.kissenpvp.discord.commands.ConnectRoleCommand;
import net.kissenpvp.discord.commands.VerifyCommand;
import net.kissenpvp.discord.configs.Token;
import net.kissenpvp.proxy.api.base.ProxyKissen;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class KissenDiscordImplementation implements DiscordImplementation {

    private JDA jda;

    @Override
    public boolean start() {
        ConfigurationImplementation configurationImplementation = ProxyKissen.getInstance().getImplementation(ConfigurationImplementation.class);
        String token = configurationImplementation.getSetting(Token.class);
        if (token.equals(configurationImplementation.getOption(Token.class).getDefault())) {
            ProxyKissen.getInstance().log("The token has not been set.", "discordbot");
            return true;
        }
        JDABuilder builder = JDABuilder
                .createDefault(token)
                .enableIntents(Arrays.asList(GatewayIntent.values()))
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableCache(Arrays.asList(CacheFlag.values()))
                .setActivity(Activity.of(Activity.ActivityType.PLAYING, "Rainbow Six Siege"));

        try {
            jda = builder.build().awaitReady();
            jda.addEventListener(new ConnectRoleCommand());
            jda.addEventListener(new VerifyCommand());

            jda.updateCommands().addCommands(Commands
                            .slash("verify",
                                    "Connect your Discord Account with your Minecraft Account")
                            .addOption(OptionType.STRING, "username", "Your Minecraft Username"),
                    Commands.slash("connectrole", "Connect a Minecraft Rank with Discord Roles")
                            .addOption(OptionType.STRING, "mcrank", "The Name of the MC Rank")
                            .addOption(OptionType.STRING, "discord_roles", "List of Discord Roles to Connect split by ;")).queue();
        } catch (InterruptedException interruptedException) {
            throw new RuntimeException(interruptedException);
        }

        return DiscordImplementation.super.start();
    }

    @NotNull
    @Override
    public JDA getJda() {
        return jda;
    }
}
