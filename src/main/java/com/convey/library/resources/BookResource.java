/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.convey.library.resources;

import com.convey.library.api.Book;
import com.codahale.metrics.annotation.Timed;
import com.convey.library.jdbi.BookDAO;
import io.dropwizard.jersey.errors.ErrorMessage;
import java.net.URISyntaxException; 
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Optional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author Stefan
 */
@Path("/books")
@Produces("application/json")
@Consumes("application/json")
public class BookResource {

    BookDAO bookDAO;

    public BookResource(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    @GET
    @Timed
    @Path("/{isbn}")
    public Response getBooks(@PathParam("isbn") Optional<String> isbn) {
        Book b = bookDAO.getBookByIsbn(isbn.get());

        if (b != null) {
            return Response.ok(b).build();
        } else {
            return Response.status(Status.NOT_FOUND).entity(new ErrorMessage("Not found isbn:"+isbn.get())).build();
        }
    }

    @POST
    @Timed
    public Response saveBook(Book b) throws URISyntaxException {
        try {
         bookDAO.insertBook(b);
        return Response.status(Status.CREATED).build();
   
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(new ErrorMessage(e.getMessage())).build();
        }
        
    }

}
