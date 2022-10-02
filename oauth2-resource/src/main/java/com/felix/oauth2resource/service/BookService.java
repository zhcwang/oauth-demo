package com.felix.oauth2resource.service;

import com.felix.oauth2resource.model.dto.BookDTO;
import com.felix.oauth2resource.model.dto.BookView;
import com.felix.oauth2resource.model.exception.BadRequestException;
import com.felix.oauth2resource.model.exception.ForbiddenException;
import com.felix.oauth2resource.repository.BookRepository;
import com.felix.oauth2resource.repository.entity.BookEntity;
import com.felix.oauth2resource.utils.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookService {
    @Autowired
    private ImagePersistenceService imagePersistenceService;

    @Autowired
    private BookRepository bookRepository;


    public List<BookView> getBooks(Pageable pageable) {
        Page<BookEntity> booksPage = bookRepository.findAll(pageable);
        List<BookEntity> books = booksPage.getContent();
        return books.stream().map(BookView::fromEntity).collect(Collectors.toList());
    }

    public BookView createBook(BookDTO dto) {
        if (dto == null) {
            return null;
        }
        BookEntity bookEntity = dto.toEntity();
        MultipartFile image = dto.getImage();
        if (image != null) {
            String location = imagePersistenceService.persistence(image);
            bookEntity.setImage(location);
        }
        bookEntity = bookRepository.save(bookEntity);
        return BookView.fromEntity(bookEntity);
    }

    public void deleteOwnBook(Long bookId) {
        if (bookId == null) {
            return;
        }
        Optional<BookEntity> bookOpt = bookRepository.findOneById(bookId);
        if (bookOpt.isEmpty()) {
            throw new BadRequestException(null, "Invalidate bookId");
        }
        BookEntity bookEntity = bookOpt.get();
        if (StringUtils.equals(bookEntity.getCreatedBy(),  SecurityUtils.getOwner())) {
            bookRepository.delete(bookEntity);
        } else {
            throw new ForbiddenException(null, "Not allowed to delete the resource.");
        }
    }

    public void updateOwnBook(Long bookId, BookDTO dto) {
        // TODO:

    }


}
