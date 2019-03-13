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
import org.postgresql.util.PSQLException;
import org.postgresql.util.ServerErrorMessage;
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;
import org.skife.jdbi.v2.sqlobject.Transaction;

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

    //Http get method for reading specific book for isbn
    @GET
    @Timed
    @Path("/{isbn}")
    public Response getBook(@PathParam("isbn") Optional<String> isbn) {
        Book b = bookDAO.getBookByIsbn(isbn.get());

        if (b != null) {
            return Response.ok(b).build();
        } else {
            return Response.status(Status.NOT_FOUND).entity(new ErrorMessage("Not found isbn:" + isbn.get())).build();
        }
    }

    //Http post method for saving book
    @POST
    @Timed
    public Response saveBook(Book b) throws URISyntaxException {
        try {
            String isbn = bookDAO.insertBook(b);
            return Response.status(Status.CREATED)
                    .entity(isbn)
                    .build();

        } catch (Exception e) {
            if (e.getCause() instanceof PSQLException) {
                PSQLException t = (PSQLException) e.getCause();
                ServerErrorMessage s = t.getServerErrorMessage();
                return Response.status(Status.BAD_REQUEST).entity(new ErrorMessage(s.getDetail())).build();
            } else {
                return Response.status(Status.BAD_REQUEST).entity(new ErrorMessage(e.getMessage())).build();
            }

        }

    }

}
