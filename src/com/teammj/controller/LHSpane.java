package com.teammj.controller;

import com.teammj.Ozlympic;
import com.teammj.model.DATA;
import com.teammj.model.persons.*;
import com.teammj.model.persons.base.Athlete;
import com.teammj.model.persons.base.Person;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import org.w3c.dom.Element;

import java.net.URL;
import java.util.ResourceBundle;

import static com.teammj.controller.Main.athletes;
import static com.teammj.controller.Main.persons;
import static com.teammj.controller.Main.officials;

final public class LHSpane implements Initializable {
    public TableView<Person> tblViewPersons;
    public ToggleGroup toggleGroup;
    public TextField txtFieldAName;
    public TextField txtFieldAAge;
    public ComboBox cmbAState;
    public Button addAthlete;
    public Label addAfeedBack;
    public TextField txtFieldRName;
    public TextField txtFieldRAge;
    public ComboBox cmbRState;
    public Button addReferee;
    public Label addRfeedBack;
    public VBox rootLHS;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupPersonsTable();
        setupValidators();
        tblViewPersons.prefHeightProperty().bind(Ozlympic.getCurrentStage().heightProperty().subtract(DATA.HEIGHT_FROM_BOTTOM));
        tblViewPersons.maxHeightProperty().bind(Ozlympic.getCurrentStage().heightProperty().subtract(DATA.HEIGHT_FROM_BOTTOM));
    }

    /**
     * Repetitive but near impossible to eliminate
     */
    private void setupValidators() {
        txtFieldAName.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if (!txtFieldAName.getText().matches(DATA.FULL_NAME)) {
                    addAfeedBack.setText("Wrong name input");
                    txtFieldAName.setText("");
                } else {
                    addAfeedBack.setText("");
                }
            }

        });

        txtFieldAAge.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if (!txtFieldAAge.getText().matches(DATA.POSTIVE_INTEGER_ONE_TO_NINE)) {
                    addAfeedBack.setText("Wrong age input");
                    txtFieldAAge.setText("");
                } else {
                    addAfeedBack.setText("");
                }
            }
        });

        txtFieldRName.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if (!txtFieldRName.getText().matches(DATA.FULL_NAME)) {
                    addRfeedBack.setText("Wrong name input");
                    txtFieldRName.setText("");
                } else {
                    addRfeedBack.setText("");
                }
            }

        });

        txtFieldRAge.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if (!txtFieldRAge.getText().matches(DATA.POSTIVE_INTEGER_ONE_TO_NINE)) {
                    addRfeedBack.setText("Wrong age input");
                    txtFieldRAge.setText("");
                } else {
                    addRfeedBack.setText("");
                }
            }
        });
    }

    private void setupPersonsTable() {
        TableColumn<Person, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Person, DATA.PERSON_TYPE> typeColumn = new TableColumn<>("Type");
        TableColumn<Person, DATA.STATE> stateColumn = new TableColumn<>("State");
        TableColumn<Person, Integer> ageColumn = new TableColumn<>("Age");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("personType"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("fromState"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

        nameColumn.setPrefWidth(140.0);
        typeColumn.setPrefWidth(70.0);

        tblViewPersons.setRowFactory(tv -> {
            TableRow<Person> tableRow = new TableRow<>();

            tableRow.setOnDragDetected(event -> {
                if (!tableRow.isEmpty()) {
                    Dragboard dragboard = tableRow.startDragAndDrop(TransferMode.COPY);
                    dragboard.setDragView(tableRow.snapshot(null, null));
                    ClipboardContent clipboardContent = new ClipboardContent();
                    clipboardContent.putString(tableRow.getItem().getUniqueID().toString());
                    dragboard.setContent(clipboardContent);
                    event.consume();
                }
            });

            return tableRow;
        });

        tblViewPersons.setItems(persons);
        tblViewPersons.getColumns().addAll(nameColumn, typeColumn, stateColumn, ageColumn);
    }

    public void addNewAthlete(KeyEvent keyEvent) {
        System.out.println(keyEvent.getCode().getName());
        try {
            if (txtFieldAAge.getText().length() < 1) {
                addAfeedBack.setText("Please input age.");
                return;
            }
            if (txtFieldAName.getText().length() < 3) {
                addAfeedBack.setText("Please enter name.");
                return;
            }

            String typeToAdd = ((RadioButton) toggleGroup.getSelectedToggle()).getText();
            String sState = (String) cmbAState.getValue();
            if (sState == null) {
                addAfeedBack.setText("Please select state");
                return;
            }

            Integer age = Integer.valueOf(txtFieldAAge.getText());
            String name = txtFieldAName.getText();
            DATA.STATE stateType = DocumentHandler.parseState(sState);
            if(stateType == null) {
                addAfeedBack.setText("Please select state");
            } else {
                addAfeedBack.setText("");
            }

            Element element;

            Athlete athlete = null;
            switch (typeToAdd) {
                case "Cyclist":
                    element = DocumentHandler.addCyclist(Main.getDocument());
                    if (element != null)
                        athlete = new Cyclist(name, age, stateType, element);
                    break;
                case "Sprinter":
                    element = DocumentHandler.addSprinter(Main.getDocument());
                    if (element != null)
                        athlete = new Sprinter(name, age, stateType, element);
                    break;
                case "Swimmer":
                    element = DocumentHandler.addSwimmer(Main.getDocument());
                    if (element != null)
                        athlete = new Swimmer(name, age, stateType, element);
                    break;
                case "SuperAthlete":
                    element = DocumentHandler.addSuperAthlete(Main.getDocument());
                    if (element != null)
                        athlete = new SuperAthlete(name, age, stateType, element);
                    break;
                default:
                    return;
            }

            if (athlete != null) {
                persons.add(athlete);
                athletes.add(athlete);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addNewReferee() {
        try {
            if (txtFieldRAge.getText().length() < 1) {
                addRfeedBack.setText("Please input age.");
                return;
            }
            if (txtFieldRName.getText().length() < 3) {
                addRfeedBack.setText("Please enter name.");
                return;
            }

            String sState = (String) cmbRState.getValue();
            if (sState == null) {
                addRfeedBack.setText("Please select state");
                return;
            }

            Integer age = Integer.valueOf(txtFieldRAge.getText());
            String name = txtFieldRName.getText();
            DATA.STATE stateType;

            stateType = DocumentHandler.parseState(sState);
            if(stateType == null) {
                addRfeedBack.setText("Please select state");
            } else {
                addRfeedBack.setText("");
            }

            Element element = DocumentHandler.addReferee(Main.getDocument());
            if (element == null) return;
            Referee referee = new Referee(name, age, stateType, element);
            persons.add(referee);
            officials.add(referee);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
