/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audioviz;

import static java.lang.Integer.min;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Zach-MacBook
 */
public class Zrhkc7SuperVisual implements Visualizer{
    
    private final String name = "Zrhkc7 Super Visual";
    private Integer numBands;
    private AnchorPane vizPane;
    
    private String vizPaneInitialStyle = "";
    
    private final Double curveHeightPercentage = 0.8;
    private final Double minCubicRadius = 50.0;  // 10.0
    private final Double rotatePhaseMultiplier = 1.0;
    
    private Double width = 0.0;
    private Double height = 0.0;
    
    private Double curveWidth = 0.0;
    private Double curveHeight = 0.0;
    private Double halfcurveHeight = 0.0;
    
    private final Double startHue = 240.0;
    
    private CubicCurve[] cubic;
    
    public Zrhkc7SuperVisual() {
    }

    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public void start(Integer numBands, AnchorPane vizPane) {        
        end();
        
        vizPaneInitialStyle = vizPane.getStyle();
        
        this.numBands = numBands;
        this.vizPane = vizPane;
        
        height = vizPane.getHeight();
        width = vizPane.getWidth();
        
        Rectangle clip = new Rectangle(width, height);
        clip.setLayoutX(0);
        clip.setLayoutY(0);
        vizPane.setClip(clip);
        
        curveWidth = width / numBands;
        curveHeight = height * curveHeightPercentage;
        halfcurveHeight = curveHeight / 2;
        cubic = new CubicCurve[numBands];
        double startX = width/2;
        double startY = height/2;
        
        for (int i = 0; i < numBands; i++) {  
            CubicCurve curve = new CubicCurve();
            curve.setStartX(startX);
            curve.setStartY(startY);
            curve.setControlX1(25.0f);
            curve.setControlY1(startY);
            curve.setControlX2(75.0f);
            curve.setControlY2(0.0f);
            curve.setEndX(width);
            curve.setEndY(height);
            curve.setFill(Color.hsb(startHue, 1.0, 1.0, 1.0));
            vizPane.getChildren().add(curve);
            cubic[i] = curve;
        }
    }
    
    @Override
    public void end() {
        if (cubic != null) {
            for (CubicCurve cubic : cubic) {
                vizPane.getChildren().remove(cubic);
            }
            cubic = null;
            vizPane.setClip(null);
            vizPane.setStyle(vizPaneInitialStyle);
        }        
    }

    
    @Override
    public void update(double timestamp, double duration, float[] magnitudes, float[] phases) {
        if (cubic == null) {
            return;
        }
        
        Integer num = min(cubic.length, magnitudes.length);
        
        for (int i = 0; i < num; i++) {
            cubic[i].setControlY1((height + 60.00f));
            //cubic[i].setControlY2((60.0 + magnitudes[i]));
            //cubic[i].setControlX1(0);
            //cubic[i].setControlX2(100);
            cubic[i].setFill(Color.hsb(startHue - (magnitudes[i] * -6.0), 1.0, 1.0, 1.0));
            cubic[i].setRotate(phases[i] * rotatePhaseMultiplier * 500);
        }
        Double hue = ((60.0 + magnitudes[0])/60.0) * 360;
        vizPane.setStyle("-fx-background-color: hsb(" + hue + ", 20%, 100%)" );
    }
    
    
}