package org.opcfoundation.uawebservicetest;

import org.opcfoundation.webapi.ApiClient;
import org.opcfoundation.webapi.clientapi.DefaultApi;
import org.opcfoundation.webapi.model.DataValue;
import org.opcfoundation.webapi.model.ReadRequest;
import org.opcfoundation.webapi.model.ReadResponse;
import org.opcfoundation.webapi.model.ReadValueId;

public class TestClient {
    public static void main(String[] args) {
        ApiClient apiClient = new ApiClient();
        apiClient.setHost("dingyan3");
        apiClient.setPort(4840);
        apiClient.setBasePath("/Server-1");


        DefaultApi api = new DefaultApi(apiClient);

        ReadValueId nodeToRead = new ReadValueId();
        nodeToRead.setNodeId("i=1");
        nodeToRead.setAttributeId(1L);

        ReadRequest readRequest = new ReadRequest();
        readRequest.addNodesToReadItem(nodeToRead);

        try
        {
            System.out.println("Reading from " + apiClient.getBaseUri());
            ReadResponse response = api.read(readRequest).get();

            for (DataValue item: response.getResults())
            {
                System.out.println(item.getValue());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
