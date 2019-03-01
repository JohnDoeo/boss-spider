package com.johndoeo.weibo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**参考地址：https://blog.csdn.net/yin__ren/article/details/79416508
 * @Auther: JohnDoeo
 * @Date: 2019/3/1 22:58
 * @Description:
 */
public class Login {

    private static ChromeDriverService service;
    private static WebDriver webDriver;

    /**
     * 创建一个浏览器实例
     *
     * @return　webDriver
     */

    public WebDriver getChromeDriver() {
        System.setProperty("webdriver.chrome.driver", "D:\\chorme\\chromedriver\\chromedriver.exe");
        //创建一个　ChromeDriver 接口
        service = new ChromeDriverService.Builder().usingDriverExecutable(new File("D:\\chorme\\chromedriver\\chromedriver.exe")).usingAnyFreePort().build();
        try {
            service.start();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ChromeDriverService启动异常");
        }
        //创建一个　chrome 浏览器实例
        return new RemoteWebDriver(service.getUrl(), DesiredCapabilities.chrome());
    }

    /**
     * 模拟新浪微博登录
     *
     * @param name     　用户名
     * @param password 　密码
     */
    public void login(String name, String password) {
        webDriver = getChromeDriver();
        webDriver.get("http://login.sina.com.cn/");
        WebElement elementName = webDriver.findElement(By.name("username"));
        elementName.sendKeys(name);
        WebElement elementPassword = webDriver.findElement(By.name("password"));
        elementPassword.sendKeys(password);
        WebElement elementClick = webDriver.findElement(By.xpath("//*[@id=\"vForm\"]/div[2]/div/ul/li[7]/div[1]/input"));
        elementClick.click();
    }

    /**
     * 进行爬取
     *
     * @param key 　用于获取正确的微博链接：http://s.weibo.com/weibo/%25E9%2598%259A%25E6%25B8%2585%25E5%25AD%2590%25E5%259B%259E%25E5%25BA%2594%25E5%2588%2586%25E6%2589%258B%25E4%25BC%25A0%25E9%2597%25BB
     */
    public void search(String key) {
        webDriver.get("http://s.weibo.com/");
        WebElement elementKey = webDriver.findElement(By.className("searchInp_form"));
        elementKey.sendKeys(key);
        WebElement elementClick = webDriver.findElement(By.className("searchBtn"));
        elementClick.click();

        //搜索特定日期的微博内容
        LocalDate localDate = LocalDate.now();
        String currentUrl = webDriver.getCurrentUrl().split("&")[0];
        System.out.println("currentUrl: " + currentUrl);
        String url = currentUrl + "&typeall=1&suball=1×cope=custom:" + localDate + ":&Refer=g";
        webDriver.get(url);

        //处理当前页面内容
        handlePage();
    }

    //页面处理
    public void handlePage() {
        while (true) {
            //sleep的作用是对付微博的反爬虫机制，抓取太快可能会判定为机器人，需要输入验证码
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //先判断是否有内容
            if (checkContent()) {
                getContent();
                //判断是否有下一页按钮
                if (checkButton()) {
                    //拿到下一页按钮
                    WebElement elementButton = webDriver.findElement(By.xpath("//a[@class='page next S_txt1 S_line1']"));
                    elementButton.click();
                } else {
                    System.out.println("没有下一页");
                    break;
                }
            } else {
                System.out.println("内容搜索完毕");
                break;
            }

        }
    }

    /**
     * 检查页面是否还有内容
     *
     * @return
     */
    public Boolean checkContent() {
        boolean flag;
        try {
            webDriver.findElement(By.xpath("//div[@class='pl_noresult']"));
            flag = false;
        } catch (Exception e) {
            flag = true;
        }
        return flag;
    }

    /**
     * 检查是否有下一页
     *
     * @return
     */
    public Boolean checkButton() {
        boolean flag;
        try {
            webDriver.findElement(By.xpath("//a[@class='page next S_txt1 S_line1']"));
            flag = true;
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }


    public void getContent() {
        List<WebElement> elementNodes = webDriver.findElements(By.xpath("//div[@class='WB_cardwrap S_bg2 clearfix']"));
        //在运行过程中微博数==0的情况，可能是微博反爬机制，需要输入验证码
        if (elementNodes == null) {
            String url = webDriver.getCurrentUrl();
            webDriver.get(url);
            getContent();
            return;
        }
        for (WebElement element : elementNodes) {
            String bz_name = element.findElement(By.xpath(".//div[@class='feed_content wbcon']/a[@class='W_texta W_fb']")).getText();
            System.out.println("博主昵称：　" + bz_name);

            String bz_homePage = element.findElement(By.xpath(".//div[@class='feed_content wbcon']/a[@class='W_texta W_fb']")).getAttribute("href");
            System.out.println("博主主页：　" + bz_homePage);

            String wb_approve;
            try {
                wb_approve = element.findElement(By.xpath(".//div[@class='feed_content wbcon']/a[@class='approve_co']")).getAttribute("title");
            } catch (Exception e) {
                wb_approve = "";
            }
            System.out.println("微博认证：　" + wb_approve);

            String wb_intelligent;
            try {
                wb_intelligent = element.findElement(By.xpath(".//div[@class='feed_content wbcon']/a[@class='ico_club']")).getAttribute("title");
            } catch (Exception e) {
                wb_intelligent = "";
            }
            System.out.println("微博达人：　" + wb_intelligent);

            String wb_content;
            try {
                wb_content = element.findElement(By.xpath(".//div[@class='feed_content wbcon']/p[@class='comment_txt']")).getText();
            } catch (Exception e) {
                wb_content = "";
            }
            System.out.println("微博内容：　" + wb_content);

            String publishTime;
            try {
                publishTime = element.findElement(By.xpath(".//div[@class='feed_from W_textb']/a[@class='W_textb']")).getText();
            } catch (Exception e) {
                publishTime = "";
            }
            System.out.println("发布时间：　" + publishTime);

            String wb_address;
            try {
                wb_address = element.findElement(By.xpath(".//div[@class='feed_from W_textb']/a[@class='W_textb']")).getAttribute("href");
            } catch (Exception e) {
                wb_address = "";
            }
            System.out.println("微博地址：　" + wb_address);

            String wb_source;
            try {
                wb_source = element.findElement(By.xpath(".//div[@class='feed_from W_textb']/a[@rel]")).getText();
            } catch (Exception e) {
                wb_source = "";
            }
            System.out.println("微博来源：　" + wb_source);

            String transmitText;
            int transmitNum = 0;
            try {
                transmitText = element.findElement(By.xpath(".//a[@action-type='feed_list_forward']//em")).getText();
                transmitNum = Integer.parseInt(transmitText);
            } catch (Exception e) {

            }
            System.out.println("转发次数：　" + transmitNum);

            int commentNum = 0;
            try {
                String commentText = element.findElement(By.xpath(".//a[@action-type='feed_list_comment']//em")).getText();
                commentNum = Integer.parseInt(commentText);
            } catch (Exception e) {

            }
            System.out.println("评论次数：　" + commentNum);

            int praiseNum = 0;
            try {
                String praiseText = element.findElement(By.xpath(".//a[@action-type='feed_list_like']//em")).getText();
                praiseNum = Integer.parseInt(praiseText);
            } catch (Exception e) {

            }
            System.out.println("点赞次数：　" + praiseNum);

            System.out.print("-----------------------------------------------------------");
            System.out.println();
        }
    }
    public static void main(String[] args) {
        Login login = new Login();
        login.login("18829348962", "dhy199409155476");
        login.search("安吉吃火锅");
    }
}


