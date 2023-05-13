package com.small.jaxrs;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author wesson
 * Create at 2023/4/3 23:13 周一
 */
@Path("/jax")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface JaxrsInterface {

    @GET
    @Path("/get")
    String getInfo();
}
