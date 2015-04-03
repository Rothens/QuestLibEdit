package hu.silur.QuestEditor;

import hu.silur.QuestEditor.Model.Quest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;


public class ComboBoxCell extends TextFieldTableCell<Quest.Requirement, String> {

    private ComboBox<String> comboBox;

    public ComboBoxCell() {
    }

    @Override
    public void startEdit() {
        super.startEdit();

        if (comboBox == null) {
            createComboBox();
        }

        setGraphic(comboBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setText(String.valueOf(getItem()));
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (comboBox != null) {
                    comboBox.setValue(getString());
                }
                setGraphic(comboBox);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            } else {
                setText(getString());
                setContentDisplay(ContentDisplay.TEXT_ONLY);
            }
        }
    }

    private void createComboBox() {

        ObservableList<String> list =  FXCollections.observableArrayList(
                "Kill","Gather","Use","Visit","Talk"
        );
        comboBox = new ComboBox<>(list);
        comboBox.setMinWidth(this.getWidth() - this.getGraphicTextGap()*2);
        comboBox.setOnKeyPressed(t -> {
            if (t.getCode() == KeyCode.ENTER) {
                commitEdit(comboBox.getSelectionModel().getSelectedItem());
            } else if (t.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });
        comboBox.getSelectionModel().selectFirst();
    }

    private String getString() {
        return getItem() == null ? "" : getItem();
    }
}