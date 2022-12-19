package com.isvora.moviereviewer.services;

import com.isvora.moviereviewer.model.Rating;
import com.isvora.moviereviewer.type.Platform;
import com.isvora.moviereviewer.type.Source;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

@Service
public class ImdbService {

    private static final String IMDB = "https://www.imdb.com/";
    private static final String IMDB_DIV_SEARCH_BAR_XPATH = "//div[@class='sc-crrsfI iDhzRL searchform__inputContainer']";
    private static final String IMDB_DIV_SEARCH_BAR_ID = "imdbHeader__search-menu";
    private static final String IMDB_RATING_SPAN_XPATH = "//span[@class='sc-7ab21ed2-1 jGRxWM']";
    private static final String IMDB_UL_XPATH = "//ul[@class='react-autosuggest__suggestions-list anim-enter-done']";
    private static final int TIMEOUT_PERIOD = 5;

    private final WebDriver webDriver;
    private final WebDriverWait wait;

    public ImdbService() {
        this.webDriver = getWebDriver();
        this.wait = new WebDriverWait(webDriver, TIMEOUT_PERIOD);
    }

    public WebDriver getWebDriver() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        var options = new ChromeOptions();
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36");
        options.addArguments("window-size=1920,1080");
        options.setHeadless(true);
        return new ChromeDriver(options);
    }

    public Rating searchRating(String movie) {
        searchImdbForMovie(movie);
        return new Rating(findMovieScore(), Platform.IMDB, movie, Source.AUDIENCE);
    }

    private void searchImdbForMovie(String movie) {
        webDriver.get(IMDB);
        var searchBar = webDriver.findElement(By.xpath(IMDB_DIV_SEARCH_BAR_XPATH))
                .findElement(By.tagName("div"))
                .findElement(By.tagName("input"));
        searchBar.sendKeys(movie);
    }

    private double findMovieScore() {
        var searchBarResult = webDriver.findElement(By.id(IMDB_DIV_SEARCH_BAR_ID))
                .findElement(By.tagName("div"));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(IMDB_UL_XPATH)));

        var searchBarResults = searchBarResult
                .findElement(By.tagName("ul"))
                .findElements(By.tagName("li"));
        searchBarResults.get(0).findElement(By.tagName("a")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(IMDB_RATING_SPAN_XPATH)));

        var score = webDriver.findElement(By.xpath(IMDB_RATING_SPAN_XPATH)).getText();
        return Double.parseDouble(score);
    }

}
