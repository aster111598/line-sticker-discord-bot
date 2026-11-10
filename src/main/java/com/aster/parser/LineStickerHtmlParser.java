package com.aster.parser;

import com.aster.model.LineSticker;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class LineStickerHtmlParser {

    private static final ObjectMapper OBJECT_MAPPER =
            new ObjectMapper();

    public static String parseStickerTitle(String html) {

        if (html == null || html.isBlank()) {
            throw new IllegalArgumentException(
                    "HTML cannot be empty"
            );
        }

        Document document = Jsoup.parse(html);

        String title = document.title();

        if (title.isBlank()) {
            throw new IllegalArgumentException(
                    "Sticker title not found"
            );
        }

        return title;
    }

    public static List<LineSticker> parseStickers(String html)
            throws JsonProcessingException {

        if (html == null || html.isBlank()) {
            throw new IllegalArgumentException(
                    "HTML cannot be empty"
            );
        }

        Document document = Jsoup.parse(html);

        Elements elements = document.select(
                "[data-test=sticker-item]"
        );

        List<LineSticker> stickers = new ArrayList<>();

        for (Element element : elements) {

            String previewJson =
                    element.attr("data-preview");

            if (previewJson.isBlank()) {
                throw new IllegalArgumentException(
                        "Sticker preview JSON is missing"
                );
            }

            LineSticker sticker = OBJECT_MAPPER.readValue(
                    previewJson,
                    LineSticker.class
            );

            if (sticker.id() == null ||
                    sticker.id().isBlank() ||
                    sticker.type() == null ||
                    sticker.type().isBlank()) {

                throw new IllegalArgumentException(
                        "Sticker ID or type is missing"
                );
            }

            stickers.add(sticker);
        }

        if (stickers.isEmpty()) {
            throw new IllegalArgumentException(
                    "No stickers found in HTML"
            );
        }

        return stickers;
    }
}
