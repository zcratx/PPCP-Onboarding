package springexample;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
public class CheckoutControllerNew {

    //private BraintreeGateway gateway = Application.gateway;

    @RequestMapping(value = "/normalize", method = RequestMethod.GET)
    public String normalize(Model model) {

        //TODO: Process the JSON, create the HTML and redirect to the HTML page
        //model.addAttribute("clientToken", clientToken);

        return "html/report";
    }
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String root(Model model) {
    	return "redirect:normalize";
    }
    

    
}
