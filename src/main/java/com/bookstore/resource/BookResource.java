package com.bookstore.resource;

import com.bookstore.model.Book;
import com.bookstore.exception.BookNotFoundException;
import com.bookstore.exception.InvalidInputException;
import com.bookstore.storage.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

    @POST
    public Response createBook(Book book) {
        if (book.getTitle() == null || book.getTitle().isEmpty() || book.getPublicationYear() > 2024) {
            throw new InvalidInputException("Invalid book data.");
        }
        Book created = DataStore.addBook(book);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    public List<Book> getAllBooks() {
        return DataStore.getAllBooks();
    }

    @GET
    @Path("/{id}")
    public Response getBookById(@PathParam("id") int id) {
        Book book = DataStore.getBook(id);
        if (book == null) throw new BookNotFoundException(id);
        return Response.ok(book).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") int id, Book updatedBook) {
        Book book = DataStore.updateBook(id, updatedBook);
        if (book == null) throw new BookNotFoundException(id);
        return Response.ok(book).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") int id) {
        if (!DataStore.deleteBook(id)) throw new BookNotFoundException(id);
        return Response.noContent().build();
    }
}
