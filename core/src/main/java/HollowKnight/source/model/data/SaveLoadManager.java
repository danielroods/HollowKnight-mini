package HollowKnight.source.model.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

public class SaveLoadManager {

    public static final int SLOT_COUNT = 4;

    private static final String SAVE_DIRECTORY = "saves/";
    private static final String FILE_PREFIX = "save_slot_";
    private static final String FILE_EXTENSION = ".json";

    private static final Json json = new Json();
    static {
        json.setOutputType(JsonWriter.OutputType.json);
    }

    private SaveLoadManager() {}

    public static void save(int slotIndex, GameData data) {
        validateSlot(slotIndex);
        FileHandle file = getSlotFile(slotIndex);
        file.writeString(json.prettyPrint(data), false);
    }

    public static GameData load(int slotIndex) {
        validateSlot(slotIndex);
        FileHandle file = getSlotFile(slotIndex);
        if (!file.exists()) return null;

        return json.fromJson(GameData.class, file);
    }

    public static boolean exists(int slotIndex) {
        validateSlot(slotIndex);
        return getSlotFile(slotIndex).exists();
    }

    public static void delete(int slotIndex) {
        validateSlot(slotIndex);
        FileHandle file = getSlotFile(slotIndex);
        if (file.exists()) {
            file.delete();
        }
    }

    private static FileHandle getSlotFile(int slotIndex) {
        return Gdx.files.local(SAVE_DIRECTORY + FILE_PREFIX + slotIndex + FILE_EXTENSION);
    }

    private static void validateSlot(int slotIndex) {
        if (slotIndex < 0 || slotIndex >= SLOT_COUNT) {
            throw new IllegalArgumentException(
                "Invalid save slot index: " + slotIndex + " (must be 0-" + (SLOT_COUNT - 1) + ")"
            );
        }
    }
}
