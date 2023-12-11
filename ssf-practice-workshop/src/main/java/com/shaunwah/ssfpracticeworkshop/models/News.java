package com.shaunwah.ssfpracticeworkshop.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class News {
    private String title;
    private String urlToImage;
    private String author;
    private String description;
    private String publishedAt;
    private String articleUrl;
}
