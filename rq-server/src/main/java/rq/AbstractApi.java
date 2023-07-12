package rq;

public abstract class AbstractApi {
    private final ReDiQuadri storage;

    public AbstractApi(ReDiQuadri storage) {
        this.storage = storage;
    }

    public ReDiQuadri storage() {
        return storage;
    }
}
