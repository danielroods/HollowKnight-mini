package HollowKnight.source.model.map;

public enum Maps {
    GREENPATH_ROOM_1("map/Greenpath-room1.tmx", "greenpath_room1"),
    GREENPATH_ROOM_2("map/Greenpath-room2.tmx", "greenpath_room2"),
    CRYSTAL_PEAK("map/CrystalPeak.tmx", "crystal_peak"),
    BOSS_ROOM("map/BossRoom.tmx", "boss_room"),
    ;

    private final String path;
    private final String id;

    Maps(String path, String id) {
        this.path = path;
        this.id = id;
    }

    public String getPath() {
        return path;
    }
    public String getId() {
        return id;
    }

    public int getIndex() {
        return this.ordinal();
    }

    public static Maps fromIndex(int index) {
        return values()[index];
    }

    public static Maps fromId(String id) {
        for (Maps map : values()) {
            if (map.id.equals(id)) return map;
        }
        return null;
    }
}
