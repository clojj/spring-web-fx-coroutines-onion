CREATE TABLE IF NOT EXISTS users (
  id uuid DEFAULT uuid_generate_v4 (),
  email TEXT NOT NULL,
  username TEXT NOT NULL,

  CONSTRAINT pk$users PRIMARY KEY (id),
  CONSTRAINT unq$email UNIQUE (email),
  CONSTRAINT unq$username UNIQUE (username)
);
