package com.felix.oauth2resource.model.dto;

import lombok.Data;

import java.util.List;
@Data
public class PageableBookView {

    private long total;

    List<BookView> books;
}
