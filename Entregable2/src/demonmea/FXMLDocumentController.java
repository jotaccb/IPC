/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demonmea;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.Gauge.KnobType;
import eu.hansolo.medusa.Gauge.NeedleShape;
import eu.hansolo.medusa.Gauge.NeedleType;
import eu.hansolo.medusa.GaugeBuilder;
import static java.awt.PageAttributes.ColorType.COLOR;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import net.sf.marineapi.nmea.event.AbstractSentenceListener;
import net.sf.marineapi.nmea.io.SentenceReader;
import net.sf.marineapi.nmea.sentence.HDGSentence;
import net.sf.marineapi.nmea.sentence.MDASentence;
import net.sf.marineapi.nmea.sentence.MWVSentence;
import net.sf.marineapi.nmea.util.Position;

/**
 *
 * @author jsoler
 */
public class FXMLDocumentController implements Initializable {
    
   
    @FXML
    private ToggleButton NightButton;
    @FXML
    private Button PanelOneButton;
    @FXML
    private Button PanelTwoButton;
    @FXML
    private Button PanelThreeButton;
    @FXML
    private Button Graphs;
    @FXML
    private Pane paneone;
    @FXML
    private Pane panetwo;
    @FXML
    private Pane panethree;
    @FXML
    private Pane panefour;
    @FXML
    private Pane tempeturepane;
    @FXML
    private LineChart TheChart;
    @FXML
    private Label ficheroLabel;
    @FXML
    private Label hdgLabel;
    @FXML
    private Label twdLabel;
    @FXML
    private Label HourLabel;
    @FXML
    private HBox hbox;
    
    private Gauge gaugeone;//HDG
    private Gauge gaugethree;//TWS
    private Gauge gaugefour;//TMP
    private Gauge gaugetwo;//TWD
    private Gauge gaugefive;//AWA
    private Gauge gaugesix;//AWS
    private Gauge gaugeseven;//PITCH
    private Gauge gaugeeight;//ROLL
    private Gauge gaugenine;//LAT
    private Gauge gaugeten;//LON
    private Gauge gaugeeleven;//COG
    private Gauge gaugetwelve;//SOG

    private Model model;
    @FXML
    private Label latLab;
    @FXML
    private Label labelone;
    @FXML
    private Label labeltwo;
    @FXML
    private Label labelthree;
    @FXML
    private Label labelfour;
    @FXML
    private Label longLab;
    @FXML
    private LineChart TWDChart;
    @FXML
    private LineChart TWSChart;
    @FXML
    private Label NightL;
    
    
    
  


    
    @Override
    public void initialize(URL url, ResourceBundle rb)  {
        
        
        model=Model.getInstance();
        // anadimos un listener para que cuando cambie el valor en el modelo 
        //se actualice su valor en su correspondiente representacion grafica
        NightL.setText("Night Mode: OFF");
        labelone.setText("HDG");
        labeltwo.setText("TWD");
        labelthree.setText("TWS");
        labelfour.setText("TEMP");
        gaugeone = GaugeBuilder.create()
                          
                          .minValue(0)
                          .maxValue(359)
                          .startAngle(180)
                          .angleRange(360)
                          .autoScale(false)
                          .customTickLabelsEnabled(true)
                          .customTickLabels("N", "", "", "", "", "", "", "", "",
                                            "E", "", "", "", "", "", "", "", "",
                                            "S", "", "", "", "", "", "", "", "",
                                            "W", "", "", "", "", "", "", "", "")
                          .customTickLabelFontSize(64)
                          .tickLabelColor(Color.ROYALBLUE)
                          .tickMarkColor(Color.ROYALBLUE)
                          .minorTickMarksVisible(false)
                          .mediumTickMarksVisible(true)
                          .majorTickMarksVisible(true)
                          .valueVisible(false)
                          .needleType(NeedleType.SCIENTIFIC)
                          .needleShape(NeedleShape.FLAT)
                          .knobType(KnobType.STANDARD)
                          .knobColor(Gauge.DARK_COLOR)
                          .borderPaint(Gauge.DARK_COLOR)
                          //.skinType(Gauge.SkinType.)
                          //.layoutY(0)
                          .layoutX(30)
                          .maxSize(190, 190)
                          //.barColor(Color.AQUAMARINE)
                          //.value()
                          .build();
        paneone.getChildren().add(gaugeone);
        Task <Void> task = new Task<Void>(){
            @Override
            protected Void call() throws Exception{
                while(true){
                    if(isCancelled()){break;}
                    LocalTime now = LocalTime.now();
                    updateMessage(now.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)));
                    Thread.sleep(1000);
                }
                return null;
            }
        };
       HourLabel.textProperty().bind(task.messageProperty());
      
       Thread th = new Thread(task);
       th.setDaemon(true);
       th.start();
        
       gaugethree = GaugeBuilder.create()
                          
                          .minValue(0)
                          .maxValue(20)
                          .autoScale(false)
                          .customTickLabelsEnabled(true)
                          .customTickLabelFontSize(38)
                          .minorTickMarksVisible(false)
                          .tickLabelColor(Color.RED)
                          .tickMarkColor(Color.RED)
                          .mediumTickMarksVisible(true)
                          .majorTickMarksVisible(true)
                          .valueVisible(false)
                          .knobType(KnobType.STANDARD)
                          .knobColor(Gauge.DARK_COLOR)
                          .borderPaint(Gauge.DARK_COLOR)
                          .skinType(Gauge.SkinType.DIGITAL)
                          .barColor(Color.RED)
                          .maxSize(190, 190)
                          .layoutX(25)
                          .layoutY(-20)
                          .build();
                          
        panethree.getChildren().add(gaugethree);
        gaugetwo = GaugeBuilder.create()
                          
                          .minValue(0)
                          .maxValue(359)
                          .startAngle(180)
                          .angleRange(360)
                          .autoScale(false)
                          .customTickLabelsEnabled(true)
                          .customTickLabels("0", "", "", "30", "", "", "60", "", "",
                                            "90", "", "", "120", "", "", "150", "", "",
                                            "180", "", "", "210", "", "", "240", "", "",
                                            "270", "", "", "300", "", "", "330", "", "")
                          .customTickLabelFontSize(30)
                          .minorTickMarksVisible(false)
                          .mediumTickMarksVisible(false)
                          .majorTickMarksVisible(true)
                          .valueVisible(false)
                          .tickLabelColor(Color.ROYALBLUE)
                          .tickMarkColor(Color.ROYALBLUE)
                          .needleType(NeedleType.SCIENTIFIC)
                          .needleShape(NeedleShape.FLAT)
                          .knobType(KnobType.STANDARD)
                          .knobColor(Gauge.DARK_COLOR)
                          .borderPaint(Gauge.DARK_COLOR)
                          .barColor(Color.AQUAMARINE)
                          .maxSize(190, 190)
                          .value(150)
                          .layoutX(30)
                          .build();
                          
        
        gaugefour = GaugeBuilder.create()
                          
                          .minValue(0)
                          .maxValue(70)
                          .startAngle(270)
                          .angleRange(230)
                          .autoScale(false)
                          .customTickLabelsEnabled(true)
                          .customTickLabels("0", "10", "20", "30", "40", "50", "60", "70")
                          .customTickLabelFontSize(64)
                          .minorTickMarksVisible(false)
                          .tickLabelColor(Color.ROYALBLUE)
                          .tickMarkColor(Color.ROYALBLUE)
                          .mediumTickMarksVisible(true)
                          .majorTickMarksVisible(true)
                          .valueVisible(false)
                          .needleType(NeedleType.AVIONIC)
                          .needleShape(NeedleShape.FLAT)
                          .knobType(KnobType.STANDARD)
                          .knobColor(Gauge.DARK_COLOR)
                          .borderPaint(Gauge.DARK_COLOR)
                          .skinType(Gauge.SkinType.QUARTER)
                          //.layoutY(20)
                          .layoutX(50)
                          .maxSize(250,160)
                          .barColor(Color.AQUAMARINE)
                          //.value()
                          .build();
        panefour.getChildren().add(gaugefour);
        
        model.HDGProperty().addListener((observable, oldValue, newValue) -> {
            Double dat = (double)newValue;
                 Platform.runLater(() -> {
                gaugeone.setValue(dat);
            });
        });
        model.TWDProperty().addListener((observable, oldValue, newValue)-> {
            double dat = (double)newValue;
            Platform.runLater(() -> {
            gaugetwo.setValue(dat);
            });
        });
        model.TWSProperty().addListener((observable, oldValue, newValue)-> {
            double dat = (double)newValue;
            Platform.runLater(() -> {
            gaugethree.setValue(dat);
            });
        });
        // en la inicialización del controlador
        model.TMPProperty().addListener((observable, oldValue, newValue)-> {
            double dat = (double)newValue;
            Platform.runLater(() -> {
            gaugefour.setValue(dat);
            });
        });
        //{TWDChart.setVisible(false);TWSChart.setVisible(false);}
        paneone.getChildren().clear();
        panethree.getChildren().clear();
        panetwo.getChildren().clear();
        panefour.getChildren().clear();
        
        paneone.getChildren().add(gaugeone);
        panetwo.getChildren().add(gaugetwo);
        panethree.getChildren().add(gaugethree);
        panefour.getChildren().add(gaugefour);
        
        
        
    }
    

    @FXML
    private void cargarFichero(ActionEvent event) throws FileNotFoundException {
        FileChooser ficheroChooser = new FileChooser();
        ficheroChooser.setSelectedExtensionFilter(new ExtensionFilter("ficheros NMEA", "*.NMEA"));
        ficheroChooser.setTitle("fichero datos NMEA");
        
        File ficheroNMEA = ficheroChooser.showOpenDialog(ficheroLabel.getScene().getWindow());
        if (ficheroNMEA != null) {
            // NO se comprueba que se trata de un fichero de datos NMEA
            // esto es una demos
            ficheroLabel.setText("fichero: " + ficheroNMEA.getName());
            
            model.addSentenceReader(ficheroNMEA);
        }
    }
    
    //private boolean Night;
    @FXML
    private void NightButton(ActionEvent event) {
        if(NightButton.isSelected()==true){
            hbox.getStylesheets().clear(); hbox.getStylesheets().add("NightMode/NightMode.css");
            NightL.setText("Night Mode: ON");
        }
        else{hbox.getStylesheets().clear();
        NightL.setText("Night Mode: OFF");
        }
}

    
    
    @FXML
    private void ChangeInfoOne(ActionEvent event) {
        
        paneone.getChildren().clear();
        panetwo.getChildren().clear();
        panethree.getChildren().clear();
        panefour.getChildren().clear();
        
        labelone.setText("HDG");
        labeltwo.setText("TWD");
        labelthree.setText("TWS");
        labelfour.setText("TEMP");
        
        gaugeone = GaugeBuilder.create()
                          
                          .minValue(0)
                          .maxValue(359)
                          .startAngle(180)
                          .angleRange(360)
                          .autoScale(false)
                          .customTickLabelsEnabled(true)
                          .customTickLabels("N", "", "", "", "", "", "", "", "",
                                            "E", "", "", "", "", "", "", "", "",
                                            "S", "", "", "", "", "", "", "", "",
                                            "W", "", "", "", "", "", "", "", "")
                          .customTickLabelFontSize(64)
                          .minorTickMarksVisible(false)
                          .mediumTickMarksVisible(true)
                          .tickLabelColor(Color.ROYALBLUE)
                          .tickMarkColor(Color.ROYALBLUE)
                          .majorTickMarksVisible(true)
                          .valueVisible(false)
                          .needleType(NeedleType.SCIENTIFIC)
                          .needleShape(NeedleShape.FLAT)
                          .knobType(KnobType.STANDARD)
                          .knobColor(Gauge.DARK_COLOR)
                          .borderPaint(Gauge.DARK_COLOR)
                          //.skinType(Gauge.SkinType.)
                          //.layoutY(0)
                          .layoutX(30)
                          .maxSize(190, 190)
                          //.barColor(Color.AQUAMARINE)
                          //.value()
                          .build();
        gaugethree = GaugeBuilder.create()
                          
                          .minValue(0)
                          .maxValue(20)
                          .autoScale(false)
                          .customTickLabelsEnabled(true)
                          .customTickLabelFontSize(38)
                          .minorTickMarksVisible(false)
                          .mediumTickMarksVisible(true)
                          .tickLabelColor(Color.RED)
                          .tickMarkColor(Color.RED)
                          .majorTickMarksVisible(true)
                          .valueVisible(false)
                          .knobType(KnobType.STANDARD)
                          .knobColor(Gauge.DARK_COLOR)
                          .borderPaint(Gauge.DARK_COLOR)
                          .skinType(Gauge.SkinType.DIGITAL)
                          .barColor(Color.RED)
                          .maxSize(190, 190)
                          .layoutX(25)
                          .layoutY(-20)
                          .build();
        gaugetwo = GaugeBuilder.create()
                          
                          .minValue(0)
                          .maxValue(359)
                          .tickLabelColor(Color.ROYALBLUE)
                          .tickMarkColor(Color.ROYALBLUE)
                          .startAngle(180)
                          .angleRange(360)
                          .autoScale(false)
                          .customTickLabelsEnabled(true)
                          .customTickLabels("0", "", "", "30", "", "", "60", "", "",
                                            "90", "", "", "120", "", "", "150", "", "",
                                            "180", "", "", "210", "", "", "240", "", "",
                                            "270", "", "", "300", "", "", "330", "", "")
                          .customTickLabelFontSize(30)
                          .minorTickMarksVisible(false)
                          .mediumTickMarksVisible(false)
                          .majorTickMarksVisible(true)
                          .valueVisible(false)
                          .needleType(NeedleType.SCIENTIFIC)
                          .needleShape(NeedleShape.FLAT)
                          .knobType(KnobType.STANDARD)
                          .knobColor(Gauge.DARK_COLOR)
                          .borderPaint(Gauge.DARK_COLOR)
                          .barColor(Color.AQUAMARINE)
                          .maxSize(190, 190)
                          .value(150)
                          .layoutX(30)
                          .build();
        gaugefour = GaugeBuilder.create()
                          
                          .minValue(0)
                          .maxValue(70)
                          .startAngle(270)
                          .angleRange(230)
                          .autoScale(false)
                          .customTickLabelsEnabled(true)
                          .customTickLabels("0", "10", "20", "30", "40", "50", "60", "70")
                          .customTickLabelFontSize(64)
                          .minorTickMarksVisible(false)
                          .mediumTickMarksVisible(true)
                          .majorTickMarksVisible(true)
                          .valueVisible(false)
                          .tickLabelColor(Color.ROYALBLUE)
                          .tickMarkColor(Color.ROYALBLUE)
                          .needleType(NeedleType.AVIONIC)
                          .needleShape(NeedleShape.FLAT)
                          .knobType(KnobType.STANDARD)
                          .knobColor(Gauge.DARK_COLOR)
                          .borderPaint(Gauge.DARK_COLOR)
                          .skinType(Gauge.SkinType.QUARTER)
                          //.layoutY(20)
                          .layoutX(50)
                          .maxSize(250,160)
                          .barColor(Color.AQUAMARINE)
                          .value(0)
                          
                          .build();
        //gaugefour.value().bind(marineapi.getAirTemperature());
        model.HDGProperty().addListener((observable, oldValue, newValue) -> {
            Double dat = (double)newValue;
                 Platform.runLater(() -> {
                gaugeone.setValue(dat);
            });
        });
        model.TWDProperty().addListener((observable, oldValue, newValue)-> {
            double dat = (double)newValue;
            Platform.runLater(() -> {
            gaugetwo.setValue(dat);
            });
        });
        model.TWSProperty().addListener((observable, oldValue, newValue)-> {
            double dat = (double)newValue;
            Platform.runLater(() -> {
            gaugethree.setValue(dat);
            });
        });
        model.TMPProperty().addListener((observable, oldValue, newValue)-> {
            double dat = (double)newValue;
            Platform.runLater(() -> {
            gaugefour.setValue(dat);
            });
        });
        
        panetwo.getChildren().add(gaugetwo);
        panefour.getChildren().add(gaugefour);
        panethree.getChildren().add(gaugethree);
        paneone.getChildren().add(gaugeone);
        
        
        
    }
    @FXML
    private void ChangeInfoTwo (ActionEvent event) {
        paneone.getChildren().clear();
        panetwo.getChildren().clear();
        panethree.getChildren().clear();
        panefour.getChildren().clear();
        
        labelone.setText("AWA");
        labeltwo.setText("AWS");
        labelthree.setText("PITCH");
        labelfour.setText("ROLL");
        
        gaugefive = GaugeBuilder.create()
                          
                          .minValue(0)
                          .maxValue(359)
                          .startAngle(180)
                          .tickLabelColor(Color.ROYALBLUE)
                          .tickMarkColor(Color.ROYALBLUE)
                          .angleRange(360)
                          .autoScale(false)
                          .customTickLabelsEnabled(true)
                          .customTickLabels("0", "", "", "30", "", "", "60", "", "",
                                            "90", "", "", "120", "", "", "150", "", "",
                                            "180", "", "", "210", "", "", "240", "", "",
                                            "270", "", "", "300", "", "", "330", "", "")
                          .customTickLabelFontSize(40)
                          .minorTickMarksVisible(false)
                          .mediumTickMarksVisible(true)
                          .majorTickMarksVisible(true)
                          .valueVisible(false)
                          .needleType(NeedleType.SCIENTIFIC)
                          .needleShape(NeedleShape.FLAT)
                          .knobType(KnobType.STANDARD)
                          .knobColor(Gauge.DARK_COLOR)
                          .borderPaint(Gauge.DARK_COLOR)
                          //.skinType(Gauge.SkinType.)
                          //.layoutY(0)
                          .layoutX(30)
                          .maxSize(190, 190)
                          //.barColor(Color.AQUAMARINE)
                          .build();
        
        gaugeseven = GaugeBuilder.create()
                          
                          .minValue(0)
                          .maxValue(260)
                          .autoScale(false)
                          .minorTickMarksVisible(false)
                          .mediumTickMarksVisible(false)
                          .majorTickMarksVisible(true)
                          .valueVisible(false)
                          .needleType(NeedleType.SCIENTIFIC)
                          .needleShape(NeedleShape.FLAT)
                          .knobType(KnobType.STANDARD)
                          .knobColor(Gauge.DARK_COLOR)
                          .borderPaint(Gauge.DARK_COLOR)
                          .skinType(Gauge.SkinType.LCD)
                          .barColor(Color.AQUAMARINE)
                          .maxSize(1000, 200)
                          .layoutY(50)
                          .build();
        
        gaugesix = GaugeBuilder.create()
                          
                          .minValue(0)
                          .maxValue(20)
                          .autoScale(false)
                          .customTickLabelsEnabled(true)
                          .customTickLabelFontSize(38)
                          .minorTickMarksVisible(false)
                          .mediumTickMarksVisible(true)
                          .majorTickMarksVisible(true)
                          .valueVisible(false)
                          .tickLabelColor(Color.DARKGOLDENROD)
                          .tickMarkColor(Color.DARKGOLDENROD)
                          .knobType(KnobType.STANDARD)
                          .knobColor(Gauge.DARK_COLOR)
                          .borderPaint(Gauge.DARK_COLOR)
                          .skinType(Gauge.SkinType.DIGITAL)
                          .barColor(Color.DARKGOLDENROD)
                          .maxSize(190, 190)
                          .layoutX(25)
                          .build();
        
        gaugeeight = GaugeBuilder.create()
                          
                          .minValue(0)
                          .maxValue(360)
                          .startAngle(270)
                          .angleRange(230)
                          .autoScale(false)
                          .customTickLabelsEnabled(true)   
                          .customTickLabelFontSize(30)
                          .minorTickMarksVisible(false)
                          .mediumTickMarksVisible(false)
                          .majorTickMarksVisible(true)
                          .valueVisible(false)
                          .needleType(NeedleType.SCIENTIFIC)
                          .needleShape(NeedleShape.FLAT)
                          .knobType(KnobType.STANDARD)
                          .knobColor(Gauge.DARK_COLOR)
                          .borderPaint(Gauge.DARK_COLOR)
                          .skinType(Gauge.SkinType.LCD)
                          .barColor(Color.AQUAMARINE)
                          .maxSize(1000, 200)
                          .layoutY(50)
                          .build();
        
        model.AWAProperty().addListener((observable, oldValue, newValue) -> {
            Double dat = (double)newValue;
                 Platform.runLater(() -> {
                gaugefive.setValue(dat);
            });
        });
        model.AWSProperty().addListener((observable, oldValue, newValue)-> {
            double dat = (double)newValue;
                Platform.runLater(() -> {
               gaugesix.setValue(dat);
            });
        });
        model.PITCHProperty().addListener((observable, oldValue, newValue)-> {
            double dat = (double)newValue;
            Platform.runLater(() -> {
            gaugeseven.setValue(dat);
            });
        });
        model.ROLLProperty().addListener((observable, oldValue, newValue)-> {
            double dat = (double)newValue;
            Platform.runLater(() -> {
            gaugeeight.setValue(dat);
            });
        });
        
        panetwo.getChildren().add(gaugesix);
        panefour.getChildren().add(gaugeeight);
        panethree.getChildren().add(gaugeseven);
        paneone.getChildren().add(gaugefive);
    
    }
    @FXML
    private void ChangeInfoThree (ActionEvent event) {
        paneone.getChildren().clear();
        panetwo.getChildren().clear();
        panethree.getChildren().clear();
        panefour.getChildren().clear();
        
        labelone.setText("LAT");
        labeltwo.setText("LON");
        labelthree.setText("COG");
        labelfour.setText("SOG");
        
        gaugeeleven = GaugeBuilder.create()
                          
                          .minValue(0)
                          .maxValue(359)
                          .startAngle(180)
                          .angleRange(360)
                          .autoScale(false)
                          .customTickLabelsEnabled(true)
                          .customTickLabels("N", "", "", "", "", "", "", "", "",
                                            "E", "", "", "", "", "", "", "", "",
                                            "S", "", "", "", "", "", "", "", "",
                                            "W", "", "", "", "", "", "", "", "")
                          .customTickLabelFontSize(64)
                          .minorTickMarksVisible(false)
                          .mediumTickMarksVisible(true)
                          .majorTickMarksVisible(true)
                          .valueVisible(false)
                          .tickLabelColor(Color.ROYALBLUE)
                          .tickMarkColor(Color.ROYALBLUE)
                          .needleType(NeedleType.SCIENTIFIC)
                          .needleShape(NeedleShape.FLAT)
                          .knobType(KnobType.STANDARD)
                          .knobColor(Gauge.DARK_COLOR)
                          .borderPaint(Gauge.DARK_COLOR)
                          //.skinType(Gauge.SkinType.)
                          .layoutY(-25)
                          .layoutX(20)
                          .maxSize(190, 190)
                          //.barColor(Color.AQUAMARINE)
                          //.value()
                          .build();
        gaugeten = GaugeBuilder.create()
                          
                          .minValue(0)
                          .maxValue(260)
                          .startAngle(270)
                          .angleRange(230)
                          .autoScale(false)
                          .customTickLabelsEnabled(true)
                          .customTickLabels("0", "", "20", "", "40", "", "60", "", "80",
                                            "", "100", "", "120", "", "140", "", "160", "",
                                            "180", "", "200", "", "220", "", "240", "", "260")
                          .customTickLabelFontSize(30)
                          .minorTickMarksVisible(false)
                          .mediumTickMarksVisible(false)
                          .majorTickMarksVisible(true)
                          .valueVisible(false)
                          .needleType(NeedleType.SCIENTIFIC)
                          .needleShape(NeedleShape.FLAT)
                          .knobType(KnobType.STANDARD)
                          .knobColor(Gauge.DARK_COLOR)
                          .borderPaint(Gauge.DARK_COLOR)
                          .skinType(Gauge.SkinType.LCD)
                          .barColor(Color.AQUAMARINE)
                          .decimals(7)
                          .maxSize(1000, 200)
                          //.layoutY(50)
                          .build();
        gaugetwelve = GaugeBuilder.create()
                          
                          .minValue(0)
                          .maxValue(10)
                          .autoScale(false)
                          .customTickLabelsEnabled(true)
                          .customTickLabelFontSize(38)
                          .minorTickMarksVisible(false)
                          .mediumTickMarksVisible(true)
                          .majorTickMarksVisible(true)
                          .valueVisible(false)
                          .tickLabelColor(Color.ROYALBLUE)
                          .tickMarkColor(Color.ROYALBLUE)
                          .knobType(KnobType.STANDARD)
                          .knobColor(Gauge.DARK_COLOR)
                          .borderPaint(Gauge.DARK_COLOR)
                          .skinType(Gauge.SkinType.DIGITAL)
                          .barColor(Color.ROYALBLUE)
                          .maxSize(190, 190)
                          .layoutX(25)
                          .layoutY(-20)
                          .build();
        gaugenine = GaugeBuilder.create()
                          
                          .minValue(0)
                          .maxValue(360)
                          .startAngle(270)
                          .angleRange(230)
                          .autoScale(false)
                          .customTickLabelsEnabled(true)   
                          .customTickLabelFontSize(30)
                          .minorTickMarksVisible(false)
                          .mediumTickMarksVisible(false)
                          .majorTickMarksVisible(true)
                          .valueVisible(false)
                          .needleType(NeedleType.SCIENTIFIC)
                          .needleShape(NeedleShape.FLAT)
                          .knobType(KnobType.STANDARD)
                          .knobColor(Gauge.DARK_COLOR)
                          .borderPaint(Gauge.DARK_COLOR)
                          .skinType(Gauge.SkinType.LCD)
                          .decimals(5)
                          .barColor(Color.AQUAMARINE)
                          .maxSize(1000, 200)
                          //.layoutY()
                          .build();
        model.LATProperty().addListener((observable, oldValue, newValue) -> {
            Double dat = (double)newValue;
                 Platform.runLater(() -> {
                gaugenine.setValue(dat);
            });
        });
        model.LONProperty().addListener((observable, oldValue, newValue)-> {
            double dat = (double)newValue;
                Platform.runLater(() -> {
               gaugeten.setValue(dat);
            });
        });
        model.COGProperty().addListener((observable, oldValue, newValue)-> {
            double dat = (double)newValue;
            Platform.runLater(() -> {
            gaugeeleven.setValue(dat);
            });
        });
        model.SOGProperty().addListener((observable, oldValue, newValue)-> {
            double dat = (double)newValue;
            Platform.runLater(() -> {
            gaugetwelve.setValue(dat);
            });
        });
        
        panetwo.getChildren().add(gaugeten);
        panefour.getChildren().add(gaugetwelve);
        panethree.getChildren().add(gaugeeleven);
        paneone.getChildren().add(gaugenine);
        
    }
    

    @FXML
    private void Graphs(ActionEvent event) {
        
        paneone.getChildren().clear();
        panetwo.getChildren().clear();
        panethree.getChildren().clear();
        panefour.getChildren().clear();
        
        labelone.setText("");
        labeltwo.setText("");
        labelthree.setText("TWS");
        labelfour.setText("TWD");
        
       
        
        paneone.getChildren().add(TWSChart);
        panetwo.getChildren().add(TWDChart);
        
        
        
    }
    
    @FXML
    private void TWDChart (ActionEvent event){
        TWDChart.animatedProperty().set(false);
        TWDChart.setCreateSymbols(false);
        TWDChart.getData().add(model.getSeriex());
    }
    @FXML
    private void TWSChart (ActionEvent event){
        TWSChart.animatedProperty().set(false);
        TWSChart.setCreateSymbols(false);
        TWSChart.getData().add(model.getSeriey());
    }
    
}

