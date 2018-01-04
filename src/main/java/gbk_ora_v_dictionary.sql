create or replace view v_dictionary as
select i.iKEY AS id,i.NAME AS name,i.DICID AS dicId,i.ODB AS odb from item i where (i.DISABLED is null or (i.DISABLED <> '1'))
union select DISTINCT '0','男','gender','2' from dual
union select DISTINCT '1','女','gender','3' from dual
union select DISTINCT '0','您母亲的姓名是什么？','problem','0'  from dual
union select DISTINCT '1','您父亲的姓名是什么？','problem','1' from dual
union select DISTINCT '2','您的大学名字是什么？','problem','2' from dual
union select DISTINCT '3','您的出生地是哪里？','problem','3'  from dual
union select DISTINCT '4','您最喜欢的电影是什么？','problem','4'from dual
union select DISTINCT '5','您最喜欢的书是什么？','problem','5' from dual
union select DISTINCT '6','其它','problem','6' from dual
union select DISTINCT '0','卡账户应用','apptype','0'   from dual
union select DISTINCT '1','网络支付应用','apptype','1'   from dual
union select DISTINCT '2','账户安全应用','apptype','2'   from dual
union select DISTINCT '0','否','istop','0'  from dual
union select DISTINCT '1','是','istop','1' from dual
union select DISTINCT '0','否','ispublish','0'  from dual
union select DISTINCT '1','是','ispublish','1' from dual
union select DISTINCT '0','记名卡','isAnonymous','0'   from dual
union select DISTINCT '1','非记名卡','isAnonymous','1' from dual
union select c.cardno||'',c.cardno||'',memberid,'' from card c where c.Remove='0'  -- 当前登录人的卡号集合
union select c.cardno||'',c.cardno||'',memberid||'anonymous','' from card c where c.isAnonymous='0' and c.Remove='0'
union select distinct e.USERID AS USERID,e.USERNAME AS USERNAME,'users' AS users,'odb' AS odb from employee e where ((e.SCRAP != '0') or (e.SCRAP is null))
union select DISTINCT '1','身份证','certificationtype','1'  from dual
union select DISTINCT '3','护照','certificationtype','2' from dual
union select DISTINCT '2','军官证','certificationtype','3'  from dual
union select DISTINCT '4','营业执照','certificationtype','4' from dual
union select DISTINCT '5','驾驶证','certificationtype','5' from dual
union select DISTINCT '6','组织机构代码证','certificationtype','6' from dual
union select DISTINCT '1','工商银行','bank_type','1'  from dual
union select DISTINCT '2','农业银行','bank_type','2' from dual
union select DISTINCT '3','建设银行','bank_type','3'  from dual
union select DISTINCT '4','中国银行','bank_type','4' from dual
union select DISTINCT '5','民生银行','bank_type','5' from dual
union select DISTINCT '6','交通银行','bank_type','6' from dual
union select DISTINCT '7','招商银行','bank_type','7' from dual
union select DISTINCT '8','华夏银行','bank_type','8' from dual
union select DISTINCT '9','光大银行','bank_type','9' from dual
union select DISTINCT '10','兴业银行','bank_type','10' from dual
union select DISTINCT '11','广发银行','bank_type','11' from dual
union select DISTINCT '12','深发展银行','bank_type','12' from dual
union select DISTINCT '01','交易短信通知（预付费卡）','infotype','0' from dual
union select DISTINCT '02','交易短信通知（支付账户）','infotype','1' from dual
union select DISTINCT '03','对账单邮寄（电子邮箱）','infotype','2' from dual
union select DISTINCT '01','关闭','infostatus','0' from dual
union select DISTINCT '02','已开启','infostatus','1' from dual
union select DISTINCT '','关闭','infostatus','2' from dual
union select '0','全部','trade_type','0' from dual
union select '100','主账户充值','trade_type','1' from dual
union select '110','现金退款','trade_type','2' from dual
union select '120','转账(进)','trade_type','3' from dual
union select '121','转账(出)','trade_type','4' from dual
union select '150','IC卡消费','trade_type','5' from dual
union select '151','主账户消费','trade_type','6' from dual
union select '600','账户管理费','trade_type','7' from dual
union select '0','全部','trade_status','0' from dual
union select '1','正常','trade_status','1' from dual
union select '2','冲正','trade_status','2' from dual
union select '3','被冲正','trade_status','3' from dual
union select '4','临时','trade_status','4' from dual
union select '5','撤销','trade_status','5' from dual
union select '6','被撤销','trade_status','6' from dual
union select '8','失效','trade_status','7' from dual
union select '10','退货','trade_status','8' from dual
union select '10','10元','phonerecharge','0' from dual
union select '20','20元','phonerecharge','1' from dual
union select '30','30元','phonerecharge','2' from dual
union select '50','50元','phonerecharge','3' from dual
union select '100','100元','phonerecharge','4' from dual
union select '300','300元','phonerecharge','5' from dual
union select '0','一般','codex_lv','0' from dual
union select '1','优先','codex_lv','1' from dual
union select '0','充值中','recharge','0' from dual
union select '1','充值成功','recharge','1' from dual
union select '2','充值失败','recharge','2' from dual
union select '0','支付成功','paystatus','0' from dual
union select '1','支付失败','paystatus','1' from dual
union select '2','退款完成','paystatus','2' from dual
union select '0','受理中','ofstatus','0' from dual
union select '1','充值成功【殴飞】','ofstatus','1' from dual
union select '2','充值失败【欧飞】','ofstatus','2' from dual
union select '1','激活','active','' from dual  
union select '0','未激活','active','' from dual
union select '0','每分钟','taskcycle','' from dual  
union select '1','每小时','taskcycle','' from dual
union select '2','每天','taskcycle','' from dual
union select '3','每周','taskcycle','' from dual
union select '4','每月','taskcycle','' from dual
union select '5','只一次','taskcycle','' from dual
union select '6','立即开始','taskcycle','' from dual;