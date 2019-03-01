package com.johndoeo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.johndoeo.bean.City;
import com.johndoeo.bean.Job;
import com.johndoeo.bean.Province;
import com.johndoeo.utils.DownloadFromNetUtil;
import com.johndoeo.utils.JDBCutil;
import com.johndoeo.utils.JSONUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BossSpider {

    public static void addCode(List<String> ls, String code) {

    }

    public static List<String> getCityCOde() {
        DownloadFromNetUtil.downloadHttpUrl("https://www.zhipin.com/common/data/city.json","E:\\chromeDriver\\","city.json");
        //https://www.zhipin.com/common/data/city.json
        String file = "E:\\chromeDriver\\city.json";
        final String s = JSONUtil.readFile(file);
        final String data = JSONObject.parseObject(s, Map.class).get("data").toString();
        final String locationCity = JSONObject.parseObject(data, Map.class).get("locationCity").toString();
        final String cityList = JSONObject.parseObject(data, Map.class).get("cityList").toString();
        final String hotCityList = JSONObject.parseObject(data, Map.class).get("hotCityList").toString();
        Province provinces1 = JSONObject.parseObject(locationCity, Province.class);
        List<Province> provinces2 = JSONArray.parseArray(cityList, Province.class);
        List<Province> provinces3 = JSONArray.parseArray(hotCityList, Province.class);

        List<String> cityCodeList = new ArrayList<>();
        if (provinces1 != null && provinces1.getCode() != null && !provinces1.getCode().trim().equals("")) {
            cityCodeList.add(provinces1.getCode());
        }
        if (provinces2 != null && provinces2.size() > 0) {
            provinces2.forEach(e -> {
                List<City> cities = e.getSubLevelModelList();
                if (cities != null && cities.size() > 0) {
                    cities.stream().forEach(f -> {
                        if (!cityCodeList.contains(f.getCode())) {
                            String code = f.getCode();
                            //城市代码后三位不全为0才加入list
                            final String substring = code.substring(code.length() - 3);
                            if (!substring.equals("000")) {
                                cityCodeList.add(code);
                            }
                        }
                    });
                }
                if (!cityCodeList.contains(e.getCode())) {
                    String code = e.getCode();
                    //城市代码后三位不全为0才加入list
                    final String substring = code.substring(code.length() - 3);
                    if (!substring.equals("000")) {
                        cityCodeList.add(code);
                    }
                }
            });
        }

        if (provinces3 != null && provinces3.size() > 0) {
            provinces3.forEach(e -> {
                List<City> cities = e.getSubLevelModelList();
                if (cities != null && cities.size() > 0) {
                    cities.stream().forEach(f -> {
                        if (!cityCodeList.contains(f.getCode())) {
                            String code = f.getCode();
                            //城市代码后三位不全为0才加入list
                            final String substring = code.substring(code.length() - 3);
                            if (!substring.equals("000")) {
                                cityCodeList.add(code);
                            }
                        }
                    });
                }
                if (!cityCodeList.contains(e.getCode())) {
                    String code = e.getCode();
                    //城市代码后三位不全为0才加入list
                    final String substring = code.substring(code.length() - 3);
                    if (!substring.equals("000")) {
                        cityCodeList.add(code);
                    }
                }
            });
        }
        return cityCodeList;
    }

    @org.junit.Test
    public void test1() {
        final List<String> cityCOde = getCityCOde();
        //获取boss上定义的各个城市的code的json文件，然后解析
//        DownloadFromNetUtil.downloadHttpUrl("https://www.zhipin.com/common/data/city.json","E:\\chromeDriver\\","city.json");
        int pageNum = 1;
        for (String e : cityCOde) {
            int i = 1;
            while (true) {
                System.out.println("开始爬取第" + pageNum + "页数据，请稍等。。。");
                final Document document = getDocument("https://www.zhipin.com/c" + e + "/?query=java&page=" + i + "&ka=page-" + i);
                parse(document);
                System.out.println("第" + pageNum + "页数据存储完毕");
                pageNum++;
                i++;
                Element select = document.select("div.page").first();
                //检验是否有下一页
                if (select == null) {
                    break;
                }
                final String ka = select.children().get(select.children().size() - 2).attr("ka");
                if (ka != null && ka.equals("page-cur")) {
                    break;
                }
            }
        }
    }

    /**
     * 后台运行浏览器，向下滑动
     *
     * @param url
     * @return
     */
    public static Document getDocument(String url) {
        Document doc = null;
        //可使用的浏览器有：IE浏览器（webdriver.ie.driver）
        //火狐浏览器  (webdriver.gecko.driver)
        //谷歌浏览器 (webdriver.chrome.driver)
        //                  是使用那个浏览器                            chromedriver所在的位置
        System.setProperty("webdriver.chrome.driver", "D:\\chorme\\chromedriver\\chromedriver.exe");

        // InternetExplorerDriver()   浏览器
        // FirefoxDriver()            火狐浏览器


        final ChromeOptions options = new ChromeOptions();


        WebDriver driver = null;

        //浏览器后台打开
        options.addArguments("headless");

        //谷歌浏览器
        driver = new ChromeDriver(options);
        driver.get(url);

        //等待几秒
        try {

            //向下滚动  方法一
            //JavascriptExecutor js = (JavascriptExecutor)driver;
            //js.executeScript("scrollTo(0,20000)");

            //向下滚动 方法二
            ((JavascriptExecutor) driver).executeScript("scrollTo(0,10000)");
            //线程睡眠20秒，等待爬取数据
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        doc = Jsoup.parse(driver.getPageSource());

        //关闭浏览器
        driver.close();
        driver.quit();

        return doc;
    }

    /**
     * 解析传过来的doc
     *
     * @param doc
     */
    public static void parse(Document doc) {
        if (doc == null) {
            System.out.println("doc is null, unable to continue! ");
            return;
        }
        Elements content = doc.select("div.job-primary");

        //System.out.println(select);
        for (Element element : content) {
            //获取职位名称
            String jobTitle = element.select("div.info-primary").select("h3.name").select("a").select("div.job-title").text();

            //获取职位薪资
            String salary = element.select("div.info-primary").select("h3.name").select("a").select("span.red").text();

            //获取公司
            String company = element.select("div.info-company").select("div.company-text").select("h3.name").select("a").text();
            String companyUrl = "https://www.zhipin.com" + element.select("div.info-company").select("div.company-text").select("h3.name").select("a").attr("href");


            //获取hr
            String hrName = element.select("div.info-publis").select("h3.name").text();

            //获取工作地址
            String address = element.select("div.info-primary").select("p").text();

            //专业
            String profession = element.select("div.info-company").select("div.company-text").select("p").text();

            //发布日期
            String publishDate = element.select("div.info-publis").select("p").text();

            System.out.println("jobTitle: " + jobTitle);
            System.out.println("salary: " + salary);
            System.out.println("company:  " + company);
            System.out.println("companyUrl: " + companyUrl);
            System.out.println("hrName: " + hrName);
            System.out.println("address: " + address);
            System.out.println("profession: " + profession);
            System.out.println("publishDate: " + publishDate);
            System.out.println("--------------\n");
            Job p = new Job(jobTitle, salary, company, companyUrl, hrName, address, profession, publishDate);
            saveInDB(p);
        }
    }

    public static boolean saveInDB(Job job) {
        final Connection connection = JDBCutil.getConnection();
        try {
            String sql = "insert into job(job_title,salary,company,company_url,hr_name,address,profession,publish_date,create_time)values(?,?,?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, job.getJobTitle());
            preparedStatement.setString(2, job.getSalary());
            preparedStatement.setString(3, job.getCompany());
            preparedStatement.setString(4, job.getCompanyUrl());
            preparedStatement.setString(5, job.getHrName());
            preparedStatement.setString(6, job.getAddress());
            preparedStatement.setString(7, job.getProfession());
            preparedStatement.setString(8, job.getPublishDate());
            preparedStatement.setString(9, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            return preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
