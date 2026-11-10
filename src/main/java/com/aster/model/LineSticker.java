package com.aster.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LineSticker(
        String id,
        String type,
        String staticUrl,
        String fallbackStaticUrl,
        String animationUrl,
        String popupUrl,
        String soundUrl
) {
}
