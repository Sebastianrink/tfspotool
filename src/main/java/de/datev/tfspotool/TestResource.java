package de.datev.tfspotool;

import de.datev.tfspotool.rest.api.GoalsApi;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("test")
public class TestResource implements GoalsApi
{

    @GET
    public String test() {
        return "Hallo";
    }

    @Override
    public Response getGoals() {
        //TfsConnection tfsConnection = new TfsConnection();
        //List<com.microsoft.tfs.core.clients.workitem.WorkItem> allGoals = tfsConnection.getAllGoals();
        return Response.status(200).build();
    }
}
