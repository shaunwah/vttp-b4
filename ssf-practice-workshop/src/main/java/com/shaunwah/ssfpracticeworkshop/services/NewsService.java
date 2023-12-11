package com.shaunwah.ssfpracticeworkshop.services;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.shaunwah.ssfpracticeworkshop.models.News;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

@Service
public class NewsService {
    @Value("${news.api.key}")
    private String apiKey;

    public List<String> getCategories() {
        String[] categories = {"business", "entertainment", "general", "health", "science", "sports", "technology"};
        return List.of(categories);
    }

    public Map<String, String> getCountries() {
        Map<String, String> countries = new HashMap<>();

        String url = UriComponentsBuilder
            .fromUriString("https://restcountries.com/v3.1/alpha")
            .queryParam("codes", "ae,ar,at,au,be,bg,br,ca,ch,cn,co,cu,cz,de,eg,fr,gb,gr,hk,hu,id,ie,il,in,it,jp,kr,lt,lv,ma,mx,my,ng,nl,no,nz,ph,pl,pt,ro,rs,ru,sa,se,sg,si,sk,th,tr,tw,ua,us,ve,za")
            .build()
            .toString();

        RequestEntity<Void> req = RequestEntity.get(url)
            .accept(MediaType.APPLICATION_JSON)
            .build();

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> res = restTemplate.exchange(req, String.class);

        JsonReader jr = Json.createReader(new StringReader(res.getBody()));

        for (JsonValue val : jr.readArray()) {
            JsonObject obj = val.asJsonObject();
            String countryCode = obj.getString("cca2");
            String country = obj.getJsonObject("name").getString("common");
            countries.put(countryCode, country);
        }

        return countries;
    }

    public List<News> getNews(String category, String country) {
        List<News> news = new LinkedList<>();

        String url = UriComponentsBuilder
            .fromUriString("https://newsapi.org/v2/top-headlines")
            .queryParam("country", country)
            .queryParam("category", category)
            .queryParam("apiKey", apiKey)
            .build()
            .toString();

        RequestEntity<Void> req = RequestEntity.get(url)
            .accept(MediaType.APPLICATION_JSON)
            .build();

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> res = restTemplate.exchange(req, String.class);

        JsonReader jr = Json.createReader(new StringReader(res.getBody()));
        JsonArray ja = jr.readObject().getJsonArray("articles");

        for (JsonValue val : ja) {
            JsonObject obj = val.asJsonObject();
            String title = obj.getString("title", "");
            String urlToImage = obj.getString("urlToImage", "");
            String author = obj.getString("author", "");
            String description = obj.getString("description", "");
            String publishedAt = obj.getString("publishedAt", "");
            String articleUrl = obj.getString("url", "");
            news.add(new News(title, urlToImage, author, description, publishedAt, articleUrl));
        }

        return news;
    }
}
