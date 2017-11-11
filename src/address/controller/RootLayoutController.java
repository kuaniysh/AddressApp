package address.controller;

import address.MainApp;
import address.model.Person;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

/**
 * Контроллер для корневого макета. Корневой макет предоставляет базовый макет приложения,
 * содержащий строку меню и место, где будут размещены
 * остальные элементы JavaFX.
 *
 *
 * Created by kuanysh on 28.10.17.
 */
public class RootLayoutController {

    // Ссылка на главн приложение
    private MainApp mainApp;

    /**
     * Вызывается главным приложением, чтобы оставить ссылку на самого себя.
     *
     * @param app
     */
    public void setMainApp(MainApp app) {
        this.mainApp = app;
    }

    @FXML
    private void initialize() {
    }

    /**
     * Создает пустую адресную книгу.
     */
    @FXML
    private void handleNew() {
        mainApp.getPersonData().clear();
        mainApp.setPersonFilePath(null);
    }

    /**
     * Открывает FileChooser, чтобы пользователь имел возможность
     * выбрать адресную книгу для загрузки.
     */
    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        // Задаем фильтр расширений
        FileChooser.ExtensionFilter extensionFilter = new  FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extensionFilter);

        // Показываем диалог загрузки файла
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            mainApp.loadPersonDataFromFile(file);
        }
    }

    /**
     * Сохраняет файл в файл адресатов, который в настоящиее время открыт.
     * Если файл не открыт, то отображается диалог "save as".
     */
    @FXML
    private void handleSave() {
        File personFile = mainApp.getPersonFilePath();
        if (personFile != null) {
            mainApp.savePersonDataToFile(personFile);
        } else {
            handleSaveAs();
        }
    }

    /**
     * Открывает FileChooser, чтобы имел возможность выбрать файл,
     * куда будут сохранены данные.
     */
    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        // Задаем фильтр расширений
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Показываем диалог сохранения файла
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            mainApp.savePersonDataToFile(file);
        }
    }

    /**
     * Открывает окно About
     */
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("AddressApp");
        alert.setHeaderText("About");
        alert.setContentText("Author: Kuanysh Bekturganov");

        alert.showAndWait();
    }

    /**
     * Закрывает приложение.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }


    @FXML
    public void handleEditPerson(ActionEvent event) throws IOException {

    }
}
