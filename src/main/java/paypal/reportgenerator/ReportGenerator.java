package paypal.reportgenerator;



import paypal.business.BusinessOrchestrator;
import paypal.processing.model.ArrayLevelElementsBDO;
import paypal.processing.model.ContainerLevelElementsBDO;
import paypal.processing.model.TopLevelElementsBDO;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * This is a class that will generate the report
 * For now this only creates HTML report
 *
 */
public class ReportGenerator {

    public static void generateReport(String reportFilePath) {

        // first get the objects from the Business Orchestrator
        ContainerLevelElementsBDO containerLevelElementsBDO = BusinessOrchestrator.getContainerLevelElementsBDO();

        TopLevelElementsBDO topLevelElementsBDO = BusinessOrchestrator.getTopLevelElementsBDO();

        ArrayLevelElementsBDO arrayLevelElementsBDO = BusinessOrchestrator.getArrayLevelElementsBDO();

        // get the TOP LEVEL HTML elements
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append(ReportGeneratorHTMLElements.HTML_OPEN);

        htmlBuilder.append(ReportGeneratorHTMLElements.HEAD_OPEN);
        htmlBuilder.append(ReportGeneratorHTMLElements.HEAD_CLOSE);

        htmlBuilder.append(ReportGeneratorHTMLElements.BODY_OPEN);

        htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_1_OPEN);

        htmlBuilder.append("REPORT GENERATED FOR NON LOGINABLE ACCOUNT (ID :"+topLevelElementsBDO.getAccount_id()+")");

        htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_1_CLOSE);

        htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_2_OPEN);

        htmlBuilder.append("Top Level NLM details are \n");
        htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_2_CLOSE);
        htmlBuilder.append("<table> <tr> <td align=right>");
        htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_3_OPEN+"External ID : "
                +ReportGeneratorHTMLElements.HEADING_3_CLOSE
                +"</td> <td>"
                +topLevelElementsBDO.getExternal_id());
        htmlBuilder.append("</td></tr>");
        htmlBuilder.append("<tr> <td align=right>");
        htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_3_OPEN+"Organization : "+
                ReportGeneratorHTMLElements.HEADING_3_CLOSE+
                "</td> <td>"+
                topLevelElementsBDO.getOrganization());
        htmlBuilder.append("</td></tr>");
        htmlBuilder.append("<tr> <td align=right>");
        htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_3_OPEN+"Primary Currency Code : "+
                ReportGeneratorHTMLElements.HEADING_3_CLOSE+
                "</td> <td>"+
                topLevelElementsBDO.getPrimary_currency_code());
        htmlBuilder.append("</td></tr>");
        htmlBuilder.append("<tr> <td align=right>");
        htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_3_OPEN+"Soft Descriptor : "+
                ReportGeneratorHTMLElements.HEADING_3_CLOSE+
                "</td> <td>"+
                topLevelElementsBDO.getSoft_descriptor());
        htmlBuilder.append("</td></tr></table>");

        // painting Agreement Details
        htmlBuilder.append("<table> <tr> <td>");
        htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_2_OPEN+"Agreement Details"+
                ReportGeneratorHTMLElements.HEADING_2_CLOSE+"</td> </tr>");

        htmlBuilder.append("<tr> <td align=right>Agreement Status :</td> <td><font color=green>"+arrayLevelElementsBDO.getAgreements().get("type")+"</font></td></tr>");
        htmlBuilder.append("<tr> <td align=right>Time :</td> <td>"+arrayLevelElementsBDO.getAgreements().get("accepted_time")+"</td> </tr></table>");

        // this is where you add individual owners attributes
        htmlBuilder.append("<table> <tr colspan=2> <td>");
        htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_2_OPEN+"Individual Owners Details "+
                ReportGeneratorHTMLElements.HEADING_2_CLOSE+"</td> </tr>");

        Map<String, String> individualOwnersDetail = arrayLevelElementsBDO.getIndividual_owners_details();
        Iterator<String> iodIter = individualOwnersDetail.keySet().iterator();

        while(iodIter.hasNext()) {
            String iodKey = iodIter.next();

            String iodValue = individualOwnersDetail.get(iodKey);
            htmlBuilder.append("<tr> <td>");
            htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_3_OPEN + iodKey+
                    ReportGeneratorHTMLElements.HEADING_3_CLOSE +
                    "</td><td>"+
                    iodValue);
            htmlBuilder.append("</td> </tr>");

        }
        htmlBuilder.append("</table>");
        // till here we paint Individual Owners Attributes

        // here we paint business entity details
        htmlBuilder.append("<table> <tr colspan=2> <td>");
        htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_2_OPEN+"Business Entity Details "+
                ReportGeneratorHTMLElements.HEADING_2_CLOSE+"</td> </tr>");

        Map<String, Map<String, String>> businessEntityNamesOuter = containerLevelElementsBDO.getBusiness_entity_names();
        Map<String, String> businessEntityNames = businessEntityNamesOuter.get("business_entity_names");
        Iterator<String> beIter = businessEntityNames.keySet().iterator();

        while(beIter.hasNext()) {
            String beKey = beIter.next();
            String beValue = businessEntityNames.get(beKey);

            htmlBuilder.append("<tr> <td>");
            htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_3_OPEN + beKey+
                    ReportGeneratorHTMLElements.HEADING_3_CLOSE +
                    "</td><td>"+
                    beValue);
            htmlBuilder.append("</td> </tr>");

        }

        // adding business entity type
        htmlBuilder.append("<tr> <td>");
        htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_3_OPEN + "Business Entity Type"+
                ReportGeneratorHTMLElements.HEADING_3_CLOSE +
                "</td><td>"+
                containerLevelElementsBDO.getBusiness_entitytype());
        htmlBuilder.append("</td> </tr>");

        // adding business entity MCC code
        htmlBuilder.append("<tr> <td>");
        htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_3_OPEN + "MCC Code"+
                ReportGeneratorHTMLElements.HEADING_3_CLOSE +
                "</td><td>"+
                containerLevelElementsBDO.getBusiness_entitymerchant_category_code());
        htmlBuilder.append("</td> </tr>");


        // here we paint business entity registered business address
        htmlBuilder.append("<table> <tr colspan=2> <td>");
        htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_2_OPEN+"Entity Reg Address"+
                ReportGeneratorHTMLElements.HEADING_2_CLOSE+"</td> </tr>");

        Map<String, Map<String, String>> businessEntityAddOuter = containerLevelElementsBDO.getBusiness_entity_registered_business_address();
        Map<String, String> businessEntityAdd = businessEntityAddOuter.get("business_entity_registered_business_address");
        Iterator<String> beAddIter = businessEntityAdd.keySet().iterator();

        while(beAddIter.hasNext()) {
            String beKey = beAddIter.next();
            String beValue = businessEntityAdd.get(beKey);

            htmlBuilder.append("<tr> <td>");
            htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_3_OPEN + beKey+
                    ReportGeneratorHTMLElements.HEADING_3_CLOSE +
                    "</td><td>"+
                    beValue);
            htmlBuilder.append("</td> </tr>");

        }

        // here we paint business entity registered business phone number
        htmlBuilder.append("<table> <tr colspan=2> <td>");
        htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_2_OPEN+"Business Entity Phone Details "+
                ReportGeneratorHTMLElements.HEADING_2_CLOSE+"</td> </tr>");

        Map<String, Map<String, String>> businessEntityPhOuter = containerLevelElementsBDO.getBusiness_entity_phone_numbers();
        Map<String, String> businessEntityPh = businessEntityPhOuter.get("business_entity_phone_numbers");
        Iterator<String> bePhIter = businessEntityPh.keySet().iterator();

        while(bePhIter.hasNext()) {
            String beKey = bePhIter.next();
            String beValue = businessEntityPh.get(beKey);

            htmlBuilder.append("<tr> <td>");
            htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_3_OPEN + beKey+
                    ReportGeneratorHTMLElements.HEADING_3_CLOSE +
                    "</td><td>"+
                    beValue);
            htmlBuilder.append("</td> </tr>");

        }


        htmlBuilder.append("</table>");
        // till here we paint business entity details

        // painting capabilities and the respective limits
        htmlBuilder.append("<table> <tr> <td>");
        htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_2_OPEN+"Capabilities " +
                ReportGeneratorHTMLElements.HEADING_2_CLOSE+
                "</td> </tr>");


        Map<String, String> capabilities = arrayLevelElementsBDO.getCapabilities();
        Iterator<String> capabilitiesIter = capabilities.keySet().iterator();

        String capability = "";
        String capabilityStatus = "";
        while(capabilitiesIter.hasNext()) {
            String key = capabilitiesIter.next();
            String value = capabilities.get(key);

            if(key.contains("name")) {
                capability = value;
            } else if(key.contains("status")) {
                capabilityStatus = value;
            }

            if(! (capability.equals("")) && (! capabilityStatus.equals(""))) {
                htmlBuilder.append("<tr> <td align=right>");
                htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_3_OPEN + capability+
                        ReportGeneratorHTMLElements.HEADING_3_CLOSE +
                        "</td><td>"+
                        "<font color=green>" + capabilityStatus + "</font>");
                htmlBuilder.append("</td> </tr>");
                // also painting limits
                if(capability.equals("APPLE_PAY")) {
                    Map<String, String> applePayLimits =  arrayLevelElementsBDO.getAPPLE_PAY_limits();

                    Iterator<String> applePayLimitsIter = applePayLimits.keySet().iterator();

                    if(!applePayLimits.isEmpty()) {

                        htmlBuilder.append("<tr> <td align=right>");
                        htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_3_OPEN + "<font color=RED>LIMITS </font>"+
                                ReportGeneratorHTMLElements.HEADING_3_CLOSE);
                        htmlBuilder.append("</td> </tr>");
                    }
                    while(applePayLimitsIter.hasNext()) {
                        String limitsKey = applePayLimitsIter.next();

                        String limitsValue = applePayLimits.get(limitsKey);
                        htmlBuilder.append("<tr ><td align=right>");
                        htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_3_OPEN + limitsKey+
                                ReportGeneratorHTMLElements.HEADING_3_CLOSE +
                                "</td><td>"+
                                "<font color=red>" + limitsValue + "</font>");
                        htmlBuilder.append("</td> </tr>");
                    }
                } else if(capability.equals("CUSTOM_CARD_PROCESSING")) {
                    Map<String, String> customCardProcessingLimits =  arrayLevelElementsBDO.getCUSTOM_CARD_PROCESSING_limits();

                    Iterator<String> customCardProcessingLimitsIter = customCardProcessingLimits.keySet().iterator();

                    if(!customCardProcessingLimits.isEmpty()) {
                        htmlBuilder.append("<tr > <td align=right>");
                        htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_3_OPEN + "<font color=RED>LIMITS </font>"+
                                ReportGeneratorHTMLElements.HEADING_3_CLOSE);
                        htmlBuilder.append("</td></tr>");
                    }

                    while(customCardProcessingLimitsIter.hasNext()) {
                        String limitsKey = customCardProcessingLimitsIter.next();

                        String limitsValue = customCardProcessingLimits.get(limitsKey);
                        htmlBuilder.append("<tr ><td align=right>");
                        htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_3_OPEN + limitsKey+
                                ReportGeneratorHTMLElements.HEADING_3_CLOSE +
                                "</td><td>"+
                                "<font color=red>" + limitsValue + "</font>");
                        htmlBuilder.append("</td></tr>");
                    }
                } else if(capability.equals("RECEIVE_MONEY")) {
                    Map<String, String> receiveMoneyLimits =  arrayLevelElementsBDO.getRECEIVE_MONEY_limits();

                    Iterator<String> receiveMoneyLimitsIter = receiveMoneyLimits.keySet().iterator();

                    if(!receiveMoneyLimits.isEmpty()) {
                        htmlBuilder.append("<tr> <td align=right>");
                        htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_3_OPEN + "<font color=RED>LIMITS </font>"+
                                ReportGeneratorHTMLElements.HEADING_3_CLOSE);
                        htmlBuilder.append("</td></tr>");
                    }

                    while(receiveMoneyLimitsIter.hasNext()) {
                        String limitsKey = receiveMoneyLimitsIter.next();

                        String limitsValue = receiveMoneyLimits.get(limitsKey);
                        htmlBuilder.append("<tr> <td align=right>");
                        htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_3_OPEN + limitsKey+
                                ReportGeneratorHTMLElements.HEADING_3_CLOSE +
                                "</td><td>"+
                                "<font color=red>" + limitsValue + "</font>");
                        htmlBuilder.append("</td></tr>");
                    }
                } else if(capability.equals("CUSTOM_BANK_PROCESSING")) {
                    Map<String, String> customBankLimits =  arrayLevelElementsBDO.getCUSTOM_BANK_PROCESSING_limits();

                    Iterator<String> customBankLimitsIter = customBankLimits.keySet().iterator();
                    htmlBuilder.append("<tr> <td align=right>");
                    if(!customBankLimits.isEmpty()) {
                        htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_3_OPEN + "<font color=RED>LIMITS </font>"+
                                ReportGeneratorHTMLElements.HEADING_3_CLOSE);
                        htmlBuilder.append("</td></tr>");
                    }

                    while(customBankLimitsIter.hasNext()) {
                        String limitsKey = customBankLimitsIter.next();

                        String limitsValue = customBankLimits.get(limitsKey);
                        htmlBuilder.append("<tr> <td align=right>");
                        htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_3_OPEN + limitsKey+
                                ReportGeneratorHTMLElements.HEADING_3_CLOSE +
                                "</td><td>"+
                                "<font color=red>" + limitsValue + "</font>");
                        htmlBuilder.append("</td></tr>");
                    }
                }


                capability = "";
                capabilityStatus = "";


            }
        }
        htmlBuilder.append("</table>");
        //painting verification checks and reason for limits
        htmlBuilder.append("<table border=0> <tr> <td colspan=2>");
        htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_2_OPEN+"Verification Status"+
                ReportGeneratorHTMLElements.HEADING_2_CLOSE+"</td></tr>");

        Map<String, Map<String,Map<String, String>>> verification = containerLevelElementsBDO.getProcess_view();

        Map<String, String> managedPathKyc = verification.get("process_view").get("MANAGED_PATH_KYC");
        htmlBuilder.append("<tr> <td colspan=2>");
        htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_3_OPEN+"MANAGED_PATH_KYC"+
                ReportGeneratorHTMLElements.HEADING_3_CLOSE+"</td></tr>");
        Iterator<String> managedPathIter= managedPathKyc.keySet().iterator();

        while(managedPathIter.hasNext()) {
            String managedPathKey = managedPathIter.next();
            htmlBuilder.append("<tr> <td>");
            htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_3_OPEN + managedPathKey+
                    ReportGeneratorHTMLElements.HEADING_3_CLOSE +
                    "</td><td>"+
                    "<font color=red>" + managedPathKyc.get(managedPathKey) + "</font>");
            htmlBuilder.append("</td> </tr>");
        }

        Map<String, String> managedPath1099K = verification.get("process_view").get("MANAGED_PATH_1099_K");
        htmlBuilder.append("<tr> <td colspan=2>");
        htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_3_OPEN+"MANAGED_PATH_1099_K"+
                ReportGeneratorHTMLElements.HEADING_3_CLOSE+"</td></tr>");

        Iterator<String> managedPath1099KIter= managedPath1099K.keySet().iterator();

        while(managedPath1099KIter.hasNext()) {
            String managedPath1099KKey = managedPath1099KIter.next();
            htmlBuilder.append("<tr> <td>");
            htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_3_OPEN + managedPath1099KKey+
                    ReportGeneratorHTMLElements.HEADING_3_CLOSE +
                    "</td><td>"+
                    "<font color=red>" + managedPath1099K.get(managedPath1099KKey) + "</font>");
            htmlBuilder.append("</td> </tr>");
        }

        Map<String, String> managedPathBoVerification = verification.get("process_view").get("MANAGED_PATH_BO_VERIFICATION");
        htmlBuilder.append("<tr> <td colspan=2>");
        htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_3_OPEN+"MANAGED_PATH_BO_VERIFICATION"+
                ReportGeneratorHTMLElements.HEADING_3_CLOSE+"</td></tr>");
        Iterator<String> managedPathBoVerificationIter= managedPathBoVerification.keySet().iterator();

        while(managedPathBoVerificationIter.hasNext()) {
            String managedPathBoVerificationKey = managedPathBoVerificationIter.next();
            htmlBuilder.append("<tr> <td>");
            htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_3_OPEN + managedPathBoVerificationKey+
                    ReportGeneratorHTMLElements.HEADING_3_CLOSE +
                    "</td><td>"+
                    "<font color=red>" + managedPathBoVerification.get(managedPathBoVerificationKey) + "</font>");
            htmlBuilder.append("</td> </tr>");
        }
        htmlBuilder.append("</table>");

        // finally painting the links

        htmlBuilder.append("<table> <tr colspan=2> <td>");
        htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_2_OPEN+"Links"+
                ReportGeneratorHTMLElements.HEADING_2_CLOSE+"</td> </tr>");

        Map<String, String> links = arrayLevelElementsBDO.getLinks();
        Iterator<String> linksIter = links.keySet().iterator();

        while(linksIter.hasNext()) {
            String beKey = linksIter.next();

            String beValue = links.get(beKey);
                            /*
                            if((!beKey.equals("href"))|| (!beKey.equals("rel")) || (!beKey.equals("method"))) {
                                beKey = beKey.substring(0, beKey.length()-1);
                            }
                            */

            htmlBuilder.append("<tr> <td>");
            htmlBuilder.append(ReportGeneratorHTMLElements.HEADING_3_OPEN + beKey+
                    ReportGeneratorHTMLElements.HEADING_3_CLOSE +
                    "</td><td>"+
                    beValue);
            htmlBuilder.append("</td> </tr>");

        }


        htmlBuilder.append("</table>");


        htmlBuilder.append(ReportGeneratorHTMLElements.BODY_CLOSE);

        htmlBuilder.append(ReportGeneratorHTMLElements.HTML_CLOSE);

        // now the htmlBuilder has to be create in resources folder
        // Defining the file name of the file
        FileWriter fWriter = null;
        try {
            System.out.println("The HTML generated is "+htmlBuilder.toString());
            fWriter = new FileWriter("report.html");
            fWriter.write(htmlBuilder.toString());
        } catch(IOException ioe) {
            System.out.println("Could not write the file and the exception is "+ioe.getMessage());
        } finally {
            try {
                if(fWriter != null)
                    fWriter.close();
            }catch(IOException ioe) {

            }
        }

    }

}
