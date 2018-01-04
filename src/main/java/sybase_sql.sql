CREATE TABLE tms.basic.annex (
  id         VARCHAR(36)   NOT NULL,
  bizid      VARCHAR(36)   NULL,
  annexname  VARCHAR(200)  NULL,
  history    CHAR(1)       NULL,
  annexurl   VARCHAR(200)  NULL,
  creator    VARCHAR(36)   NULL,
  createdate DATE          NULL,
  modifier   VARCHAR(36)   NULL,
  modifydate DATE          NULL,
  remarks    VARCHAR(200)  NULL,
  suffix     VARCHAR(20)   NULL,
  content    TEXT,
  title      VARCHAR(200)  NULL,
  isvip      VARCHAR(1)    NULL,
  deptid     VARCHAR(36)   NULL,
  files      VARCHAR(1200) NULL,
  year       VARCHAR(6)    NULL,
  enable     VARCHAR(6)    NULL,
  type       VARCHAR(20)   NULL,
  deleted    CHAR(1)       NULL,
  property   CHAR(1)       NULL,
  url        VARCHAR(200)  NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (creator) REFERENCES basic.employee (userid),
  FOREIGN KEY (deptid) REFERENCES basic.department (deptid)
)


CREATE TABLE tms.basic.annex_file (
  id         VARCHAR(64)  NOT NULL,
  bizid      VARCHAR(36)  NULL,
  type       VARCHAR(100) NULL,
  file_name  VARCHAR(100) NULL,
  sha2       VARCHAR(64)  NULL,
  status     CHAR(1)      NULL,
  creator    VARCHAR(36)  NULL,
  modifier   VARCHAR(36)  NULL,
  createdate DATETIME     NULL,
  modifydate DATETIME     NULL,
  PRIMARY KEY (id)
)


CREATE TABLE tms.basic.app_channel (
  id         VARCHAR(36) NOT NULL,
  channel_no VARCHAR(36) NULL,
  memo       TEXT,
  apk_url    TEXT,
  version    VARCHAR(20) NULL,
  appid      VARCHAR(36) NULL,
  PRIMARY KEY (id)
)


CREATE TABLE tms.basic.app_token (
  id              VARCHAR(36)  NOT NULL,
  appid           VARCHAR(36)  NULL,
  userid          VARCHAR(36)  NULL,
  token           TEXT,
  empiredin       INT          NULL,
  lasttime        DATETIME     NULL,
  issued          VARCHAR(36)  NULL,
  createdate      DATETIME     NULL,
  creator         VARCHAR(36)  NULL,
  d_key           TEXT,
  s_key           TEXT,
  token_empiredin INT          NULL,
  token_lasttime  DATETIME     NULL,
  appversion      VARCHAR(36)  NULL,
  p_key           TEXT,
  app_os          VARCHAR(200) NULL,
  PRIMARY KEY (id)
)


CREATE TABLE tms.basic.appinfo (
  appid         VARCHAR(36)  NOT NULL,
  appsecret     VARCHAR(200) NULL,
  appname       VARCHAR(200) NOT NULL,
  appowner      VARCHAR(200) NULL,
  owneremail    VARCHAR(200) NULL,
  appdescribe   VARCHAR(200) NULL,
  status        VARCHAR(2)   NOT NULL,
  createdate    DATETIME     NULL,
  creator       VARCHAR(200) NULL,
  modifydate    DATETIME     NULL,
  modifier      VARCHAR(200) NULL,
  version       VARCHAR(20)  NOT NULL,
  id            VARCHAR(36)  NOT NULL,
  public_key    TEXT,
  private_key   TEXT,
  j_public_key  TEXT,
  j_private_key TEXT,
  clienturl     VARCHAR(200) NULL,
  whatsnew      TEXT,
  PRIMARY KEY (id)
)

CREATE TABLE tms.basic.b_role (
  id         VARCHAR(36) NOT NULL,
  name       VARCHAR(50) NULL,
  type       CHAR(50)    NULL,
  isvalid    CHAR(1)     NULL,
  creator    VARCHAR(36) NULL,
  createdate DATETIME    NULL,
  modifier   VARCHAR(36) NULL,
  modifydate DATETIME    NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (creator) REFERENCES basic.employee (userid),
  FOREIGN KEY (modifier) REFERENCES basic.employee (userid)
)

CREATE TABLE tms.basic.billno (
  id         VARCHAR(36) NOT NULL,
  billtype   VARCHAR(20) NULL,
  batchno    VARCHAR(20) NULL,
  serialno   VARCHAR(20) NULL,
  datestring VARCHAR(20) NULL,
  PRIMARY KEY (id)
)


CREATE TABLE tms.basic.bundle (
  id    VARCHAR(36)        NOT NULL,
  item  VARCHAR(36) UNIQUE NULL,
  lang  VARCHAR(36)        NULL,
  value VARCHAR(360)       NULL,
  PRIMARY KEY (id)
)


CREATE TABLE tms.basic.department (
  deptid       VARCHAR(36) NOT NULL,
  deptname     VARCHAR(40) NULL,
  parentdeptid VARCHAR(36) NULL,
  deptgrade    CHAR(1)     NULL,
  remark       VARCHAR(40) NULL,
  creator      VARCHAR(36) NULL,
  createdate   DATE        NULL,
  modifier     VARCHAR(36) NULL,
  modifydate   DATE        NULL,
  manager      VARCHAR(36) NULL,
  leader       VARCHAR(36) NULL,
  manager2     VARCHAR(36) NULL,
  manager3     VARCHAR(36) NULL,
  scrap        VARCHAR(10) NULL,
  coststate    CHAR(1)     NULL,
  orderby      VARCHAR(36) NULL,
  parentid     VARCHAR(36) NULL,
  code         VARCHAR(36) NULL,
  depth        VARCHAR(20) NULL,
  isend        VARCHAR(10) NULL,
  PRIMARY KEY (deptid),
  FOREIGN KEY (leader) REFERENCES basic.employee (userid),
  FOREIGN KEY (manager) REFERENCES basic.employee (userid),
  FOREIGN KEY (manager2) REFERENCES basic.employee (userid),
  FOREIGN KEY (manager3) REFERENCES basic.employee (userid),
  FOREIGN KEY (parentid) REFERENCES basic.department (deptid)
)


CREATE TABLE tms.basic.dictionary (
  dicid  VARCHAR(50) UNIQUE NOT NULL,
  id     VARCHAR(36)        NOT NULL,
  name   VARCHAR(36)        NOT NULL,
  isview CHAR(1)            NULL,
  belong CHAR(1)            NULL,
  PRIMARY KEY (id)
)


CREATE TABLE tms.basic.document (
  id         VARCHAR(20)   NOT NULL,
  bizid      VARCHAR(20)   NULL,
  docname    VARCHAR(200)  NULL,
  history    CHAR(1)       NULL,
  docurl     VARCHAR(200)  NULL,
  creator    VARCHAR(36)   NULL,
  createdate DATE          NULL,
  modifier   VARCHAR(36)   NULL,
  modifydate DATE          NULL,
  remarks    VARCHAR(200)  NULL,
  suffix     VARCHAR(20)   NULL,
  content    TEXT,
  title      VARCHAR(200)  NULL,
  isvip      VARCHAR(1)    NULL,
  deptid     VARCHAR(36)   NULL,
  files      VARCHAR(1200) NULL,
  year       VARCHAR(6)    NULL,
  enable     VARCHAR(6)    NULL,
  type       CHAR(1)       NULL,
  deleted    CHAR(1)       NULL,
  property   CHAR(1)       NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (creator) REFERENCES basic.employee (userid),
  FOREIGN KEY (deptid) REFERENCES basic.department (deptid)
)


CREATE TABLE tms.basic.employee (
  userid              VARCHAR(36)        NOT NULL,
  username            VARCHAR(20)        NULL,
  password            VARCHAR(32)        NULL,
  telno               VARCHAR(20)        NULL,
  sex                 VARCHAR(20)        NULL,
  loginname           VARCHAR(20) UNIQUE NOT NULL,
  email               VARCHAR(30)        NULL,
  lastlogindate       DATETIME           NULL,
  type                VARCHAR(1)         NULL,
  signature           VARCHAR(280)       NULL,
  address             VARCHAR(60)        NULL,
  state               CHAR(2)            NULL,
  status              CHAR(1)            NULL,
  remark              VARCHAR(250)       NULL,
  creator             VARCHAR(60)        NULL,
  createdate          DATETIME           NULL,
  modifier            VARCHAR(60)        NULL,
  modifydate          DATETIME           NULL,
  user_no             VARCHAR(100)       NULL,
  user_key            VARCHAR(255)       NULL,
  inst_id             VARCHAR(36)        NULL,
  pic                 VARCHAR(255)       NULL,
  cntclicks_startdate DATETIME           NULL,
  country             VARCHAR(255)       NULL,
  province            VARCHAR(255)       NULL,
  city                VARCHAR(255)       NULL,
  area                VARCHAR(255)       NULL,
  PRIMARY KEY (userid)
)

CREATE INDEX user_email_uni
  ON tms.basic.employee (email)
CREATE INDEX index_employee1
  ON tms.basic.employee (username)
CREATE INDEX creator
  ON tms.basic.employee (creator)
CREATE INDEX userid
  ON tms.basic.employee (userid)


CREATE TABLE tms.basic.erplog (
  id         VARCHAR(60)   NOT NULL,
  username   VARCHAR(200)  NULL,
  usertime   DATE          NULL,
  usetime    DECIMAL(8, 0) NULL,
  usepackage VARCHAR(50)   NULL,
  usemethod  VARCHAR(50)   NULL,
  usetype    VARCHAR(20)   NULL,
  message    VARCHAR(4000) NULL,
  useclass   VARCHAR(50)   NULL,
  userid     VARCHAR(20)   NULL,
  moduleid   VARCHAR(20)   NULL,
  servername VARCHAR(50)   NULL,
  PRIMARY KEY (id)
)

CREATE TABLE tms.basic.exception (
  id          VARCHAR(36)   NOT NULL,
  name        VARCHAR(50)   NULL,
  users       VARCHAR(500)  NULL,
  description VARCHAR(500)  NULL,
  csql        VARCHAR(1000) NULL,
  circle      VARCHAR(12)   NULL,
  moban       VARCHAR(1000) NULL,
  creator     VARCHAR(36)   NULL,
  createdate  DATE          NULL,
  modifier    VARCHAR(36)   NULL,
  modifydate  DATE          NULL,
  mailtitle   VARCHAR(100)  NULL,
  PRIMARY KEY (id)
)


CREATE TABLE tms.basic.function (
  functionid   VARCHAR(60) NOT NULL,
  functionname VARCHAR(50) NULL,
  moduleid     VARCHAR(20) NULL,
  creator      VARCHAR(36) NULL,
  createdate   DATE        NULL,
  modifier     VARCHAR(36) NULL,
  modifydate   DATE        NULL,
  remark       VARCHAR(60) NULL,
  PRIMARY KEY (functionid),
  FOREIGN KEY (moduleid) REFERENCES basic.module (moduleid)
)


CREATE TABLE tms.basic.holiday (
  id         VARCHAR(36) NOT NULL,
  dates      DATE        NULL,
  year       VARCHAR(4)  NULL,
  y_month    VARCHAR(7)  NULL,
  week       INT         NULL,
  is_holiday CHAR(1)     NULL,
  creator    VARCHAR(36) NULL,
  createdate DATETIME    NULL,
  modifier   VARCHAR(36) NULL,
  modifydate DATETIME    NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (creator) REFERENCES basic.employee (userid),
  FOREIGN KEY (modifier) REFERENCES basic.employee (userid)
)

CREATE TABLE tms.basic.item (
  dicid      VARCHAR(36)  NOT NULL,
  creator    VARCHAR(36)  NULL,
  modifier   VARCHAR(36)  NULL,
  modifydate DATETIME     NULL,
  id         VARCHAR(36)  NOT NULL,
  icascade   CHAR(1)      NULL,
  disabled   CHAR(1)      NULL,
  name       VARCHAR(250) NOT NULL,
  createdate DATETIME     NULL,
  odb        VARCHAR(20)  NULL,
  ikey       VARCHAR(200) NULL,
  memo       VARCHAR(200) NULL,
  PRIMARY KEY (id),
  UNIQUE (ikey, dicid)
)


CREATE TABLE tms.basic.keyword (
  id         VARCHAR(36)    NOT NULL,
  rank       DECIMAL(36, 0) NULL,
  name       VARCHAR(100)   NULL,
  creator    VARCHAR(36)    NULL,
  createdate DATETIME       NULL,
  modifier   VARCHAR(36)    NULL,
  modifydate DATE           NULL,
  type       VARCHAR(36)    NULL,
  PRIMARY KEY (id)
)


CREATE TABLE tms.basic.keyword_re (
  id         VARCHAR(36)   NOT NULL,
  mid        VARCHAR(36)   NULL,
  name       VARCHAR(1000) NULL,
  creator    VARCHAR(36)   NULL,
  createdate DATE          NULL,
  modifier   VARCHAR(36)   NULL,
  modifydate DATE          NULL,
  PRIMARY KEY (id)
)

CREATE TABLE tms.basic.ldap_server (
  id       VARCHAR(36) NOT NULL,
  ip       VARCHAR(20) NULL,
  root     VARCHAR(60) NULL,
  name     VARCHAR(20) NULL,
  account  VARCHAR(36) NULL,
  password VARCHAR(36) NULL,
  PRIMARY KEY (id)
)

CREATE TABLE tms.basic.license (
  id          VARCHAR(36)   NOT NULL,
  name        VARCHAR(60)   NULL,
  description VARCHAR(40)   NULL,
  remark      VARCHAR(40)   NULL,
  creator     VARCHAR(36)   NULL,
  createdate  DATETIME      NULL,
  modifier    VARCHAR(36)   NULL,
  modifydate  DATETIME      NULL,
  expiredate  DATETIME      NULL,
  code        VARCHAR(2000) NULL,
  pub_key     VARCHAR(1000) NULL,
  pri_key     VARCHAR(1000) NULL,
  status      VARCHAR(1)    NULL,
  secret      VARCHAR(1000) NULL,
  mac         VARCHAR(100)  NULL,
  cores       VARCHAR(10)   NULL,
  regdate     DATETIME      NULL,
  max_term    INT           NULL,
  PRIMARY KEY (id)
)


CREATE TABLE tms.basic.link_desktop (
  id         VARCHAR(36)  NOT NULL,
  name       VARCHAR(50)  NULL,
  link       VARCHAR(100) NULL,
  pic        VARCHAR(36)  NULL,
  creator    VARCHAR(36)  NULL,
  createdate DATETIME     NULL,
  modifier   VARCHAR(36)  NULL,
  modifydate DATETIME     NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (pic) REFERENCES basic.annex (id),
  FOREIGN KEY (creator) REFERENCES basic.employee (userid),
  FOREIGN KEY (modifier) REFERENCES basic.employee (userid)
)


CREATE TABLE tms.basic.loginlog (
  id            VARCHAR(36)  NOT NULL,
  userid        VARCHAR(36)  NULL,
  username      VARCHAR(36)  NULL,
  login_ip      VARCHAR(36)  NULL,
  login_address VARCHAR(360) NULL,
  createdate    DATETIME     NULL,
  modifydate    DATETIME     NULL,
  PRIMARY KEY (id)
)


CREATE TABLE tms.basic.message (
  id         VARCHAR(36)   NOT NULL,
  sender     VARCHAR(36)   NULL,
  content    VARCHAR(1000) NULL,
  receiver   VARCHAR(1000) DEFAULT '0',
  creator    VARCHAR(36)   NULL,
  createdate DATETIME      NULL,
  modifier   VARCHAR(36)   NULL,
  modifydate DATE          NULL,
  type       VARCHAR(36)   NULL,
  title      VARCHAR(100)  NULL,
  readed     VARCHAR(2)    DEFAULT '0',
  instate    CHAR(1)       DEFAULT '0',
  outstate   CHAR(1)       DEFAULT '0',
  PRIMARY KEY (id),
  FOREIGN KEY (sender) REFERENCES basic.employee (userid)
)

CREATE TABLE tms.basic.moban (
  id          VARCHAR(36)    NOT NULL,
  name        VARCHAR(36)    NOT NULL,
  description VARCHAR(200)   NULL,
  width       DECIMAL(10, 0) DEFAULT 1,
  height      DECIMAL(10, 0) NULL,
  logo        VARCHAR(36)    NULL,
  type        CHAR(1)        NULL,
  creator     VARCHAR(36)    NULL,
  createdate  DATE           NULL,
  modifier    VARCHAR(36)    NULL,
  modifydate  DATE           NULL,
  printtime   DECIMAL(10, 0) NULL,
  printer     VARCHAR(36)    NULL,
  tablename   VARCHAR(255)   NULL,
  modulename  VARCHAR(255)   NULL,
  dtablename  VARCHAR(255)   NULL,
  PRIMARY KEY (id),
  UNIQUE (name)
)


CREATE TABLE tms.basic.moban_detail (
  id         VARCHAR(36) NOT NULL,
  mobanid    VARCHAR(36) NOT NULL,
  showname   VARCHAR(255) NOT NULL,
  tablename  VARCHAR(255) NULL,
  istitle    VARCHAR(2) DEFAULT '0',
  showtype   VARCHAR(2)   NULL,
  optiontype VARCHAR(60)  NULL,
  field      VARCHAR(255) NOT NULL,
  isform     VARCHAR(2) DEFAULT '0',
  action     VARCHAR(255) NULL,
  isnull     VARCHAR(2) DEFAULT '0',
  editable   VARCHAR(2) DEFAULT '0',
  showvalue  VARCHAR(255) NULL,
  serialno   VARCHAR(2)   NOT NULL,
  width      VARCHAR(2)   NULL,
  name       VARCHAR(36)  NOT NULL,
  fkfield    VARCHAR(36)  NULL,
  PRIMARY KEY (id),
  UNIQUE (mobanid, serialno),
  FOREIGN KEY (mobanid) REFERENCES basic.moban (id)
)


CREATE TABLE tms.basic.moban_list (
  id         VARCHAR(36) NOT NULL,
  mobanid    VARCHAR(36) NOT NULL,
  showname   VARCHAR(255) NOT NULL,
  tablename  VARCHAR(255) NULL,
  istitle    VARCHAR(2) DEFAULT '0',
  showtype   VARCHAR(2)   NULL,
  optiontype VARCHAR(60)  NULL,
  field      VARCHAR(255) NOT NULL,
  isform     VARCHAR(2) DEFAULT '0',
  action     VARCHAR(255) NULL,
  isnull     VARCHAR(2) DEFAULT '0',
  editable   VARCHAR(2) DEFAULT '0',
  showvalue  VARCHAR(255) NULL,
  serialno   VARCHAR(2)   NOT NULL,
  width      VARCHAR(2)   NULL,
  name       VARCHAR(36)  NOT NULL,
  fkfield    VARCHAR(36)  NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (mobanid
  ) REFERENCES basic.moban (id
  )
)


CREATE TABLE tms.basic.module (
  moduleid    VARCHAR(20) NOT NULL,
  modulename  VARCHAR(40) NULL,
  description VARCHAR(40) NULL,
  remark      VARCHAR(40) NULL,
  creator     VARCHAR(36) NULL,
  createdate  DATE        NULL,
  modifier    VARCHAR(36) NULL,
  modifydate  DATE        NULL,
  systemid    CHAR(1)     NULL,
  priority    VARCHAR(3)  NULL,
  PRIMARY KEY (moduleid)
)


CREATE TABLE tms.basic.my_plugin_config (
  mpid      VARCHAR(36) NOT NULL,
  closes    CHAR(1)     NULL,
  plugin_id VARCHAR(36) NULL,
  owns      VARCHAR(36) NULL,
  sort      INT DEFAULT 0,
  PRIMARY KEY (mpid)
)


CREATE TABLE tms.basic.plugin_config (
  pcid      VARCHAR(36) NOT NULL,
  plugin_id VARCHAR(36) NOT NULL,
  username  VARCHAR(36) NOT NULL,
  closed     TINYINT     NOT NULL  ,
  config    TEXT        NOT NULL,
  PRIMARY KEY (pcid)
)


CREATE TABLE tms.basic.re_b_role_function (
  id          VARCHAR(36) NOT NULL,
  b_role_id   VARCHAR(36) NULL,
  function_id VARCHAR(60) NULL,
  creator     VARCHAR(36) NULL,
  createdate  DATETIME    NULL,
  modifier    VARCHAR(36) NULL,
  modifydate  DATETIME    NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (b_role_id) REFERENCES basic.b_role (id),
  FOREIGN KEY (function_id) REFERENCES basic.function (functionid),
  FOREIGN KEY (creator) REFERENCES basic.employee (userid),
  FOREIGN KEY (modifier) REFERENCES basic.employee (userid)
)


CREATE TABLE tms.basic.re_b_role_user (
  id         VARCHAR(36) NOT NULL,
  b_role_id  VARCHAR(36) NULL,
  user_id    VARCHAR(36) NULL,
  creator    VARCHAR(36) NULL,
  createdate DATETIME    NULL,
  modifier   VARCHAR(36) NULL,
  modifydate DATETIME    NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (b_role_id) REFERENCES basic.b_role (id),
  FOREIGN KEY (user_id) REFERENCES basic.employee (userid),
  FOREIGN KEY (creator) REFERENCES basic.employee (userid),
  FOREIGN KEY (modifier) REFERENCES basic.employee (userid)
)


CREATE TABLE tms.basic.re_bill_function (
  id          VARCHAR(36) NOT NULL,
  bill_id     VARCHAR(36) NULL,
  function_id VARCHAR(60) NULL,
  bill_type   VARCHAR(50) NULL,
  creator     VARCHAR(36) NULL,
  createdate  DATETIME    NULL,
  modifier    VARCHAR(36) NULL,
  modifydate  DATETIME    NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (function_id) REFERENCES basic.function (functionid),
  FOREIGN KEY (creator) REFERENCES basic.employee (userid),
  FOREIGN KEY (modifier) REFERENCES basic.employee (userid)
)


CREATE TABLE tms.basic.re_bill_role (
  id         VARCHAR(36) NOT NULL,
  b_role_id  VARCHAR(36) NULL,
  bill_id    VARCHAR(36) NULL,
  bill_type  VARCHAR(50) NULL,
  duty       VARCHAR(50) NULL,
  creator    VARCHAR(36) NULL,
  createdate DATETIME    NULL,
  modifier   VARCHAR(36) NULL,
  modifydate DATETIME    NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (b_role_id) REFERENCES basic.b_role (id),
  FOREIGN KEY (creator) REFERENCES basic.employee (userid),
  FOREIGN KEY (modifier) REFERENCES basic.employee (userid)
)

CREATE TABLE tms.basic.re_fun_license (
  licenseid  VARCHAR(36) NULL,
  functionid VARCHAR(60) NULL,
  creator    VARCHAR(36) NULL,
  createdate DATE        NULL,
  modifier   VARCHAR(36) NULL,
  modifydate DATE        NULL,
  id         VARCHAR(36) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (licenseid) REFERENCES basic.license (id),
  FOREIGN KEY (functionid) REFERENCES basic.function (functionid)
)


CREATE TABLE tms.basic.re_fun_role (
  roleid     VARCHAR(20) NULL,
  functionid VARCHAR(60) NULL,
  creator    VARCHAR(36) NULL,
  createdate DATE        NULL,
  modifier   VARCHAR(36) NULL,
  modifydate DATE        NULL,
  id         VARCHAR(36) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (roleid) REFERENCES basic.role_b (roleid),
  FOREIGN KEY (functionid) REFERENCES basic.function (functionid)
)


CREATE TABLE tms.basic.re_general (
  id         VARCHAR(36)   NOT NULL,
  aid        VARCHAR(36)   NULL,
  bid        VARCHAR(36)   NULL,
  value      VARCHAR(1000) NULL,
  creator    VARCHAR(36)   NULL,
  createdate DATETIME      NULL,
  modifier   VARCHAR(36)   NULL,
  modifydate DATETIME      NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (creator) REFERENCES basic.employee (userid)
)


CREATE TABLE tms.basic.re_module_role (
  id         VARCHAR(20) NOT NULL,
  moduleid   VARCHAR(20) NULL,
  roleid     VARCHAR(20) NULL,
  purview    CHAR(4)     NULL,
  remark     VARCHAR(40) NULL,
  creator    VARCHAR(20) NULL,
  createdate DATE        NULL,
  modifier   VARCHAR(20) NULL,
  modifydate DATE        NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (moduleid) REFERENCES basic.module (moduleid),
  FOREIGN KEY (roleid) REFERENCES basic.role_b (roleid)
)

CREATE TABLE tms.basic.re_user_link_desktop (
  id              VARCHAR(36)    NOT NULL,
  name            VARCHAR(100)   NULL,
  user_id         VARCHAR(36)    NULL,
  link_desktop_id VARCHAR(36)    NULL,
  orderby         DECIMAL(10, 0) NULL,
  creator         VARCHAR(36)    NULL,
  createdate      DATETIME       NULL,
  modifier        VARCHAR(36)    NULL,
  modifydate      DATETIME       NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES basic.employee (userid),
  FOREIGN KEY (creator) REFERENCES basic.employee (userid),
  FOREIGN KEY (modifier) REFERENCES basic.employee (userid)
)


CREATE TABLE tms.basic.re_user_role (
  id         VARCHAR(36) NOT NULL,
  deptid     VARCHAR(36) NULL,
  userid     VARCHAR(36) NULL,
  roleid     VARCHAR(20) NULL,
  remark     VARCHAR(40) NULL,
  creator    VARCHAR(36) NULL,
  createdate DATE        NULL,
  modifier   VARCHAR(36) NULL,
  modifydate DATE        NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (deptid) REFERENCES basic.department (deptid),
  FOREIGN KEY (userid) REFERENCES basic.employee (userid),
  FOREIGN KEY (roleid) REFERENCES basic.role_b (roleid)
)


CREATE TABLE tms.basic.role_b (
  roleid       VARCHAR(20) NOT NULL,
  rolename     VARCHAR(60) NULL,
  description  VARCHAR(40) NULL,
  home_plugins TEXT,
  remark       VARCHAR(40) NULL,
  creator      VARCHAR(36) NULL,
  createdate   DATE        NULL,
  modifier     VARCHAR(36) NULL,
  modifydate   DATE        NULL,
  PRIMARY KEY (roleid)
)

CREATE TABLE tms.basic.tag (
  id         VARCHAR(36)  NOT NULL,
  bizid      VARCHAR(36)  NULL,
  creator    VARCHAR(36)  NULL,
  createdate DATE         NULL,
  modifier   VARCHAR(36)  NULL,
  modifydate DATE         NULL,
  remarks    VARCHAR(200) NULL,
  keyword    VARCHAR(200) NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (creator) REFERENCES tms.basic.employee (userid)
)


CREATE TABLE tms.basic.task (
  id         VARCHAR(36)   NOT NULL,
  taskname   VARCHAR(30)   NULL,
  servername VARCHAR(30)   NULL,
  tasktime   VARCHAR(20)   NULL,
  taskcycle  VARCHAR(20)   NULL,
  tasksecond VARCHAR(20)   NULL,
  taskminute VARCHAR(20)   NULL,
  taskhour   VARCHAR(20)   NULL,
  taskweek   VARCHAR(20)   NULL,
  taskdate   VARCHAR(20)   NULL,
  result     CHAR(1)       NULL,
  consumtime VARCHAR(20)   NULL,
  active     VARCHAR(2)    NULL,
  taskclass  VARCHAR(30)   NULL,
  creator    VARCHAR(36)   NULL,
  createdate DATE          NULL,
  modifier   VARCHAR(36)   NULL,
  modifydate DATE          NULL,
  runstatus  VARCHAR(1)    NULL,
  rundate    TIME          NULL,
  memo       VARCHAR(2000) NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (creator) REFERENCES tms.basic.employee (userid),
  FOREIGN KEY (modifier) REFERENCES tms.basic.employee (userid)
)


CREATE TABLE tms.basic.tasklog (
  id      VARCHAR(36)   NOT NULL,
  taskid  VARCHAR(36)   NOT NULL,
  result  CHAR(1)       NULL,
  usetime VARCHAR(10)   NULL,
  rundate DATETIME      NULL,
  remark  VARCHAR(2000) NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (taskid) REFERENCES tms.basic.task (id)
)


CREATE TABLE tms.tms.app (
  id          VARCHAR(36)  NOT NULL,
  app_name    VARCHAR(255) NULL,
  app_no      VARCHAR(255) NULL,
  description VARCHAR(255) NULL,
  status      VARCHAR(1)   NULL,
  createdate  DATETIME     NULL,
  modifydate  DATETIME     NULL,
  creator     VARCHAR(36)  NULL,
  modifier    VARCHAR(36)  NULL,
  PRIMARY KEY (id)
)


CREATE TABLE tms.tms.app_version (
  id           VARCHAR(36)  NOT NULL,
  app_id       VARCHAR(36)  NULL,
  app_type     VARCHAR(1)   NULL,
  tms_app_ver  VARCHAR(255) NULL,
  ter_com_id   VARCHAR(36)  NULL,
  branch_com   VARCHAR(255) NULL,
  acquirer     VARCHAR(255) NULL,
  app_ver_code VARCHAR(255) NULL,
  basic_ver    VARCHAR(255) NULL,
  remark       VARCHAR(255) NULL,
  status       VARCHAR(1)   NULL,
  createdate   DATETIME     NULL,
  modifydate   DATETIME     NULL,
  creator      VARCHAR(36)  NULL,
  modifier     VARCHAR(36)  NULL,
  PRIMARY KEY (id)
)




CREATE TABLE tms.tms.dll_path (
  id         VARCHAR(36)   NOT NULL,
  ter_com_id VARCHAR(36)   NULL,
  dll_path   VARCHAR(1000) NULL,
  status     VARCHAR(1)    NULL,
  createdate DATETIME      NULL,
  modifydate DATETIME      NULL,
  creator    VARCHAR(36)   NULL,
  modifier   VARCHAR(36)   NULL,
  PRIMARY KEY (id)
)


CREATE TABLE tms.tms.institution (
  id            VARCHAR(36)  NOT NULL,
  inst_no       VARCHAR(36)  NULL,
  name          VARCHAR(100) NULL,
  address       VARCHAR(100) NULL,
  tel           VARCHAR(100) NULL,
  ip            VARCHAR(100) NULL,
  home_path     VARCHAR(255) NULL,
  ftp_username  VARCHAR(50)  NULL,
  ftp_password  VARCHAR(50)  NULL,
  tcp_num       VARCHAR(1)   NULL,
  dial_num      VARCHAR(11)  NULL,
  download_time VARCHAR(15)  NULL,
  enc_type      VARCHAR(1)   NULL,
  ptc_key_index VARCHAR(100) NULL,
  mis_key_index VARCHAR(100) NULL,
  key_type      VARCHAR(1)   NULL,
  fixed_key     VARCHAR(100) NULL,
  remark        VARCHAR(255) NULL,
  status        VARCHAR(1)   NULL,
  createdate    DATETIME     NULL,
  modifydate    DATETIME     NULL,
  creator       VARCHAR(36)  NULL,
  modifier      VARCHAR(36)  NULL,
  PRIMARY KEY (id)
)


CREATE TABLE tms.tms.merchant_info (
  id               VARCHAR(36)  NOT NULL,
  serialno         VARCHAR(15)  NULL,
  name             VARCHAR(100) NULL,
  branchno         VARCHAR(15)  NULL,
  inst_id          VARCHAR(36)  NULL,
  address          VARCHAR(255) NULL,
  opening_time_str TIME         NULL,
  opening_time_end TIME         NULL,
  contactname      VARCHAR(255) NULL,
  contactphone     VARCHAR(255) NULL,
  email            VARCHAR(255) NULL,
  net_server_id    VARCHAR(36)  NULL,
  mer_mcc          VARCHAR(45)  NULL,
  mer_pro          VARCHAR(1)   NULL,
  tran_channel     VARCHAR(45)  NULL,
  pay_channel      VARCHAR(45)  NULL,
  tran_type        VARCHAR(1)   NULL,
  status           VARCHAR(1)   NULL,
  remark           VARCHAR(255) NULL,
  createdate       DATETIME     NULL,
  modifydate       DATETIME     NULL,
  creator          VARCHAR(36)  NULL,
  modifier         VARCHAR(36)  NULL,
  PRIMARY KEY (id)
)


CREATE TABLE tms.tms.param (
  id             VARCHAR(36)   NOT NULL,
  code           VARCHAR(20)   NULL,
  name           VARCHAR(30)   NULL,
  type           CHAR(1)       NULL,
  inst_id        VARCHAR(36)   NULL,
  description    VARCHAR(4000) NULL,
  data_type      VARCHAR(20)   NULL,
  length         INT           NULL,
  def            VARCHAR(50)   NULL,
  group_name     VARCHAR(100)  NULL,
  operation_mode CHAR(1)       NULL,
  is_standard    CHAR(1)       NULL,
  status         CHAR(1)       NULL,
  creator        VARCHAR(36)   NULL,
  createdate     DATETIME      NULL,
  modifier       VARCHAR(36)   NULL,
  modifydate     DATETIME      NULL,
  PRIMARY KEY (id)
)


CREATE TABLE tms.tms.plan_task (
  id             VARCHAR(36)  NOT NULL,
  plan_no        VARCHAR(255) NULL,
  plan_name      VARCHAR(50)  NULL,
  mer_no         VARCHAR(255) NULL,
  ter_no         VARCHAR(255) NULL,
  status         VARCHAR(1)   NULL,
  plandate       DATETIME     NULL,
  closeddate     DATETIME     NULL,
  try_times      VARCHAR(1)   NULL,
  inst_id        VARCHAR(36)  NULL,
  type           VARCHAR(1)   NULL,
  remark         VARCHAR(255) NULL,
  app_ver_id     VARCHAR(35)  NULL,
  template_id    VARCHAR(36)  NULL,
  is_task_update VARCHAR(255) NULL,
  is_task_finish VARCHAR(255) NULL,
  ter_num        VARCHAR(10)  NULL,
  ter_num_finish VARCHAR(10)  NULL,
  creator        VARCHAR(36)  NULL,
  createdate     DATETIME     NULL,
  modifier       VARCHAR(36)  NULL,
  modifydate     DATETIME     NULL,
  PRIMARY KEY (id)
)


CREATE TABLE tms.tms.re_app_tertype (
  id          VARCHAR(36) NOT NULL,
  app_ver_id  VARCHAR(36) NULL,
  ter_type_id VARCHAR(36) NULL,
  status      VARCHAR(1)  NULL,
  createdate  DATETIME    NULL,
  modifydate  DATETIME    NULL,
  creator     VARCHAR(36) NULL,
  modifier    VARCHAR(36) NULL,
  PRIMARY KEY (id)
)


CREATE TABLE tms.tms.re_param_template (
  id              VARCHAR(36) NOT NULL,
  template_id     VARCHAR(36) NULL,
  param_id        VARCHAR(36) NULL,
  def             VARCHAR(50) NULL,
  data_type       VARCHAR(20) NULL,
  length          INT         NULL,
  input_type      VARCHAR(1)  NULL,
  range_start     VARCHAR(10) NULL,
  range_end       VARCHAR(10) NULL,
  isfixed         CHAR(50)    NULL,
  status          CHAR(1)     NULL,
  creator         VARCHAR(36) NULL,
  createdate      DATETIME    NULL,
  modifier        VARCHAR(36) NULL,
  modifydate      DATETIME    NULL,
  group_public_id VARCHAR(36) NULL,
  PRIMARY KEY (id)
)


CREATE TABLE tms.tms.re_plantask_terminal (
  id          VARCHAR(36) NOT NULL,
  plantask_id VARCHAR(36) NULL,
  terminal_id VARCHAR(36) NULL,
  status      VARCHAR(1)  NULL,
  creator     VARCHAR(36) NULL,
  createdate  DATETIME    NULL,
  modifier    VARCHAR(36) NULL,
  modifydate  DATETIME    NULL,
  PRIMARY KEY (id)
)


CREATE TABLE tms.tms.script (
  id         VARCHAR(36)   NOT NULL,
  pck_name   VARCHAR(1000) NULL,
  remark     VARCHAR(255)  NULL,
  status     VARCHAR(255)  NULL,
  createdate DATETIME      NULL,
  modifydate DATETIME      NULL,
  creator    VARCHAR(36)   NULL,
  modifier   VARCHAR(36)   NULL,
  PRIMARY KEY (id)
)

CREATE TABLE tms.tms.template (
  id                           VARCHAR(36)   NOT NULL,
  code                         VARCHAR(20)   NULL,
  name                         VARCHAR(101)  NULL,
  inst_id                      VARCHAR(36)   NULL,
  app_id                       VARCHAR(36)   NULL,
  description                  VARCHAR(4000) NULL,
  version                      VARCHAR(20)   NULL,
  is_standard                  CHAR(1)       NULL,
  status                       CHAR(1)       NULL,
  creator                      VARCHAR(36)   NULL,
  createdate                   DATETIME      NULL,
  modifier                     VARCHAR(36)   NULL,
  modifydate                   DATETIME      NULL,
  type                         CHAR(1)       NULL,
  original_template_version_id VARCHAR(36)   NULL,
  PRIMARY KEY (id)
)


CREATE TABLE tms.tms.ter_company (
  id             VARCHAR(36)  NOT NULL,
  com_no         VARCHAR(255) NULL,
  com_name       VARCHAR(255) NULL,
  address        VARCHAR(255) NULL,
  contact_person VARCHAR(255) NULL,
  tel            VARCHAR(255) NULL,
  sup_phone      VARCHAR(255) NULL,
  fax            VARCHAR(255) NULL,
  post_code      VARCHAR(255) NULL,
  rule_path      VARCHAR(255) NULL,
  service_point  VARCHAR(255) NULL,
  tpm_no         VARCHAR(50)  NULL,
  status         VARCHAR(255) NULL,
  createdate     DATETIME     NULL,
  modifydate     DATETIME     NULL,
  creator        VARCHAR(36)  NULL,
  modifier       VARCHAR(36)  NULL,
  PRIMARY KEY (id)
)

CREATE TABLE tms.tms.ter_server (
  id             VARCHAR(36)  NOT NULL,
  server_no      VARCHAR(50)  NULL,
  server_name    VARCHAR(100) NULL,
  address        VARCHAR(255) NULL,
  inst_id        VARCHAR(36)  NULL,
  contact_person VARCHAR(36)  NULL,
  tel            VARCHAR(36)  NULL,
  email          VARCHAR(36)  NULL,
  remark         VARCHAR(255) NULL,
  type           CHAR(1)      NULL,
  status         CHAR(1)      NULL,
  createdate     DATETIME     NULL,
  modifydate     DATETIME     NULL,
  creator        VARCHAR(36)  NULL,
  modifier       VARCHAR(36)  NULL,
  PRIMARY KEY (id)
)


CREATE TABLE tms.tms.ter_type (
  id           VARCHAR(36)  NOT NULL,
  ter_co       VARCHAR(255) NULL,
  ter_name     VARCHAR(255) NULL,
  ter_com_id   VARCHAR(36)  NULL,
  program_path VARCHAR(255) NULL,
  dll_path_id  VARCHAR(36)  NULL,
  net_license  VARCHAR(255) NULL,
  status       VARCHAR(1)   NULL,
  createdate   DATETIME     NULL,
  modifydate   DATETIME     NULL,
  creator      VARCHAR(36)  NULL,
  modifier     VARCHAR(36)  NULL,
  PRIMARY KEY (id)
)

CREATE TABLE tms.tms.terminals (
  id             VARCHAR(36)  NOT NULL,
  ter_com_id     VARCHAR(36)  NULL,
  ter_type_id    VARCHAR(36)  NULL,
  sn             VARCHAR(255) NULL,
  storagedate    DATETIME     NULL,
  ter_ser_id     VARCHAR(36)  NULL,
  inst_id        VARCHAR(36)  NULL,
  remark         VARCHAR(255) NULL,
  part_one       VARCHAR(255) NULL,
  part_two       VARCHAR(255) NULL,
  part_three     VARCHAR(255) NULL,
  part_four      VARCHAR(255) NULL,
  ter_status     VARCHAR(1)   NULL,
  replace_status VARCHAR(1)   NULL,
  createdate     DATETIME     NULL,
  modifydate     DATETIME     NULL,
  creator        VARCHAR(36)  NULL,
  modifier       VARCHAR(36)  NULL,
  PRIMARY KEY (id)
)


CREATE TABLE tms.tms.tpm (
  id         VARCHAR(36)  NOT NULL,
  tpm_type   VARCHAR(255) NULL,
  ter_com_id VARCHAR(255) NULL,
  remark     VARCHAR(255) NULL,
  status     VARCHAR(1)   NULL,
  createdate DATETIME     NULL,
  modifydate DATETIME     NULL,
  creator    VARCHAR(36)  NULL,
  modifier   VARCHAR(36)  NULL,
  PRIMARY KEY (id)
)
