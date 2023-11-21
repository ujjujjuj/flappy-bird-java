public interface Callback {
    void onCallback(Type type);

    public enum Type {
        GAME_ENDED,
        INCREMENT_SCORE,
        JUMP,
    }
}