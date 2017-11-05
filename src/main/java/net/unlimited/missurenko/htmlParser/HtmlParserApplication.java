package net.unlimited.missurenko.htmlParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SpringBootApplication
@Controller
public class HtmlParserApplication {

    @GetMapping("/alert")
    @ResponseBody
    public String alert() {
        return "I am alive";
    }

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/*")
    @ResponseBody
    public String proxy(HttpServletRequest request, HttpServletResponse response,
                        @RequestBody String str) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (requestURI.equalsIgnoreCase("/ACTION=GETSTATUS")) {
            String url = "http://" + request.getRemoteHost() + ":7000" + requestURI;
            String res = restTemplate.postForObject(url, str, String.class);
            return res;

        }
//        request.getHeaders();
        System.out.println("Hi");
        return "";
    }


    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(HtmlParserApplication.class, args);
    }
}
