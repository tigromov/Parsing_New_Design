package org.example;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;


public class Main {
    public static void main(String[] args) throws InterruptedException, IOException, NoSuchElementException {

        ///////////////////////////ВРЕМЯ//////////////////////////////////////////////////////////////////

        ZonedDateTime start = ZonedDateTime.now();


        String time = start.toString();
        time = time.replace(":","-");
        time = time.replace("T"," ");
        time = time.substring(0,19);


        ///////////////////////////////АДРЕС ФАЙЛА ВЫГРУЗКИ////////////////////////////////////////////////////////////////////
        String ParsOutput = "C:/Rest/Parsing/Parsing " + time + ".xls";





        //////////////////////////////////////////////////////////СОЗДАНИЕ ФАЙЛА EXEL//////////////////////////////////////////
        Workbook midWb = new HSSFWorkbook();
        Sheet midSheet = midWb.createSheet();
        FileOutputStream fos = new FileOutputStream(ParsOutput);





        //////////////////////////////////////////////////////////ЗАПУСК CHROME DRIVER////////////////////////////////////////

        System.setProperty("webdriver.chrome.driver", "selenium\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        WebDriver webDriver = new ChromeDriver(options);
        webDriver.manage().window().maximize();



        ////////////////////////////////////////////////////////АВТОРИЗАЦИЯ В ЛИЧНОМ КАБИНЕТЕ////////////////////////////////
        webDriver.get("https://kaspi.kz/mc/");

        Thread.sleep(5000);
        webDriver.findElement(By.xpath("//*[@id=\"app\"]/div/div/div/div/form/div[1]/div/div/section/div/nav/ul/li[2]/a/span")).click();
        Thread.sleep(2000);
        webDriver.findElement(By.id("user_email")).sendKeys("zakaz@invask.kz");
        webDriver.findElement(By.xpath("//*[@id=\"app\"]/div/div/div/div/form/div[2]/div/button")).click();
        webDriver.findElement(By.xpath("//*[@id=\"app\"]/div/div/div/div/form/div[1]/div/div/div[1]/input")).sendKeys("!ZakaZ2022" + Keys.ENTER);




        /////////////////////////////////////////////////////ПЕРЕХОД НА СТРАНИЦУ ТОВАРОВ//////////////////////////////////////////////////
        Thread.sleep(1000);
        webDriver.get("https://kaspi.kz/mc/#/products");
        Thread.sleep(10000);


        /////////////////////////////////////////////////////ПАРСИНГ СТРАНИЦЫ ТОВАРОВ//////////////////////////////////////////

        int prodCounter = 0;
        int prodCounterTotal = 0;
        Document doc = Jsoup.parse(webDriver.getPageSource());
       // System.out.println(doc);
        String numberOfPages = doc.getElementsByClass("pagination-list").text();

        int productsListSize = Integer.parseInt(numberOfPages.substring(6, 8));
        System.out.println("Всего страниц с товарами в каспи магазине: " + productsListSize);



       /////////////////////////////////////////////ЦИКЛ ПО СТРАНИЦАМ ТОВАРОВ/////////////////////////////////////////

       //for (int i = 0; i < productsListSize; i++) {

            ////////////////////////////////////////////////////ПОЛУЧЕНИЕ АРТИКУЛА/////////////////////////////////////////////////
            Elements productsDescriptions = doc.getElementsByClass("subtitle is-6");
            ArrayList skusList = new ArrayList();
            for (Element element : productsDescriptions) {
                String productDescription = element.text();
               // System.out.println(productDescription); тест
                List worldsInLineOfProductDescriptionList = Arrays.asList(productDescription.split(" "));
                int numberOFWorlds = worldsInLineOfProductDescriptionList.size();
                String article = (String) worldsInLineOfProductDescriptionList.get(numberOFWorlds - 1);
                skusList.add(article);
            }
            int skusListSize = skusList.size();

            System.out.println("Получены артикулы: " + skusList);

            ///////////////////////////////////////////////Запись 10ти артикулов в файл//////////////////////////////////////
             for (int j = 0; j < skusListSize; j++) {
                midSheet.createRow(prodCounterTotal + 1 + j).createCell(0).setCellValue((String) skusList.get(j));
                    }


          ////////////////////////////////////////////////////переход по ссылкам///////////////////////////////////////////////

        Elements links = doc.select(".is-5").not(".subtitle");



        //////////////////////////////////////////////////////парсинг страницы товара////////////////////////////////
        for (Element link: links) {
            webDriver.get(link.getElementsByAttribute("href").attr("href"));
            Thread.sleep(3000);

           try{ WebElement buttonClose = webDriver.findElement(By.xpath("//*[@id=\"dialogService\"]/div/div[1]/div[1]/div/ul[1]/li[11]/a"));
            buttonClose.click();}catch (org.openqa.selenium.NoSuchElementException e){}

            Document productPageDoс = Jsoup.parse(webDriver.getPageSource());
            Elements priceEl = productPageDoс.getElementsByClass("sellers-table__price-cell-text");
            //System.out.println(priceEl);
            String firstPrice = priceEl.first().text();
            System.out.println(firstPrice);





        }





















        System.out.println(prodCounter);
     //  }   ///открыть скобку общего главного цикла



        ///////////////////////////////////////////////////////////ЗАПИCЬ В ФАЙЛ //////////////////////////////////////////////
        midWb.write(fos);
        fos.close();
        System.out.println("Файл выгрузился по адресу: " + ParsOutput);
        //webDriver.close();


        ///////////////////////////////////////////////////////////ВРЕМЯ ВЫПОЛНЕНИЯ ПРОГРАММЫ//////////////////////////////////
        ZonedDateTime finish = ZonedDateTime.now();
        double elapsed = (double) Duration.between(start,finish).toMinutes();
        System.out.println("Время выполнения программы: " + elapsed + " минут");





    }
}
