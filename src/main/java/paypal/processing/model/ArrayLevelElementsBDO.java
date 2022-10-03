package paypal.processing.model;

import java.util.HashMap;
import java.util.Map;

public class ArrayLevelElementsBDO {

    private Map<String, String> agreements;

    private Map<String, String> capabilities;


    private Map<String, String> individual_owners_details;

    //private Map<String, String> individual_owners_primary_residence;


    private Map<String, String> APPLE_PAY_limits;

    private Map<String, String> CUSTOM_CARD_PROCESSING_limits;

    private Map<String, String> RECEIVE_MONEY_limits;

    private Map<String, String> CUSTOM_BANK_PROCESSING_limits;

    private Map<String, String> links;

    public Map<String, String> getAgreements() {
        return agreements;
    }

    public void setAgreements(Map<String, String> agreements) {
        this.agreements = agreements;
    }

    public Map<String, String> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(Map<String, String> capabilities) {
        this.capabilities = capabilities;
    }

   public void setIndividual_owners_details(Map<String, String> individual_owners_details) {
        this.individual_owners_details = individual_owners_details;
    }

    public Map<String, String> getIndividual_owners_details() {
        return individual_owners_details;
    }

    public Map<String, String> getAPPLE_PAY_limits() {
        if(APPLE_PAY_limits == null) {
            return new HashMap<>();
        }
        return APPLE_PAY_limits;
    }

    public void setAPPLE_PAY_limits(Map<String, String> APPLE_PAY_limits) {
        this.APPLE_PAY_limits = APPLE_PAY_limits;
    }

    public Map<String, String> getCUSTOM_CARD_PROCESSING_limits() {
        if(CUSTOM_CARD_PROCESSING_limits == null) {
            return new HashMap<>();
        }
        return CUSTOM_CARD_PROCESSING_limits;
    }

    public void setCUSTOM_CARD_PROCESSING_limits(Map<String, String> CUSTOM_CARD_PROCESSING_limits) {
        this.CUSTOM_CARD_PROCESSING_limits = CUSTOM_CARD_PROCESSING_limits;
    }

    public Map<String, String> getRECEIVE_MONEY_limits() {
        if(RECEIVE_MONEY_limits == null) {
            return new HashMap<>();
        }
        return RECEIVE_MONEY_limits;
    }

    public void setRECEIVE_MONEY_limits(Map<String, String> RECEIVE_MONEY_limits) {
        this.RECEIVE_MONEY_limits = RECEIVE_MONEY_limits;
    }

    public Map<String, String> getCUSTOM_BANK_PROCESSING_limits() {
        if(CUSTOM_BANK_PROCESSING_limits == null) {
            return new HashMap<>();
        }
        return CUSTOM_BANK_PROCESSING_limits;
    }

    public void setCUSTOM_BANK_PROCESSING_limits(Map<String, String> CUSTOM_BANK_PROCESSING_limits) {
        this.CUSTOM_BANK_PROCESSING_limits = CUSTOM_BANK_PROCESSING_limits;
    }

    public void setLinks(Map<String, String> linksMap) {
        this.links = linksMap;
    }

    public Map<String, String> getLinks() {
        return this.links;
    }
}
