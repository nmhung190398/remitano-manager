package net.devnguyen.remitanomanager.service.auth;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.Person;
import net.devnguyen.remitanomanager.dto.auth.GoogleDTO;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GoogleService {
    private final String GOOGLE_CLIENT_ID;
    private final String GOOGLE_CLIENT_SECRET;

    private final String GOOGLE_REDIRECT_URI;
    private final String GOOGLE_GRANT_TYPE;

    private final String GOOGLE_LINK_GET_TOKEN;
    private final String GOOGLE_LINK_GET_USER_INFO;

    public GoogleService(OAuthGoogleProperty properties) {
        this.GOOGLE_CLIENT_ID = properties.getGoogleClientId();
        this.GOOGLE_CLIENT_SECRET = properties.getGoogleClientSecret();
        this.GOOGLE_REDIRECT_URI = properties.getGoogleRedirectUri();
        this.GOOGLE_GRANT_TYPE = properties.getGoogleGrantType();
        this.GOOGLE_LINK_GET_TOKEN = properties.getGoogleLinkGetToken();
        this.GOOGLE_LINK_GET_USER_INFO = properties.getGoogleLinkGetUserInfo();
    }

    public String getToken(final String code) throws IOException {
        String response = Request.Post(GOOGLE_LINK_GET_TOKEN)
                .bodyForm(Form.form().add("client_id", GOOGLE_CLIENT_ID)
                        .add("client_secret", GOOGLE_CLIENT_SECRET)
                        .add("redirect_uri", GOOGLE_REDIRECT_URI).add("code", code)
                        .add("grant_type", GOOGLE_GRANT_TYPE).build())
                .execute()
                .returnContent()
                .asString();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(response).get("access_token");

        return node.textValue();
    }

    public GoogleDTO getUserInfo(final String accessToken) throws IOException {
        String link = GOOGLE_LINK_GET_USER_INFO + accessToken;
        String response = Request.Get(link).execute().returnContent().asString();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response, GoogleDTO.class);
    }

    public GoogleDTO getUserInfoMobile(final String accessToken) throws IOException {
        String link = "https://oauth2.googleapis.com/tokeninfo?id_token=" + accessToken;
        String response = Request.Get(link).execute().returnContent().asString();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response, GoogleDTO.class);
    }

    public Person getPeopleInfo(final String accessToken) throws IOException {
        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();

        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setClientSecrets(GOOGLE_CLIENT_ID, GOOGLE_CLIENT_SECRET)
                .build()
                .setAccessToken(accessToken);

        PeopleService peopleService =
                new PeopleService.Builder(httpTransport, jsonFactory, credential).build();

        return peopleService.people().get("people/me")
                .setPersonFields("phoneNumbers,genders,birthdays,addresses")
                .execute();
    }
}
