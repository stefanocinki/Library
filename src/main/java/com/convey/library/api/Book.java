/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.convey.library.api;

import com.fasterxml.jackson.annotation.JsonProperty; 
import java.util.HashSet; 
import java.util.Set;

/**
 *
 * @author Stefan
 */
public class Book {

    private String isbn;
    private String title;
    private int numberOfPages;
    private Genre genre;
    private Set<Author> authors = new HashSet<Author>(0);

    public Book() {
    }

    @JsonProperty
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @JsonProperty
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("number_of_pages")
    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    @JsonProperty
    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
    @JsonProperty
    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }
    
    

}
