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

import javax.naming.AuthenticationException;
import java.io.IOException;

public class RestClient {

    public RestClient() {
    }

    public JSONArray getAll(String endpointURL, String jwt) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            RequestBuilder requestBuilder = RequestBuilder.get()
                    .setUri(endpointURL);
            if (!jwt.equals("")) {
                requestBuilder.setHeader("Authorization", "Bearer " +  jwt);
            }
            HttpUriRequest request = requestBuilder.build();
            HttpResponse response = httpclient.execute(request);
            String responseString = new BasicResponseHandler().handleResponse(response);
            return new JSONArray(responseString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject getOne(String endpointURL, String jwt) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            RequestBuilder requestBuilder = RequestBuilder.get()
                    .setUri(endpointURL);
            if (!jwt.equals("")) {
                requestBuilder.setHeader("Authorization", "Bearer " +  jwt);
            }
            HttpUriRequest request = requestBuilder.build();
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

    public void putCustomer(String username, String password, String email, String type, String endpointURL, String jwt) {
        if (username == null || username.equals("")) {
            return;
        }
        JSONObject obj = this.create(username, password, email, type);
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            RequestBuilder requestBuilder = RequestBuilder.put()
                    .setUri(endpointURL)
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .setEntity(new StringEntity(obj.toString()));
            if (!jwt.equals("")) {
                requestBuilder.setHeader("Authorization", "Bearer " +  jwt);
            }
            HttpUriRequest request = requestBuilder.build();
            httpclient.execute(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(JSONObject customer, String endpointURL, String jwt) {
        if (customer == null) {
            return;
        }
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            RequestBuilder requestBuilder = RequestBuilder.put()
                    .setUri(endpointURL)
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .setEntity(new StringEntity(customer.toString()));
            if (!jwt.equals("")) {
                requestBuilder.setHeader("Authorization", "Bearer " +  jwt);
            }
            HttpUriRequest request = requestBuilder.build();
            httpclient.execute(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void put(String endpointURL, String jwt) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            RequestBuilder requestBuilder = RequestBuilder.put().setUri(endpointURL);
            if (!jwt.equals("")) {
                requestBuilder.setHeader("Authorization", "Bearer " +  jwt);
            }
            HttpUriRequest request = requestBuilder.build();
            httpclient.execute(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createReservation(String startDate, String endDate, Integer customer, Integer product, String endpointURL, String jwt) {
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
            RequestBuilder requestBuilder = RequestBuilder.put()
                    .setUri(endpointURL)
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .setEntity(new StringEntity(jsonObject.toString()));
            if (!jwt.equals("")) {
                requestBuilder.setHeader("Authorization", "Bearer " +  jwt);
            }
            HttpUriRequest request = requestBuilder.build();
            httpclient.execute(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createProduct(double price, String endpointURL, String jwt) {
        if (price == 0) {
            return;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("productID", 1);
        jsonObject.put("price", price);
        jsonObject.put("reservations", new JSONArray());
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            RequestBuilder requestBuilder = RequestBuilder.put()
                    .setUri(endpointURL)
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .setEntity(new StringEntity(jsonObject.toString()));
            if (!jwt.equals("")) {
                requestBuilder.setHeader("Authorization", "Bearer " +  jwt);
            }
            HttpUriRequest request = requestBuilder.build();
            httpclient.execute(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String endpointURL, String jwt) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            RequestBuilder requestBuilder = RequestBuilder.delete().setUri(endpointURL);
            if (!jwt.equals("")) {
                requestBuilder.setHeader("Authorization", "Bearer " +  jwt);
            }

            HttpUriRequest request = requestBuilder.build();
            httpclient.execute(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONArray findByUsername(String name, String endpointURL, String jwt) {

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            RequestBuilder requestBuilder = RequestBuilder.get()
                    .setUri(endpointURL)
                    .addParameter("username", name);
            if (!jwt.equals("")) {
                requestBuilder.setHeader("Authorization", "Bearer " +  jwt);
            }
            HttpUriRequest request = requestBuilder.build();
            HttpResponse response = httpclient.execute(request);
            String responseString = new BasicResponseHandler().handleResponse(response);
            return new JSONArray(responseString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String login(String username, String password, String endpointURL) throws AuthenticationException {
        JSONObject object = new JSONObject();
        object.put("username", username);
        object.put("password", password);
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpUriRequest request = RequestBuilder.post()
                    .setUri(endpointURL)
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .setEntity(new StringEntity(object.toString()))
                    .build();
            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 401) {
                throw new AuthenticationException();
            }
            return new BasicResponseHandler().handleResponse(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
