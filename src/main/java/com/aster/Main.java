package com.aster;

import com.aster.command.PingCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class Main {

    public static void main(String[] args) {

        String token = System.getenv("DISCORD_BOT_TOKEN");

        if (token == null || token.isBlank()) {
            System.out.println("Discord Bot Token is missing!");
            return;
        }

        try {
            JDA jda = JDABuilder.createDefault(token)
                    .addEventListeners(new PingCommand())
                    .build();

            jda.awaitReady();

            jda.updateCommands()
                    .addCommands(Commands.slash("ping", "Check bot status"))
                    .queue();

            System.out.println("Bot connected: "
                    + jda.getSelfUser().getName());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Bot startup interrupted");
        } catch (Exception e) {
            System.err.println("Bot startup failed: " + e.getMessage());
        }
    }
}
