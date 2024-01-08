package ro.deiutzentartainment.games.data;

public interface Game {

     String getName();
     String getSavePath();
     String getGamePath();
     boolean existsGamePath();

     default String getExportInfo(){
      return getName()+";"+getRawSavePath()+";"+getRawGamePath();
     }

     String getRawGamePath();

     String getRawSavePath();


     default String getPrettyWritten(){
          return "Name: " + getName() + "\n"+"Save Path: " + getSavePath() +"\n" + "Game path: " + getGamePath();
     }
}
