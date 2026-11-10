package com.aster.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LineStickerUrlParserTest {

    @Test
    void shouldParseValidStickerUrl() {

        String url =
                "https://store.line.me/stickershop/product/37882/zh-Hant";

        String result =
                LineStickerUrlParser.parseStickerId(url);

        assertEquals("37882", result);
    }

    @Test
    void shouldParseUrlWithoutLanguage() {

        String url =
                "https://store.line.me/stickershop/product/37882";

        String result =
                LineStickerUrlParser.parseStickerId(url);

        assertEquals("37882", result);
    }

    @Test
    void shouldRejectInvalidDomain() {

        String url =
                "https://example.com/stickershop/product/37882";

        String result =
                LineStickerUrlParser.parseStickerId(url);

        assertNull(result);
    }

    @Test
    void shouldRejectNonNumericId() {

        String url =
                "https://store.line.me/stickershop/product/abc";

        String result =
                LineStickerUrlParser.parseStickerId(url);

        assertNull(result);
    }

    @Test
    void shouldRejectNullUrl() {

        String result =
                LineStickerUrlParser.parseStickerId(null);

        assertNull(result);
    }

    @Test
    void shouldRejectBlankUrl() {

        String result =
                LineStickerUrlParser.parseStickerId("   ");

        assertNull(result);
    }
}
