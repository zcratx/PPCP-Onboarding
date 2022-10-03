package paypal.processing.model;

/**
 * This class will hold the top level elements of the JSON
 */


public class TopLevelElementsBDO {

    private String account_id;

    private String legal_country_code;

    private String external_id;

    private String organization;

    private String primary_currency_code;

    private String soft_descriptor;

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getLegal_country_code() {
        return legal_country_code;
    }

    public void setLegal_country_code(String legal_country_code) {
        this.legal_country_code = legal_country_code;
    }

    public String getExternal_id() {
        return external_id;
    }

    public void setExternal_id(String external_id) {
        this.external_id = external_id;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getPrimary_currency_code() {
        return primary_currency_code;
    }

    public void setPrimary_currency_code(String primary_currency_code) {
        this.primary_currency_code = primary_currency_code;
    }

    public String getSoft_descriptor() {
        return soft_descriptor;
    }

    public void setSoft_descriptor(String soft_descriptor) {
        this.soft_descriptor = soft_descriptor;
    }
}
