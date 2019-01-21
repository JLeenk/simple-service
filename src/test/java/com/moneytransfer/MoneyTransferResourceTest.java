package com.moneytransfer;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.moneytransfer.resourcebeans.TransferBean;
import org.glassfish.grizzly.http.server.HttpServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class MoneyTransferResourceTest {

    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() {
        // start the server
        server = Main.startServer();
        // create the client
        Client c = ClientBuilder.newClient();

        // uncomment the following line if you want to enable
        // support for JSON in the client (you also have to uncomment
        // dependency on jersey-media-json module in pom.xml and Main.startServer())
        // --
        // c.configuration().enable(new org.glassfish.jersey.media.json.JsonJaxbFeature());

        target = c.target(Main.BASE_URI);
    }

    @After
    public void tearDown() {
        server.stop();
    }

    @Test
    public void testCreateNewTransferAndGetTransferById() {
        TransferBean transferBean = new TransferBean();
        transferBean.setSenderName("Sender1");
        transferBean.setAmount(1234);
        Response response = target.path("moneytransfer").request().post(Entity.entity(transferBean, MediaType.APPLICATION_JSON));
        String location = response.getHeaderString("Location");
        String transferID = location.substring(location.lastIndexOf('/') + 1);

        TransferBean jsonResponse = target.path("moneytransfer/" + transferID).request().get(TransferBean.class);
        assertEquals(Long.parseLong(transferID), jsonResponse.getId());
        assertEquals(transferBean.getSenderName(), jsonResponse.getSenderName());
        assertEquals(transferBean.getAmount(), jsonResponse.getAmount());
    }

    @Test
    public void testDeleteTransfer() {
        TransferBean transferBean = new TransferBean();
        transferBean.setSenderName("Sender1");
        transferBean.setAmount(1234);
        Response response = target.path("moneytransfer").request().post(Entity.entity(transferBean, MediaType.APPLICATION_JSON));
        String location = response.getHeaderString("Location");
        String firstTransferID = location.substring(location.lastIndexOf('/') + 1);

        transferBean = new TransferBean();
        transferBean.setSenderName("Sender2");
        transferBean.setAmount(1234);
        response = target.path("moneytransfer").request().post(Entity.entity(transferBean, MediaType.APPLICATION_JSON));
        location = response.getHeaderString("Location");
        String secondTransferID = location.substring(location.lastIndexOf('/') + 1);

        transferBean = new TransferBean();
        transferBean.setSenderName("Sender3");
        transferBean.setAmount(1234);
        response = target.path("moneytransfer").request().post(Entity.entity(transferBean, MediaType.APPLICATION_JSON));
        location = response.getHeaderString("Location");
        String thirdTransferID = location.substring(location.lastIndexOf('/') + 1);

        target.path("moneytransfer/" + firstTransferID).request().delete();
        TransferBean jsonResponse = target.path("moneytransfer/" + firstTransferID).request().get(TransferBean.class);
        assertNull(jsonResponse);

        target.path("moneytransfer/" + secondTransferID).request().delete();
        jsonResponse = target.path("moneytransfer/" + secondTransferID).request().get(TransferBean.class);
        assertNull(jsonResponse);

        target.path("moneytransfer/" + thirdTransferID).request().delete();
        jsonResponse = target.path("moneytransfer/" + thirdTransferID).request().get(TransferBean.class);
        assertNull(jsonResponse);
    }

    @Test
    public void testCleanAll() {
        TransferBean transferBean = new TransferBean();
        transferBean.setSenderName("Sender1");
        transferBean.setAmount(1234);
        Response response = target.path("moneytransfer").request().post(Entity.entity(transferBean, MediaType.APPLICATION_JSON));
        String location = response.getHeaderString("Location");
        String firstTransferID = location.substring(location.lastIndexOf('/') + 1);

        transferBean = new TransferBean();
        transferBean.setSenderName("Sender2");
        transferBean.setAmount(1234);
        response = target.path("moneytransfer").request().post(Entity.entity(transferBean, MediaType.APPLICATION_JSON));
        location = response.getHeaderString("Location");
        String secondTransferID = location.substring(location.lastIndexOf('/') + 1);

        transferBean = new TransferBean();
        transferBean.setSenderName("Sender3");
        transferBean.setAmount(1234);
        response = target.path("moneytransfer").request().post(Entity.entity(transferBean, MediaType.APPLICATION_JSON));
        location = response.getHeaderString("Location");
        String thirdTransferID = location.substring(location.lastIndexOf('/') + 1);

        target.path("moneytransfer").request().delete();

        TransferBean jsonResponse = target.path("moneytransfer/" + firstTransferID).request().get(TransferBean.class);
        assertNull(jsonResponse);

        jsonResponse = target.path("moneytransfer/" + secondTransferID).request().get(TransferBean.class);
        assertNull(jsonResponse);

        jsonResponse = target.path("moneytransfer/" + thirdTransferID).request().get(TransferBean.class);
        assertNull(jsonResponse);
    }

    @Test
    public void testGetAllTransfers() {

        target.path("moneytransfer").request().delete();

        TransferBean transferBean = new TransferBean();
        transferBean.setSenderName("Sender1");
        transferBean.setAmount(1234);
        Response response = target.path("moneytransfer").request().post(Entity.entity(transferBean, MediaType.APPLICATION_JSON));
        String location = response.getHeaderString("Location");
        String firstTransferID = location.substring(location.lastIndexOf('/') + 1);

        transferBean = new TransferBean();
        transferBean.setSenderName("Sender2");
        transferBean.setAmount(1234);
        response = target.path("moneytransfer").request().post(Entity.entity(transferBean, MediaType.APPLICATION_JSON));
        location = response.getHeaderString("Location");
        String secondTransferID = location.substring(location.lastIndexOf('/') + 1);

        transferBean = new TransferBean();
        transferBean.setSenderName("Sender3");
        transferBean.setAmount(1234);
        response = target.path("moneytransfer").request().post(Entity.entity(transferBean, MediaType.APPLICATION_JSON));
        location = response.getHeaderString("Location");
        String thirdTransferID = location.substring(location.lastIndexOf('/') + 1);

        List<TransferBean> jsonResponse = target.path("moneytransfer").request().get(Response.class).readEntity(new GenericType<List<TransferBean>>() {
        });
        final List<Long> idList = Arrays.asList(Long.parseLong(firstTransferID), Long.parseLong(secondTransferID), Long.parseLong(thirdTransferID));
        for(TransferBean item: jsonResponse) {
            assertTrue(idList.contains(item.getId()));
        }
    }
}
