package DTO;

public class WorldPrintDTO implements java.io.Serializable{
    private String WorldPrint;

    public WorldPrintDTO(String WorldPrint){
        this.WorldPrint = WorldPrint;
    }

  public String toString(){
    return WorldPrint;
  }
}
