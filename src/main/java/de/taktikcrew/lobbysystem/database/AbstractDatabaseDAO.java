package de.taktikcrew.lobbysystem.database;

import com.google.common.collect.Maps;
import de.chojo.sadu.mapper.wrapper.Row;
import de.chojo.sadu.queries.api.query.Query;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public abstract class AbstractDatabaseDAO<T, O> {

    private final Map<O, T> cache = Maps.newConcurrentMap();

    private final String tableName;

    public abstract void create(T t);

    public abstract void update(T t);

    public abstract void delete(O o);

    protected abstract T map(Row row) throws SQLException;

    protected abstract Optional<T> getFromDatabase(O o);

    protected abstract boolean existsInDatabase(O o);

    public Optional<T> get(O o) {
        return Optional.ofNullable(this.cache.computeIfAbsent(o, key -> this.getFromDatabase(key).orElse(null)));
    }

    public boolean exists(O o) {
        return this.cache.containsKey(o) || this.existsInDatabase(o);
    }

    public List<T> getAll() {
        return Query.query("SELECT * FROM " + this.tableName).single().map(this::map).all();
    }
}
