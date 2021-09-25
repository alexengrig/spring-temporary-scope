package dev.alexengrig.temporaryscope;

public final class SingletonDefaultTemporaryScopeProvider extends DefaultTemporaryScopeProvider {

    private SingletonDefaultTemporaryScopeProvider() {
        super(SingletonMapTemporaryScopeBeanHolder.instance(), SingletonMapTemporaryScopeMetadataHolder.instance());
    }

    public static SingletonDefaultTemporaryScopeProvider instance() {
        return Holder.INSTANCE;
    }

    private static final class Holder {

        private static final SingletonDefaultTemporaryScopeProvider INSTANCE = new SingletonDefaultTemporaryScopeProvider();

    }

}
