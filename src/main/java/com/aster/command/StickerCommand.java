package com.aster.command;

import com.aster.parser.LineStickerUrlParser;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class StickerCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(
            SlashCommandInteractionEvent event) {

        if (!event.getName().equals("sticker")) {
            return;
        }

        OptionMapping option = event.getOption("url");

        if (option == null) {
            event.reply("Please provide a URL.")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        String url = option.getAsString();

        String stickerId =
                LineStickerUrlParser.parseStickerId(url);

        if (stickerId == null) {
            event.reply("Invalid LINE sticker URL.")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        event.reply("LINE Sticker ID: " + stickerId)
                .queue();
    }
}
