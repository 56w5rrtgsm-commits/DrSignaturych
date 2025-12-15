import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainController {

    @FXML private TextField nameField, hexField, offsetField, pathField;
    @FXML private TableView<ScanResult> table;
    @FXML private TableColumn<ScanResult,String> fileColumn, sigColumn;
    @FXML private TableColumn<ScanResult,Long> offsetColumn;
    @FXML private TableColumn<ScanResult, Long> sizeColumn;
    @FXML private TableColumn<ScanResult, String> createdColumn;
    @FXML private TableColumn<ScanResult, String> modifiedColumn;

    private final List<Signature> signatures = new ArrayList<>();

    @FXML
    public void initialize() {
        fileColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getFile().getAbsolutePath()));
        sigColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getSignature().getName()));
        offsetColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleLongProperty(
                data.getValue().getSignature().getOffset()).asObject());
        sizeColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleLongProperty(
                data.getValue().getSize()).asObject());
        createdColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getCreated()));
        modifiedColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getModified()));
    }
    @FXML
    public void addSignature() {
        String name = nameField.getText();
        String hexText = hexField.getText();
        String offsetText = offsetField.getText();

        long offset;
        try {
            offset = Long.parseLong(offsetText);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: Offset должен быть числом!");
            return;
        }

        byte[] bytes;
        try {
            bytes = Signature.fromHex(hexText);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: HEX должен состоять только из символов 0-9, A-F и быть чётной длины!");
            return;
        }

        signatures.add(new Signature(name, bytes, offset));

        nameField.clear();
        hexField.clear();
        offsetField.clear();
    }




    @FXML
    public void chooseFolder(){
        DirectoryChooser dc = new DirectoryChooser();
        File dir = dc.showDialog(null);
        if(dir!=null) pathField.setText(dir.getAbsolutePath());
    }

    @FXML
    public void startSearch(){
        table.getItems().clear();
        Scanner scanner = new Scanner(signatures);
        scanner.scanFolder(new File(pathField.getText()), table.getItems()::add);
    }
}
