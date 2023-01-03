package rest;

import org.apache.http.*;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URI;

public class RestClient {

    public RestClient() {
    }

    public CloseableHttpClient createHttpClientCustom() {

        String[] methods = new String[]{"GET", "POST", "HEAD", "DELETE", "PUT"};
        return HttpClients.custom().setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).setRedirectStrategy(new DefaultRedirectStrategy(methods) {

            @Override
            public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
                final URI uri = getLocationURI(request, response, context);
                final String method = request.getRequestLine().getMethod();
                if (method.equalsIgnoreCase(HttpHead.METHOD_NAME)) {
                    return new HttpHead(uri);
                } else if (method.equalsIgnoreCase(HttpGet.METHOD_NAME)) {
                    return new HttpGet(uri);
                } else {
                    final int status = response.getStatusLine().getStatusCode();
                    if (status == HttpStatus.SC_TEMPORARY_REDIRECT || status == HttpStatus.SC_MOVED_TEMPORARILY) { //HttpStatus.SC_MOVED_TEMPORARILY == 302
                        return RequestBuilder.copy(request).setUri(uri).build();
                    } else {
                        return new HttpGet(uri);
                    }
                }
            }
        }).build();
    }

    public JSONArray getAll(String endpointURL, String jwt) {
        try (CloseableHttpClient httpClient = this.createHttpClientCustom()) {
            RequestBuilder requestBuilder = RequestBuilder.get()
                    .setUri(endpointURL);
            if (!jwt.equals("")) {
                requestBuilder.setHeader("Authorization", "Bearer " +  jwt);
            }
            HttpUriRequest request = requestBuilder.build();
            HttpResponse response = httpClient.execute(request);
            String responseString = new BasicResponseHandler().handleResponse(response);
            return new JSONArray(responseString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpResponse getOne(String endpointURL, String jwt) {
        try (CloseableHttpClient httpClient = this.createHttpClientCustom()) {
            RequestBuilder requestBuilder = RequestBuilder.get()
                    .setUri(endpointURL);
            if (!jwt.equals("")) {
                requestBuilder.setHeader("Authorization", "Bearer " +  jwt);
            }
            HttpUriRequest request = requestBuilder.build();
            HttpResponse response = httpClient.execute(request);
            return response;
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

    public void putCustomer(String username, String password, String email, String type, String endpointURL, String jwt) throws Exception {
        if (username == null || username.equals("")) {
            return;
        }
        JSONObject obj = this.create(username, password, email, type);
        try (CloseableHttpClient httpClient = this.createHttpClientCustom()) {
            RequestBuilder requestBuilder = RequestBuilder.put()
                    .setUri(endpointURL)
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .setEntity(new StringEntity(obj.toString()));
            if (!jwt.equals("")) {
                requestBuilder.setHeader("Authorization", "Bearer " +  jwt);
            }
            HttpUriRequest request = requestBuilder.build();
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == 400) {
                throw new Exception("Username already exist!");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(JSONObject customer, String endpointURL, String jwt, String jws) throws Exception{
        if (customer == null) {
            return;
        }
        try (CloseableHttpClient httpClient = this.createHttpClientCustom()) {
            RequestBuilder requestBuilder = RequestBuilder.put()
                    .setUri(endpointURL)
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .setEntity(new StringEntity(customer.toString()));
            if (!jwt.equals("")) {
                requestBuilder.setHeader("Authorization", "Bearer " +  jwt);
            }
            if (!jws.equals("")) {
                requestBuilder.setHeader("If-Match", jws);
            }
            HttpUriRequest request = requestBuilder.build();
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == 400) {
                throw new Exception("JWS hasn't been provided or is invalid!");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void put(String endpointURL, String jwt) {
        try (CloseableHttpClient httpClient = this.createHttpClientCustom()) {
            RequestBuilder requestBuilder = RequestBuilder.put().setUri(endpointURL);
            if (!jwt.equals("")) {
                requestBuilder.setHeader("Authorization", "Bearer " +  jwt);
            }
            HttpUriRequest request = requestBuilder.build();
            httpClient.execute(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createReservation(String startDate, String endDate, Integer product, String endpointURL, String jwt) {
        if (product == null || startDate == null || startDate.equals("")) {
            return;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("reservationID", 1);
        jsonObject.put("startDate", startDate);
        jsonObject.put("endDate", endDate);
        jsonObject.put("product", product);
        try (CloseableHttpClient httpClient = this.createHttpClientCustom()) {
            RequestBuilder requestBuilder = RequestBuilder.put()
                    .setUri(endpointURL)
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .setEntity(new StringEntity(jsonObject.toString()));
            if (!jwt.equals("")) {
                requestBuilder.setHeader("Authorization", "Bearer " +  jwt);
            }
            HttpUriRequest request = requestBuilder.build();
            httpClient.execute(request);
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
        try (CloseableHttpClient httpClient = this.createHttpClientCustom()) {
            RequestBuilder requestBuilder = RequestBuilder.put()
                    .setUri(endpointURL)
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .setEntity(new StringEntity(jsonObject.toString()));
            if (!jwt.equals("")) {
                requestBuilder.setHeader("Authorization", "Bearer " +  jwt);
            }
            HttpUriRequest request = requestBuilder.build();
            httpClient.execute(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String endpointURL, String jwt) {
        try (CloseableHttpClient httpClient = this.createHttpClientCustom()) {
            RequestBuilder requestBuilder = RequestBuilder.delete().setUri(endpointURL);
            if (!jwt.equals("")) {
                requestBuilder.setHeader("Authorization", "Bearer " +  jwt);
            }

            HttpUriRequest request = requestBuilder.build();
            httpClient.execute(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONArray findByUsername(String name, String endpointURL, String jwt) {

        try (CloseableHttpClient httpClient = this.createHttpClientCustom()) {
            RequestBuilder requestBuilder = RequestBuilder.get()
                    .setUri(endpointURL)
                    .addParameter("username", name);
            if (!jwt.equals("")) {
                requestBuilder.setHeader("Authorization", "Bearer " +  jwt);
            }
            HttpUriRequest request = requestBuilder.build();
            HttpResponse response = httpClient.execute(request);
            String responseString = new BasicResponseHandler().handleResponse(response);
            return new JSONArray(responseString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String login(String username, String password, String endpointURL) throws Exception {
        JSONObject object = new JSONObject();
        object.put("username", username);
        object.put("password", password);
        try (CloseableHttpClient httpClient = this.createHttpClientCustom()) {
            HttpUriRequest request = RequestBuilder.post()
                    .setUri(endpointURL)
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .setEntity(new StringEntity(object.toString()))
                    .build();
            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            switch (statusCode) {
                case 401 -> throw new Exception("Wrong credentials!");
                case 400 -> throw new Exception("Inactive account!");
            }
            return new BasicResponseHandler().handleResponse(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void changePassword(String oldPassword, String newPassword, String endpointURL, String jwt) throws Exception {
        JSONObject object = new JSONObject();
        object.put("oldPassword", oldPassword);
        object.put("newPassword", newPassword);
        try (CloseableHttpClient httpClient = this.createHttpClientCustom()) {
            RequestBuilder requestBuilder = RequestBuilder.put()
                    .setUri(endpointURL)
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .setEntity(new StringEntity(object.toString()));
            if (!jwt.equals("")) {
                requestBuilder.setHeader("Authorization", "Bearer " +  jwt);
            }
            HttpUriRequest request = requestBuilder.build();
            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 400) {
                throw new Exception("Old password is wrong!");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getJws(String endpointURL, String jwt) {
        try (CloseableHttpClient httpClient = this.createHttpClientCustom()) {
            RequestBuilder requestBuilder = RequestBuilder.get()
                    .setUri(endpointURL);
            if (!jwt.equals("")) {
                requestBuilder.setHeader("Authorization", "Bearer " +  jwt);
            }
            HttpUriRequest request = requestBuilder.build();
            HttpResponse response = httpClient.execute(request);
            return response.getFirstHeader("ETag").getValue();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
