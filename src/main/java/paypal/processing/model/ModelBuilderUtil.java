package paypal.processing.model;

import java.lang.reflect.Method;
import java.util.*;

/**
 * This class is responsible for Model Building
 */

public class ModelBuilderUtil {
    private static String SET = "set";
    private static final String DELIMITER = "<->";

    public static void buildTopLevelModel(Map<String, String > topLevelElementsMap,
                                                            TopLevelElementsBDO topLevelElementsBDO) {
        Iterator<String> topLevelElementsMapIter =  topLevelElementsMap.keySet().iterator();

        while(topLevelElementsMapIter.hasNext()) {
            String key = topLevelElementsMapIter.next();

            String value = topLevelElementsMap.get(key);

            String formedSetterName = SET+key.substring(0,1).toUpperCase()+key.substring(1);
            try {
                Method setMethod = topLevelElementsBDO.getClass().getMethod(formedSetterName, String.class);

                setMethod.invoke(topLevelElementsBDO, value);
            } catch(Exception e) {
                // nothing to print, reflection did not work
            }

        }

    }

    public static void buildContainerLevelModel(Map<String, Map<String, String>> containerElementsMap,
                                                            ContainerLevelElementsBDO containerLevelElementsBDO) {

        List<String> keysInBusinessEntity = new LinkedList<>();

        Iterator<String> containerElementsMapKeyIter = containerElementsMap.keySet().iterator();

        Map<String, Map<String,Map<String,String>>> process_view_map = new LinkedHashMap<>();

        Map<String, Map<String, String>> requiredMap = new LinkedHashMap<>();

        String process_map_inner_layer1_key = "MANAGED_PATH_KYC";
        Map<String, Map<String, String>> process_map_inner_layer1 = new LinkedHashMap<>();

        while(containerElementsMapKeyIter.hasNext()) {
            String key = containerElementsMapKeyIter.next();

            if(key.equals("business_entity")) {
                Map<String, String> innerMap = containerElementsMap.get(key);

                Iterator<String> innerMapIter = innerMap.keySet().iterator();

                while(innerMapIter.hasNext()) {
                    String innerMapKey = innerMapIter.next();

                    String innerMapValue = innerMap.get(innerMapKey);

                    if(innerMapValue!= null) {

                        String formedSetterName = SET+key.substring(0,1).toUpperCase()+key.substring(1)+innerMapKey;
                        try {
                            Method setMethod = containerLevelElementsBDO.getClass().getMethod(formedSetterName, String.class);

                            setMethod.invoke(containerLevelElementsBDO, innerMapValue);
                        } catch(Exception e) {
                            // nothing to print, reflection did not work
                        }
                    } else {
                        // store all the keys, you need to iterate over them and add it to business entity
                        keysInBusinessEntity.add(innerMapKey);
                    }
                }
            } else if(keysInBusinessEntity.contains(key)) {
                Map<String, Map<String, String>> businessEntityOuterMap = new LinkedHashMap<>();
                Map<String, String> businessEntityInnerElementsMap = new LinkedHashMap<>();
                Map<String, String> innerMap = containerElementsMap.get(key);
                Iterator<String> innerMapIter = innerMap.keySet().iterator();

                while(innerMapIter.hasNext()) {
                    String innerMapKey = innerMapIter.next();
                    String innerMapValue = innerMap.get(innerMapKey);
                    businessEntityInnerElementsMap.put(innerMapKey, innerMapValue);
                }

                businessEntityOuterMap.put("business_entity_"+key, businessEntityInnerElementsMap);


                String formedSetterName = SET+"Business_entity_"+key;
                try {
                    Method setMethod = containerLevelElementsBDO.getClass().getMethod(formedSetterName, Map.class);

                    setMethod.invoke(containerLevelElementsBDO, businessEntityOuterMap);
                } catch(Exception e) {
                    // nothing to print, reflection did not work
                }
            } else if(key.equals("required")) {

                Map<String, String> requiredMapManagesPathKYC = new LinkedHashMap<>();
                Map<String, String> requiredMapManagesPath1099K = new LinkedHashMap<>();
                Map<String, String> requiredMapManagesPathBOVerification = new LinkedHashMap<>();

                Map<String, String> innerMap = containerElementsMap.get(key);
                Iterator<String> innerMapIter = innerMap.keySet().iterator();

                while(innerMapIter.hasNext()) {
                    String innerMapKey = innerMapIter.next();
                    String innerMapValue = innerMap.get(innerMapKey);

                    String innerMapKeyPart0 = innerMapKey.substring(0,innerMapKey.indexOf(DELIMITER));
                    String innerMapKeyPart1 = innerMapKey.substring(innerMapKey.indexOf(DELIMITER)+DELIMITER.length());

                    if(innerMapKeyPart0.equals("MANAGED_PATH_KYC")) {
                        requiredMapManagesPathKYC.put(innerMapKeyPart1, innerMapValue);
                    } else if(innerMapKeyPart0.equals("MANAGED_PATH_1099_K")){
                        requiredMapManagesPath1099K.put(innerMapKeyPart1, innerMapValue);
                    } else if(innerMapKeyPart0.equals("MANAGED_PATH_BO_VERIFICATION")) {
                        requiredMapManagesPathBOVerification.put(innerMapKeyPart1, innerMapValue);
                    }

                }

                if(!requiredMapManagesPathKYC.isEmpty()) {
                    requiredMap.put("MANAGED_PATH_KYC",requiredMapManagesPathKYC );
                }
                if(!requiredMapManagesPath1099K.isEmpty()) {
                    requiredMap.put("MANAGED_PATH_1099_K",requiredMapManagesPath1099K );
                }
                if(!requiredMapManagesPathBOVerification.isEmpty()) {
                    requiredMap.put("MANAGED_PATH_BO_VERIFICATION",requiredMapManagesPathBOVerification );
                }

            } else if(key.equals("process_view")) {

                Map<String, String> process_map_inner_layer2 = new LinkedHashMap<>();

                Map<String, String> innerMap = containerElementsMap.get(key);

                Iterator<String> innerMapIter = innerMap.keySet().iterator();

                while(innerMapIter.hasNext()) {
                    String innerMapKey = innerMapIter.next();
                    String innerMapValue = innerMap.get(innerMapKey);

                    if(innerMapValue!= null && innerMapKey.contains("name")) {
                        if(!process_map_inner_layer2.isEmpty()) {
                            // put the constructed map in the outer later
                            process_map_inner_layer1.put(process_map_inner_layer1_key, process_map_inner_layer2);
                            // clean it up for the next run
                            process_map_inner_layer2 = new LinkedHashMap<>();
                        }
                        process_map_inner_layer1_key = innerMapValue;
                    } else if(innerMapValue!= null) {
                        // inner layer map is set here
                        process_map_inner_layer2.put(innerMapKey.substring(0,innerMapKey.length()-1), innerMapValue);
                    } else {
                        // store all the keys, you need to iterate over them and add it to business entity
                        keysInBusinessEntity.add(innerMapKey);
                    }
                }
                process_map_inner_layer1.put(process_map_inner_layer1_key, process_map_inner_layer2);
            }

            // put the final map in place
            if(!process_map_inner_layer1.isEmpty()) {
                process_view_map.put("process_view", process_map_inner_layer1);
            }

            // if the required map is populated, then put the required values in the right place
            if(!requiredMap.isEmpty() && ! process_view_map.isEmpty()) {
               Iterator<String> it = requiredMap.keySet().iterator();
               while(it.hasNext()) {
                   String requiredOuterMapKey = it.next();
                   Map<String, String> requiredOuterMapValue = requiredMap.get(requiredOuterMapKey);
                   Iterator<String> innerIt = requiredOuterMapValue.keySet().iterator();
                   while(innerIt.hasNext()) {
                       String innerKey = innerIt.next();
                       String innerValue = requiredOuterMapValue.get(innerKey);

                       if(requiredOuterMapKey.equals("MANAGED_PATH_KYC")) {
                           process_view_map.get("process_view").get("MANAGED_PATH_KYC").put(innerKey, innerValue);
                       }

                       if(requiredOuterMapKey.equals("MANAGED_PATH_1099_K")) {
                           process_view_map.get("process_view").get("MANAGED_PATH_1099_K").put(innerKey, innerValue);
                       }

                       if(requiredOuterMapKey.equals("MANAGED_PATH_BO_VERIFICATION")) {
                           process_view_map.get("process_view").get("MANAGED_PATH_BO_VERIFICATION").put(innerKey, innerValue);
                       }
                   }
               }
            }

            // finally put the process map in the continer level element BDO
            containerLevelElementsBDO.setProcess_view(process_view_map);
        }
    }

    public static void buildArrayLevelModel(Map<String, Map<String, String>> arrayElementsMap,
                                                                ArrayLevelElementsBDO arrayLevelElementsBDO) {


        List<String> capabilitiesAvailable = new LinkedList<>();
        int capabilitiesAvailableIncrementer = 0; // this will increment as and when we process each limit

        Map<String, String> individual_owners_map = new LinkedHashMap<>();

        Iterator<String> arrayElementsMapIter = arrayElementsMap.keySet().iterator();

        while(arrayElementsMapIter.hasNext()) {

            String arrayElementsMapKey = arrayElementsMapIter.next();

            Map<String, String> arrayElementsMapValue = arrayElementsMap.get(arrayElementsMapKey);

            if(arrayElementsMapKey.equals("agreements")) {
                // create a map with agreements and then attach it to the BDO
                Iterator<String> agreementsIter = arrayElementsMapValue.keySet().iterator();
                Map<String, String> agreementsMap = new LinkedHashMap<>();

                while(agreementsIter.hasNext()) {
                    String agreementsMapKey = agreementsIter.next();
                    String agreementsMapValue = arrayElementsMapValue.get(agreementsMapKey);

                    agreementsMap.put(agreementsMapKey, agreementsMapValue);
                }
                // put the map in the BDO
                arrayLevelElementsBDO.setAgreements(agreementsMap);
            }// this is where agreements end
            else if(arrayElementsMapKey.equals("capabilities")) {

                Iterator<String> capabilitiesIter = arrayElementsMapValue.keySet().iterator();
                Map<String, String> capabilitiesMap = new LinkedHashMap<>();

                while(capabilitiesIter.hasNext()) {
                    String capabilitiesMapKey = capabilitiesIter.next();
                    String capabilitiesMapValue = arrayElementsMapValue.get(capabilitiesMapKey);

                    capabilitiesMap.put(capabilitiesMapKey, capabilitiesMapValue);

                    // put the capabilities seen in the list
                    if(capabilitiesMapKey.contains("name"))
                        capabilitiesAvailable.add(capabilitiesMapValue);
                }
                // put the map in the BDO
                arrayLevelElementsBDO.setCapabilities(capabilitiesMap);

            } // this end capabilities
            else if(arrayElementsMapKey.contains("limits")) {
                // first the safety check
                String capabilityName = "capabilityName";
                if(capabilitiesAvailableIncrementer <= capabilitiesAvailable.size()) {
                    capabilityName = capabilitiesAvailable.get(capabilitiesAvailableIncrementer);
                }

                if(capabilityName.equals("WITHDRAW_MONEY")) {
                    capabilityName = capabilitiesAvailable.get(++capabilitiesAvailableIncrementer);
                }

                if(! capabilityName.equals("WITHDRAW_MONEY")) {
                    // this has no limits and hence do not process
                    // do not increment capabilitiesAvailableIncrementer
                    // this is for all other cases
                    Iterator<String> limitsIter = arrayElementsMapValue.keySet().iterator();
                    Map<String, String> limitsMap = new LinkedHashMap<>();
                    while (limitsIter.hasNext()) {
                        String limitsMapKey = limitsIter.next();
                        String limitsMapValue = arrayElementsMapValue.get(limitsMapKey);
                        limitsMap.put(limitsMapKey, limitsMapValue);
                    }

                    // use introspection to set the right value

                    String formedSetterName = "set" + capabilityName + "_limits";
                    try {
                        Method setMethod = arrayLevelElementsBDO.getClass().getMethod(formedSetterName, Map.class);

                        setMethod.invoke(arrayLevelElementsBDO, limitsMap);
                    } catch (Exception e) {
                        // nothing to print, reflection did not work
                    }

                    // finally increment the capabilitiesAvailableIncrementer
                    capabilitiesAvailableIncrementer++;
                } // ! of withdraw money
            } else if(arrayElementsMapKey.equals("links")) {
                Iterator<String> linksIter = arrayElementsMapValue.keySet().iterator();
                Map<String, String> linksMap = new LinkedHashMap<>();
                while (linksIter.hasNext()) {
                    String linksMapKey = linksIter.next();
                    String linksMapValue = arrayElementsMapValue.get(linksMapKey);
                    linksMap.put(linksMapKey, linksMapValue);
                }
                // put the map in the BDO
                arrayLevelElementsBDO.setLinks(linksMap);
            } // links ends here
            else {
                // we loop everything else into individual owners
                Iterator<String> individuals_owners_element_iter = arrayElementsMapValue.keySet().iterator();

                while (individuals_owners_element_iter.hasNext()) {
                    String individuals_owners_element_key = individuals_owners_element_iter.next();
                    String individuals_owners_element_value = arrayElementsMapValue.get(individuals_owners_element_key);
                    individual_owners_map.put(individuals_owners_element_key, individuals_owners_element_value);
                }

            }
        } // this is the end of main arrayElementsMapIter

        // finally add the individual_owners_map in the business data object
        arrayLevelElementsBDO.setIndividual_owners_details(individual_owners_map);
    }
}
