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
    public Entity getEntityNear(Entity mainEntity, String secondaryEntity, int depth)
    {
        Location mainEntityLocation = mainEntity.getLocation();
        int mainEntityRow = mainEntityLocation.getRow();
        int mainEntityColumn = mainEntityLocation.getColumn();

        for (int i = mainEntityRow - depth; i <= mainEntityRow + depth; i++) {
            for (int j = mainEntityColumn - depth; j <= mainEntityColumn + depth; j++) {
                int currentRow = Math.floorMod((i + row) , row);
                int currentColumn = Math.floorMod((j + column) , column);

                if(currentRow != mainEntityRow || currentColumn != mainEntityColumn) {
                    if (grid[currentRow][currentColumn] != null && grid[currentRow][currentColumn].getName().equals(secondaryEntity)) {
                           return grid[currentRow][currentColumn];
                        }
                }
            }
        }
        return null;
    }
    public void moveEntity(Entity entityToMove) {
    Location loc = entityToMove.getLocation();
    grid[loc.getRow()][loc.getColumn()] = null;
    loc.generateRandomMove();
    grid[loc.getRow()][loc.getColumn()] = entityToMove;
    entityToMove.setLocation(loc);
    }
    private boolean isOccupied(Location location)
    {
        return grid[location.getRow()][location.getColumn()] != null;
    }

    public void addEntity(Entity entity) {
        if(isOccupied(entity.getLocation()))
        {
            throw new RuntimeException("Location is occupied");
        }
        Location loc = entity.getLocation();
        grid[loc.getRow()][loc.getColumn()] = entity;
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
                    int newRow = Math.floorMod((this.row + directionEnum.dx + Grid.this.row) , Grid.this.row);
                    int newColumn = Math.floorMod((this.column + directionEnum.dy + Grid.this.column) , Grid.this.column);
                    Location newLocation = new Location(newRow, newColumn);

                    if (!isOccupied(newLocation)){
                        this.column = newLocation.column;
                        this.row = newLocation.row;
                    }
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
