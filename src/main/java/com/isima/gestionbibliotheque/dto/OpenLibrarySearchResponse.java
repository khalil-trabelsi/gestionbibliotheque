package com.isima.gestionbibliotheque.dto;


import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
public class OpenLibrarySearchResponse {
    private int numFound;
    private int start;
    private List<BookResult> docs;
    @Data
    static class BookResult {
        private String title;
        @Getter
        private String[] ia;
        private String[] author_name;
        private String[] publisher;
        private String publishDate;

    }

    public List<String> getBookIsbn() {
        List<String> isbns = new ArrayList<>();

        for (BookResult book: docs) {
            log.info(""+book);
            if (book.getIa() != null) {
                for (String ia: book.getIa()) {
                    log.info(ia);
                    if (ia.contains("isbn") && ia.substring(5).length() == 13) {
                        isbns.add(ia.substring(5));
                        break;
                    }
                }
            }

        }

        return isbns;
    }
}


