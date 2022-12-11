package net.devnguyen.remitanomanager.feign.request;

import lombok.Data;

import java.util.List;

@Data
public class GetVideoYoutubeRequest {
    private String key = "AIzaSyAbOzhelLQv-HKWWKdJIN1VVzKumDeT99A";
    private List<String> part = List.of("snippet", "contentDetails", "statistics", "statistics");
    private List<String> id;

    public GetVideoYoutubeRequest(List<String> id) {
        this.id = id;
    }
}
