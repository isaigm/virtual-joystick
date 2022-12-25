package com.mygdx.joystick;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Cross {
    public static class Button{
        public static final int LEFT  = 0;
        public static final int RIGHT = 1;
        public static final int UP    = 2;
        public static final int DOWN  = 3;
        public static final int NONE  = 4;
    }
    private Rectangle bottom = new Rectangle();
    private Rectangle top = new Rectangle();
    private float width;
    private float height;
    private float centerx;
    private float centery;

    public Cross(float width, float height, float x, float y)
    {
        this.width = width;
        this.height = height;
        centerx = x;
        centery = y;
        bottom.width = width;
        bottom.height = height;
        bottom.x = x - width / 2;
        bottom.y = y - height / 2;
        top.width = height;
        top.height = width;
        top.x = x - height / 2;
        top.y = y - width / 2;
    }
    public void render(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(top.x, top.y, top.width, top.height);
        shapeRenderer.rect(bottom.x, bottom.y, bottom.width, bottom.height);
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.line(bottom.x, bottom.y, centerx - height / 2f, bottom.y);
        shapeRenderer.line(bottom.x, bottom.y + bottom.height, centerx - height / 2f, bottom.y + bottom.height);
        shapeRenderer.line(bottom.x, bottom.y, bottom.x, bottom.y + bottom.height);
        shapeRenderer.line(centerx - height / 2f, bottom.y + bottom.height, centerx - height / 2f, top.y + top.height);
        shapeRenderer.line(centerx - height / 2f, bottom.y, centerx - height / 2f, top.y);
        shapeRenderer.line(centerx - height / 2f, top.y, centerx + height / 2f, top.y);
        shapeRenderer.line(centerx + height / 2f, top.y, centerx + height / 2f, bottom.y);
        shapeRenderer.line(centerx + height / 2f, bottom.y, bottom.x + bottom.width, bottom.y);
        shapeRenderer.line(bottom.x + bottom.width, bottom.y, bottom.x + bottom.width, bottom.y + bottom.height);
        shapeRenderer.line(bottom.x + bottom.width, bottom.y + bottom.height, centerx + height / 2f, bottom.y + bottom.height);
        shapeRenderer.line(centerx + height / 2f, bottom.y + bottom.height, centerx + height / 2f, top.y + top.height);
        shapeRenderer.line(centerx + height / 2f, top.y + top.height, centerx - height / 2f, top.y + top.height);
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        float offset = 1.2f;
        shapeRenderer.circle(centerx, centery, 0.8f, 50);
        shapeRenderer.line(centerx - height / 2f, centery, bottom.x + offset, centery);
        shapeRenderer.line(centerx + height / 2f, centery, bottom.x + width - offset, centery);
        shapeRenderer.line(centerx, centery - height / 2f, centerx, top.y  + offset );
        shapeRenderer.line(centerx, centery + height / 2f, centerx, top.y + width - offset);
        shapeRenderer.end();
    }

    public int getButtonPressed(float x, float y)
    {
        if(bottom.contains(x, y) && top.contains(x, y))
        {
           return Button.NONE;
        }
        if(bottom.contains(x, y)){
            if(x < centerx) return Button.LEFT;
            return Button.RIGHT;
        }
        if(top.contains(x, y))
        {
            if(y < centery) return Button.DOWN;
            return Button.UP;
        }
        return Button.NONE;
    }
}

