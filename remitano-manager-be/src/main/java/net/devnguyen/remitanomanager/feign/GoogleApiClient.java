package net.devnguyen.remitanomanager.feign;

import net.devnguyen.remitanomanager.feign.request.GetVideoYoutubeRequest;
import net.devnguyen.remitanomanager.feign.response.ListVideoYoutubeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "google-api-client",
        url = "https://youtube.googleapis.com/"
)
public interface GoogleApiClient {
    @GetMapping(value = "/youtube/v3/videos")
    ListVideoYoutubeResponse getInfoYoutube(@SpringQueryMap GetVideoYoutubeRequest request);
}
