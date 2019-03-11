/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.convey.library.jdbi;

import com.convey.library.api.Author;
import com.convey.library.api.Book;
import com.convey.library.api.Genre;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 *
 * @author Stefan
 */
public class BookMapper implements ResultSetMapper<Book> {

    private Book book;

    // this mapping method will get called for every row in the result set 
    @Override
    public Book map(int i, ResultSet rs, StatementContext sc) throws SQLException {

        // for the first row of the result set, we create the wrapper object
        if (i == 0) {
            book = new Book();
            book.setIsbn(rs.getString("isbn"));
            book.setTitle(rs.getString("title"));
            book.setNumberOfPages(rs.getInt("number_of_pages"));
            book.setGenre(new Genre(rs.getInt("g_id"), rs.getString("g_name")));
            book.setAuthors(new HashSet<Author>());
        }
        Author author = new Author(rs.getInt("a_id"), rs.getString("a_name"));
        //if (user.getId() > 0) {
        book.getAuthors().add(author);
        //}

        return book;
    }

}
