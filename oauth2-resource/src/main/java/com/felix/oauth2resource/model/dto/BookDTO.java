package com.felix.oauth2resource.model.dto;

import com.felix.oauth2resource.repository.entity.BookEntity;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;

@Data
public class BookDTO {

    private MultipartFile image;

    @NotEmpty
    private String name;

    private String description;

    private String reason;

    public BookEntity toEntity() {
        BookEntity book = new BookEntity();
        book.setName(this.name);
        book.setDescription(this.description);
        book.setReason(this.reason);
        //book.setImage("");
        return book;
    }
}
