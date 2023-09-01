package sstiscanner.engines;

public class Engine {
    public String name;
    public String payload;
    public Boolean isActivated;

    public Engine(String name, String payload, Boolean isActivated) {
        this.name = name;
        this.payload = payload;
        this.isActivated = isActivated;
    }
}
