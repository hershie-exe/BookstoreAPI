package com.bookstore.resource;

import com.bookstore.model.Customer;
import com.bookstore.exception.CustomerNotFoundException;
import com.bookstore.storage.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    @POST
    public Response createCustomer(Customer customer) {
        Customer created = DataStore.addCustomer(customer);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    public List<Customer> getAllCustomers() {
        return DataStore.getAllCustomers();
    }

    @GET
    @Path("/{id}")
    public Response getCustomerById(@PathParam("id") int id) {
        Customer customer = DataStore.getCustomer(id);
        if (customer == null) throw new CustomerNotFoundException(id);
        return Response.ok(customer).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") int id, Customer updated) {
        Customer customer = DataStore.updateCustomer(id, updated);
        if (customer == null) throw new CustomerNotFoundException(id);
        return Response.ok(customer).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") int id) {
        if (!DataStore.deleteCustomer(id)) throw new CustomerNotFoundException(id);
        return Response.noContent().build();
    }
}
