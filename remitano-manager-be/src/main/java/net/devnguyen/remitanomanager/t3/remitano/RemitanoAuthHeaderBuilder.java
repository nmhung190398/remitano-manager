package net.devnguyen.remitanomanager.t3.remitano;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.servlet.function.ServerRequest;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class RemitanoAuthHeaderBuilder {
    private final String secretKey;
    private final String accessKey;

    static final Base64 base64 = new Base64();

    static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter
            .ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH).withZone(ZoneId.of("GMT"));

    public RemitanoAuthHeaderBuilder(String secretKey, String accessKey) {
        this.secretKey = secretKey;
        this.accessKey = accessKey;
    }


    public LinkedMultiValueMap<String, String> build(HttpMethod method, String url, String data) {
        var headers = new LinkedMultiValueMap<String, String>();
        var md5Data = hashMD5(data);
        var date = dateTimeFormatter.format(Instant.now());

        String authString = String.join(",", method.name(), "application/json", md5Data, url, date);
        System.out.println(authString);
        var authorization = "APIAuth " + this.accessKey + ":" + computeHmac(authString);

        headers.add("Content-Type", "application/json");
        headers.add("Content-MD5", md5Data);
        headers.add("date", date);
        headers.add("Authorization", authorization);
        return headers;
    }

    private String hashMD5(String data) {
        return new String(base64.encode(DigestUtils
                .md5(data)));
    }


    private String computeHmac(String data) {
        return new String(base64.encode(new HmacUtils(HmacAlgorithms.HMAC_SHA_1.getName(), secretKey).hmac(data)));
    }

}
