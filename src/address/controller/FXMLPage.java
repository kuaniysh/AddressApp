package address.controller;

import java.net.URL;

/**
 * Created by kuanysh on 31.10.17.
 */
public enum FXMLPage {


    ROOT_LAYOUT("view/fxml/RootLayout.fxml"),
    PERSON_EDIT_DIALOG("view/fxml/PersonEditDialog.fxml"),
    PERSON_OVERVIEW("view/fxml/PersonOverview.fxml");

    private String fxml;
    private URL resources;

    FXMLPage(String fxml) {
        this.fxml = fxml;
        resources = FXMLPage.class.getResource(fxml);
    }

    public String getFxml() {
        return fxml;
    }

    public URL getResources() {
        return resources;
    }
}
