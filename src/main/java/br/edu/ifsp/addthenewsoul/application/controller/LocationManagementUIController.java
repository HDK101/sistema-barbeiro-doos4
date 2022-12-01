package br.edu.ifsp.addthenewsoul.application.controller;

import br.edu.ifsp.addthenewsoul.application.view.WindowLoader;
import br.edu.ifsp.addthenewsoul.domain.entities.asset.Asset;
import br.edu.ifsp.addthenewsoul.domain.entities.asset.Location;
import br.edu.ifsp.addthenewsoul.domain.usecases.UseCases;
import br.edu.ifsp.addthenewsoul.domain.usecases.location.FindLocationUseCase;
import br.edu.ifsp.addthenewsoul.domain.usecases.location.RemoveLocationUseCase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;

public class LocationManagementUIController {

    @FXML
    private TableView<Location> tableView;

    @FXML
    private TableColumn<Location, Integer> cNumber;

    @FXML
    private TableColumn<Location, String> cSection;

    @FXML
    private TableColumn<Location, Integer> cId;

    @FXML
    private TableColumn<Location, List<Integer>> cAssets;

    private ObservableList<Location> tableData;

    @FXML
    private void initialize(){
        bindTableViewToItemsList();
        bindColumnsToValueSources();
        loadDataAndShow();
    }

    private void bindTableViewToItemsList() {
        tableData = FXCollections.observableArrayList();
        tableView.setItems(tableData);
    }

    private void bindColumnsToValueSources() {
        cId.setCellValueFactory(new PropertyValueFactory<>("id"));
        cNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
        cSection.setCellValueFactory(new PropertyValueFactory<>("section"));
        // Implementar o value factory da propriedade Assets de Location.
    }

    private void loadDataAndShow() {
        FindLocationUseCase findLocationUseCase = UseCases.getInstance().findLocationUseCase;
        List<Location> locations = findLocationUseCase.findAll();
        tableData.clear();
        tableData.addAll(locations);
    }

    @FXML
    void addLocation(ActionEvent event) throws IOException {
        WindowLoader.setRoot("LocationUI");
    }

    @FXML
    void editLocation(ActionEvent event) throws IOException {
        Location selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null)
            WindowLoader.setRoot("LocationUI");
            LocationUIController controller = (LocationUIController) WindowLoader.getController();
            controller.setLocation(selectedItem, UIMode.UPDATE);
    }

    @FXML
    void removeLocation(ActionEvent event) {
        Location selectedItem = tableView.getSelectionModel().getSelectedItem();
        RemoveLocationUseCase removeLocationUseCase = UseCases.getInstance().removeLocationUseCase;
        if (selectedItem != null)
            try {
                removeLocationUseCase.deleteLocation(selectedItem);
                loadDataAndShow();
            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Erro");
                errorAlert.setContentText(e.getMessage());
                errorAlert.showAndWait();
            }
    }

    @FXML
    void backToPreviousScene(ActionEvent event) {
        // Voltar para dashboard
    }
}
