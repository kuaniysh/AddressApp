package address.controller;

import javafx.fxml.FXMLLoader;

import java.io.IOException;

/**
 * Created by kuanysh on 01.11.17.
 */
public class LayoutFactory<C> {
    private C controller;

    public LayoutFactory() {

    }

    public <T> T makeLayout(FXMLPage page) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setClassLoader(LayoutFactory.class.getClassLoader());
        loader.setLocation(page.getResources());
        T ui = loader.load();
        controller = loader.getController();
        return ui;
    }

    public C getController() {
        return controller;
    }
}
