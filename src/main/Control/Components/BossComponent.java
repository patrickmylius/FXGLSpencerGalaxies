package main.Control.Components;

import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;

public class BossComponent extends Component {
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
        Point2D velocity = entity.getObject("velocityBoss");
        entity.translate(velocity);
        if (entity.getX() < 0){
            entity.setX(0);
            entity.setProperty("velocityBoss", new Point2D(-velocity.getX(), velocity.getY()));
        }
        if (entity.getRightX() > 900){
            entity.setX(900 - 110);
            entity.setProperty("velocityBoss", new Point2D(-velocity.getX(), velocity.getY()));
        }
        if (entity.getY() < 0){
            entity.setY(0);
            entity.setProperty("velocityBoss", new Point2D(velocity.getX(), -velocity.getY()));
        }
        if (entity.getBottomY() > 800){
            entity.setY(800 - 110);
            entity.setProperty("velocityBoss", new Point2D(velocity.getX(), -velocity.getY()));
        }
    }
}
