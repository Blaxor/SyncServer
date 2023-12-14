package ro.deiutzentartainment.games.data;

public enum SavePathGame {



    SPACE_ENGINEERS("Space Engineers", "C:\\Users\\"+System.getenv("USERNAME")+"\\AppData\\Roaming\\SpaceEngineers"),
    RIMWORLD("RimWorld","C:\\Users\\"+System.getenv("USERNAME")+"\\AppData\\LocalLow\\Ludeon Studios\\RimWorld by Ludeon Studios"),

    TEST_GAME("test_game","C:\\Users\\"+System.getenv("USERNAME")+"\\AppData\\LocalLow\\TEST_GAME");

    public String name;
    public String defaultPath;
    SavePathGame(String name, String defaultPath) {
        this.name=name;
        this.defaultPath=defaultPath;
    }
    public Game generateGame(){
        return generateGame(this);
    }
    public static Game generateGame(SavePathGame data){
        return new GameImpl(data.name, data.defaultPath);
    }
}
