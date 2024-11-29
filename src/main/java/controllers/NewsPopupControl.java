package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class NewsPopupControl   {

    @FXML
    private Label articleTitleLabel;

    @FXML
    private TextArea articleDescriptionTextArea;

    private Stage popupStage;

    // Initializes the popup with the provided article data
    public void initializePopup(String articleTitle, String articleDescription) {
        setArticleTitle(articleTitle);
        setArticleDescription(articleDescription);
    }

    // Sets the article title in the UI
    private void setArticleTitle(String articleTitle) {
        if (articleTitle != null && !articleTitle.trim().isEmpty()) {
            articleTitleLabel.setText(articleTitle);
        } else {
            articleTitleLabel.setText("No Title Available");
        }
    }

    // Sets the article description in the UI
    private void setArticleDescription(String articleDescription) {
        if (articleDescription != null && !articleDescription.trim().isEmpty()) {
            articleDescriptionTextArea.setText(articleDescription);
        } else {
            articleDescriptionTextArea.setText("No Description Available");
        }
    }

    // Closes the popup stage when the close button is clicked
    @FXML
    private void onCloseButtonClicked() {
        closePopup();
    }

    // Utility method to close the popup window
    private void closePopup() {
        if (popupStage != null) {
            popupStage.close();
        }
    }

    // Setter for the Stage to control the popup window
    public void setPopupStage(Stage popupStage) {
        this.popupStage = popupStage;
    }
}

