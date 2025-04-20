package com.bookstore.resource;

import com.bookstore.model.Order;
import com.bookstore.exception.*;
import com.bookstore.storage.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("/customers/{customerId}/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    @POST
    public Response createOrder(@PathParam("customerId") int customerId) {
        Order order = DataStore.createOrderFromCart(customerId);
        return Response.status(Response.Status.CREATED).entity(order).build();
    }

    @GET
    public List<Order> getAllOrders(@PathParam("customerId") int customerId) {
        return DataStore.getOrdersByCustomer(customerId); // Corrected method call
    }


    @GET
    @Path("/{orderId}")
    public Response getOrder(@PathParam("customerId") int customerId,
                             @PathParam("orderId") int orderId) {
        Order order = DataStore.getOrder(customerId, orderId);
        return Response.ok(order).build();
    }
}
