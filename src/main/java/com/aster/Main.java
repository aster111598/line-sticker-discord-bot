package com.aster;

import com.aster.command.PingCommand;
import com.aster.command.StickerCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
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
                    .addEventListeners(
                            new PingCommand(),
                            new StickerCommand()
                    )
                    .build();

            jda.awaitReady();

            jda.updateCommands()
                    .addCommands(
                            Commands.slash("ping", "Check bot status"),
                            Commands.slash("sticker", "Parse LINE sticker URL")
                                    .addOption(
                                            OptionType.STRING,
                                            "url",
                                            "LINE sticker store URL",
                                            true
                                    )
                    )
                    .queue(
                            commands -> {
                                System.out.println("Commands registered successfully!");
                                commands.forEach(command ->
                                        System.out.println("Registered: /" + command.getName())
                                );
                            },
                            error -> {
                                System.err.println("Command registration failed!");
                                error.printStackTrace();
                            }
                    );

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
