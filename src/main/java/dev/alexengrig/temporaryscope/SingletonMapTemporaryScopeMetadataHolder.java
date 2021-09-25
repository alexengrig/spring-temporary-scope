package dev.alexengrig.temporaryscope;

public final class SingletonMapTemporaryScopeMetadataHolder extends MapTemporaryScopeMetadataHolder {

    private SingletonMapTemporaryScopeMetadataHolder() {
        super();
    }

    public static SingletonMapTemporaryScopeMetadataHolder instance() {
        return Holder.INSTANCE;
    }

    private static final class Holder {
        private static final SingletonMapTemporaryScopeMetadataHolder INSTANCE = new SingletonMapTemporaryScopeMetadataHolder();
    }
}
