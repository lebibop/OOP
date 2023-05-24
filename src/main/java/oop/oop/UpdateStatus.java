package oop.oop;

public final class UpdateStatus {

    private UpdateStatus() {

    }

    private static boolean isWorkerAdded;

    public static boolean isIsWorkerAdded() {
        return isWorkerAdded;
    }

    public static void setIsWorkerAdded(boolean isWorkerAdded) {
        UpdateStatus.isWorkerAdded = isWorkerAdded;
    }
}
