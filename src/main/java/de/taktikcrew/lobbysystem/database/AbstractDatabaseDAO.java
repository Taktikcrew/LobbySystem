package de.taktikcrew.lobbysystem.database;

import com.google.common.collect.Maps;
import dev.httpmarco.evelon.MariaDbLayer;
import dev.httpmarco.evelon.Repository;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
@Accessors(fluent = true)
public abstract class AbstractDatabaseDAO<T, O> {

    private final Repository<T> repository;

    private final String id;

    private final Map<O, T> cache = Maps.newConcurrentMap();

    public AbstractDatabaseDAO(Class<T> type, String id) {
        this.repository = Repository.build(type).withLayer(MariaDbLayer.class).build();

        this.id = id;
    }

    public abstract void create(T t);

    public abstract void update(T o);

    public Optional<T> get(O o) {
        return Optional.ofNullable(this.cache.computeIfAbsent(o, _ ->
                this.repository.query().match(this.id, o).findFirst()
        ));
    }

    public List<T> getAll() {
        return this.repository.query().find();
    }

    public boolean exists(O o) {
        return this.repository.query().match(this.id, o).exists();
    }

    public void delete(O o) {
        this.repository.query().match(this.id, o).delete();
    }
}
