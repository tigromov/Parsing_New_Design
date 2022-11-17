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
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {

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

        Thread.sleep(1000);
        webDriver.findElement(By.xpath("//*[@id=\"app\"]/div/div/div/div/form/div[1]/div/div/section/div/nav/ul/li[2]/a/span")).click();
        webDriver.findElement(By.id("user_email")).sendKeys("zakaz@invask.kz");
        webDriver.findElement(By.xpath("//*[@id=\"app\"]/div/div/div/div/form/div[2]/div/button")).click();
        webDriver.findElement(By.xpath("//*[@id=\"app\"]/div/div/div/div/form/div[1]/div/div/div[1]/input")).sendKeys("!ZakaZ2022" + Keys.ENTER);




        /////////////////////////////////////////////////////ПЕРЕХОД НА СТРАНИЦУ ТОВАРОВ//////////////////////////////////////////////////
        Thread.sleep(1000);
        webDriver.get("https://kaspi.kz/mc/#/products");
        Thread.sleep(10000);


        /////////////////////////////////////////////////////ПАРСИНГ СТРАНИЦЫ ТОВАРОВ//////////////////////////////////////////

        ////////////////////////////////////////////////////ПОЛУЧЕНИЕ АРТИКУЛА/////////////////////////////////////////////////

        Document doc = Jsoup.parse(webDriver.getPageSource());
        Elements page = doc.getElementsByClass("subtitle is-6");
        ArrayList skus = new ArrayList();
        for (Element element :page) {
            String firstProduct = element.text();
            List productStringList = Arrays.asList(firstProduct.split(" "));
            int numberOFWorlds = productStringList.size();
            String articule = (String) productStringList.get(numberOFWorlds - 1);
            skus.add(articule);
        }
        System.out.println(skus);






//        ///////////////////////////////////////////////////////////ЗАПИCЬ В ФАЙЛ //////////////////////////////////////////////
//        midWb.write(fos);
//        fos.close();
//        System.out.println("Файл выгрузился по адресу: " + ParsOutput);
//        //webDriver.close();
//
//
//        ///////////////////////////////////////////////////////////ВРЕМЯ ВЫПОЛНЕНИЯ ПРОГРАММЫ//////////////////////////////////
//        ZonedDateTime finish = ZonedDateTime.now();
//        double elapsed = (double)Duration.between(start,finish).toMinutes();
//        System.out.println("Время выполнения программы: " + elapsed);





    }
}
