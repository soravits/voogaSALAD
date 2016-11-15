package base.integration;

import javafx.scene.Parent;

/**
 * Created by Delia on 11/13/2016.
 */
public interface ISplashScreen {

    Parent setUpWindow();

    void launchWith(); //take GameData param

    void launch();

    void launchGallery();

    void launchSelectedGalleryItem();

}
