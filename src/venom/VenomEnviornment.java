/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package venom;

import audio.AudioPlayer;
import environment.Environment;
import environment.GraphicsPalette;
import environment.LocationValidatorIntf;
import grid.Grid;
import images.ResourceTools;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 *
 * @author Jared
 */
class VenomEnviornment extends Environment implements GridDrawData, LocationValidatorIntf {

    Grid grid;
    private Snake snake;
    private Score score;

    public final int SLOW_SPEED = 7;
    public final int MEDIUM_SPEED = 3;
    public final int HIGH_SPEED = 1;

    private int moveDelayLimit = SLOW_SPEED;
    private int moveDelayCounter = 0;

    private ArrayList<GridObject> gridObjects;
    private GameState gameState = GameState.PLAYING;
    private Image gameOverImage;

    public VenomEnviornment() {
    }

    @Override
    public void initializeEnvironment() {
        score = new Score();
        score.setPosition(new Point(55, 55));

//        Image img;
//        this.setBackground(ResourceTools.loadImageFromResource("resources/fire.jpg"));
        this.setBackground(ResourceTools.loadImageFromResource("resources/cool-red-background.jpg").getScaledInstance(2000, 2000, Image.SCALE_FAST));
        gameOverImage = ResourceTools.loadImageFromResource("resources/game_over.jpg");
        
        grid = new Grid(25, 25, 25, 25, new Point(50, 100), Color.RED);

        snake = new Snake();
        snake.setDirection(Direction.DOWN);
        snake.setDrawData(this);
        snake.setLocationValidator(this);

        ArrayList<Point> body = new ArrayList<>();
        body.add(new Point(3, 1));
        body.add(new Point(3, 2));
        body.add(new Point(2, 3));
        body.add(new Point(2, 2));

        snake.setBody(body);

        gridObjects = new ArrayList<>();
        gridObjects.add(new GridObject(GridObjectType.POISON_BOTTLE, new Point(1, 10)));
        gridObjects.add(new GridObject(GridObjectType.APPLE, new Point(2, 5)));

    }

    @Override
    public void timerTaskHandler() {
        if (snake != null) {
            // if counter >= limit then reset counter and move snake
            //else increment counter  
            if (moveDelayCounter >= this.moveDelayLimit) {
                moveDelayCounter = 0;
                snake.move();
            } else {
                moveDelayCounter++;
            }

        }

    }

    @Override
    public void keyPressedHandler(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_C) {
            grid.setShowCellCoordinates(!grid.getShowCellCoordinates());
        } else if (e.getKeyCode() == KeyEvent.VK_P) {
            snake.togglePaused();
        } else if (e.getKeyCode() == KeyEvent.VK_W) {
            snake.setDirection(Direction.UP);
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            snake.setDirection(Direction.DOWN);
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            snake.setDirection(Direction.LEFT);
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            snake.setDirection(Direction.RIGHT);
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            snake.grow(2);
        } else if (e.getKeyCode() == KeyEvent.VK_M) {
            AudioPlayer.play("/resources/jackinthebox.wav");
        }
    }

    @Override
    public void keyReleasedHandler(KeyEvent e) {
    }

    @Override
    public void environmentMouseClicked(MouseEvent e) {

    }

    @Override
    public void paintEnvironment(Graphics graphics) {

        switch (gameState) {
            case START:

                break;

            case PLAYING:
                if (score != null) {
                    score.draw(graphics);
                }
                if (grid != null) {
                    grid.paintComponent(graphics);
                }

                if (snake != null) {
                    if (snake.isAlive()) {
                        snake.draw(graphics);
                    }
                    if (snake.isPaused()) {
                        graphics.setFont(new Font("Chiller", Font.BOLD, 150));
                        graphics.setColor(Color.BLUE);
                        graphics.drawString("Paused", 300, 300);
                        
                    }
                }

                if (gridObjects != null) {
                    for (GridObject gridObject : gridObjects) {
                        if (gridObject.getType() == GridObjectType.POISON_BOTTLE) {
                            GraphicsPalette.drawPoisonBottle(graphics, grid.getCellSystemCoordinate(gridObject.getLocation()), grid.getCellSize(), Color.GREEN);
                        }

                        if (gridObject.getType() == GridObjectType.APPLE) {
                            GraphicsPalette.drawApple(graphics, grid.getCellSystemCoordinate(gridObject.getLocation()), grid.getCellSize(), Color.RED);
                        }
                    }
                }
                break;

            case OVER:
                graphics.drawImage(gameOverImage, 0, 0, 800, 600, this);

                graphics.setColor(Color.white);
                graphics.setFont(new Font("Chiller", Font.BOLD, 150));
                graphics.drawString("Sorry, But...", 100, 100);
        }
    }

    public Point getRandomPoint() {
        return new Point((int) (grid.getRows() * Math.random()), (int) (grid.getColumns() * Math.random()));
    }

//<editor-fold defaultstate="collapsed" desc="GridDrawData Interface">
    @Override
    public int getCellHeight() {
        return grid.getCellHeight();

    }

    @Override
    public int getCellWidth() {
        return grid.getCellWidth();

    }

    @Override
    public Point getCellSystemCoordinate(Point cellCoordinate
    ) {
        return grid.getCellSystemCoordinate(cellCoordinate);

    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="LocationValidatorIntf">
    @Override
    public Point validateLocation(Point point) {
//        if (point.x < 0) {
//            point.x = grid.getColumns() - 1;
//        } else if (point.x >= grid.getColumns()) {
//            point.x = 0;
//        }
//
//        if (point.y < 0) {
//            point.y = grid.getRows() - 1;
//        } else if (point.y >= grid.getRows()) {
//            point.y = 0;
//        }
//        
        if (point.x < 0) {
            killSnake();
        }
        if (point.x > grid.getColumns() - 1) {
            killSnake();
        }

        if (point.y < 0) {
            killSnake();
        }

        if (point.y > grid.getColumns() - 1) {
            killSnake();
        }

        // check if the snake hit a GridObject, then take appropriate
        // action:
        //   -Apple - grow the snake by 3
        //   -Poison - make sound, kill snake
        //
        // look at all the locations stored in the gridObject ArrayList
        // for each, compare it to the head location stored
        // in the "point" parameter
        for (GridObject object : gridObjects) {
            if (object.getLocation().equals(point)) {
                System.out.println("HIT = " + object.getType());

                if (object.getType() == GridObjectType.APPLE) //snake grow by 2 
                {
                    snake.grow(4);
                    this.score.addToValue(2);
                }

                if (object.getType() == GridObjectType.POISON_BOTTLE) //snake grow by 2 
                {

                    this.score.addToValue(-16);
                }

//remove object from the screen
                object.setLocation(this.getRandomPoint());
                increaseSnakeSpeed();

            } else if (true) {
                //play yucky sound?
                //decrease score?
                //kill snake?
                //turn snake different color...?
            }

        }

        return point;
    }
    
    private void killSnake(){
        snake.kill();
        this.gameState = GameState.OVER;
    }

    private void increaseSnakeSpeed() {
        //logic to increase snake speed
        if (moveDelayLimit == SLOW_SPEED) {
            moveDelayLimit = MEDIUM_SPEED;
            System.out.println("woot");
        } else if (SLOW_SPEED == HIGH_SPEED);

    }
//</editor-fold>

    /**
     * @return the gameState
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * @param gameState the gameState to set
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

}
