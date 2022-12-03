package rest;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;

public class RestMethods {

    public RestMethods() {
    }

    public JSONArray getAll(String endpointURL) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpUriRequest request = RequestBuilder.get()
                    .setUri(endpointURL)
                    .build();
            HttpResponse response = httpclient.execute(request);
            String responseString = new BasicResponseHandler().handleResponse(response);
            return new JSONArray(responseString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject getOne(String endpointURL) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpUriRequest request = RequestBuilder.get()
                    .setUri(endpointURL)
                    .build();
            HttpResponse response = httpclient.execute(request);
            String responseString = new BasicResponseHandler().handleResponse(response);
            return new JSONObject(responseString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject create(String username, String password, String email, String type) {
        JSONObject obj = new JSONObject();
        obj.put("username", username);
        obj.put("password", password);
        obj.put("type", type);
        if (type.equals("MODERATOR")) {
            obj.put("email", email);
            obj.put("moderatorID", 0);
        } else if (type.equals("ADMINISTRATOR")) {
            obj.put("AdministratorID", 0);
        } else if (type.equals("CUSTOMER")){
            obj.put("email", email);
            obj.put("customerID", 0);
            obj.put("isActive", false);
            obj.put("reservations", new JSONArray());
        } else {
            throw new IllegalArgumentException("Cannot match type " + type + "!");
        }
        return obj;
    }

    public void putCustomer(String username, String password, String email, String type, String endpointURL) {
        if (username == null || username.equals("")) {
            return;
        }
        JSONObject obj = this.create(username, password, email, type);
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpUriRequest request = RequestBuilder.put()
                    .setUri(endpointURL)
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .setEntity(new StringEntity(obj.toString()))
                    .build();
            httpclient.execute(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(JSONObject customer, String endpointURL) {
        if (customer == null) {
            return;
        }
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpUriRequest request = RequestBuilder.put()
                    .setUri(endpointURL)
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .setEntity(new StringEntity(customer.toString()))
                    .build();
            httpclient.execute(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void put(String endpointURL) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpUriRequest request = RequestBuilder.put()
                    .setUri(endpointURL)
                    .build();
            httpclient.execute(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createReservation(String startDate, String endDate, Integer customer, Integer product, String endpointURL) {
        if (customer == null || product == null || startDate == null || startDate.equals("")) {
            return;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("reservationID", 1);
        jsonObject.put("startDate", startDate);
        jsonObject.put("endDate", endDate);
        jsonObject.put("customer", customer);
        jsonObject.put("product", product);
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpUriRequest request = RequestBuilder.put()
                    .setUri(endpointURL)
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .setEntity(new StringEntity(jsonObject.toString()))
                    .build();
            httpclient.execute(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createProduct(double price, String endpointURL) {
        if (price == 0) {
            return;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("productID", 1);
        jsonObject.put("price", price);
        jsonObject.put("reservations", new JSONArray());
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpUriRequest request = RequestBuilder.put()
                    .setUri(endpointURL)
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .setEntity(new StringEntity(jsonObject.toString()))
                    .build();
            httpclient.execute(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String endpointURL) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpUriRequest request = RequestBuilder.delete()
                    .setUri(endpointURL)
                    .build();
            httpclient.execute(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONArray findByUsername(String name, String endpointURL) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpUriRequest request = RequestBuilder.get()
                    .setUri(endpointURL)
                    .addParameter("username", name)
                    .build();
            HttpResponse response = httpclient.execute(request);
            String responseString = new BasicResponseHandler().handleResponse(response);
            return new JSONArray(responseString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
