package com.aster.parser;

import com.aster.model.LineSticker;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LineStickerHtmlParserTest {

    // 固定的測試 HTML，不需要連線 LINE 商店
    private static final String TEST_HTML = """
            <html>
                <head>
                    <title>測試貼圖包 - LINE STORE</title>
                </head>
                <body>
                    <ul>
                        <li data-test="sticker-item"
                            data-preview='{
                                "id": "10001",
                                "type": "animation",
                                "staticUrl": "https://example.com/1.png",
                                "animationUrl": "https://example.com/1_apng.png"
                            }'>
                        </li>

                        <li data-test="sticker-item"
                            data-preview='{
                                "id": "10002",
                                "type": "static",
                                "staticUrl": "https://example.com/2.png"
                            }'>
                        </li>
                    </ul>
                </body>
            </html>
            """;

    @Test
    void shouldParsePageTitle() {

        String title =
                LineStickerHtmlParser.parseStickerTitle(TEST_HTML);

        assertEquals("測試貼圖包 - LINE STORE", title);
    }

    @Test
    void shouldParseStickerCount() throws Exception {

        List<LineSticker> stickers =
                LineStickerHtmlParser.parseStickers(TEST_HTML);

        assertEquals(2, stickers.size());
    }

    @Test
    void shouldParseStickerProperties() throws Exception {

        List<LineSticker> stickers =
                LineStickerHtmlParser.parseStickers(TEST_HTML);

        LineSticker firstSticker = stickers.get(0);

        assertEquals("10001", firstSticker.id());
        assertEquals("animation", firstSticker.type());

        assertEquals(
                "https://example.com/1.png",
                firstSticker.staticUrl()
        );

        assertEquals(
                "https://example.com/1_apng.png",
                firstSticker.animationUrl()
        );
    }

    @Test
    void shouldHandleMissingOptionalFields() throws Exception {

        List<LineSticker> stickers =
                LineStickerHtmlParser.parseStickers(TEST_HTML);

        LineSticker secondSticker = stickers.get(1);

        assertEquals("10002", secondSticker.id());
        assertEquals("static", secondSticker.type());
        assertNull(secondSticker.animationUrl());
    }

    @Test
    void shouldRejectHtmlWithoutStickers() {

        String html = "<html><body></body></html>";

        assertThrows(
                IllegalArgumentException.class,
                () -> LineStickerHtmlParser.parseStickers(html)
        );
    }

    @Test
    void shouldRejectBlankHtml() {

        assertThrows(
                IllegalArgumentException.class,
                () -> LineStickerHtmlParser.parseStickerTitle("")
        );
    }
}
