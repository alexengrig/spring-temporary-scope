package dev.alexengrig.temporaryscope;

public final class SingletonMapTemporaryScopeBeanHolder extends MapTemporaryScopeBeanHolder {

    private SingletonMapTemporaryScopeBeanHolder() {
        super();
    }

    public static SingletonMapTemporaryScopeBeanHolder instance() {
        return Holder.INSTANCE;
    }

    private static final class Holder {
        private static final SingletonMapTemporaryScopeBeanHolder INSTANCE = new SingletonMapTemporaryScopeBeanHolder();
    }

}
