package cli;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Scanner;
import java.lang.String;


public class App {
    private static final String URL = "http://localhost:8080/rest/mercuryBase";

    public static void main(String[] args) {
        Client client = Client.create();

        Scanner in = new Scanner(System.in);
        while(true) {
            System.out.println("Select action: 1 - gather selenium, 2 - get base status, 3 - create battery, 4 - repair battery, 5 - save SPD robot");
            int action = in.nextInt();
            try
            {
                switch (action)
                {
                    case 1:
                        System.out.println("Enter required selenium number: ");
                        int seleniumNumber = in.nextInt();
                        gatherSelenium(client, seleniumNumber);
                        break;
                    case 2:
                        getBaseStatus(client);
                        break;
                    case 3:
                        createBattery(client);
                        break;
                    case 4:
                        System.out.println("Enter battery number: ");
                        int batteryNumber = in.nextInt();
                        repairBattery(client, batteryNumber);
                        break;
                    case 5:
                        System.out.println("Enter SPD id: ");
                        int id = in.nextInt();
                        saveRobot(client, id);
                        break;
                }
            }
            catch (Exception e)
            {
                System.out.println("Received error: " + e.getMessage());
            }
        }
    }

    public static void gatherSelenium(Client client, int seleniumNumber){
        WebResource webResource = client.resource(URL.concat("/seleniumOrder"));
        webResource = webResource.queryParam("requiredSeleniumNumber", Integer.toString(seleniumNumber));

        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class);

        System.out.println(response.getClientResponseStatus());
    }

    public static void getBaseStatus(Client client){
        WebResource webResource = client.resource(URL);

        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

        String output = response.getEntity(String.class);
        System.out.println(output.replace(",","\n"));
    }

    public static void createBattery(Client client){
        WebResource webResource = client.resource(URL.concat("/createBatteryOrder"));

        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class);

        System.out.println(response.getClientResponseStatus());
    }

    public static void repairBattery(Client client, int batteryNumber){
        WebResource webResource = client.resource(URL.concat("/repairBatteryOrder"));
        webResource = webResource.queryParam("BatteryNumber", Integer.toString(batteryNumber));

        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class);

        System.out.println(response.getClientResponseStatus());
    }

    public static void saveRobot(Client client, int id){
        WebResource webResource = client.resource(URL.concat("/saveRobotOrder"));
        webResource = webResource.queryParam("SPDIndex", Integer.toString(id));

        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class);

        System.out.println(response.getClientResponseStatus());
    }
}
