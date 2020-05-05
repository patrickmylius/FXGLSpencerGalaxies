package main.Control.Components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;

public class EnemyComponent extends Component {
    public boolean isPoweredUp(){
        return poweredUp;
    }
    public void setPoweredUp(boolean poweredUp){
        this.poweredUp = poweredUp;
    }
    private boolean poweredUp;
    public boolean isFreezeUp(){
        return freezePoweredUp;
    }
    public void setFreezeUp(boolean freezePoweredUp){
        this.freezePoweredUp = freezePoweredUp;
    }
    private boolean freezePoweredUp;

    public void onUpdate(double trf){
        Point2D velocity = entity.getObject("velocity");
        entity.translate(velocity);
        if(FXGL.getb("poweredUp")){
            entity.getViewComponent().clearChildren();
            entity.getViewComponent().addChild(FXGL.texture("enemyRun.gif"));
        }
        else if(FXGL.getb("freezePoweredUp")){
            entity.getViewComponent().clearChildren();
            entity.getViewComponent().addChild(FXGL.texture("enemyFrozen.gif"));
        }
        else{
            entity.getViewComponent().clearChildren();
            entity.getViewComponent().addChild(FXGL.texture("enemy.gif"));
        }
            if (entity.getX() < 0){
                entity.setX(0);
                entity.setProperty("velocity", new Point2D(-velocity.getX(), velocity.getY()));
            }
            if (entity.getRightX() > 900){
                entity.setX(900 - 55);
                entity.setProperty("velocity", new Point2D(-velocity.getX(), velocity.getY()));
            }
            if (entity.getY() < 0){
                entity.setY(0);
                entity.setProperty("velocity", new Point2D(velocity.getX(), -velocity.getY()));
            }
            if (entity.getBottomY() > 800){
                entity.setY(800 - 55);
                entity.setProperty("velocity", new Point2D(velocity.getX(), -velocity.getY()));
            }
    }
}