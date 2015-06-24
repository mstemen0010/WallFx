/**
 * Sample Skeleton for 'WallFXMLDocument.fxml' Controller Class
 */
package wallfx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class WallFXMLDocumentController extends AnchorPane {
    
    protected WallChanger changer = null;
    private String imagePathName = "C:\\Users\\matthew.g.stemen\\Pictures\\Backgrounds";
    private ObservableList<String> imageObservableList;
    private Path currentPath;
    
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;
    
    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    
    @FXML // fx:id="imageThumbNail"
    private ImageView imageThumbNail; // Value injected by FXMLLoader

    @FXML // fx:id="loadPathButton"
    private Button loadPathButton; // Value injected by FXMLLoader

    @FXML // fx:id="imagePath"
    private TextField imagePath; // Value injected by FXMLLoader

    @FXML // fx:id="imageList"
    private ListView<String> imageList; // Value injected by FXMLLoader

    @FXML
    private ToggleGroup toggleGroup;
    
    @FXML
    private RadioButton tileButton;
    @FXML
    private RadioButton fitButton;
    @FXML
    private RadioButton centeredButton;
    @FXML
    private RadioButton fillButton;
    @FXML
    private RadioButton streachButton;
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert imageThumbNail != null : "fx:id=\"imageThumbNail\" was not injected: check your FXML file 'WallFXMLDocument.fxml'.";
        assert loadPathButton != null : "fx:id=\"loadPathButton\" was not injected: check your FXML file 'WallFXMLDocument.fxml'.";
        assert imagePath != null : "fx:id=\"imagePath\" was not injected: check your FXML file 'WallFXMLDocument.fxml'.";
        assert imageList != null : "fx:id=\"imageList\" was not injected: check your FXML file 'WallFXMLDocument.fxml'.";
        List<String> values = Arrays.asList("one", "two", "three");
        this.imageObservableList = FXCollections.observableList(values);
        changer = new WallChanger();
        toggleGroup = new ToggleGroup();
        this.tileButton.setToggleGroup(toggleGroup);
        this.fillButton.setToggleGroup(toggleGroup);
        this.centeredButton.setToggleGroup(toggleGroup);
        this.fitButton.setToggleGroup(toggleGroup);
        this.streachButton.setToggleGroup(toggleGroup);
        // this.tileButton.setId(  );
        

        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                    Toggle old_toggle, Toggle new_toggle) {
                if (toggleGroup.getSelectedToggle() != null) {
                    RadioButton selected = (RadioButton) toggleGroup.getSelectedToggle();
                    System.out.println("Selected is: " + selected.getId());
                    if (selected.getId().equals("tileButton")) {
                        changer.setTiled(1);
                    } else if (selected.getId().equals("centeredButton")) {                        
                        changer.setTiled(0);
                    } else if (selected.getId().equals("fillButton")) {
                        changer.setStyle(WallChanger.StyleType.Fill);                        
                    } else if (selected.getId().equals("fitButton")) {
                        changer.setStyle(WallChanger.StyleType.Fit);                        
                    } else if (selected.getId().equals("streachButton")) {
                        changer.setStyle(WallChanger.StyleType.Stretched);                        
                    }
                    
                }
            }
        });
        
        
        imagePath.setOnKeyPressed(
            new EventHandler<KeyEvent>() {
                public void handle(KeyEvent event) {
                    updateImagePathName(event); 
                }
            });
        
    }
    
    @FXML
    void loadFromPath() {
        ArrayList<String> extNameArray = new ArrayList<>();
        extNameArray.add("jpg");
        extNameArray.add("jpeg");
        extNameArray.add("bmp");
        
        ArrayList<String> imageNameArray = new ArrayList<>();
        try {
            Files.walk(Paths.get(this.imagePathName)).forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    String imageName = filePath.getFileName().toString();
                    this.currentPath = filePath.getParent();
                    // System.out.println("Current Path is: " + this.currentPath);
                    // System.out.println("Current value is" + filePath);
                    boolean extFound = false;
                    Iterator<String> it = extNameArray.iterator();
                    while (it.hasNext()) {
                        extFound = imageName.toLowerCase().contains(it.next().toLowerCase());
                        if (extFound) {
                            break;
                        }
                    }
                    if (extFound) {
                        imageNameArray.add(filePath.getFileName().toString());
                        System.out.println(filePath.getFileName());
                    }
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(WallFXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        imageList.setItems(FXCollections.observableList(imageNameArray));
        
    }
    
    @FXML
    public void setStyle(Event evt
    ) {
        RadioButton selected = (RadioButton) this.toggleGroup.getSelectedToggle();
        System.out.println("Toggle style is: " + selected.getText());
    }
    
    @FXML
    public void loadImageThumbNail(Event evt
    ) {
        String imagePath = this.imagePath.getText();
        String imageName = this.imageList.getSelectionModel().getSelectedItem();
        String imageFullPath = imagePath + "\\" + imageName;
        File initialFile = new File(imagePath);
        Image img;
        InputStream is = null;
        StringBuilder fullImagePathSb = new StringBuilder("file:");
        fullImagePathSb.append(currentPath);
        fullImagePathSb.append("\\");
        fullImagePathSb.append(imageName);
        // is = new FileInputStream(initialFile);
        System.out.println("Attempting to load: " + fullImagePathSb);
        img = new Image(fullImagePathSb.toString());
        this.imageThumbNail.setImage(img);
        this.changer.load(currentPath + "\\" + imageName);
        
    }
    
    @FXML
    public void updateImagePathName(Event event)
    {
        this.imagePathName = this.imagePath.getText();
        System.out.println("Image path is now: " + this.imagePathName);
    }
}
