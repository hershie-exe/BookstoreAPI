package com.bookstore.resource;

import com.bookstore.model.Cart;
import com.bookstore.exception.*;
import com.bookstore.storage.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/customers/{customerId}/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {

    @POST
    @Path("/items")
    public Response addItem(@PathParam("customerId") int customerId, Cart.Item item) {
        if (DataStore.getCustomer(customerId) == null) {
            throw new CustomerNotFoundException(customerId);
        }
        if (DataStore.getBook(item.getBookId()) == null) {
            throw new BookNotFoundException(item.getBookId());
        }
        if (item.getQuantity() <= 0) {
            throw new InvalidInputException("Quantity must be greater than zero");
        }

        Cart cart = DataStore.addToCart(customerId, item);
        return Response.ok(cart).build();
    }

    @GET
    public Response getCart(@PathParam("customerId") int customerId) {
        if (DataStore.getCustomer(customerId) == null) {
            throw new CustomerNotFoundException(customerId);
        }

        Cart cart = DataStore.getCart(customerId);
        if (cart == null) {
            throw new CartNotFoundException(customerId);
        }

        return Response.ok(cart).build();
    }

    @PUT
    @Path("/items/{bookId}")
    public Response updateItem(@PathParam("customerId") int customerId,
                               @PathParam("bookId") int bookId,
                               Cart.Item item) {
        if (DataStore.getCustomer(customerId) == null) {
            throw new CustomerNotFoundException(customerId);
        }
        if (DataStore.getBook(bookId) == null) {
            throw new BookNotFoundException(bookId);
        }
        if (item.getQuantity() <= 0) {
            throw new InvalidInputException("Quantity must be greater than zero");
        }

        // Set the correct bookId
        item.setBookId(bookId);

        Cart cart = DataStore.updateCartItem(customerId, item);
        if (cart == null) {
            throw new CartNotFoundException(customerId);
        }

        return Response.ok(cart).build();
    }

    @DELETE
    @Path("/items/{bookId}")
    public Response removeItem(@PathParam("customerId") int customerId,
                               @PathParam("bookId") int bookId) {
        if (DataStore.getCustomer(customerId) == null) {
            throw new CustomerNotFoundException(customerId);
        }

        Cart cart = DataStore.removeFromCart(customerId, bookId);
        if (cart == null) {
            throw new CartNotFoundException(customerId);
        }

        return Response.ok(cart).build();
    }
}