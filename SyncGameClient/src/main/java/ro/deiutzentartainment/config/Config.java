package ro.deiutzentartainment.config;

public enum Config {

    PORT(ConfigFiles.CONFIG_CONNECTION,"port-server",8020),
    IP_ADDRESS(ConfigFiles.CONFIG_CONNECTION,"ip-server","192.168.1.132"),
    ID_CLIENT(ConfigFiles.CONFIG_CONNECTION,"id","0000-0000-0000-0000"),
    TEMP_FOLDER(ConfigFiles.CONFIG_CONNECTION,"temp_folder","/client/temp/"),
    BATCH_SIZE(ConfigFiles.CONFIG_CONNECTION,"size_batch","1048576"),
    CHECK_SIZE(ConfigFiles.CONFIG_CONNECTION,"check_size","true");







    public final ConfigFiles file;
    public final String field;
    public final Object _default;
    Config(ConfigFiles file, String field, Object _default) {
        this.file=file;
        this.field=field;
        this._default=_default;

    }
}
