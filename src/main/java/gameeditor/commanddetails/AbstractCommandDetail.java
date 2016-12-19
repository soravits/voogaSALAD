package gameeditor.commanddetails;
import gameeditor.controller.interfaces.IGameEditorData;
import gameeditor.designarea.IDesignArea;
import gameeditor.view.ViewResources;
import gameeditor.view.interfaces.IDetailPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
/**
 * @author John Martin
 *
 */
public abstract class AbstractCommandDetail  implements IAbstractCommandDetail{
    protected IDetailPane myDetailPane;
    protected ScrollPane myContainerPane;
    protected IGameEditorData myDataStore;
    protected IDesignArea myDesignArea;
    protected VBox myVBox;
    protected DetailFrontEndUtil myDetailFrontEndUtil;

    public AbstractCommandDetail() {
        myContainerPane = new ScrollPane();
        myDetailFrontEndUtil = new DetailFrontEndUtil();
        myContainerPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        myContainerPane.setVbarPolicy(ScrollBarPolicy.NEVER);
        myContainerPane.setMinWidth(MY_PANE_WIDTH);
        myContainerPane.setMaxWidth(MY_PANE_WIDTH);
        myContainerPane.setMinHeight(MY_PANE_HEIGHT);
        myContainerPane.setMaxHeight(MY_PANE_HEIGHT);
        myContainerPane.setHmax(0);
        myContainerPane.setLayoutX(DETAIL_PANE_WIDTH/2 - MY_PANE_WIDTH/2 + 10);
        myContainerPane.setLayoutY(DETAIL_PADDING);
        myContainerPane.setBackground(new Background(new BackgroundFill(ViewResources.DETAIL_PANE_BG.getColorResource(), 
                                                                        CornerRadii.EMPTY, Insets.EMPTY)));
        myContainerPane.getStylesheets().add("gameeditor/commanddetails/DetailPane.css");
    }

    public abstract  void init();

    public void setDataStore(IGameEditorData ged){
        myDataStore = ged;
    }


    public void setDesignArea(IDesignArea myDesignArea2){
        myDesignArea = myDesignArea2;
    }

    public ScrollPane getPane(){
        return myContainerPane;
    }

    public void setDetailPane(IDetailPane idp) {
        myDetailPane = idp;
    }

    protected void addVBoxSettings(){
        myVBox = new VBox();
        myVBox.setSpacing(MY_DETAIL_PADDING);
        myVBox.setAlignment(Pos.CENTER);
        myContainerPane.setContent(myVBox);    
    }
}
