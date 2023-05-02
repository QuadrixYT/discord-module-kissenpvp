package net.kissenpvp.discord.configs;


import net.kissenpvp.core.api.config.OptionDefault;
import org.jetbrains.annotations.NotNull;

public class Token extends OptionDefault<String> {

    @Override
    public @NotNull String getGroup() {
        return "discord";
    }

    @Override
    public @NotNull String getDescription() {
        return "This is the Token that was needed to start a bot application";
    }

    @Override
    public @NotNull String getDefault() {
        return "token";
    }
}
