/* PGPreferences.java
 * =========================================================================
 */

package azul.preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * Class to handle PG preferences management. When the preferences are not set
 * by the user, a default value is taken.
 */
public class PGPreferences {
    
    //This class is based upon GrinvinPreferences from the GrInvIn-project
    
    //
    public enum Preference {
        CURRENT_DIRECTORY("gui.currentdir");
        
        private String id;
        
        Preference(String id) {
            this.id = id;
        }
        
        protected String getId() {
            return id;
        }
    }
    
    //
    private static final String OUR_NODE_NAME = "/be/ugent/twi/pg";
    
    //
    private final Preferences userPreferences = Preferences.userRoot().node(OUR_NODE_NAME);
    
    //
    private Preferences systemPreferences;
        
    //
    private List<PGPreferencesListener> listeners;
        
    //
    private final static PGPreferences INSTANCE = new PGPreferences();
    
    //
    private PGPreferences() {
        listeners = new ArrayList<PGPreferencesListener>();
    }
    
    public static PGPreferences getInstance() {
        return INSTANCE;
    }
    
    //
    public int getIntPreference(Preference key) {
        throw new RuntimeException("No default value available for: " + key);
    }
    
    //
    public void setIntPreference(Preference key, int value) {
        userPreferences.putInt(key.getId(), value);
        firePreferenceChanged(key);
    }
    
    private String getDefaultDir() {
        return null;
    }
    
    //
    public String getStringPreference(Preference key) {
        if (key == Preference.CURRENT_DIRECTORY) {
            String defaultValue = getDefaultDir();
            return userPreferences.get(key.getId(), defaultValue);
        } else {
            throw new RuntimeException("No default value available for: " + key);
        }
    }
    
    //
    public void setStringPreference(Preference key, String value) {
        userPreferences.put(key.getId(), value);
        firePreferenceChanged(key);
    }
    
    //
    public void addListener(PGPreferencesListener listener) {
        listeners.add(listener);
    }
    
    //
    private void firePreferenceChanged(Preference preference) {
        for(PGPreferencesListener listener : listeners)
            listener.preferenceChanged(preference);
    }
    
}
