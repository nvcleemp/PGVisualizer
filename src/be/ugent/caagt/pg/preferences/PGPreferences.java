/* PGPreferences.java
 * =========================================================================
 * This file is part of the PG project - http://caagt.ugent.be/azul
 * 
 * Copyright (C) 2008 Universiteit Gent
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program (see
 * the META-INF directory in the source jar). This license can also be
 * found on the GNU website at http://www.gnu.org/licenses/gpl.html.
 * 
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package be.ugent.caagt.pg.preferences;

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
    private static final String OUR_NODE_NAME = "/be/ugent/caagt/pg/preferences";
    
    //
    private final Preferences userPreferences = Preferences.userRoot().node(OUR_NODE_NAME);
            
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
