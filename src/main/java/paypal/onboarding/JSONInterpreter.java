package paypal.onboarding;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;




/**
 * This is the main entry class
 *
 */
public final class JSONInterpreter {

    // all class variables declared here
    private static JSONInterpreter jsonInterpreter = null;
    private ObjectMapper objectMapper;
    protected final static String enableClientBasedFilterName = "enableClientBasedFiltering";
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String UTC_TIMEZONE_ID = "UTC";


    // this is the main init wherein all required resources are loaded
    private void init() {
        // first load Jackson
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }

    // constructor
    private JSONInterpreter() {
        // before creating this object we ensure needed objects are initialized
        init();
    }

    // this class is a singelton and this is the only way to get hold of the class
    public synchronized static JSONInterpreter getJSONInterpreter() {
        if(jsonInterpreter == null) {
            jsonInterpreter = new JSONInterpreter();
        }

        return jsonInterpreter;
    }

    public ObjectMapper getObjectMapper() {
        // if for some reason, object mapper is null, reinit and send it
        if(objectMapper == null) {
            init();
        }
        return objectMapper;
    }


}
