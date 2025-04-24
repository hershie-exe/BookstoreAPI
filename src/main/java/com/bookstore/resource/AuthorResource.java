package com.bookstore.resource;

import com.bookstore.model.Author;
import com.bookstore.model.Book;
import com.bookstore.exception.AuthorNotFoundException;
import com.bookstore.storage.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {

    @POST
    public Response createAuthor(Author author) {
        Author created = DataStore.addAuthor(author);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    public List<Author> getAllAuthors() {
        return DataStore.getAllAuthors();
    }

    @GET
    @Path("/{id}")
    public Response getAuthorById(@PathParam("id") int id) {
        Author author = DataStore.getAuthor(id);
        if (author == null) {
            throw new AuthorNotFoundException(id);
        }
        return Response.ok(author).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateAuthor(@PathParam("id") int id, Author author) {
        Author updated = DataStore.updateAuthor(id, author);
        if (updated == null) {
            throw new AuthorNotFoundException(id);
        }
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") int id) {
        if (!DataStore.deleteAuthor(id)) {
            throw new AuthorNotFoundException(id);
        }
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/books")
    public List<Book> getBooksByAuthor(@PathParam("id") int authorId) {
        // You can handle case where there are no books for the author
        List<Book> books = DataStore.getBooksByAuthor(authorId);
        if (books.isEmpty()) {
            throw new AuthorNotFoundException(authorId); // Alternatively, you can return an empty list
        }
        return books;
    }
}
