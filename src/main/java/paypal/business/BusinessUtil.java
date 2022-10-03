package paypal.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * This has all the util methods
 */

public class BusinessUtil {

   private static List<String> exclusionList = new ArrayList<>();

   private static List<String> arrayExclusionList = new ArrayList<>();

   private static String key = "";

   private static boolean TOP_LEVEL_ARRAY_FIND = true;

   private static final String PROCESSES = "processes";

   private static final String REQUIRED = "required";

   private static final String ATTRIBUTES = "attributes";

   private static final String DELIMITER = "<->";

    protected static JsonNode loadJson(String JSONFile, ObjectMapper objectMapper) throws IOException {

            byte[] jsonData = Files.readAllBytes(Paths.get(JSONFile));

            JsonNode rootNode = objectMapper.readTree(jsonData);

            return rootNode;
    }

    protected static void extractTopLevelElements(JsonNode rootNode, Map<String, String> jsonMap) {
        // manipulating the rootNode and hence making a deep copy


        if(rootNode == null) {
            return; // this is a safety net
        }

        if(rootNode.isObject() && !rootNode.isArray()){
            Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();

            fields.forEachRemaining(field -> {

                // if the present node is a container node, then find all elements of it and exclude them
                if(field.getValue().isContainerNode() && !field.getValue().isArray()) {

                    Iterator<Map.Entry<String, JsonNode>> currentNodeArray = field.getValue().fields();
                    for (Map.Entry<String, JsonNode> elt; currentNodeArray.hasNext(); ) {
                        elt = currentNodeArray.next();

                        JsonNode currentNode = elt.getValue();
                        String currentKey = elt.getKey();
                        exclusionList.add(currentKey);
                    }
                }
            });

            //System.out.println("The exclusion list is "+exclusionList.toString());

            //reset the fields
            fields = rootNode.fields();

            fields.forEachRemaining(field -> {

                if(!field.getValue().isContainerNode() && !field.getValue().isArray()
                        && !exclusionList.contains(field.getKey().trim())) {

                    jsonMap.put(field.getKey(), field.getValue().textValue());
                };
                extractTopLevelElements(field.getValue(), jsonMap);
            });
        }
    }

    public static void extractContainerElements(JsonNode rootNode, Map<String,Map<String, String>> containerElementsMap) {

        // this will contain the process map
        Map<String, String> processRequiredMap =new LinkedHashMap<>();

        if(rootNode == null) {
            return; // this is a safety net
        }

        // first go through the array and figure out all top level array elements
        if(TOP_LEVEL_ARRAY_FIND) {
            findTopLevelArrayElements(rootNode);
            TOP_LEVEL_ARRAY_FIND = false;
        }

        Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();


        fields.forEachRemaining(field -> {
            if (field.getValue().isContainerNode() && !field.getValue().isArray()) {
                //if(field.getValue().isContainerNode() || field.getValue().isArray()) {
                // extract the elements in a container node and populate the map
                key = field.getKey();

                Iterator<Map.Entry<String, JsonNode>> currentNodeArray = field.getValue().fields();

                // define a new map, add all the elements and put it into a list
                Map<String, String> innerContainerElementMap = new LinkedHashMap<>();
                for (Map.Entry<String, JsonNode> elt; currentNodeArray.hasNext(); ) {
                    elt = currentNodeArray.next();

                    JsonNode currentNode = elt.getValue();
                    String currentKey = elt.getKey();


                    // there is a chance even the current Node can be an array
                    // the array further can have container node and an array
                    if(currentKey.equals(PROCESSES)) {

                        if(currentNode.isObject() || currentNode.isContainerNode() && !currentNode.isArray()) {
                            int keyIncrementer = 0;
                            Iterator<Map.Entry<String, JsonNode>> innerNodesMap = currentNode.fields();
                            for (Map.Entry<String, JsonNode> innerMap; innerNodesMap.hasNext(); ) {
                                innerMap = currentNodeArray.next();

                                JsonNode innerNode = innerMap.getValue();
                                String innerKey = innerMap.getKey();

                                innerContainerElementMap.put(innerKey+keyIncrementer++, innerNode.textValue());
                            }

                        } else if(currentNode.isArray()) {
                            int keyIncrementer = 0;
                            String processKey = "processKey";
                            for (JsonNode arrayItem : currentNode) {
                                Iterator<Map.Entry<String, JsonNode>> arrayNodeIter = arrayItem.fields();
                                keyIncrementer++;
                                while (arrayNodeIter.hasNext()) {

                                    Map.Entry<String, JsonNode> innerNode = arrayNodeIter.next();
                                    if(innerNode.getKey().equals(REQUIRED)) {
                                        // this needs to be handled as it will have the required values
                                        //evaluate the container map separately and then grab what is needed
                                        evaluateProcessNode(processRequiredMap, innerNode.getValue().fields(), processKey);
                                    } else {
                                        if(innerNode.getValue() != null && innerNode.getValue().textValue() != null
                                                &&
                                                (innerNode.getValue().textValue().equals("MANAGED_PATH_KYC")
                                                ||innerNode.getValue().textValue().equals("MANAGED_PATH_1099_K")
                                                || innerNode.getValue().textValue().equals("MANAGED_PATH_BO_VERIFICATION"))) {
                                            // resetting the key here
                                            processKey = innerNode.getValue().asText();
                                        }
                                        innerContainerElementMap.put(innerNode.getKey() + keyIncrementer, innerNode.getValue().textValue());
                                    }
                                }
                            }

                            if(!processRequiredMap.isEmpty()) {
                                // put the process map
                                containerElementsMap.put(REQUIRED, processRequiredMap);
                            }
                        }
                    }

                    //if (!currentNode.isNull() && !arrayExclusionList.contains(key))
                    //if (!currentNode.isNull() && processRequiredMap.isEmpty())
                    //if (!currentNode.isNull())
                    innerContainerElementMap.put(currentKey, currentNode.textValue());
                }

                // put the map in the outer list
                //if (innerContainerElementMap.size() > 0 && processRequiredMap.isEmpty())
                if (innerContainerElementMap.size() > 0)
                    containerElementsMap.put(key, innerContainerElementMap);
            } else if ((field.getValue().isArray())) {

                key = field.getKey();

                // this is new code to detect an array
                Map<String, String> innerContainerElementMap = new LinkedHashMap<>();
                for (JsonNode arrayItem : field.getValue()) {
                    Iterator<Map.Entry<String, JsonNode>> arrayNodeIter = arrayItem.fields();
                    while (arrayNodeIter.hasNext()) {
                        Map.Entry<String, JsonNode> innerNode = arrayNodeIter.next();
                        if(!arrayExclusionList.contains(key))
                            innerContainerElementMap.put(innerNode.getKey(), innerNode.getValue().textValue());


                    }
                }

                // put the map in the outer list
                if (innerContainerElementMap.size() > 0)
                    containerElementsMap.put(key, innerContainerElementMap);

            }
            extractContainerElements(field.getValue(), containerElementsMap);
        });

    }

    /**
     * This will extract the top level array elements
     * @param rootNode
     */

    public static void extractArrayElements(JsonNode rootNode, Map<String,Map<String, String>> arrayElementsMap) {
        //System.out.println("The top level arrays are \n" + arrayExclusionList.toString());
        Iterator<String> arrayListIter = arrayExclusionList.listIterator();

        int keyIncrementer = 0;

        while(arrayListIter.hasNext()) {

            String topLevelKey = arrayListIter.next().trim();

            JsonNode jsonNode = rootNode.path(topLevelKey);

            if(jsonNode.isArray()) {

                Iterator<JsonNode> elements = jsonNode.elements();

                while(elements.hasNext()) {

                    keyIncrementer = keyIncrementer+1;

                    JsonNode innerJsonNode = elements.next();

                    if ((innerJsonNode.isContainerNode() || innerJsonNode.isObject()) && !innerJsonNode.isArray()) {
                        Iterator<Map.Entry<String, JsonNode>> fields = innerJsonNode.fields();

                        while(fields.hasNext()) {
                            Map.Entry<String, JsonNode> fieldEntryMap = fields.next();

                            String key = fieldEntryMap.getKey();

                            JsonNode field = fieldEntryMap.getValue();

                            Map<String, String> innerContainerElementMap = new LinkedHashMap<>();

                            // if field is a textnode, then grab it
                            if(field.isTextual()) {

                                if(arrayElementsMap.containsKey(topLevelKey)) {
                                    innerContainerElementMap = arrayElementsMap.get(topLevelKey);

                                    // if the key is also present, then we put in a new key
                                    if(innerContainerElementMap.containsKey(key)) {
                                        key = key+keyIncrementer;
                                    }
                                    innerContainerElementMap.put(key, field.textValue());
                                } else {
                                    innerContainerElementMap.put(key,field.textValue());
                                }

                                arrayElementsMap.put(topLevelKey, innerContainerElementMap);
                            } else if (field.isContainerNode() && !field.isArray()) {
                                Iterator<Map.Entry<String, JsonNode>> currentNodeArray = field.fields();
                                innerContainerElementMap = new LinkedHashMap<>();
                                for (Map.Entry<String, JsonNode> elt; currentNodeArray.hasNext(); ) {
                                    elt = currentNodeArray.next();

                                    JsonNode currentNode = elt.getValue();
                                    String currentKey = elt.getKey();

                                    if (!currentNode.isNull())
                                        innerContainerElementMap.put(currentKey, currentNode.textValue());
                                }

                               // put the map in the outer list
                               if (innerContainerElementMap.size() > 0)
                                    arrayElementsMap.put(key, innerContainerElementMap);


                            } else if (field.isArray()) {

                                // this is new code to detect an array
                                innerContainerElementMap = new LinkedHashMap<>();
                                for (JsonNode arrayItem : field) {
                                    Iterator<Map.Entry<String, JsonNode>> arrayNodeIter = arrayItem.fields();
                                    while (arrayNodeIter.hasNext()) {
                                        Map.Entry<String, JsonNode> innerNode = arrayNodeIter.next();
                                        innerContainerElementMap.put(innerNode.getKey(), innerNode.getValue().textValue());
                                    }
                                }

                                // put the map in the outer list
                                //if ( arrayElementsMap.containsKey(key))
                                    arrayElementsMap.put(key+keyIncrementer, innerContainerElementMap);

                            }

                        };

                    } else if(innerJsonNode.isArray()) {

                    }
                }

            }
        }

    }


    private static void findTopLevelArrayElements(JsonNode rootNode) {

        Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();

        fields.forEachRemaining(field -> {

            // if the present node is a container node, then find all elements of it and exclude them
            if (field.getValue().isArray()) {

                String currentKey = field.getKey();
                arrayExclusionList.add(currentKey);

            }
        });
    }

    private static void evaluateProcessNode(Map<String, String> processRequiredMap,
                                            Iterator<Map.Entry<String, JsonNode>> fields,
                                            String key) {

        fields.forEachRemaining(field ->{
            //System.out.println("Key is >>>>>>>>>>>> "+field.getKey()+" value is >>>>>>>>>>>> "+field.getValue());
            if (field.getValue().isArray() && (!field.getValue().isObject() || !field.getValue().isContainerNode())) {
                Iterator<JsonNode> attributeNode = field.getValue().elements();
                while (attributeNode.hasNext()) {
                    JsonNode attributeInnerNode = attributeNode.next();
                    //System.out.println("The process key that is processed is "+key);
                    //System.out.println("Attribute Inner Node that is processed " + attributeInnerNode.textValue());

                    //TODO: New code
                    if(!attributeInnerNode.isTextual() && attributeInnerNode.isContainerNode()) {
                        Iterator<Map.Entry<String, JsonNode>> attributeInnerNodeFields = attributeInnerNode.fields();
                        attributeInnerNodeFields.forEachRemaining(attributeInnerNodeField -> {
                            if(attributeInnerNodeField.getKey().equals(ATTRIBUTES) && !attributeInnerNodeField.getValue().isEmpty()) {
                                processRequiredMap.put(key + DELIMITER + ATTRIBUTES, attributeInnerNodeField.getValue()+"");
                            }
                        });
                    }

                    if(attributeInnerNode.textValue() != null)
                        processRequiredMap.put(key + DELIMITER + ATTRIBUTES, attributeInnerNode.textValue());

                }
            } else {
                // if it is a array node, processes it differently
                String currentKey = field.getKey();
                JsonNode currentValue = field.getValue();
                processRequiredMap.put(key+ DELIMITER + currentKey, currentValue.textValue());
            }

        });
    }

}
