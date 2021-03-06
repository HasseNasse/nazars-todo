package it;

import application.todo.Todo;

import static org.junit.Assert.assertTrue;

import javax.ws.rs.client.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class EndpointTest {
    // TODO: 2018-02-28  FAILSAFE PROPERTIES ALWAYS RETURNING NULL!?
    protected String dns = "localhost";
    protected String port = "9080";
    protected String war = "test";
    protected String url = "http://" + dns + ":" + port + "/" + war;

    protected Client client = ClientBuilder.newClient();

    public void testEndpoint(String endpoint, String expectedOutput) {
        String url = this.url + endpoint;
        System.out.println("Testing " + url);
        Response response = sendRequest(url, "GET", null);
        int responseCode = response.getStatus();
        assertTrue("Incorrect response code: " + responseCode,
                   responseCode == 200);

        String responseString = response.readEntity(String.class);
        response.close();
        assertTrue("Incorrect response, response is " + responseString, responseString.contains(expectedOutput));
    }

    public Response sendRequest(String url, String requestType, String jwtToken) {
        System.out.println("Testing " + url);
        WebTarget target = this.client.target(url);
        Invocation.Builder invoBuild = target.request();

        if(jwtToken != null)
            invoBuild = invoBuild.header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);

        Response response = invoBuild.build(requestType).invoke();

        return response;
    }

    public Response sendPostRequest(String url, String jsonData, String jwtToken){
        System.out.println("Testing " + url);
        WebTarget target = this.client.target(url);
        Invocation.Builder invoBuild = target.request(MediaType.APPLICATION_JSON);

        if(jwtToken != null)
            invoBuild = invoBuild.header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);

        Response response = invoBuild.post(Entity.entity(jsonData, MediaType.APPLICATION_JSON));
        return response;
    }

    public Response sendPutRequest(String url, String jsonData, String jwtToken){
        System.out.println("Testing " + url);
        WebTarget target = this.client.target(url);
        Invocation.Builder invoBuild = target.request(MediaType.APPLICATION_JSON);

        if(jwtToken != null)
            invoBuild = invoBuild.header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);

        Response response = invoBuild.put(Entity.entity(jsonData, MediaType.APPLICATION_JSON));
        return response;
    }
}
