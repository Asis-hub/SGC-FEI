/*
 * Autor: Equipo H
 * Fecha de Creación: 18/Diciembre/2020
 * Ultima Fecha de Modificación: 13/Enero/2021
 * Descripcion: Controlador encargado de redireccionar al modulo seleccionado.
 *
 */

/*
 *Lista de Contenidos
 * Metodos:
 * initialize Método que permite la carga de los componentes principales en pantalla.
 * clicMenuCatalogo: Permite la carga de la vista menu catalogo mediante el método irMenuCatalogo.
 * clicMenuControlUsuarios: Permite la carga de la vista control usuarios mediante  el método irMenuControlUsuarios.
 * clicMenuEgelCeneval: Permite la carga de la vista EGEL-CENEVAL mediante  el método irMenuEgelCeneval.
 * clicMenuTrabajoRecepcional: Permite la carga de la vista trabajo recepcional mediante  el método irMenuTrabajoRecepcional.
 * irMenuCatalogo: Redirige hacia la vista menu catalogo.
 * irMenuControlUsuarios: Redirige hacia la vista menu control usuarios.
 * irMenuEgelCeneval: Redirige hacia la vista menu egel ceneval.
 * irMenuTrabajoRecepcional: Redirige hacia la vista menu trabajo recepcional. 
 */

package sgcfei;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public class FXMLMenuPrincipalController implements Initializable {

    @FXML
    private Label lbModulos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    private void clicMenuCatalogo(ActionEvent event) {
        irMenuCatalogo();
    }
    
    @FXML
    private void clicMenuControlUsuarios(ActionEvent event) {
        irMenuControlUsuarios();
    }

    @FXML
    private void clicMenuTrabajoRecepcional(ActionEvent event) {
        irMenuTrabajoRecepcional();
    }
    
    @FXML
    private void clicMenuEgelCeneval(ActionEvent event) {
        irMenuEgelCeneval();
    }
    
    private void irMenuCatalogo(){
        try{           
            Stage stage = (Stage) lbModulos.getScene().getWindow();
            Scene sceneCatalogo= new Scene(FXMLLoader.load(getClass().getResource("FXMLMenuCatalogo.fxml")));
            stage.setScene(sceneCatalogo);
            stage.show();
        }catch(IOException ex) {
            System.out.println("Error al cargar FXML: "+ex.getMessage());
        }
    }
    
    private void irMenuControlUsuarios(){
        try{           
            Stage stage = (Stage) lbModulos.getScene().getWindow();
            Scene sceneUsuarios= new Scene(FXMLLoader.load(getClass().getResource("FXMLControlUsuarios.fxml")));
            stage.setScene(sceneUsuarios);
            stage.show();
        }catch(IOException ex) {
            System.out.println("Error al cargar FXML: "+ex.getMessage());
        }
    }
    
    private void irMenuTrabajoRecepcional(){
        try{
            Stage stage = (Stage) lbModulos.getScene().getWindow();
            Scene sceneTrabajoRecepcional= new Scene(FXMLLoader.load(getClass().getResource("FXMLTrabajoRecepcionalConsulta.fxml")));
            stage.setScene(sceneTrabajoRecepcional);
            stage.show();
        }catch (IOException ex) {
            System.out.println("Error al cargar FXML: "+ex.getMessage());
        }
    }
    
    private void irMenuEgelCeneval(){
        try{   
            Stage stage = (Stage) lbModulos.getScene().getWindow();
            Scene sceneEgelCeneval = new Scene(FXMLLoader.load(getClass().getResource("FXMLEgelCenevalConsulta.fxml")));
            stage.setScene(sceneEgelCeneval);
            stage.show();
        }catch(IOException ex) {
            System.out.println("Error al cargar FXML: "+ex.getMessage());
        }
    }
}
