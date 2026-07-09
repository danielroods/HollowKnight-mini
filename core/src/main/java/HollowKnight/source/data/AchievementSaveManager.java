package HollowKnight.source.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

public class AchievementSaveManager {

    private static final String FILE_PATH = "saves/achievements.json";

    private static final Json json = new Json();
    static {
        json.setOutputType(JsonWriter.OutputType.json);
    }

    private AchievementSaveManager() {}

    public static void save(AchievementSaveData data) {
        FileHandle file = getFile();
        file.writeString(json.prettyPrint(data), false);
    }

    public static AchievementSaveData load() {
        FileHandle file = getFile();
        if (!file.exists()) return null;

        return json.fromJson(AchievementSaveData.class, file);
    }

    public static boolean exists() {
        return getFile().exists();
    }

    public static void delete() {
        FileHandle file = getFile();
        if (file.exists()) {
            file.delete();
        }
    }

    private static FileHandle getFile() {
        return Gdx.files.local(FILE_PATH);
    }
}
