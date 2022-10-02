package com.felix.oauth2resource;

import com.felix.oauth2resource.model.dto.BookDTO;
import com.felix.oauth2resource.model.dto.BookView;
import com.felix.oauth2resource.service.BookService;
import com.felix.oauth2resource.utils.ResourceLoadUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = Oauth2ResourceApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Test
    public void testCreate() {
        byte[] bytes = ResourceLoadUtils.loadResourceAsBytes("test.jpg");
        MockMultipartFile file = new MockMultipartFile("test.jpg", "test.jpg", "image/png", bytes);
        BookDTO dto = new BookDTO();
        dto.setImage(file);
        dto.setName("universe");
        dto.setDescription("A book about universe");
        dto.setReason("mysterious");

        BookView book = bookService.createBook(dto);
        assertThat(book).isNotNull();
    }

    // TODO: enrich testcases
}
