package springexample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;


@Controller
public class NormalizeController {

    //private BraintreeGateway gateway = Application.gateway;

    @RequestMapping(value = "/normalize", method = RequestMethod.GET)
    public String normalize(Model model) {

       // redirect to the HTML page that will show the JSON
       // display a button to Normalize
       // show the normalized JSON in an HTML format

       //load the file post.json
        File configFile = new File("post.json");
        String jsonFileContent = "";
        String jsonFileContentBeautified = "";
        Object json = null;

        if(configFile.exists() && !configFile.isDirectory()) {
            jsonFileContent = PPCPVisualizerFactory.fromConfigFile(configFile);
        }

        // loading the Jackson Object mapper to beautify the JSON
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            json = objectMapper.readValue(jsonFileContent, Object.class);
            jsonFileContentBeautified = objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(json);
        } catch(Exception jpe) {
            System.out.println("Problem converting JSON text to JSON Object");
            jsonFileContentBeautified = jsonFileContent;
        }

        // load the file report.html
        File reportFile = new File("report.html");
        String reportFileContent = "";

        if(reportFile.exists() && !reportFile.isDirectory()) {
            reportFileContent = PPCPVisualizerFactory.fromConfigFile(reportFile);
        }

        model.addAttribute("JSONFILE", jsonFileContentBeautified);

        model.addAttribute("REPORTFILE", reportFileContent);

        return "checkouts/new";
    }
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String root(Model model) {
    	return "redirect:normalize";
    }
    

    
}
