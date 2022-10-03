package paypal.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import paypal.com.paypal.business.exception.OnboardingBusinessException;
import paypal.processing.model.ArrayLevelElementsBDO;
import paypal.processing.model.ContainerLevelElementsBDO;
import paypal.processing.model.ModelBuilderUtil;
import paypal.processing.model.TopLevelElementsBDO;


import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class BusinessOrchestrator {

    /**
     *
     * @param JSONFile
     * @param objectMapper
     * @throws IOException
     *
     * This file loads the JSON, creates an in memory tree
     * and then calls utils to process the tree and create respective models
     */

    private static ContainerLevelElementsBDO containerLevelElementsBDO;

    private static TopLevelElementsBDO topLevelElementsBDO;

    private static ArrayLevelElementsBDO arrayLevelElementsBDO;


    public static void loadJSON(String JSONFile, ObjectMapper objectMapper)
                                                         throws OnboardingBusinessException {

        // load the JSON
        try {
            JsonNode rootNode = BusinessUtil.loadJson(JSONFile, objectMapper);

           // extract the top level elements in the JSON
            Map<String, String > topLevelElementsMap = new LinkedHashMap<>();
            BusinessUtil.extractTopLevelElements(rootNode, topLevelElementsMap);

            System.out.println("Top Level Elements Map is ...");
            System.out.println(topLevelElementsMap.toString()+"\n");

            topLevelElementsBDO = new TopLevelElementsBDO();
            ModelBuilderUtil.buildTopLevelModel(topLevelElementsMap, topLevelElementsBDO);

            System.out.println(">>>>>>>>>>>>>>>>Top Level elements Account ID is "+topLevelElementsBDO.getAccount_id());

            // TODO: build the model for Top Level Elements

            // extract the elements which are containers
            Map<String,Map<String, String>> containerElementsMap = new LinkedHashMap<>();
            BusinessUtil.extractContainerElements(rootNode, containerElementsMap);

            containerLevelElementsBDO = new ContainerLevelElementsBDO();
            ModelBuilderUtil.buildContainerLevelModel(containerElementsMap, containerLevelElementsBDO);

            // TODO: build the model for containers
            // TODO: Logic, if an element is present in the previous, then it is part of the previous
            // TODO: Else, we consider it as a new element
            Iterator<String> it = containerElementsMap.keySet().iterator();
            while(it.hasNext()) {
                String outerKey = it.next();
                Map<String, String> innerMap = containerElementsMap.get(outerKey);
                System.out.print(outerKey + "-> {");
                Iterator<String> itInner = innerMap.keySet().iterator();

                while(itInner.hasNext()) {
                    String key = itInner.next();
                    System.out.print(key+" -> "+innerMap.get(key)+", ");
                }
                System.out.print(outerKey + "}");
                System.out.println("");
            }

            System.out.println("");

            System.out.println(">>>>>>>>>>>>>>>>Container element entityType is "+containerLevelElementsBDO.getBusiness_entitytype());

            // extract the elements which are arrays
            Map<String,Map<String, String>> arrayElementsMap = new LinkedHashMap<>();
            BusinessUtil.extractArrayElements(rootNode, arrayElementsMap);

            arrayLevelElementsBDO = new ArrayLevelElementsBDO();
            ModelBuilderUtil.buildArrayLevelModel(arrayElementsMap, arrayLevelElementsBDO);

            System.out.println(">>>>>>>>>>>>>>>>Array element Apple Pay limits is "+arrayLevelElementsBDO.getAPPLE_PAY_limits());
            System.out.println(">>>>>>>>>>>>>>>>Array element Receive Money limits is "+arrayLevelElementsBDO.getRECEIVE_MONEY_limits());


            // TODO: build the model for arrays
            // TODO: Logic, if an element is present in the previous, then it is part of the previous
            // TODO: Else, we consider it as a new element
            it = arrayElementsMap.keySet().iterator();
            while(it.hasNext()) {
                String outerKey = it.next();
                Map<String, String> innerMap = arrayElementsMap.get(outerKey);
                System.out.print(outerKey + "-> {");
                Iterator<String> itInner = innerMap.keySet().iterator();

                while(itInner.hasNext()) {
                    String key = itInner.next();
                    System.out.print(key+" -> "+innerMap.get(key)+", ");
                }
                System.out.print(outerKey + "}");
                System.out.println("");
            }

        } catch(IOException ioe) {
            System.out.println("Can't load the JSON! and the exception is "+ioe);
            new OnboardingBusinessException().setMessage(ioe);
        }

    }

    public static ContainerLevelElementsBDO getContainerLevelElementsBDO() {
        return containerLevelElementsBDO;
    }

    public static TopLevelElementsBDO getTopLevelElementsBDO() {
        return topLevelElementsBDO;
    }

    public static ArrayLevelElementsBDO getArrayLevelElementsBDO() {
        return arrayLevelElementsBDO;
    }
}
