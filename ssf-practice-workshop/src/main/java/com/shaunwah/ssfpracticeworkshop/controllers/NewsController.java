package com.shaunwah.ssfpracticeworkshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shaunwah.ssfpracticeworkshop.services.NewsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/")
public class NewsController {
    @Autowired
    private NewsService newsService;

    @GetMapping("/")
    public String getHome(Model model) {
        model.addAttribute("categories", newsService.getCategories());
        model.addAttribute("countries", newsService.getCountries());
        return "home";
    }

    @GetMapping("/news")
    public String getNews(@RequestParam String category, @RequestParam String country, Model model) {
        model.addAttribute("news", newsService.getNews(category, country));
        return "news";
    }
    
    
}
