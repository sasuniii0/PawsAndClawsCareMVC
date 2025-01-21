package lk.ijse.gdse.pawsandclawscaremvc.controller;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.ArrayList;

public class IntroController {

    @FXML
    private ImageView ImgViewSlideShow;

    private ArrayList<Image> images = new ArrayList<>();
    private int currentIndex = 0;

    public void initialize() {
        // Load images into the list
        images.add(new Image(getClass().getResource("/images/pic77.jpg").toExternalForm()));
        images.add(new Image(getClass().getResource("/images/pic7.jpg").toExternalForm()));
        images.add(new Image(getClass().getResource("/images/pic66.jpg").toExternalForm()));


        startSlideshowWithFade();
    }

    private void startSlideshowWithFade() {
        // Set initial image
        if (!images.isEmpty()) {
            ImgViewSlideShow.setImage(images.get(currentIndex));
        }

        // Create a timeline to change the image every 3 seconds
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
            currentIndex = (currentIndex + 1) % images.size(); // Cycle through images

            // Set up fade transition
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), ImgViewSlideShow);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> {
                // Update the image when fade-out completes
                ImgViewSlideShow.setImage(images.get(currentIndex));

                // Fade in after setting the new image
                FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), ImgViewSlideShow);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });

            fadeOut.play();
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
