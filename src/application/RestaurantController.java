package application;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import jDBCProjet.connexion.Connexion;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

public class RestaurantController implements Initializable {

	@FXML
	private Button ajouterID;

	@FXML
	private Button supID;

	@FXML
	private Button calcId;

	@FXML
	private ComboBox<String> platCom;

	@FXML
	private TextField quantId;

	@FXML
	private RadioButton SupOUI;

	@FXML
	private RadioButton supNon;

	@FXML
	private TextField MMT;
	@FXML
	private TableView<Plat_cmd> tableau;

	@FXML
	private TableColumn<Plat_cmd, String> Tnom;

	@FXML
	private TableColumn<Plat_cmd, Integer> Tquan;

	@FXML
	private TableColumn<Plat_cmd, Double> TSupp;

	@FXML
	private TableColumn<Plat_cmd, Double> Tprix;

	@FXML
	private TableColumn<Plat_cmd, Double> Tmont;

	@FXML
	void CalcBtn(ActionEvent event) {
		double Total = 0;
		for (int i = 0; i < tableau.getItems().size(); i++) {
			System.out.println(Tmont.getCellData(i));
			Total = Total + Tmont.getCellData(i);
			
		}

		MMT.setText(String.valueOf(Total));
		BufferedWriter outputStream;
		try {
			outputStream = new BufferedWriter(new FileWriter("D:\\fichiers\\commande.txt"));

			outputStream.write("le montant total de la commande :" + MMT.getText());
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML
	void SupBtn(ActionEvent event) {

		Plat_cmd selectedPlat = tableau.getSelectionModel().getSelectedItem();
	    if (selectedPlat != null) {
	        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	        alert.setTitle("Confirmation");
	        alert.setHeaderText("Supprimer plat");
	        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce plat ?");
	        Optional<ButtonType> result = alert.showAndWait();
	        if (result.get() == ButtonType.OK) {
	            list1.remove(selectedPlat);
	        }
	    } else {
	        Alert alert = new Alert(Alert.AlertType.WARNING);
	        alert.setTitle("Avertissement");
	        alert.setHeaderText(null);
	        alert.setContentText("Veuillez sélectionner un plat à supprimer.");
	        alert.showAndWait();
	    }

	}

	@FXML
	void ajouterBtn(ActionEvent event) {
	    String libelle = platCom.getValue();
	    System.out.println(libelle);
	    String query = "select prix from Plat where libelle = '" + libelle + "'";
	    ResultSet rs = null;
	    Statement stmt = null;
	    double prix = 0;
	    try {
	        stmt = Connexion.getConn().createStatement();
	        rs = stmt.executeQuery(query);
	        while (rs.next()) {
	            prix = rs.getDouble(1);
	            System.out.println(prix);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    int quant = Integer.valueOf(quantId.getText());
	    System.out.println(quant);

	    double prixsupp = 0;
	    if (SupOUI.isSelected()) {
	        prixsupp = 2000.0;
	    }
	    double totale = quant * prix + prixsupp;
        System.out.println(totale);

	    tableau.getItems().add(new Plat_cmd(platCom.getValue(), quant, prixsupp,prix,totale));
	}

	ObservableList<Plat_cmd> list1 = FXCollections.observableArrayList();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ArrayList<String> list = new ArrayList<String>();
		// à compléter
		/*
		 * La liste construite va être attribuer à l’objet ComboBox cbPlat (une liste
		 * déroulante) pour lister les plats disponibles.
		 */

		String query = "select libelle from Plat";
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = Connexion.getConn().createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {

				list.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		ObservableList<String> cb = FXCollections.observableArrayList(list);
		platCom.setItems(cb);
		Tnom.setCellValueFactory(new PropertyValueFactory<Plat_cmd, String>("libelle"));
		Tquan.setCellValueFactory(new PropertyValueFactory<Plat_cmd, Integer>("quantite"));
		Tprix.setCellValueFactory(new PropertyValueFactory<Plat_cmd, Double>("PU"));
		TSupp.setCellValueFactory(new PropertyValueFactory<Plat_cmd, Double>("supplement"));
		Tmont.setCellValueFactory(new PropertyValueFactory<Plat_cmd, Double>("montant"));

		

		tableau.setItems(list1);
	}

}
