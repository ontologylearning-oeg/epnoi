package org.epnoi.learner.service.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.epnoi.learner.filesystem.DemoDataLoader;
import org.epnoi.learner.modules.Learner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;


@Component
@Path("/learner/demo")
@Api(value = "/learner/demo", description = "Operations for retrieving the learned relations from a domain")
public class DemoResource {
    private static final Logger logger = Logger.getLogger(DemoResource.class
            .getName());
    @Autowired
    private Learner learner;

    @Autowired
    private DemoDataLoader demoDataLoader;


    @PostConstruct
    public void init() {
        logger.info("Starting the " + this.getClass());
    }



    @POST
    @Path("/load")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The demo data has been created"),
            @ApiResponse(code = 500, message = "Something went wrong in the trainer module of the learner")})
    public Response createDemoData() {
        demoDataLoader.load();

        return Response.ok().build();
    }

    @POST
    @Path("/erase")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The demo data has been created"),
            @ApiResponse(code = 500, message = "Something went wrong in the trainer module of the learner")})
    public Response removeDemoData() {
        demoDataLoader.erase();

        return Response.ok().build();
    }

}