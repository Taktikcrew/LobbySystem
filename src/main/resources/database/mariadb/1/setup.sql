CREATE TABLE IF NOT EXISTS LobbyPlayer
(
    uuid          UUID NOT NULL PRIMARY KEY,
    dsgvoAccepted TINYINT(1)
);

CREATE TABLE IF NOT EXISTS JumpAndRun
(
    name          VARCHAR(64) NOT NULL PRIMARY KEY,
    builder       VARCHAR(64)          DEFAULT 'Taktikcrew',
    difficulty    VARCHAR(10),
    startLocation TEXT        NOT NULL,
    endLocation   TEXT        NOT NULL,
    recordTime    MEDIUMTEXT,
    playable      TINYINT(1)  NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS JumpAndRun_checkpoints
(
    name       VARCHAR(64) NOT NULL,
    checkpoint TEXT        NOT NULL,
    FOREIGN KEY (name) REFERENCES JumpAndRun (name) ON DELETE CASCADE
);