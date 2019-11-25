create table if not exists USERS(
  ID INTEGER not null auto_increment primary key,
  GAME_COUNT INTEGER not null,
  RATING DOUBLE not null,
  PASSWORD VARCHAR(255),
  USERNAME VARCHAR(255)
);

create table if not exists  LOGS(
  USER_ID INTEGER not null,
  ATTEMPT INTEGER not null,
  STEP_NUMBER INTEGER not null,
  `NUMBER` INTEGER not null,
  primary key (USER_ID, ATTEMPT, STEP_NUMBER),
  constraint FK_LOGS
  foreign key (USER_ID) references USERS(ID)
);

create table if not exists  ANSWERS(
  USER_ID INTEGER not null,
  ATTEMPT INTEGER not null,
  `NUMBER` INTEGER not null,
  primary key (USER_ID, ATTEMPT),
  constraint FK_ANSWERS
  foreign key (USER_ID) references USERS(ID)
);





