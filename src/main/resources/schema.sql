DROP TABLE IF EXISTS USERS, ITEMS, BOOKINGS, REQUESTS, COMMENTS, ITEM_REQUEST;

CREATE TABLE IF NOT EXISTS USERS
(
    ID    BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    NAME  VARCHAR(255) NOT NULL,
    EMAIL VARCHAR(512) NOT NULL,
    CONSTRAINT UQ_USER_EMAIL UNIQUE (EMAIL)
);
CREATE unique INDEX USERS_EMAILS ON USERS (EMAIL);

CREATE TABLE IF NOT EXISTS ITEMS
(
    ID           BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    NAME         VARCHAR(255) NOT NULL,
    DESCRIPTION  VARCHAR(255),
    IS_AVAILABLE BOOLEAN DEFAULT TRUE,
    ID_OWNER     BIGINT       NOT NULL,
    ID_REQUEST   BIGINT,
    CONSTRAINT FK_OWNER FOREIGN KEY (ID_OWNER) REFERENCES USERS (ID) ON DELETE CASCADE
);
CREATE INDEX ITEMS_ITEM_OWNER ON ITEMS (ID_OWNER);

CREATE TABLE IF NOT EXISTS BOOKINGS
(
    ID         BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    DATE_START TIMESTAMP NOT NULL,
    DATE_END   TIMESTAMP NOT NULL,
    ID_ITEM    BIGINT    NOT NULL,
    ID_BOOKER  BIGINT    NOT NULL,
    STATUS     VARCHAR(10),
    CONSTRAINT FK_BOOKER FOREIGN KEY (ID_BOOKER) REFERENCES USERS (ID) ON DELETE CASCADE,
    CONSTRAINT FK_ITEM FOREIGN KEY (ID_ITEM) REFERENCES ITEMS (ID) ON DELETE CASCADE
);
CREATE INDEX BOOKINGS_DATE_START_DATE_END ON BOOKINGS (DATE_START, DATE_END);
CREATE INDEX BOOKINGS_ID_BOOKER ON BOOKINGS (ID_BOOKER);
CREATE INDEX BOOKINGS_ID_ITEM ON BOOKINGS (ID_ITEM);

CREATE TABLE IF NOT EXISTS REQUESTS
(
    ID           BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    DESCRIPTION  VARCHAR(255),
    ID_REQUESTOR BIGINT    NOT NULL,
    CREATED      TIMESTAMP NOT NULL,
    CONSTRAINT FK_REQUESTOR FOREIGN KEY (ID_REQUESTOR) REFERENCES USERS (ID) ON DELETE CASCADE
);
CREATE INDEX REQUESTS_ID_REQUESTOR ON REQUESTS (DESCRIPTION);
CREATE INDEX REQUESTS_CREATED ON REQUESTS (CREATED);

CREATE TABLE IF NOT EXISTS COMMENTS
(
    ID        BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    TEXT      TEXT      NOT NULL,
    ID_ITEM   BIGINT    NOT NULL,
    ID_AUTHOR BIGINT    NOT NULL,
    CREATED   TIMESTAMP NOT NULL,
    CONSTRAINT FK_ITEM_COMMENT FOREIGN KEY (ID_ITEM) REFERENCES ITEMS (ID) ON DELETE CASCADE,
    CONSTRAINT FK_AUTHOR FOREIGN KEY (ID_AUTHOR) REFERENCES USERS (ID) ON DELETE CASCADE
);
CREATE INDEX COMMENTS_ID_ITEM_ID_AUTHOR ON COMMENTS (ID_ITEM, ID_AUTHOR);
CREATE INDEX COMMENTS_CREATED ON COMMENTS (CREATED);

CREATE TABLE IF NOT EXISTS ITEM_REQUEST
(
    ID_REQUEST INT NOT NULL,
    ID_ITEM    INT NOT NULL,
    PRIMARY KEY (ID_REQUEST, ID_ITEM),
    FOREIGN KEY (ID_REQUEST) REFERENCES REQUESTS (ID),
    FOREIGN KEY (ID_ITEM) REFERENCES ITEMS (ID)
);