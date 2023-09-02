package world.utils;

import entity.Entity;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class Grid {
    private final int column;
    private final int row;

    Entity[][] grid;

    public Grid(int column, int row, Map<String, List<Entity>> entities)  {
        this.column = column;
        this.row = row;
        int totalPopulation = 0 ;
        for (List<Entity> entityList : entities.values())
        {
            totalPopulation += entityList.size();
        }
        if(this.getMaxAmountOfEntities() < totalPopulation)
        {
            throw new RuntimeException("Too many entities for this grid");
        }
        grid = new Entity[row][column];
        for (List<Entity> entityList : entities.values())
        {
            for (Entity entity : entityList)
            {
                Location randomLocation = getRandomUnoccupiedLocation();
                entity.setLocation(randomLocation);
                grid[randomLocation.getRow()][randomLocation.getColumn()] = entity;
            }
        }

    }
    public Location getRandomUnoccupiedLocation()
    {
        Location randomLocation = new Location();
        while(grid[randomLocation.getRow()][randomLocation.getColumn()] != null)
        {
            randomLocation = new Location();
        }
        return randomLocation;
    }
    public void removeEntity(Entity entityToRemove)
    {
        Location loc = entityToRemove.getLocation();
        grid[loc.getRow()][loc.getColumn()] = null;
    }
    public boolean isEntityNear(Entity mainEntity,String secondaryEntity,int depth)
    {
        Location mainEntityLocation = mainEntity.getLocation();
        int mainEntityRow = mainEntityLocation.getRow();
        int mainEntityColumn = mainEntityLocation.getColumn();
        for (int i = mainEntityRow - depth; i <= mainEntityRow + depth; i++)
        {
            for (int j = mainEntityColumn - depth; j <= mainEntityColumn + depth; j++)
            {
                if(i != mainEntityRow || j != mainEntityColumn) {
                    if (i >= 0 && i < row && j >= 0 && j < column) {
                        if (grid[i][j] != null && grid[i][j].getName().equals(secondaryEntity)) {
                            return true;
                        }
                    }
//                else { todo should we do this?
//                    int fixedRow = i, fixedColumn = j;
//                    int rowMultiplier = Math.abs(i / row);
//                    int columnMultiplier = Math.abs(j / column);
//                    if(i<0)
//                    {
//                        fixedRow = i + row * (rowMultiplier + 1);
//                    }
//                    if(i >= row)
//                    {
//                        fixedRow  = i - row * rowMultiplier;
//                    }
//                    if(j < 0)
//                    {
//                        fixedColumn = j + column * (columnMultiplier + 1);
//                    }
//                    if(j >= column)
//                    {
//                        fixedColumn =  j - column * columnMultiplier;
//                    }
//                    if(grid[fixedRow][fixedColumn] != null && grid[fixedRow][fixedColumn].getName().equals(secondaryEntity))
//                    {
//                        return true;
//                    }
//
//                }
                }
            }
        }
        return false;


    }
    public void moveEntity(Entity entityToMove) {
    //search for entity and get x,y



    Location loc = entityToMove.getLocation();
    grid[loc.getRow()][loc.getColumn()] = null;
    loc.generateRandomMove();
    grid[loc.getRow()][loc.getColumn()] = entityToMove;

    }
    private boolean isOccupied(Location location)
    {
        return grid[location.getRow()][location.getColumn()] != null;
    }

    public class Location
    {
        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public int getColumn() {
            return column;
        }


        public void setColumn(int column) {
            this.column = column;
        }

        private int row;
        private int column;
        public Location(int row, int column)
        {
            this.row = row;
            this.column = column;
        }
        public Location()
        {
            Random random = new Random();
            this.row = random.nextInt(Grid.this.row);
            this.column = random.nextInt(Grid.this.column);
        }

        public void generateRandomMove()
        {
                Random random = new Random();
                int randInt = random.nextInt(4);
                for (int i = 0; i < 4; i++) {
                    int newDirection = (randInt + i) % 4;
                    Direction directionEnum = Direction.fromValue(newDirection);
                    int newRow = this.row + directionEnum.dx;
                    int newColumn = this.column + directionEnum.dy;
                    Location newLocation = new Location(newRow, newColumn);
                    newLocation.fixOutOfBounds(directionEnum);

                    if (!isOccupied(newLocation)){
                        this.column = newLocation.column;
                        this.row = newLocation.row;
                    }
                }


        }
        private void fixOutOfBounds(Direction direction)
        {

           switch (direction)
              {
                case UP:
                     if(row < 0)
                     {
                          this.row = Grid.this.row - 1;
                     }
                     break;
                case DOWN:
                     if(row >= Grid.this.row)
                     {
                         this.row = 0;
                     }
                     break;
                case RIGHT:
                     if(column >= Grid.this.column)
                     {
                            this.column = 0;
                     }
                     break;
                case LEFT:
                     if(column < 0)
                     {
                            this.column = Grid.this.column - 1;
                     }
                     break;
              }
        }


    }
    public enum Direction{
        LEFT(0, -1),
        RIGHT(0, 1),
        UP(-1, 0),
        DOWN(1, 0);



        private final int dx;
        private final int dy;
        Direction(int dx,int dy)
        {
            this.dx = dx;
            this.dy = dy;
        }
        public static Direction fromValue(int value)
        {
            switch (value)
            {
                case 0:
                    return UP;
                case 1:
                    return DOWN;
                case 2:
                    return RIGHT;
                case 3:
                    return LEFT;
                default:
                    throw new RuntimeException("Invalid direction");
            }
        }
    }
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (Entity[] entities : grid)
        {
            for (Entity entity : entities)
            {
                if(entity == null)
                {
                    sb.append("0");
                }
                else
                {
                    sb.append("X");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public int getMaxAmountOfEntities() {
        return column * row;
    }
}
