package com.convey.library.resources;

import com.convey.library.api.Author;
import com.convey.library.api.Book;
import com.convey.library.api.Genre;
import com.convey.library.jdbi.BookDAO;
import com.convey.library.resources.BookResource;
import io.dropwizard.testing.junit.ResourceTestRule;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

/**
 * Test class for BookResource
 *
 * @author Stefan
 */
public class BookResourceTest {

    private static final BookDAO bookDAO = mock(BookDAO.class, Mockito.CALLS_REAL_METHODS);
    Book book;

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new BookResource(bookDAO))
            .build();

    @Before
    public void setup() {

        Genre g = new Genre(100, "Science work");
        Author a = new Author(2019, "Dr Nele Karajlic");
        book = new Book();
        book.setIsbn("0000-1111-HHHH-3333");
        book.setNumberOfPages(260);
        book.setTitle("Doktor Nele");
        book.setGenre(g);
        HashSet<Author> authors = new HashSet<>();
        authors.add(a);
        book.setAuthors(authors);

        when(bookDAO.saveBook(any(Book.class))).thenReturn(book.getIsbn());
        when(bookDAO.getAuthorId(any(Author.class))).thenReturn(a.getId());
        when(bookDAO.getBookByIsbn(any(String.class))).thenReturn(book);
    }

    @After
    public void tearDown() {
        reset(bookDAO);
    }

    @Test
    public void testSaveBook() throws InterruptedException, ExecutionException {

        Entity<?> entity = Entity.entity(book, MediaType.APPLICATION_JSON_TYPE);
        final Response response = (Response) resources.target("/books").request()
                .post(entity);

        assertThat(response.readEntity(String.class)).isEqualTo(book.getIsbn());
        verify(bookDAO).insertBook(any(Book.class));
    }

    @Test
    public void testGetBook() throws InterruptedException, ExecutionException {
        Book bookResponse = resources.target("/books/0000-1111-HHHH-3333").request()
                .get(Book.class);
        assertThat(bookResponse.getIsbn()).isEqualTo(book.getIsbn());
        assertThat(bookResponse.getNumberOfPages()).isEqualTo(book.getNumberOfPages());
        assertThat(bookResponse.getTitle()).isEqualTo(book.getTitle());
        verify(bookDAO).getBookByIsbn("0000-1111-HHHH-3333");
    }
}
