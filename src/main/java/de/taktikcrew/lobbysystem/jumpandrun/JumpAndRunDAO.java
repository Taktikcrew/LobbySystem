package de.taktikcrew.lobbysystem.jumpandrun;

import de.chojo.sadu.mapper.wrapper.Row;
import de.chojo.sadu.queries.api.call.Call;
import de.chojo.sadu.queries.api.query.Query;
import de.taktikcrew.lobbysystem.database.AbstractDatabaseDAO;

import java.sql.SQLException;
import java.util.Optional;

public class JumpAndRunDAO extends AbstractDatabaseDAO<JumpAndRun, String> {

    public JumpAndRunDAO() {
        super("JumpAndRun");
    }

    @Override
    public void create(JumpAndRun jumpAndRun) {
        if (this.exists(jumpAndRun.name())) {
            return;
        }

        Query.query("INSERT INTO JumpAndRun (name, builder, difficulty, startLocation, endLocation, recordTime, playable) VALUES (?, ?, ?, ?, ?, ?, ?)")
                .single(Call.of()
                        .bind(jumpAndRun.name())
                        .bind(jumpAndRun.builder())
                        .bind(jumpAndRun.difficulty().name())
                        .bind(jumpAndRun.startLocation())
                        .bind(jumpAndRun.endLocation())
                        .bind(jumpAndRun.recordTime())
                        .bind(jumpAndRun.playable())
                )
                .insert();

        for (var checkpoint : jumpAndRun.checkpoints()) {
            Query.query("INSERT INTO JumpAndRun_checkpoints (name, checkpoint) VALUES (?, ?)")
                    .single(Call.of()
                            .bind(jumpAndRun.name())
                            .bind(checkpoint)
                    )
                    .insert();
        }

        this.cache().put(jumpAndRun.name(), jumpAndRun);
    }

    @Override
    public void update(JumpAndRun jumpAndRun) {
        if (!this.exists(jumpAndRun.name())) {
            return;
        }

        Query.query("UPDATE JumpAndRun SET recordTime = ?, playable = ? WHERE name = ?")
                .single(Call.of()
                        .bind(jumpAndRun.recordTime())
                        .bind(jumpAndRun.playable())
                        .bind(jumpAndRun.name())
                )
                .update();

        this.cache().put(jumpAndRun.name(), jumpAndRun);
    }

    @Override
    public void delete(String name) {
        if (!this.exists(name)) {
            return;
        }

        Query.query("DELETE FROM JumpAndRun WHERE name = ?").single(Call.of().bind(name)).delete();
    }

    @Override
    protected JumpAndRun map(Row row) throws SQLException {
        return new JumpAndRun(row);
    }

    @Override
    protected Optional<JumpAndRun> getFromDatabase(String name) {
        return Query.query("SELECT * FROM JumpAndRun WHERE name = ?")
                .single(Call.of().bind(name))
                .map(this::map)
                .first();
    }

    @Override
    protected boolean existsInDatabase(String name) {
        return Query.query("SELECT COUNT(*) as count FROM JumpAndRun WHERE name = ?")
                .single(Call.of().bind(name))
                .map(row -> row.getInt("count") > 0)
                .first()
                .orElse(false);
    }
}
