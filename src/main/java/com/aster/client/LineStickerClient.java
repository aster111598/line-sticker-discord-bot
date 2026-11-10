package com.aster.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class LineStickerClient {

    private static final HttpClient HTTP_CLIENT =
            HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .build();

    private static HttpRequest createRequest(String stickerId) {

        if (stickerId == null ||
                !stickerId.matches("\\d+")) {
            throw new IllegalArgumentException(
                    "Invalid sticker ID"
            );
        }

        String url = "https://store.line.me/stickershop/product/"
                + stickerId + "/zh-Hant";

        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(15))
                .header("User-Agent", "Mozilla/5.0")
                .GET()
                .build();
    }

    // 同步版本：保留供現有測試使用
    public static String fetchStickerPage(String stickerId)
            throws IOException, InterruptedException {

        HttpRequest request = createRequest(stickerId);

        HttpResponse<String> response = HTTP_CLIENT.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200) {
            throw new IOException(
                    "LINE Store returned HTTP "
                            + response.statusCode()
            );
        }

        return response.body();
    }

    // 非同步版本：供 Discord 指令使用
    public static CompletableFuture<String> fetchStickerPageAsync(
            String stickerId) {

        HttpRequest request = createRequest(stickerId);

        return HTTP_CLIENT.sendAsync(
                request,
                HttpResponse.BodyHandlers.ofString()
        ).thenApply(response -> {

            if (response.statusCode() != 200) {
                throw new CompletionException(
                        new IOException(
                                "LINE Store returned HTTP "
                                        + response.statusCode()
                        )
                );
            }

            return response.body();
        });
    }
}
