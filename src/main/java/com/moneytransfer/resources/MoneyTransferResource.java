package com.moneytransfer.resources;

import com.moneytransfer.resourcebeans.TransferBean;
import com.moneytransfer.somebackend.ITransferOperations;
import com.moneytransfer.somebackend.defaultimplementation.DefaultImplementation;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("moneytransfer")
public class MoneyTransferResource {
    private ITransferOperations transferOperations = new DefaultImplementation();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTransfers() {
        return Response.ok(transferOperations.getAllTransfers()).build();
    }

    @GET
    @Path("/{transferId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTransferById (@PathParam("transferId") long transferId) {
        return Response.ok(transferOperations.getTransferById(transferId)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createNewTransfer (TransferBean transfer, @Context UriInfo uriInfo) {
        long transferId = transferOperations.createNewTransfer(transfer);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(Long.toString(transferId));
        return Response.created(builder.build()).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateTransfer (TransferBean transfer) {
        transferOperations.updateTransfer(transfer);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{transferId}")
    public Response deleteTransfer (@PathParam("transferId") long transferId) {
        transferOperations.deleteTransfer(transferId);
        return Response.ok().build();
    }

    @DELETE
    public Response cleanAll () {
        transferOperations.cleanAll();
        return Response.ok().build();
    }
}
