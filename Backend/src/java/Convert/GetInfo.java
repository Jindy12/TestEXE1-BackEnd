/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Convert;

import model.GoogleAccount;

/**
 *
 * @author Sonvu
 */
public class GetInfo {
     public String ConvetGoogleAccountToInfo(GoogleAccount gg) {
        String id = gg.getId();
        return id;
    }
    
    public String GoogleAccountToEmail(GoogleAccount gg) {
        String string = gg.getEmail();
        return string;
    }
    
    public String GoogleAccountToFirstName(GoogleAccount gg) {
        
        
        String string = gg.getGiven_name();
        return string;
    }
    public String GoogleAccountToLastName(GoogleAccount gg) {
        String id = gg.getFamily_name();
        return id;
    }
    
}
