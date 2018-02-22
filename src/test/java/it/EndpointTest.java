package it;

import application.todo.Todo;

import static org.junit.Assert.assertTrue;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class EndpointTest {
    protected String dns = System.getProperty("app.dns");
    protected String port = System.getProperty("liberty.test.port");
    protected String war = System.getProperty("war.context");
    protected String url = "http://" + dns + ":" + port + "/" + war;

    public void testEndpoint(String endpoint, String expectedOutput) {
        String url = this.url + endpoint;
        System.out.println("Testing " + url);
        Response response = sendRequest(url, "GET");
        int responseCode = response.getStatus();
        assertTrue("Incorrect response code: " + responseCode,
                   responseCode == 200);

        String responseString = response.readEntity(String.class);
        response.close();
        assertTrue("Incorrect response, response is " + responseString, responseString.contains(expectedOutput));
    }

    public Response sendRequest(String url, String requestType) {
        Client client = ClientBuilder.newClient();
        System.out.println("Testing " + url);
        WebTarget target = client.target(url);
        Invocation.Builder invoBuild = target.request();
        Response response = invoBuild.build(requestType).invoke();
        return response;
    }

    public Response sendPostRequest(String url, String data){
        Client client = ClientBuilder.newClient();
        System.out.println("Testing " + url);
        WebTarget target = client.target(url);
        Invocation.Builder invoBuild = target.request(MediaType.APPLICATION_JSON);
        Response response = invoBuild.post(Entity.entity(data, MediaType.APPLICATION_JSON));
        return response;
    }
}
