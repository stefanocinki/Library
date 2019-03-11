/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.convey.library.jdbi;

import com.convey.library.api.Author;
import com.convey.library.api.Book;
import com.convey.library.api.Genre;
import java.util.List;
import org.skife.jdbi.v2.exceptions.TransactionFailedException;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.Transaction;

/**
 *
 * @author Stefan
 */
public abstract class BookDAO {

    @Mapper(BookMapper.class)
    @SqlQuery("select b.isbn, b.title, b.number_of_pages, a.id a_id, a.name a_name, g.id g_id, g.name "
            + "g_name from  books  b join authors_books ab on b.isbn=ab.ISBN join \n"
            + "authors a on ab.author_id=a.id join genres g on b.genre=g.id\n"
            + "where b.isbn = :isbn ")
    public abstract List<Book> _getBookByIsbn(@Bind("isbn") String isbn);

    @SqlUpdate("INSERT INTO books (isbn, title, number_of_pages, genre) VALUES(:b.isbn, :b.title, :b.numberOfPages, :genre)")
    public abstract int saveBook(@BindBean("b") Book b, @Bind("genre") int genre);

    @SqlQuery("SELECT Id FROM Authors WHERE id = :id")
    abstract Integer getAuthorId(@BindBean Author a);

    @SqlUpdate("INSERT INTO Authors_Books(isbn, author_id) VALUES(:isbn, :authorId)")
    abstract void saveAuthorBook(@Bind("isbn") String isbn, @Bind("authorId") int authorId);

    @SqlQuery("SELECT Id FROM Genres WHERE id = :id")
    abstract Integer getGenreId(@BindBean Genre a);

    public Book getBookByIsbn(@Bind("isbn") String isbn) {
        List<Book> bookList = _getBookByIsbn(isbn);
        if (bookList == null || bookList.size() < 1) {
            // Log it or report error if needed
            return null;
        }
        // The mapper will have given a reference to the same value for every entry in the list
        return bookList.get(bookList.size() - 1);
    }

    @Transaction
    public void insertBook(Book book) {
        int affectedRows = 0; 
        if (null != getGenreId(book.getGenre())) {
            affectedRows = saveBook(book, book.getGenre().getId());
            if (affectedRows > 0) {
                book.getAuthors().forEach(a -> {
                    if (getAuthorId(a) != null) {
                        saveAuthorBook(book.getIsbn(), a.getId());
                    } else {
                        throw new TransactionFailedException("No author found. ID=" + a.getId());
                    }
                });
                
            } else {
                throw new TransactionFailedException("The book is not saved");
            }

        } else {
            throw new TransactionFailedException("No genre found. ID=" + book.getGenre().getId());
        }
    }

}
