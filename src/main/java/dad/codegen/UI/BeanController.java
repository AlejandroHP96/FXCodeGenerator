package dad.codegen.UI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dad.codegen.model.javafx.Bean;
import dad.codegen.model.javafx.Property;
import dad.codegen.model.javafx.Type;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.GridPane;

public class BeanController implements Initializable {

    //controllers

    ModificarController modificarController = new ModificarController();

    //model

    private StringProperty name = new SimpleStringProperty();
    private ObjectProperty<Bean> parent = new SimpleObjectProperty<>();
    private ListProperty<Property> properties = new SimpleListProperty<>(FXCollections.observableArrayList());

    private ObjectProperty<Bean> bean = new SimpleObjectProperty<>();
    private ListProperty<Bean> beans = new SimpleListProperty<>(FXCollections.observableArrayList());

    // view

    @FXML
    private Button editarPropiedadButton;

    @FXML
    private Button eliminarPropiedadButton;

    @FXML
    private TableColumn<Property, Bean> genericoColumn;

    @FXML
    private TableColumn<Property, String> nombreColumn;

    @FXML
    private TextField nombreText;

    @FXML
    private Button nuevaPropiedadButton;

    @FXML
    private ComboBox<Bean> padreCombo;

    @FXML
    private TableView<Property> propiedadesTable;

    @FXML
    private Button quitarPadreButton;

    @FXML
    private TableColumn<Property, Boolean> soloLecturaColumn;

    @FXML
    private TableColumn<Property, Type> tipoColumn;

    @FXML
    private GridPane view;

    public GridPane getView() {
        return view;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // bindings

        nombreText.textProperty().bindBidirectional(name);
        padreCombo.valueProperty().bindBidirectional(parent);
        padreCombo.itemsProperty().bind(beans);
        propiedadesTable.itemsProperty().bind(properties);

        quitarPadreButton.disableProperty().bind(parent.isNull());

        //cell value factories

        nombreColumn.setCellValueFactory(v -> v.getValue().nameProperty());
        soloLecturaColumn.setCellValueFactory(v -> v.getValue().readOnlyProperty());
        tipoColumn.setCellValueFactory(v -> v.getValue().typeProperty());
        genericoColumn.setCellValueFactory(v -> v.getValue().genericProperty());

        //cell factories

        soloLecturaColumn.setCellFactory(CheckBoxTableCell.forTableColumn(soloLecturaColumn));

        //listener

        bean.addListener((o, ov, nv) -> onBeanChanged(o, ov, nv));

    }

    private void onBeanChanged(ObservableValue<? extends Bean> o, Bean ov, Bean nv) {

        if (ov != null) {
            name.unbindBidirectional(ov.nameProperty());
            parent.unbindBidirectional(ov.parentProperty());
            properties.unbind();
        }
        if (nv != null) {
            name.bindBidirectional(nv.nameProperty());
            parent.bindBidirectional(nv.parentProperty());
            properties.bind(nv.propertiesProperty());
        }
    }

    public BeanController() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/BeanView.fxml"));
        loader.setController(this);
        loader.load();
    }

    @FXML
    void onEditarPropiedadButton(ActionEvent event) {

    }

    @FXML
    void onEliminarPropiedadButton(ActionEvent event) {

    }

    @FXML
    void onNuevaPropiedadButton(ActionEvent event) {

        Property nuevo = new Property();
        nuevo.setName("nuevaPropiedad");
        nuevo.setType(Type.STRING);
        nuevo.setReadOnly(false);
        nuevo.setGeneric(null);
        
        properties.setAll(nuevo);

        propiedadesTable.getSelectionModel().select(nuevo);

    }

    @FXML
    void onQuitarPadreButton(ActionEvent event) {

        parent.set(null);
    }
    // getters y setters

    public final ObjectProperty<Bean> beanProperty() {
		return this.bean;
	}

	public final Bean getBean() {
		return this.beanProperty().get();
	}

	public final void setBean(final Bean bean) {
		this.beanProperty().set(bean);
	}

	public final ListProperty<Bean> beansProperty() {
		return this.beans;
	}

	public final ObservableList<Bean> getBeans() {
		return this.beansProperty().get();
	}

	public final void setBeans(final ObservableList<Bean> beans) {
		this.beansProperty().set(beans);
	}

}
