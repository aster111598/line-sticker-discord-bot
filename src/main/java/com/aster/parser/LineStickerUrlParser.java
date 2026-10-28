package com.aster.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineStickerUrlParser {

    private static final Pattern STICKER_URL_PATTERN =
            Pattern.compile(
                    "^https://store\\.line\\.me/stickershop/product/(\\d+)(?:/[^?#]*)?(?:\\?[^#]*)?(?:#.*)?$"
            );

    public static String parseStickerId(String url) {

        if (url == null || url.isBlank()) {
            return null;
        }

        Matcher matcher = STICKER_URL_PATTERN.matcher(url.trim());

        if (matcher.matches()) {
            return matcher.group(1);
        }

        return null;
    }
}
