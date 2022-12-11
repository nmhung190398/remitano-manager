package net.devnguyen.remitanomanager.t3.remitano;


import com.fasterxml.jackson.databind.ObjectMapper;
import net.devnguyen.remitanomanager.dto.RemitanoOffer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class RemitanoClient {
    private final RestTemplate restTemplate;
    final ObjectMapper mapper = new ObjectMapper();
    private final String URL = "https://api.remitano.com";

    private final String secretKey;
    private final String accessKey;

    private final ExecutorService executorService = Executors.newFixedThreadPool(200);

    public RemitanoClient(String secretKey, String accessKey) {
        this.secretKey = secretKey;
        this.accessKey = accessKey;
        this.restTemplate = new RestTemplate();
    }

    public Map enableOffer(String offerId) {
        return request("/api/v1/my_offers/" + offerId + "/enable", HttpMethod.PUT, null, Map.class);
    }

    public Map disableOffer(String offerId) {
        return request("/api/v1/my_offers/" + offerId + "/disable", HttpMethod.PUT, null, Map.class);
    }

    public Map updateOffer(String offerId, Object offer) {
        return request("/api/v1/my_offers/" + offerId, HttpMethod.PUT, offer, Map.class);
    }

    public Map earningSummary(){
        return get("/api/v1/my_offers/earning_summary?coin_currency=vndr",Map.class);
    }

    public Map getOfferEdit(String offerId) {
        return get("/api/v1/my_offers/" + offerId + "/edit", Map.class);
    }

    public Map me() {
        return get("/api/v1/users/me", Map.class);
    }

    public Map meVndr() {
        return get("/api/v1/fiat_accounts/me?currency=vnd", Map.class);
    }

    public List<Object> getOffers() {
        var buys = (List) get("/api/v1/my_offers?offer_type=buy&offline=false&page=1&offer_scope=my", Map.class).get("my_offers");
        var sells = (List) get("/api/v1/my_offers?offer_type=sell&offline=false&page=1&offer_scope=my", Map.class).get("my_offers");


        var result = new ArrayList<>();
        result.addAll(buys);
        result.addAll(sells);

        return result;
    }

    private <T> T get(String uri, Class<T> rClass) {
        return request(uri, HttpMethod.GET, null, rClass);
    }

    private <T> T request(String uri, HttpMethod httpMethod, Object body, Class<T> rClass) {
        try {
            MultiValueMap<String, String> headers = new RemitanoAuthHeaderBuilder(secretKey, accessKey)
                    .build(httpMethod, uri, body == null ? "" : mapper.writeValueAsString(body));
            System.out.println(
                    mapper.writeValueAsString(headers));
            String url = URL + uri;

            var httpEntity = new HttpEntity<>(body, headers);

            var resp = restTemplate.exchange(url, httpMethod, httpEntity, rClass);
            if (resp.getStatusCode().is2xxSuccessful()) {
                return resp.getBody();
            }
            throw new RuntimeException("api error");

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static void main(String[] args) throws Exception {
        var access_key = "62b05600f82b63c5b012189c03428e7230c0085945dc403974fb42ab044b8155";
        var secret_key = "9u0WJnNbkSgCbPx84xvS6mkH9WdboJFfhhndjDJ4JuBX3jj3VniqK+DNKmpl1HhRFdrNidC4q/DrWDEtVJ+Obg==";
        var client = new RemitanoClient(secret_key, access_key);
//        var data = client.get("/api/v1/fiat_accounts/me?currency=vnd");
        var data = client.enableOffer("1848746");
        System.out.println();

        /*
        {
  'Content-Type': 'application/json',
  'Content-MD5': '1B2M2Y8AsgTpgAmY7PhCfg==',
  date: 'Thu, 08 Dec 2022 13:16:06 GMT',
  Authorization: '
  APIAuth 62b05600f82b63c5b012189c03428e7230c0085945dc403974fb42ab044b8155:jHxB3ZbE2bCl6qdJAjhrHMX57V4='
  APIAuth 62b05600f82b63c5b012189c03428e7230c0085945dc403974fb42ab044b8155:8ac5b34c4cd9025e06c3d0742c6562c7aa7a8454
  APIAuth 62b05600f82b63c5b012189c03428e7230c0085945dc403974fb42ab044b8155:15425f2bf6117caacdda5141af5376f49af720db
}

         */
    }
}
