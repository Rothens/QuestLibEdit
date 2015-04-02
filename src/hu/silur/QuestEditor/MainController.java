package hu.silur.QuestEditor;

import hu.silur.QuestEditor.Model.Quest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable{
    private File file;
    private List<Quest> questList;
    private Quest selectedQuest;
    @FXML private TextField editFile;
    @FXML private ListView<String> questListView;
    @FXML private TextField titleEdit;
    @FXML private TextArea descEdit;
    @FXML private ListView<Integer> giverList;
    @FXML private TextArea ongoingEdit;
    @FXML private TextArea onfinishedEdit;
    @FXML private TableView<Quest.Requirement> reqTable;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        questListView.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> {
            selectedQuest = questList.get(t1.intValue());
            if (selectedQuest!=null) {
                titleEdit.setText(selectedQuest.Title);
                descEdit.setText(selectedQuest.Description);
                ObservableList<Integer> givers = FXCollections.observableArrayList();
                givers.addAll(selectedQuest.QuestGivers);
                giverList.setItems(givers);
                ongoingEdit.setText(selectedQuest.OngoingText);
                onfinishedEdit.setText(selectedQuest.OnfinishedText);
                ((TableColumn) reqTable.getColumns().get(0)).setCellValueFactory(
                        new PropertyValueFactory<Quest.Requirement, Integer>("id")
                );
                ((TableColumn) reqTable.getColumns().get(1)).setCellValueFactory(
                        new PropertyValueFactory<Quest.Requirement, Integer>("type")
                );
                ((TableColumn) reqTable.getColumns().get(2)).setCellValueFactory(
                        new PropertyValueFactory<Quest.Requirement, Integer>("count")
                );
                ObservableList<Quest.Requirement> reqs = FXCollections.observableArrayList();
                reqs.addAll(selectedQuest.requirements);
                reqTable.setItems(reqs);
                System.out.println("reqs.size() = " + reqs.size());
            }
        });

    }

    @FXML private void openBtnClick() {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);
        //Show open file dialog
        file = fileChooser.showOpenDialog(null);
        editFile.setText(file.getPath());
        if (file.exists() && file.canRead()) {
            System.out.println("File is readable");
            questList = JSONHandler.parseJson(file);
            ObservableList<String> observableQuestList =
                    FXCollections.observableArrayList();
            for (Quest q : questList) {
                observableQuestList.add(q.Title);
            }
            questListView.setItems(observableQuestList);

        }

    }
    @FXML private void saveBtnClick() {
        //TODO Save JSON file
        if (file!=null && file.exists() && file.canWrite())
            System.out.println("File is writable");
    }



}
