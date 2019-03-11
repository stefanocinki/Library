/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.convey.library.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Stefan
 */
public class Genre {
    
    private int id;
    private String name;
    private Set<Book> books = new HashSet<Book>(0);

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Genre() {
    }
    
    
    
    @JsonProperty
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @JsonProperty
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    @JsonIgnore
    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    
}
