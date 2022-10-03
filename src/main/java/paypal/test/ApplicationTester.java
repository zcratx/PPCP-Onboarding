package paypal.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import paypal.business.BusinessOrchestrator;

import paypal.com.paypal.business.exception.OnboardingBusinessException;
import paypal.onboarding.JSONInterpreter;
import paypal.reportgenerator.ReportGenerator;



public class ApplicationTester {

    private void test() {
        JSONInterpreter jsonInterpreter = JSONInterpreter.getJSONInterpreter();

        ObjectMapper objectMapper = jsonInterpreter.getObjectMapper();

        try {

            //System.out.println("The classloader is "+getClass().getClassLoader().getResource("JSON/post.json"));
            String filePath = getClass().getClassLoader().getResource("JSON/post.json").getPath();
            System.out.println("The POST.JSON file path is "+ filePath);
            String reportFilePath = getClass().getClassLoader().getResource("templates.html/").getPath();
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

    /*
    public static void main(String args[]) {
        ApplicationTester appTester = new ApplicationTester();
        appTester.test();
    }

     */
}
