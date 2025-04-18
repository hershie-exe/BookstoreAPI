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
        Cart cart = DataStore.addToCart(customerId, item);
        return Response.ok(cart).build();
    }

    @GET
    public Cart getCart(@PathParam("customerId") int customerId) {
        return DataStore.getCart(customerId);
    }

    @PUT
    @Path("/items/{bookId}")
    public Response updateItem(@PathParam("customerId") int customerId,
                               @PathParam("bookId") int bookId,
                               Cart.Item item) {
        Cart cart = DataStore.updateCartItem(customerId, bookId, item.getQuantity());
        return Response.ok(cart).build();
    }

    @DELETE
    @Path("/items/{bookId}")
    public Response removeItem(@PathParam("customerId") int customerId,
                               @PathParam("bookId") int bookId) {
        Cart cart = DataStore.removeFromCart(customerId, bookId);
        return Response.ok(cart).build();
    }
}
