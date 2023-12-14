package ro.deiutzentartainment.config;

public enum ConfigFiles {

    CONFIG("config.yaml");
    private String path;
    ConfigFiles(String string) {
        this.path = string;
    }

    public String getPath() {
        return path;
    }
}
