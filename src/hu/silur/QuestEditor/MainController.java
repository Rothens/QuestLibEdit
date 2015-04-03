package hu.silur.QuestEditor;

import hu.silur.QuestEditor.Model.Quest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
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
    @FXML private TabPane tabPane;
    @FXML private Button btnAddQuest;
    @FXML private Button btnRemoveQuest;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        questListView.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> {
            if (t1.intValue() == -1)
                return;

            selectedQuest = questList.get(t1.intValue());
            if (selectedQuest != null) {
                titleEdit.setText(selectedQuest.Title);
                descEdit.setText(selectedQuest.Description);
                updateGiverList();
                ongoingEdit.setText(selectedQuest.OngoingText);
                onfinishedEdit.setText(selectedQuest.OnfinishedText);
                updateReqTable();
            }
        });

        TableColumn idColumn = (TableColumn) reqTable.getColumns().get(0);
        idColumn.setCellValueFactory(
                new PropertyValueFactory<Quest.Requirement, Integer>("id")
        );
        TableColumn typeColumn = (TableColumn) reqTable.getColumns().get(1);
        typeColumn.setCellValueFactory(
                new PropertyValueFactory<Quest.Requirement, Integer>("type")
        );
        TableColumn countColumn = (TableColumn) reqTable.getColumns().get(2);
        countColumn.setCellValueFactory(
                new PropertyValueFactory<Quest.Requirement, Integer>("count")
        );

       StringConverter<Integer> intToString = new StringConverter<Integer>() {
            @Override
            public String toString(Integer integer) {
                return integer.toString();
            }

            @Override
            public Integer fromString(String s) {
                return Integer.parseInt(s);
            }
        };
        idColumn.setCellFactory(TextFieldTableCell.forTableColumn(intToString));
        idColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Quest.Requirement, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Quest.Requirement, Integer> cellEditEvent) {
                reqTable.getItems().get(cellEditEvent.getTablePosition().getRow())
                        .setId(cellEditEvent.getNewValue());
            }
        });
        typeColumn.setCellFactory(tableColumn -> new ComboBoxCell());
        typeColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Quest.Requirement, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Quest.Requirement, String> cellEditEvent) {
                reqTable.getItems().get(cellEditEvent.getTablePosition().getRow())
                        .setType(cellEditEvent.getNewValue());
            }
        });
        countColumn.setCellFactory(TextFieldTableCell.forTableColumn(intToString));
        countColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Quest.Requirement, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Quest.Requirement, Integer> cellEditEvent) {
                reqTable.getItems().get(cellEditEvent.getTablePosition().getRow())
                        .setCount(cellEditEvent.getNewValue());
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
            questList = JSONHandler.parseJson(file);
            updateQuestList();
            tabPane.setDisable(false);
            btnAddQuest.setDisable(false);
            btnRemoveQuest.setDisable(false);

        }

    }
    @FXML private void saveBtnClick() {
        //TODO Save JSON file
        if (file!=null && file.exists() && file.canWrite())
            System.out.println("File is writable");
    }

    @FXML private void btnAddQuestClick() {
        selectedQuest = new Quest(questList.size());
        questList.add(selectedQuest);
        updateQuestList();
    }



    @FXML private void btnRemoveQuestClick() {
        int i = questListView.getSelectionModel().getSelectedIndex();
        if (i!=-1) {
            questList.remove(i);
            updateQuestList();
            questListView.getSelectionModel().selectLast();
        }


    }
    @FXML private void btnAddDependencyClick() {
        selectedQuest.requirements.add(new Quest.Requirement(0,"Kill",10));
        updateReqTable();
    }
    @FXML private void btnRemoveDependencyClick() {
        int i = reqTable.getSelectionModel().getSelectedIndex();
        if (i!=-1) {
            selectedQuest.requirements.remove(i);
            updateReqTable();
            reqTable.getSelectionModel().selectLast();
        }
    }

    @FXML private void btnAddQuestGiverClick() {
        selectedQuest.QuestGivers.add(0);
        updateGiverList();
    }

    @FXML private void btnRemoveQuestGiverClick() {
        int i = giverList.getSelectionModel().getSelectedIndex();
        if (i!=-1) {
            selectedQuest.QuestGivers.remove(i);
            updateGiverList();
            giverList.getSelectionModel().selectLast();
        }
    }
    private void updateQuestList() {
        questListView.getSelectionModel().clearSelection();
        ObservableList<String> observableQuestList =
                FXCollections.observableArrayList();
        for (Quest q : questList) {
            observableQuestList.add(q.Title);
        }
        questListView.setItems(observableQuestList);
    }
    private void updateReqTable() {
        ObservableList<Quest.Requirement> reqs = FXCollections.observableArrayList();
        reqs.addAll(selectedQuest.requirements);
        reqTable.setItems(reqs);
    }
    private void updateGiverList() {
        ObservableList<Integer> givers = FXCollections.observableArrayList();
        givers.addAll(selectedQuest.QuestGivers);
        giverList.setItems(givers);
    }

}
