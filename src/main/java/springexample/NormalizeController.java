package springexample;

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

        if(configFile.exists() && !configFile.isDirectory()) {
            jsonFileContent = PPCPVisualizerFactory.fromConfigFile(configFile);
        }

        // load the file report.html
        File reportFile = new File("report.html");
        String reportFileContent = "";

        if(reportFile.exists() && !reportFile.isDirectory()) {
            reportFileContent = PPCPVisualizerFactory.fromConfigFile(reportFile);
        }

        model.addAttribute("JSONFILE", jsonFileContent);

        model.addAttribute("REPORTFILE", reportFileContent);

        return "checkouts/new";
    }
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String root(Model model) {
    	return "redirect:normalize";
    }
    

    
}
