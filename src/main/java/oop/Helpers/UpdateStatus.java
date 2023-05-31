package oop.Helpers;

public final class UpdateStatus {

    private UpdateStatus() {

    }

    private static boolean isWorkerAdded;
    private static boolean isRoomAdded;
    private static boolean isClientAdded;
    private static boolean isReportAdded;

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

    public static boolean isIsReportAdded() {
        return isReportAdded;
    }

    public static void setIsReportAdded(boolean isReportAdded) {
        UpdateStatus.isReportAdded = isReportAdded;
    }
}
