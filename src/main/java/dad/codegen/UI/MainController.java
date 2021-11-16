package dad.codegen.UI;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dad.codegen.model.javafx.Bean;
import dad.codegen.model.javafx.FXModel;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainController implements Initializable {

    //controllers

    private BeanController beanController = new BeanController();

    // model

    private ObjectProperty<FXModel> fxModel = new SimpleObjectProperty<>();
    private ObjectProperty<Bean> selectBean = new SimpleObjectProperty<>();
    private StringProperty packegeName = new SimpleStringProperty();
    private ListProperty<Bean> beans = new SimpleListProperty<>(FXCollections.observableArrayList());
    

    @FXML
    private Button abrirButton;

    @FXML
    private ListView<Bean> beanList;

    @FXML
    private Button eliminarBeanButton;

    @FXML
    private Button generarButton;

    @FXML
    private Button guardarButton;

    @FXML
    private VBox noBeanPane;

    @FXML
    private Button nuevoBeanButton;

    @FXML
    private Button nuevoButton;

    @FXML
    private TextField paqueteText;

    @FXML
    private BorderPane rightPane;

    @FXML
    private GridPane view;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // bindings

        paqueteText.textProperty().bindBidirectional(packegeName);
        beanList.itemsProperty().bind(beans);
        selectBean.bind(beanList.getSelectionModel().selectedItemProperty());

        eliminarBeanButton.disableProperty().bind(selectBean.isNull());

        beanController.beanProperty().bind(selectBean);
        beanController.beansProperty().bind(beans);

        // listenerss
        fxModel.addListener((o, ov, nv) -> onFXModelChanged(o, ov, nv));

        selectBean.addListener((o,ov,nv) -> onSelectedBeanChanged(o,ov,nv));

        fxModel.set(new FXModel());

    }

    private void onSelectedBeanChanged(ObservableValue<? extends Bean> o, Bean ov, Bean nv) {

        if (nv != null) {
            
           rightPane.setCenter(beanController.getView()) ;
        }
        //no hay ningún bean seleccionado
        else{
            rightPane.setCenter(noBeanPane);
        }

    }

    private void onFXModelChanged(ObservableValue<? extends FXModel> o, FXModel ov, FXModel nv) {

        if (ov != null) {

            System.out.println("El modelo viejos es: " + ov.getPackageName());
            packegeName.unbindBidirectional(ov.packageNameProperty());
            beans.unbind();
        }
        if (nv != null) {

            packegeName.bindBidirectional(nv.packageNameProperty());
            beans.bind(nv.beansProperty());
            System.out.println("El modelo nuevo es: " + nv.getPackageName());
        }
    }

    public MainController() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
        loader.setController(this);
        loader.load();
    }

    @FXML
    void onAbrirAction(ActionEvent event) {

        if (FXCodeGenApp.confirm("Abrir Modelo FX",
                "Va a abrir un modelo FX desde fichero. \n\nLos cambios que haya realizado en el modelo actual se perderán.",
                "¿Desea continuar?")) {

            FileChooser fileChooser = new FileChooser();

            fileChooser.setTitle("Abrir Modelo FX");
            fileChooser.getExtensionFilters().add(new ExtensionFilter("Modelo FX", "*.fx"));
            fileChooser.getExtensionFilters().add(new ExtensionFilter("Todos los archivos", "*.*"));
            fileChooser.setInitialDirectory(new File("."));
            File fichero = fileChooser.showOpenDialog(FXCodeGenApp.getPrimaryStage());
            if (fichero != null) {
                try {

                    fxModel.set(FXModel.load(fichero));

                } catch (Exception e) {
                    e.printStackTrace();
                    FXCodeGenApp.error("Error al abrir el model FX desde el fichero '" + fichero.getName() + ".'", e);
                }
            }
        }

    }

    @FXML
    void onGenerarAction(ActionEvent event) {

        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Seleccinar carpeta para generar codigo");
        dirChooser.setInitialDirectory(new File("."));
        File directorio = dirChooser.showDialog(FXCodeGenApp.getPrimaryStage());
        if (directorio != null) {
            try {
                fxModel.get().generateCode(directorio);

                FXCodeGenApp.info("Se ha generado el código");
            } catch (Exception e) {
                e.printStackTrace();
                FXCodeGenApp.error(
                        "Error al generar el código del modelo FX en el directorio '" + directorio.getName() + "'.", e);
            }
        }

    }

    @FXML
    void onGuardarAction(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Guardar Modelo FX");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Modelo FX", "*.fx"));
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Todos los archivos", "*.*"));
        fileChooser.setInitialDirectory(new File("."));
        File fichero = fileChooser.showSaveDialog(FXCodeGenApp.getPrimaryStage());
        if (fichero != null) {
            try {

                fxModel.get().save(fichero);

            } catch (Exception e) {
                e.printStackTrace();
                FXCodeGenApp.error("Error al guardar el model FX en el fichero '" + fichero.getName() + ".'", e);
            }
        }

    }

    @FXML
    void onNuevoAction(ActionEvent event) {

        if (FXCodeGenApp.confirm("Nuevo Modelo FX",
                "Se va a crear un modelo FX. \n\nLos cambios que haya realizado en el modelo actual se perderán.",
                "¿Desea continuar?")) {
            fxModel.set(new FXModel());
        }

    }

    @FXML
    void onNuevoBeanAction(ActionEvent event) {

        Bean nuevo = new Bean();
        nuevo.setName("NuevoBean");

        fxModel.get().getBeans().add(nuevo);

        beanList.getSelectionModel().select(nuevo);

    }

    @FXML
    void onEliminarBeanAction(ActionEvent event) {

        if (FXCodeGenApp.confirm(
            "Eliminar bean",
             "Se va a eliminar el bean '" + selectBean.get().getName() + "'",
              "Desea continuar")) {

                fxModel.get().getBeans().remove(selectBean.get());
            
        }

    }

    public GridPane getView() {
        return view;
    }
}
