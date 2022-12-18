package com.isvora.moviereviewer.services;

import com.isvora.moviereviewer.database.ReviewEntity;
import com.isvora.moviereviewer.model.Rating;
import com.isvora.moviereviewer.type.Platform;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ImdbService extends ScraperService {

    private final static String IMDB = "https://www.imdb.com/";
    private final static String IMDB_DIV_SEARCH_BAR_XPATH= "//div[@class='sc-crrsfI iDhzRL searchform__inputContainer']";
    private final static String IMDB_DIV_SEARCH_BAR_ID = "imdbHeader__search-menu";
    private final static String IMDB_RATING_SPAN_XPATH = "//span[@class='sc-7ab21ed2-1 jGRxWM']";
    private final static String IMDB_UL_XPATH = "//ul[@class='react-autosuggest__suggestions-list anim-enter-done']";

    private final WebDriver webDriver;
    private final WebDriverWait wait;

    public ImdbService() {
        this.webDriver = getWebDriver();
        this.wait = new WebDriverWait(webDriver, 5);
    }

    @Override
    public Rating searchRating(String movie) {
        searchImdbForMovie(movie);
        return new Rating(findMovieScore(), Platform.IMDB, movie);
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
