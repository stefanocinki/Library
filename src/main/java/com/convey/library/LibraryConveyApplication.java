package com.convey.library;

import com.convey.library.jdbi.BookDAO;
import com.convey.library.resources.BookResource;
import io.dropwizard.Application;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

public class LibraryConveyApplication extends Application<LibraryConveyConfiguration> {

    public static void main(final String[] args) throws Exception {
        new LibraryConveyApplication().run(args);
    }

    @Override
    public String getName() {
        return "LibraryConvey";
    }

    @Override
    public void initialize(final Bootstrap<LibraryConveyConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final LibraryConveyConfiguration configuration,
            final Environment environment) throws ClassNotFoundException {
       final DBIFactory factory = new DBIFactory();
       final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
       final BookDAO bookDAO = jdbi.onDemand(BookDAO.class); 

        environment.jersey().register(new BookResource(bookDAO));
    }

}
