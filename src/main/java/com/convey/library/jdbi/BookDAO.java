/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.convey.library.jdbi;

import com.convey.library.api.Author;
import com.convey.library.api.Book;
import com.convey.library.api.Genre;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
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

    /**
     * Reading specific book from DB. Number of list elements depends on number
     * of how many authors are wrote book.
     *
     * @param isbn
     * @return List of Books
     */
    @Mapper(BookMapper.class)
    @SqlQuery("select b.isbn, b.title, b.number_of_pages, a.id a_id, a.name a_name, g.id g_id, g.name "
            + "g_name from  books  b join authors_books ab on b.isbn=ab.ISBN join \n"
            + "authors a on ab.author_id=a.id join genres g on b.genre=g.id\n"
            + "where b.isbn = :isbn ")
    abstract public List<Book> _getBookByIsbn(@Bind("isbn") String isbn);

    /**
     * Insert row in table book.
     *
     * @param b Book for insert
     * @return return ISBN of inserted book
     */
    @SqlQuery("INSERT INTO books (isbn, title, number_of_pages, genre) VALUES(:isbn, :title, :numberOfPages, :genreID) "
            + "RETURNING isbn")
    abstract public String saveBook(@BindBook Book b);

    /**
     * Insertion of associative relationship between Book and Author
     *
     * @param isbn
     * @param authorId
     */
    @SqlUpdate("INSERT INTO Authors_Books(isbn, author_id) VALUES(:isbn, :authorId)")
    abstract public void saveAuthorBook(@Bind("isbn") String isbn, @Bind("authorId") int authorId);

    /**
     * Get specific Author from DB
     *
     * @param a ID of Author
     * @return Author
     */
    @SqlQuery("SELECT Id FROM Authors WHERE id = :id")
    abstract public Integer getAuthorId(@BindBean Author a);

    /**
     * Get specific book from DB.
     *
     * @param isbn
     * @return Book
     */
    public Book getBookByIsbn(@Bind("isbn") String isbn) {
        List<Book> bookList = _getBookByIsbn(isbn);
        if (bookList == null || bookList.size() < 1) {
            return null;
        }
        // The mapper will have given a reference to the same value for every entry in the list
        return bookList.get(bookList.size() - 1);
    }

    /**
     * Save book and association relation to DB in one transaction.
     *
     * @param book
     * @return
     */
    @Transaction
    public String insertBook(Book book) {
        String isbn = saveBook(book);
        if (isbn != null) {
            book.getAuthors().forEach(a -> { 
                
                saveAuthorBook(book.getIsbn(), a.getId()); 
            }); 
        } else {
            throw new TransactionFailedException("The book is not saved");
        }
        return isbn;
    }
}
