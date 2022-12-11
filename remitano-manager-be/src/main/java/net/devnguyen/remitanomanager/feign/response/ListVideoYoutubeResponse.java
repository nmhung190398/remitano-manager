package net.devnguyen.remitanomanager.feign.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class ListVideoYoutubeResponse {
    public String kind;
    public String etag;
    public ArrayList<Item> items;
    public PageInfo pageInfo;


    public static class ContentDetails {
        public String duration;
        public String dimension;
        public String definition;
        public String caption;
        public boolean licensedContent;
        public String projection;
    }

    public static class Default {
        public String url;
        public int width;
        public int height;
    }

    public static class High {
        public String url;
        public int width;
        public int height;
    }

    public static class Item {
        public String kind;
        public String etag;
        public String id;
        public Snippet snippet;
        public ContentDetails contentDetails;
        public Statistics statistics;
        public TopicDetails topicDetails;
    }

    public static class Localized {
        public String title;
        public String description;
    }

    public static class Maxres {
        public String url;
        public int width;
        public int height;
    }

    public static class Medium {
        public String url;
        public int width;
        public int height;
    }

    public static class PageInfo {
        public int totalResults;
        public int resultsPerPage;
    }

    public static class Snippet {
        //        public Instant publishedAt;
        public String channelId;
        public String title;
        public String description;
        public Thumbnails thumbnails;
        public String channelTitle;
        public ArrayList<String> tags;
        public String categoryId;
        public String liveBroadcastContent;
        public Localized localized;
        public String defaultAudioLanguage;
    }

    public static class Standard {
        public String url;
        public int width;
        public int height;
    }

    public static class Statistics {
        public String viewCount;
        public String likeCount;
        public String favoriteCount;
        public String commentCount;
    }

    public static class Thumbnails {
        @JsonProperty("default")
        public Default mydefault;
        public Medium medium;
        public High high;
        public Standard standard;
        public Maxres maxres;
    }

    public static class TopicDetails {
        public ArrayList<String> topicCategories;
    }

}
