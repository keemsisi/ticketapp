CREATE TABLE IF NOT EXISTS shedlock (
      name          VARCHAR(100) primary KEY,
      lock_until    TIMESTAMP,
      locked_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      locked_by     VARCHAR(255)
    )
