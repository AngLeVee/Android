//Angela LeVee (alevee@cnm.edu)
//LeVeeP6 FXMLDocumentController.java
//Contains all data for the form
package leveep1;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import static oracle.jrockit.jfr.events.Bits.intValue;

/**
 *
 * @author Angela
 */
public class FXMLDocumentController implements Initializable {

    @FXML private TextField tFieldTotal;
    @FXML private TextField tFieldDiners;
    @FXML private TextField tFieldTip;
    @FXML private TextField tFieldSub;
    @FXML private TextField tFieldPer;
    @FXML private Label labelTip;
    @FXML private Label labelException;
    @FXML private Slider sliderTip;
    @FXML private Button buttReset;

    TipsterCalc calculator = new TipsterCalc();
    NumberFormat number;
    double tip;
    int diners;
    double bill;
    
    public class TipListener implements ChangeListener<Number>
    {
        @Override
            public void changed(ObservableValue<? extends Number> source, Number oldValue, Number newValue) {
                tip = intValue(sliderTip.getValue());
                labelTip.setText(Integer.toString((int)tip));
                tip /= 100;
                try {
                    diners = Integer.parseInt(tFieldDiners.getText());
                } catch (NumberFormatException e) {
                    labelException.setText("Please set Diners to a whole number");
                }

                try {
                    bill = Double.parseDouble(tFieldTotal.getText());
                } catch (NumberFormatException e) {
                    labelException.setText("Please set Total to a dollar amount without $");
                }
                
                number = NumberFormat.getCurrencyInstance();
                calculator.setInputData(tip, diners, bill);
                tFieldTip.setText(number.format(calculator.getTipAmount()));
                tFieldSub.setText(number.format(calculator.getSubtotal()));
                tFieldPer.setText(number.format(calculator.getPerDiner()));
            }
    }
    
    public void ResetListen ()
    {
        tFieldTotal.setText("0.00");
        tFieldDiners.setText("1");
        labelTip.setText("0");
        sliderTip.setValue(0);
        tFieldTip.setText("");
        tFieldSub.setText("");
        tFieldPer.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sliderTip.valueProperty().addListener(new TipListener());
    }

}
