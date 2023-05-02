package net.kissenpvp.discord;

import net.dv8tion.jda.api.JDA;
import net.kissenpvp.core.api.base.Implementation;

public interface DiscordImplementation extends Implementation {

    JDA getJda();

}
