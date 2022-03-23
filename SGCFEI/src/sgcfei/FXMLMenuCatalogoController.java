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
 * clicConsutaAcademia: Permite la carga de la vista principal de consulta academia mediante el método irAcademiaConsulta.
 * clicConsultacuerpoAcademico Permite la carga de la vista principal de consulta cuerpo academico mediante  el método irCuerpoAcademicoConsulta.
 * clicConsultaEE : Permite la carga de la vista principal de consulta experiencia educativa mediante  el método irEEConsulta.
 * clicConsultaEstudiante: Permite la carga de la vista principal de consulta estudiante mediante  el método irEstudianteConsulta.
 * clicConsultaLGCA: Permite la carga de la vista principal de consulta LGCA mediante  el método irLGCAConsulta.
 * clicConsultaPersonal: Permite la carga de la vista principal de consulta Personal mediante  el método irPersonalConsulta.
 * clicMenuPrincipla: Permite la carga de la vista principal de Menu mediante  el método irMenuPrincipla.
 * irAcademiaConsulta: Redirige hacia la vista consulta academia.
 * irCuerpoAcademicoConsulta: Redirige hacia la vista consulta cuerpo academico.
 * irEEConsulta: Redirige hacia la vista consulta experiencia educativa.
 * irEstudianteConsulta: Redirige hacia la vista consulta estudiante.
 * irLGCAConsulta: Redirige hacia la vista consulta LGCA.
 *irPersonalConsulta: Redirige hacia la vista consulta Personal 
 *irMenuPrincipal: Redirige hacia la vista menu principla.
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


public class FXMLMenuCatalogoController implements Initializable{

    @FXML
    private Label tfCatalogo;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        // TODO
    }
    
    @FXML
    private void clicMenuPrincipal(ActionEvent event){
        irMenuPrincipal();
    }
    
    @FXML
    private void clicConsultaEstudiante(ActionEvent event){
        irEstudianteConsulta();
    }
    
    @FXML
    private void clicConsultaAcademia(ActionEvent event){
        irAcademiaConsulta();
    }
    
    @FXML
    private void clicConsultaEE(ActionEvent event){
        irEEConsulta();
    }
    
    @FXML
    private void clicConsultaCuerpoAcademico(ActionEvent event){
        irCuerpoAcademicoConsulta();
    }
    
    @FXML
    private void clicConsultaPersonal(ActionEvent event){
        irPersonalConsulta();
    }
    
    @FXML
    private void clicConsultaLGCA(ActionEvent event){
        irLGCAConsulta();
    }
    
    private void irEstudianteConsulta(){
        try{
            Stage stage = (Stage) tfCatalogo.getScene().getWindow();
            Scene sceneEstudiante = new Scene(FXMLLoader.load(getClass().getResource("FXMLEstudianteConsulta.fxml")));
            stage.setScene(sceneEstudiante);
            stage.show();
        }catch(IOException ex) {
            System.out.println("Error al cargar FXML: "+ex.getMessage());
        }
    }
    
    private void irAcademiaConsulta(){
        try{
            Stage stage = (Stage) tfCatalogo.getScene().getWindow();
            Scene sceneAcademia = new Scene(FXMLLoader.load(getClass().getResource("FXMLAcademiaConsulta.fxml")));
            stage.setScene(sceneAcademia);
            stage.show();
        }catch(IOException ex) {
            System.out.println("Error al cargar FXML: "+ex.getMessage());
        }
    }
    
    private void irEEConsulta(){
        try{
            Stage stage = (Stage) tfCatalogo.getScene().getWindow();
            Scene sceneEE = new Scene(FXMLLoader.load(getClass().getResource("FXMLEEConsulta.fxml")));
            stage.setScene(sceneEE);
            stage.show();
        }catch(IOException ex) {
            System.out.println("Error al cargar FXML: "+ex.getMessage());
        }
    }
    
    private void irCuerpoAcademicoConsulta(){
        try{
            Stage stage = (Stage) tfCatalogo.getScene().getWindow();
            Scene sceneCuerpoAcademico = new Scene(FXMLLoader.load(getClass().getResource("FXMLCuerpoAcademicoConsulta.fxml")));
            stage.setScene(sceneCuerpoAcademico);
            stage.show();
        }catch(IOException ex) {
            System.out.println("Error al cargar FXML: "+ex.getMessage());
        }
    }
    
    private void irPersonalConsulta(){
        try{
            Stage stage = (Stage) tfCatalogo.getScene().getWindow();
            Scene scenePersonal = new Scene(FXMLLoader.load(getClass().getResource("FXMLPersonalConsulta.fxml")));
            stage.setScene(scenePersonal);
            stage.show();
        }catch(IOException ex) {
            System.out.println("Error al cargar FXML: "+ex.getMessage());
        }
    }
    
    private void irLGCAConsulta(){
        try{
            Stage stage = (Stage) tfCatalogo.getScene().getWindow();
            Scene sceneLGCA = new Scene(FXMLLoader.load(getClass().getResource("FXMLLGCAConsulta.fxml")));
            stage.setScene(sceneLGCA);
            stage.show();
        }catch(IOException ex) {
            System.out.println("Error al cargar FXML: "+ex.getMessage());
        }
    }
    
    private void irMenuPrincipal(){
        try{
            Stage stage = (Stage) tfCatalogo.getScene().getWindow();
            Scene scenePrincipal= new Scene(FXMLLoader.load(getClass().getResource("FXMLMenuPrincipal.fxml")));
            stage.setScene(scenePrincipal);
            stage.show();
        }catch(IOException ex) {
            System.out.println("Error al cargar FXML: "+ex.getMessage());
        }
    } 
}
