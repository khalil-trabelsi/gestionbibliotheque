package com.isima.gestionbibliotheque.dto;

import com.isima.gestionbibliotheque.model.Book;
import com.isima.gestionbibliotheque.model.Tag;
import com.isima.gestionbibliotheque.model.User;
import com.isima.gestionbibliotheque.model.UserBook;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagDto {
    private Long id;
    private String label;
    private String color;
    private User user;
    private UserBook userBook;

    public static TagDto fromEntity(Tag tag) {
        return tag == null ? null :
                TagDto.builder()
                        .id(tag.getId())
                        .label(tag.getLabel())
                        .color(tag.getColor())
                        .user(tag.getUser())
                        .userBook(tag.getUserBook())
                        .build();
    }

    public static Tag toEntity(TagDto tagDto, User user, UserBook book) {
        if (tagDto == null) {
            return null;
        }

        Tag tag = new Tag();
        tag.setId(tagDto.getId());
        tag.setLabel(tagDto.getLabel());
        tag.setColor(tagDto.getColor());
        tag.setUser(user);
        tag.setUserBook(book);

        return tag;
    }

}
