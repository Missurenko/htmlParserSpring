package net.unlimited.missurenko.htmlParser;

import net.unlimited.missurenko.htmlParser.parser.dto.AllInformationForTaskDto;
import net.unlimited.missurenko.htmlParser.parser.service.FileReadWrite;
import net.unlimited.missurenko.htmlParser.parser.service.impl.FileReadWriteImpl;
import net.unlimited.missurenko.htmlParser.parser.service.mainWork.ConfigAnalise;
import net.unlimited.missurenko.htmlParser.parser.service.mainWork.Spliter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.SAXException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

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

    @Value("${cfs.host}")
    private String CFSHost;
    @Value("${cfs.port}")
    private String CFSPort;

    private List<AllInformationForTaskDto> infoListAboutTask;

    @RequestMapping(value = "/*")
    @ResponseBody
    public String proxy(HttpServletRequest request, HttpServletResponse response,
                        @RequestBody String str) throws ServletException, IOException, ParserConfigurationException, SAXException {
        String requestURI = request.getRequestURI();
        System.out.println("console log - " + requestURI);
        Spliter spliter = new Spliter(str, infoListAboutTask);
        String resultBySpliter = spliter.splitByPart();
        if (requestURI.equalsIgnoreCase("/ACTION=INGEST")) {
            String url = "http://" + CFSHost + ":" + CFSPort + requestURI;
            String res = restTemplate.postForObject(url, str, String.class);
            System.out.println("log Ingest");
            return res;
        } else {
            String url = "http://" + CFSHost + ":" + CFSPort + requestURI;
            String res = restTemplate.postForObject(url, str, String.class);
            return res;
        }
    }


    @Bean
    public RestTemplate restTemplate() {

        RestTemplate restTemplate = new RestTemplate();

        FileReadWrite fileReadWrite = new FileReadWriteImpl();
        List<String> configsWebConnector = fileReadWrite.webConnectorConfigByLines();
        ConfigAnalise configAnalise = new ConfigAnalise();
        infoListAboutTask = configAnalise.parceConfigCFG(configsWebConnector);
        infoListAboutTask.remove(0);
        return restTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(HtmlParserApplication.class, args);
    }
}
