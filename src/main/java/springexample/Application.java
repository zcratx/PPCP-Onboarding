package springexample;

import java.io.File;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import paypal.business.BusinessOrchestrator;
import paypal.com.paypal.business.exception.OnboardingBusinessException;
import paypal.onboarding.JSONInterpreter;
import paypal.reportgenerator.ReportGenerator;

@SpringBootApplication

public class Application {
    public static String DEFAULT_JSON_FILENAME = "post.json";

    private void test() {
        JSONInterpreter jsonInterpreter = JSONInterpreter.getJSONInterpreter();

        ObjectMapper objectMapper = jsonInterpreter.getObjectMapper();

        try {


            String filePath = DEFAULT_JSON_FILENAME;
            System.out.println("The POST.JSON file path is "+ filePath);
            String reportFilePath = "";
            System.out.println("The Report.JSON file path is "+ filePath);

            // load the JSON
            BusinessOrchestrator.loadJSON(filePath, objectMapper);

            // once the JSON is loaded, create the HTML report
            ReportGenerator.generateReport(reportFilePath);

        } catch(OnboardingBusinessException obe) {
            if(obe.getStackTrace().length > 0) {
                StackTraceElement[] ste = obe.getStackTrace();
                System.out.println(ste[0]);
            }
        }
    }

    public static void main(String[] args) {
        /*File configFile = new File(DEFAULT_CONFIG_FILENAME);
        try {
            if(configFile.exists() && !configFile.isDirectory()) {
                gateway = BraintreeGatewayFactory.fromConfigFile(configFile);
            } else {
                gateway = BraintreeGatewayFactory.fromConfigMapping(System.getenv());
            }
        } catch (NullPointerException e) {
            System.err.println("Could not load Braintree configuration from config file or system environment.");
            System.exit(1);
        } */

        Application appTester = new Application();
        appTester.test();

        System.out.println("Application file is called and it is our new invoker");
        SpringApplication.run(Application.class, args);
    }
}
