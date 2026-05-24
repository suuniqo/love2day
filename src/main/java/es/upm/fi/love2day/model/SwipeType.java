package es.upm.fi.love2day.model;

public enum SwipeType {
    LIKE,
    PASS;

    public boolean isLike() {
        return this == LIKE;
    }
}
