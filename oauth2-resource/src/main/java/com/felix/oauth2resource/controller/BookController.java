package com.felix.oauth2resource.controller;

import com.felix.oauth2resource.model.dto.BookDTO;
import com.felix.oauth2resource.model.dto.BookView;
import com.felix.oauth2resource.model.dto.PageableBookView;
import com.felix.oauth2resource.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<PageableBookView> getBooks(@RequestParam int pageNum, @RequestParam int pageSize, @RequestParam(required = false) String bookName) {
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
        PageableBookView books = bookService.getBooks(pageNum, pageSize, bookName);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/_my")
    public ResponseEntity<PageableBookView> getMyBooks(@RequestParam int page, @RequestParam int results, @RequestParam(required = false) String sortField, @RequestParam(required = false) String sortOrder) {
        PageableBookView myBooks = bookService.getMyBooks(page, results, sortField, sortOrder);
        return new ResponseEntity<>(myBooks, HttpStatus.OK);
    }


    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BookView> createBook(@ModelAttribute BookDTO dto) {
        return new ResponseEntity<>(bookService.createBook(dto), HttpStatus.OK);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteOwnBook(@PathVariable Long bookId) {
        bookService.deleteOwnBook(bookId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
