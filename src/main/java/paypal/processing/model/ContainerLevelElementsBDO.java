package paypal.processing.model;

import java.util.Map;

/**
 * This class will hold the Container Level Objects
 */

public class ContainerLevelElementsBDO {
    private String business_entitytype;

    private String business_entitymerchant_category_code;

    // this map internally will have key as businessEntityIncDetails and values of incorporation details
    private Map<String, Map<String, String>> business_entity_incorporation_details;

    // this map internally will have key as businessEntityNames and the values of names
    private Map<String, Map<String, String>> business_entity_names;

    // this map internally will have key as businessEntityRegBusinessAddr and the values of registered_business_address
    private Map<String, Map<String, String>> business_entity_registered_business_address;

    // this map internally will have key as businessEntityPhoneNumbers and the value of the phone_numbers
    private Map<String, Map<String, String>> business_entity_phone_numbers;

    // this map internally will have key as process and then an internal key of name and then the values
    private Map<String, Map<String,Map<String,String>>> process_view;

    public String getBusiness_entitytype() {
        return business_entitytype;
    }

    public void setBusiness_entitytype(String business_entitytype) {
        this.business_entitytype = business_entitytype;
    }

    public String getBusiness_entitymerchant_category_code() {
        return business_entitymerchant_category_code;
    }

    public void setBusiness_entitymerchant_category_code(String business_entitymerchant_category_code) {
        this.business_entitymerchant_category_code = business_entitymerchant_category_code;
    }

    public Map<String, Map<String, String>> getBusiness_entity_incorporation_details() {
        return business_entity_incorporation_details;
    }

    public void setBusiness_entity_incorporation_details(Map<String, Map<String, String>> business_entity_incorporation_details) {
        this.business_entity_incorporation_details = business_entity_incorporation_details;
    }

    public Map<String, Map<String, String>> getBusiness_entity_names() {
        return business_entity_names;
    }

    public void setBusiness_entity_names(Map<String, Map<String, String>> business_entity_names) {
        this.business_entity_names = business_entity_names;
    }

    public Map<String, Map<String, String>> getBusiness_entity_registered_business_address() {
        return business_entity_registered_business_address;
    }

    public void setBusiness_entity_registered_business_address(Map<String, Map<String, String>> business_entity_registered_business_address) {
        this.business_entity_registered_business_address = business_entity_registered_business_address;
    }

    public Map<String, Map<String, String>> getBusiness_entity_phone_numbers() {
        return business_entity_phone_numbers;
    }

    public void setBusiness_entity_phone_numbers(Map<String, Map<String, String>> business_entity_phone_numbers) {
        this.business_entity_phone_numbers = business_entity_phone_numbers;
    }

    public Map<String, Map<String,Map<String,String>>> getProcess_view() {
        return process_view;
    }

    public void setProcess_view(Map<String, Map<String,Map<String,String>>> process_view) {
        this.process_view = process_view;
    }
}
