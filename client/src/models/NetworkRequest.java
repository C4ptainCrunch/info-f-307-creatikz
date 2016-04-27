package models;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * A NetworkRequest object represents a request sent through the network.
 */

public class NetworkRequest {

    private Builder request_builder;
    private Response response;

    /**
     * Default constructor.
     * @param target The target of the request
     * @param path The url path
     * @param type  The type of the data passed
     */

    public NetworkRequest(String target, String path, MediaType type){
        Client client = ClientBuilder.newClient();
        this.request_builder = client.target(target).path(path).request(type);
    }

    public void post(Form postFrom){
        this.response = this.request_builder.post(Entity.form(postFrom));
    }

    public Response getResponse(){
        return this.response;
    }
}
