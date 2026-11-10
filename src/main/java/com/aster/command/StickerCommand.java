package com.aster.command;

import com.aster.client.LineStickerClient;
import com.aster.model.LineSticker;
import com.aster.parser.LineStickerHtmlParser;
import com.aster.parser.LineStickerUrlParser;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.List;
import java.util.stream.Collectors;

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

        // 先向 Discord 確認收到指令
        event.deferReply().queue(
                hook -> {

                    // 確認成功後才開始 HTTP 請求
                    LineStickerClient.fetchStickerPageAsync(stickerId)
                            .whenComplete((html, error) -> {

                                if (error != null) {
                                    System.err.println(
                                            "HTTP request failed: " + error
                                    );

                                    hook.editOriginal(
                                            "Failed to fetch LINE sticker page."
                                    ).queue();
                                    return;
                                }

                                try {
                                    String title =
                                            LineStickerHtmlParser
                                                    .parseStickerTitle(html);

                                    List<LineSticker> stickers =
                                            LineStickerHtmlParser
                                                    .parseStickers(html);

                                    String types = stickers.stream()
                                            .map(LineSticker::type)
                                            .distinct()
                                            .collect(Collectors.joining(", "));

                                    String message =
                                            "**LINE 貼圖資訊**\n"
                                                    + "貼圖名稱：" + title + "\n"
                                                    + "Package ID：" + stickerId + "\n"
                                                    + "貼圖數量：" + stickers.size() + "\n"
                                                    + "貼圖類型：" + types + "\n"
                                                    + "資源解析成功";

                                    hook.editOriginal(message).queue();

                                } catch (Exception e) {

                                    System.err.println(
                                            "Sticker parsing failed: " + e
                                    );

                                    hook.editOriginal(
                                            "Failed to parse LINE sticker data."
                                    ).queue();
                                }
                            });
                },
                error -> {
                    System.err.println(
                            "Discord deferReply failed: " + error
                    );
                }
        );
    }
}
