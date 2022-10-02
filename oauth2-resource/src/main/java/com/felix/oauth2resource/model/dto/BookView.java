package com.felix.oauth2resource.model.dto;

import com.felix.oauth2resource.repository.entity.BookEntity;
import lombok.Data;

@Data
public class BookView {

    private Long id;

    private String name;

    private String description;

    private String reason;

    private String image;

    private String createdBy;

    private Long createdOn;

    public static BookView fromEntity(BookEntity entity) {
        if (entity == null) {
            return null;
        }
        BookView bookView = new BookView();
        bookView.setId(entity.getId());
        bookView.setName(entity.getName());
        bookView.setDescription(entity.getDescription());
        bookView.setReason(entity.getReason());
        bookView.setImage(entity.getImage());
        bookView.setCreatedBy(entity.getCreatedBy());
        bookView.setCreatedOn(entity.getCreatedOn().toEpochMilli());
        return bookView;
    }
}
