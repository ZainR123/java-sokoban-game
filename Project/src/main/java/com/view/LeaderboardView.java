package com.view;

import com.model.engine.GameModel;
import com.model.features.LeaderboardModel;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.MotionBlur;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * View class for Leaderboard
 * Outputs scene to game when requested
 *
 * @author Zain Rashid
 */
public class LeaderboardView {

    private final Stage m_dialogStage;
    private final GameModel m_gameModel;
    private Stage m_leaderboardStage;

    /**
     * LeaderboardView constructor to get current instances of certain objects
     *
     * @param m_dialogStage    dialog box stage
     * @param m_gameModel GameModel current instance
     */
    public LeaderboardView(Stage m_dialogStage, GameModel m_gameModel) {
        this.m_dialogStage = m_dialogStage;
        this.m_gameModel = m_gameModel;
    }

    /**
     * Create leaderboard dialog box and output to user
     */
    public void leaderboardDialog() {
        LeaderboardModel leaderboardModel =
                new LeaderboardModel(m_gameModel);
        m_leaderboardStage = new Stage();
        m_leaderboardStage.setResizable(false);
        m_leaderboardStage.setTitle(GameModel.getGameName());

        Text title = new Text("Level Leaderboard");
        title.setTextAlignment(TextAlignment.CENTER);
        title.getStyleClass().add("subTitle");


        Text body = new Text(leaderboardModel.printArray());
        body.setTextAlignment(TextAlignment.LEFT);
        body.setFont(Font.font(java.awt.Font.MONOSPACED, 16));
        body.setStyle("-fx-font-weight: BOLD");

        MotionBlur mb = new MotionBlur(2, 1);
        body.setEffect(mb);

        Button back = new Button("Back");
        back.getStyleClass().add("exitButton");
        back.setOnAction(e -> {
            m_leaderboardStage.close();
            m_dialogStage.show();
        });
        VBox leaderboardVbox = new VBox();

        leaderboardVbox.setAlignment(Pos.CENTER);
        leaderboardVbox.getStylesheets().add("style.css");
        leaderboardVbox.getStyleClass().add("background");
        leaderboardVbox.getStyleClass().add("dialogMessage");
        leaderboardVbox.getChildren().addAll(title, body, back);

        Scene leaderboardScene = new Scene(leaderboardVbox, 500, 500);
        m_leaderboardStage.setScene(leaderboardScene);
        m_leaderboardStage.show();
    }
}