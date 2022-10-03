package springexample;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class NormalizeController {

    //private BraintreeGateway gateway = Application.gateway;

    @RequestMapping(value = "/normalize", method = RequestMethod.GET)
    public String normalize(Model model) {

       // redirect to the HTML page that will show the JSON
       // display a button to Normalize
       // show the normalized JSON in an HTML format

        return "checkouts/new";
    }
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String root(Model model) {
    	return "redirect:normalize";
    }
    

    
}
