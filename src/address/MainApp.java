package address;

import address.model.Person;
import address.model.PersonListWrapper;
import address.controller.FXMLPage;
import address.controller.PersonEditDialogController;
import address.controller.PersonOverviewController;
import address.controller.RootLayoutController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    private ObservableList<Person> personData = FXCollections.observableArrayList();

    public MainApp() {
        personData.add(new Person("Hans", "Muster"));
        personData.add(new Person("Ruth", "Mueller"));
        personData.add(new Person("Heinz", "Kurz"));
        personData.add(new Person("Cornelia", "Meier"));
        personData.add(new Person("Werner", "Meyer"));
    }

    public ObservableList<Person> getPersonData() {
        return personData;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AddressApp");
        this.primaryStage.getIcons().add(new Image("file:src/address/resources/images/address_book_32.png"));
        this.primaryStage.setResizable(false);

        initRootLayout();
        showPersonOverview();
    }

    /**
     * Иниц корнев макета
     */
    public void initRootLayout() {
        try {
            // Загруж корн макет из fxml
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(FXMLPage.ROOT_LAYOUT.getFxml()));
            rootLayout = (BorderPane) loader.load();

            // Отображ сцену, содерж корн макет
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Даем контроллеру доступ к главному приложению.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Пытается загрузить последний открытый файл с адресатами.
        File file = getPersonFilePath();
        if (file != null) {
            loadPersonDataFromFile(file);
        }
    }

    public void showPersonOverview() {
        try {
            // Загруж сведен об адресатах
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(FXMLPage.PERSON_OVERVIEW.getFxml()));
            AnchorPane personOverview = (AnchorPane) loader.load();

            // Помещаем свед об адресатах в центр корн макета
            rootLayout.setCenter(personOverview);

            PersonOverviewController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean showPersonEditDialog(Person person) {
        try {
            // Грузим fxml-файл и создаем новую сцену для всплыв диалог окна
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(FXMLPage.PERSON_EDIT_DIALOG.getFxml()));
            AnchorPane page = (AnchorPane) loader.load();

            // Создаем диалог окно Stage
            Stage dialogStage = new Stage();
            if (person.getFirstName() == null) {
                dialogStage.setTitle("New Person");
            } else dialogStage.setTitle("Edit Person " + person.getFirstName());

            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Передаю адресата в контроллер
            PersonEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(person);

            dialogStage.getIcons().add(new Image("file:src/address/resources/images/edit.png"));

            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *  Возвращает pref файла адресатов, то есть, послед открытый файл.
     *  Preference считывается из реестра, специфичного для конкретной ОС.
     *  Если preference не был найден, то return null.
     * @return
     */
    public File getPersonFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    public void setPersonFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());

            primaryStage.setTitle("AddressApp - " + file.getName());
        } else {
            prefs.remove("filePath");

            primaryStage.setTitle("AddressApp");
        }
    }

    /**
     * Загружает информ об адресатах из указанного файла.
     * Текущая информ об адресатах будет заменена.
     *
     * @param file
     */
    public void loadPersonDataFromFile(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Чтение XML из файла и демаршализация.
            PersonListWrapper wrapper = (PersonListWrapper) um.unmarshal(file);

            personData.clear();
            personData.addAll(wrapper.getPersons());

            // Сохраняем путь к файлу в реестре.
            setPersonFilePath(file);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    public void savePersonDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Обертываем наши данные об адресатах.
            PersonListWrapper wrapper = new PersonListWrapper();
            wrapper.setPersons(personData);

            // Маршаллируем и сохраняем XML в файл.
            m.marshal(wrapper, file);

            // Сохраняем путь к файлу в реестре.
            setPersonFilePath(file);

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
    public static void main(String[] args) {
        launch(args);
    }
}
