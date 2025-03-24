package de.taktikcrew.lobbysystem.jumpandrun;

import de.taktikcrew.lobbysystem.database.AbstractDatabaseDAO;
import org.jetbrains.annotations.NotNull;

public class JumpAndRunDAO extends AbstractDatabaseDAO<JumpAndRun, String> {

    public JumpAndRunDAO() {
        super(JumpAndRun.class, "name");
    }

    @Override
    public void create(@NotNull JumpAndRun jumpAndRun) {
        if (this.exists(jumpAndRun.name())) {
            return;
        }
        this.repository().query().create(jumpAndRun);
        this.cache().put(jumpAndRun.name(), jumpAndRun);
    }

    @Override
    public void update(@NotNull JumpAndRun jumpAndRun) {
        this.repository().query().match(this.id(), jumpAndRun.name()).update(jumpAndRun);
    }
}
