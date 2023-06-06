package oop.Helpers;

/**
 * Класс для проверки на добавление элемента в базу данных
 * @author lebibop
 */
public final class UpdateStatus {

    private UpdateStatus() {}

    private static boolean isWorkerAdded;
    private static boolean isRoomAdded;
    private static boolean isClientAdded;

    public static boolean isIsWorkerAdded() {
        return isWorkerAdded;
    }

    public static void setIsWorkerAdded(boolean isWorkerAdded) {
        UpdateStatus.isWorkerAdded = isWorkerAdded;
    }

    public static boolean isIsRoomAdded() {
        return isRoomAdded;
    }

    public static void setIsRoomAdded(boolean isRoomAdded) {
        UpdateStatus.isRoomAdded = isRoomAdded;
    }

    public static boolean isIsClientAdded() {
        return isClientAdded;
    }

    public static void setIsClientAdded(boolean isClientAdded) {
        UpdateStatus.isClientAdded = isClientAdded;
    }
}
