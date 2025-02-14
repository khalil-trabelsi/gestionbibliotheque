package com.isima.gestionbibliotheque.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Permission {

    COLLECTION_READ("collection:read"),
    COLLECTION_UPDATE("collection:update"),
    COLLECTION_SHARE("collection:share");

    private final String permission;
}
