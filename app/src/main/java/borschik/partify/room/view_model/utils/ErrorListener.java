package borschik.partify.room.view_model.utils;

public interface ErrorListener {
    // This listener is used to inform views about errors, which may occur asynchronously
    void onError(int errorCode);
}
