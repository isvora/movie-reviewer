package com.isvora.moviereviewer.services;

import com.isvora.moviereviewer.model.Rating;
import com.netflix.dgs.codegen.generated.types.ReviewInput;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public abstract class ScraperService {

    public WebDriver getWebDriver() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        var options = new ChromeOptions();
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36");
        options.addArguments("window-size=1920,1080");
        options.setHeadless(true);
        return new ChromeDriver(options);
    }

    public abstract Rating searchRating(String movie);
}
