package com.dcmall.back.Service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class WebCrawlerService {
    public Document scrapeWebPageWithSelenium(String url) {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/driver/chromedriver.exe"); // Chromedriver 경로 설정

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // 헤드리스 모드로 실행
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(url);

        String pageSource = driver.getPageSource();
        driver.quit();

        return Jsoup.parse(pageSource);
    }

    public Elements getDivElementsByClass(Document document, String className) {
        return document.select("div." + className);
    }

    public void QuasaCrawling() {
        String url = "https://www.quasarzone.com/bbs/qb_saleinfo";
        Document document = scrapeWebPageWithSelenium(url);
        Elements elements =  getDivElementsByClass(document, "market-type-list");

        for(Element e : elements){
            System.out.println(e.text());
        }
    }
}
