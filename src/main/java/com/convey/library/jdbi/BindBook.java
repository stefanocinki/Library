/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.convey.library.jdbi;

import com.convey.library.api.Book;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.eclipse.jetty.server.Authentication.User;
import org.skife.jdbi.v2.SQLStatement;
import org.skife.jdbi.v2.sqlobject.Binder;
import org.skife.jdbi.v2.sqlobject.BinderFactory;
import org.skife.jdbi.v2.sqlobject.BindingAnnotation;

/**
 * Custom Bean form class Book
 * @author Stefan
 */
@BindingAnnotation(BindBook.BookBinderFactory.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface BindBook {

    public static class BookBinderFactory implements BinderFactory {

        public Binder build(Annotation annotation) {
            return new Binder<BindBook, Book>() {
                public void bind(SQLStatement q, BindBook bind, Book book) {
                    q.bind("isbn", book.getIsbn());
                    q.bind("title", book.getTitle());
                    q.bind("numberOfPages", book.getNumberOfPages());
                    q.bind("genreID", book.getGenre().getId()); 
                }
            };
        }
    }
}
