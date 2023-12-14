package ro.deiutzentartainment.config;

public enum Config {

    PORT(ConfigFiles.CONFIG,"listener-port",8020),
    SAVE_GAME_FOLDER(ConfigFiles.CONFIG,"save-folder","/saveGames/"),
    TEMP_FOLDER(ConfigFiles.CONFIG,"temp-folder","/temp/");
    public final ConfigFiles file;
    public final String field;
    public final Object _default;
    Config(ConfigFiles file, String field, Object _default) {
        this.file=file;
        this.field=field;
        this._default=_default;

    }
}
