package address.controller;

import address.MainApp;
import address.model.Person;
import address.util.DateUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Created by kuanysh on 23.10.17.
 */
public class PersonOverviewController {

    @FXML
    public TableView<Person> personTable;

    @FXML
    private TableColumn<Person, String> firstNameColumn;

    @FXML
    private TableColumn<Person, String> lastNameColumn;

    @FXML
    private Label firstNameLabel;

    @FXML
    private Label lastNameLabel;

    @FXML
    private Label streetLabel;

    @FXML
    private Label postalCodeLabel;

    @FXML
    private Label cityLabel;

    @FXML
    private Label birthdayLabel;

    // Ссылка на глав апп
    private MainApp mainApp;

    public PersonOverviewController() {

    }

    public PersonOverviewController(String s) {
        initialize();
    }

    @FXML
    private void initialize() {
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

        showPersonDetails(null);

        personTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPersonDetails(newValue)
        );
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        personTable.setItems(mainApp.getPersonData());
    }

    public void showPersonDetails(Person person) {
        if (person != null) {
            // Заполняем метки информацией из объекта person
            firstNameLabel.setText(person.getFirstName());
            lastNameLabel.setText(person.getLastName());
            streetLabel.setText(person.getStreet());
            postalCodeLabel.setText(Integer.toString(person.getPostalCode()));
            cityLabel.setText(person.getCity());
            birthdayLabel.setText(DateUtil.format(person.getBirthday()));
        } else {
            // Если Person = null, то убираем весь текст
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            streetLabel.setText("");
            postalCodeLabel.setText("");
            cityLabel.setText("");
            birthdayLabel.setText("");
        }
    }

    /**
     * Вызываю при клике по кнопке New...
     * Открывает диалог окно с доп информ нового адресата
     */
    @FXML
    private void handleNewPerson() {
        Person tempPerson = new Person();
        boolean okClicked = mainApp.showPersonEditDialog(tempPerson);
        if (okClicked) {
            mainApp.getPersonData().add(tempPerson);
        }

    }

    @FXML
    private void handleEditPerson() {
        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            boolean okClicked = mainApp.showPersonEditDialog(selectedPerson);
            if (okClicked) {
                showPersonDetails(selectedPerson);
            }
        } else {
            // Никто не выбран
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Person Selected");
            alert.setContentText("Please select a person in the table.");

            alert.showAndWait();
        }
    }

    private TableView<Person> getPersonTable() {
        return personTable;
    }

    @FXML
    private void handleDeletePerson() {
        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            personTable.getItems().remove(selectedIndex);
        } else {
            // Ничего не выбрано
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Person Selected");
            alert.setContentText("Please select a person in the table");
            alert.showAndWait();
        }
    }


}
