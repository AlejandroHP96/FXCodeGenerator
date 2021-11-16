package dad.codegen.UI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dad.codegen.model.javafx.Bean;
import dad.codegen.model.javafx.Type;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ModificarController implements Initializable {

    // model

    private StringProperty nombreProperty = new SimpleStringProperty();

    // view

    @FXML
    private ComboBox<Type> ComboBoxType;

    @FXML
    private CheckBox checkBoxLectura;

    @FXML
    private ComboBox<Bean> comboBoxGeneric;

    @FXML
    private TextField nombreTextField;

    @FXML
    private Button volverButton;

    @FXML
    private GridPane view;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        nombreTextField.textProperty().bind(nombreProperty);

    }

    public ModificarController() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ModificarView.fxml"));
        loader.setController(this);
        loader.load();
    }

    @FXML
    void onVolverButtonAction(ActionEvent event) {

        
    }

    public GridPane getView() {
        return view;
    }
}
