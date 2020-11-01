package sample;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.*;


public class Controller implements Initializable {

    @FXML
    public Button addButton;

    @FXML
    public Button searchButton;

    @FXML
    public Button updateButton;

    @FXML
    public Button deleteButton;

    @FXML
    public Button soundButton;

    @FXML
    public Button apiButton;

    @FXML
    public TextArea wordTextArea;

    @FXML
    public ListView<String> wordListView;

    @FXML
    public TextField wordTextField;

    @FXML
    public TextArea meaningTextArea;

    public Controller() {
    }

    // maps: https://www.javatpoint.com/java-map
    Map<String,String> Dictionary = new HashMap<String,String>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.createDic();
        } catch (Exception e) {
            e.printStackTrace();
        }

        searchButton.setOnMouseClicked(event -> {
            String searchedWord = wordTextField.getText();
            if (searchedWord != null) {
                String wordMeaning = Dictionary.get(searchedWord);
                meaningTextArea.setText(wordMeaning);
                meaningTextArea.setStyle("-fx-text-fill: #1C1C1C; -fx-font-size: 20px;");
                wordTextArea.setText(searchedWord);
                Vector<String> results = new Vector<String>();
                for (String s: Dictionary.keySet()) {
                    if (s.startsWith(searchedWord)) {
                        results.add(s);
                    }
                }
                wordListView.getItems().clear();
                wordListView.getItems().addAll(results);
            } else {
                wordListView.getItems().clear();
                wordListView.getItems().addAll(Dictionary.keySet());
            }
        });

        wordListView.setOnMouseClicked(event -> {
            String searchedWord = wordListView.getSelectionModel().getSelectedItem();
            if (searchedWord != null && !searchedWord.equals(" ")){
                String wordMeaning = Dictionary.get(searchedWord);
                meaningTextArea.setText(wordMeaning);
                meaningTextArea.setStyle("-fx-text-fill: #1C1C1C; -fx-font-size: 20px;");
                wordTextArea.setText(searchedWord);
                wordTextArea.setStyle("-fx-text-fill: #1C1C1C; -fx-font-size: 27px;");
            }
        });

        // https://www.howkteam.vn/course/lap-trinh-javafx-co-ban/thong-bao-alert-trong-javafx-2641
        deleteButton.setOnAction(event -> {
            String deletedWord = wordListView.getSelectionModel().getSelectedItem();
            if (!deletedWord.equals(" ")) {
                Alert.AlertType alertAlertType;
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Notification");
                alert.setHeaderText("DELETE CONFIRMATION");
                alert.setContentText("Do you really want to delete *** "  + deletedWord + " *** from dictionary?");
                Optional<ButtonType> optional = alert.showAndWait();
                if (optional.get() == ButtonType.OK) {
                    Dictionary.remove(deletedWord);
                    wordTextField.clear();
                    wordListView.getItems().clear();
                    wordListView.getItems().addAll(Dictionary.keySet());
                }

            }
        });

        // https://www.howkteam.vn/course/lap-trinh-javafx-co-ban/thay-doi-dialog-trong-javafx-2642
        addButton.setOnAction(event -> {
            Dialog<Pair<String,String>> dialog = new Dialog<>();
            dialog.setTitle("Dialog");
            dialog.setHeaderText("Add Word To Dictionary");

            ButtonType addButtonType = new ButtonType("Add",ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(20,150,10,10));

            TextField newTarget = new TextField();
            newTarget.setPromptText("New Target");
            TextField newExplain = new TextField();
            newExplain.setPromptText("New Explain");

            // cot,hang
            gridPane.add(new Label("New Target"),0,0);
            gridPane.add(newTarget,1,0);
            gridPane.add(new Label("New Explain"),0,1);
            gridPane.add(newExplain,1,1);

            Node addButtonNode = dialog.getDialogPane().lookupButton(addButtonType);
            addButtonNode.setDisable(true);

            newTarget.textProperty().addListener((observableValue, oldValue, newValue) -> {
                addButtonNode.setDisable(newValue.trim().isEmpty());
            });

            dialog.getDialogPane().setContent(gridPane);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == addButtonType){
                    return new Pair<>(newTarget.getText(), newExplain.getText());
                }
                return null;
            } );
            Optional<Pair<String, String>> result = dialog.showAndWait();
            result.ifPresent(word -> {
                if (Dictionary.containsKey(word.getKey())) {
                } else {
                    Dictionary.put(word.getKey(),word.getValue());
                    wordListView.getItems().add(word.getKey());
                }
            });
        });

        // Update both target and explain
        updateButton.setOnMouseClicked(event -> {
            String updateTargetWord = wordListView.getSelectionModel().getSelectedItem();
            String updateExplainWord = Dictionary.get(updateTargetWord);
            Dictionary.remove(updateTargetWord);
            wordTextField.clear();
            wordListView.getItems().clear();
            Dialog<Pair<String,String>> dialog = new Dialog<>();
            dialog.setTitle("Dialog");
            dialog.setHeaderText("Update Word In Dictionary");

            ButtonType updateButtonType = new ButtonType("Update",ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(20,150,10,10));

            TextField updateTarget = new TextField();
            updateTarget.setPromptText(updateTargetWord);
            TextField updateExplain = new TextField();
            updateExplain.setPromptText(updateExplainWord);

            // cot,hang
            gridPane.add(new Label("Update Target"),0,0);
            gridPane.add(updateTarget,1,0);
            gridPane.add(new Label("Update Explain"),0,1);
            gridPane.add(updateExplain,1,1);

            Node updateButtonNode = dialog.getDialogPane().lookupButton(updateButtonType);
            updateButtonNode.setDisable(true);

            updateTarget.textProperty().addListener((observableValue, oldValue, newValue) -> {
                updateButtonNode.setDisable(newValue.trim().isEmpty());
            });

            dialog.getDialogPane().setContent(gridPane);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == updateButtonType){
                    return new Pair<>(updateTarget.getText(), updateExplain.getText());
                }
                return null;
            } );
            Optional<Pair<String, String>> result = dialog.showAndWait();
            result.ifPresent(word -> {
                if (Dictionary.containsKey(word.getKey())) {
                    return;
                } else {
                    Dictionary.put(word.getKey(),word.getValue());
                    wordListView.getItems().add(word.getKey());
                }
            });
        });

        // https://www.youtube.com/watch?v=swuYhvwHw9w
        soundButton.setOnMouseClicked(event -> {
            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
            String searchedWord = wordTextArea.getText();
            Voice voice;
            VoiceManager voiceManager = VoiceManager.getInstance();
            voice = voiceManager.getVoice("kevin16");
            voice.allocate();
            voice.speak(searchedWord);
        });
        apiButton.setOnMouseClicked(event -> {
            String apiTarget = wordTextField.getText();
            try {
                String apiMeaning = API.translate("en", "vi", apiTarget);
                meaningTextArea.setText(apiMeaning);
                meaningTextArea.setStyle("-fx-text-fill: #1C1C1C; -fx-font-size: 20px;");
                wordTextArea.setText(apiTarget);
                wordTextArea.setStyle("-fx-text-fill: #1C1C1C; -fx-font-size: 27px;");
                System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
                Voice voice;
                VoiceManager voiceManager = VoiceManager.getInstance();
                voice = voiceManager.getVoice("kevin16");
                voice.allocate();
                voice.speak(API.translate("en", "vi", apiMeaning));

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void createDic() throws Exception {
        DictionaryManagement dictionaryManagement = new DictionaryManagement();
        Vector<Word> words = dictionaryManagement.insertFromDatabase();
        try
        {
            for (Word word : words) {
                Dictionary.put(word.getWord_target(), word.getWord_explain());
            }
            wordListView.getItems().addAll(Dictionary.keySet());
            // keySet = target
            // value = explain
        } catch (NullPointerException e) {
            System.out.println("ERROR Create Dic");
        }
    }

}

