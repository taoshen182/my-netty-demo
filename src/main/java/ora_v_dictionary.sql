create or replace view v_dictionary as
select i.iKEY AS id,i.NAME AS name,i.DICID AS dicId,i.ODB AS odb from item i where (i.DISABLED is null or (i.DISABLED <> '1'))

--功能访问控制
union select DISTINCT access_id,access_state,FUNCTION_ID,'0' from UPORDOWN
union select DISTINCT 'denied','拒绝访问','access','0' from dual
union select DISTINCT 'allow','允许访问','access','0' from dual

-- 性别
union select DISTINCT '0','男','gender','2' from dual
union select DISTINCT '1','女','gender','3' from dual
-- 密码问题
union select DISTINCT '0','您母亲的姓名是什么？','problem','0'  from dual
union select DISTINCT '1','您父亲的姓名是什么？','problem','1' from dual
union select DISTINCT '2','您的大学名字是什么？','problem','2' from dual
union select DISTINCT '3','您的出生地是哪里？','problem','3'  from dual
union select DISTINCT '4','您最喜欢的电影是什么？','problem','4'from dual
union select DISTINCT '5','您最喜欢的书是什么？','problem','5' from dual
union select DISTINCT '6','其它','problem','6' from dual

--应用类型
union select DISTINCT '0','卡账户应用','apptype','0'   from dual
union select DISTINCT '1','网络支付应用','apptype','1'   from dual
union select DISTINCT '2','账户安全应用','apptype','2'   from dual

-- 是否置顶
union select DISTINCT '0','否','istop','0'  from dual
union select DISTINCT '1','是','istop','1' from dual

-- 是否发布
union select DISTINCT '0','否','ispublish','0'  from dual
union select DISTINCT '1','是','ispublish','1' from dual


-- 是否记名卡
union select DISTINCT '0','记名卡','isAnonymous','0'   from dual
union select DISTINCT '1','非记名卡','isAnonymous','1' from dual


union select c.cardno||'',c.cardno||'',memberid,'' from card c where c.Remove='0'  -- 当前登录人的卡号集合
union select c.cardno||'',c.cardno||'',memberid||'anonymous','' from card c where c.isAnonymous='0' and c.Remove='0'


-- 用户
union select distinct e.USERID AS USERID,e.USERNAME AS USERNAME,'users' AS users,'odb' AS odb from employee e where ((e.SCRAP != '0') or (e.SCRAP is null))

-- 实名认证证件类型
union select DISTINCT '1','身份证','certificationtype','1'  from dual
union select DISTINCT '3','护照','certificationtype','2' from dual
union select DISTINCT '2','军官证','certificationtype','3'  from dual
union select DISTINCT '4','营业执照','certificationtype','4' from dual
union select DISTINCT '5','驾驶证','certificationtype','5' from dual
union select DISTINCT '6','组织机构代码证','certificationtype','6' from dual

-- 银行名称
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

-- 账户信息服务类型
union select DISTINCT '01','交易短信通知（预付费卡）','infotype','0' from dual
union select DISTINCT '02','交易短信通知（支付账户）','infotype','1' from dual
union select DISTINCT '03','对账单邮寄（电子邮箱）','infotype','2' from dual

-- 账户信息服务状态
union select DISTINCT '01','关闭','infostatus','0' from dual
union select DISTINCT '02','已开启','infostatus','1' from dual
union select DISTINCT '','关闭','infostatus','2' from dual

-- 交易类型
union select '0','全部','trade_type','0' from dual
union select '100','主账户充值','trade_type','1' from dual
union select '110','现金退款','trade_type','2' from dual
union select '120','转账(进)','trade_type','3' from dual
union select '121','转账(出)','trade_type','4' from dual
union select '150','IC卡消费','trade_type','5' from dual
union select '151','主账户消费','trade_type','6' from dual
union select '600','账户管理费','trade_type','7' from dual

-- 交易状态
union select '0','全部','trade_status','0' from dual
union select '1','正常','trade_status','1' from dual
union select '2','冲正','trade_status','2' from dual
union select '3','被冲正','trade_status','3' from dual
union select '4','临时','trade_status','4' from dual
union select '5','撤销','trade_status','5' from dual
union select '6','被撤销','trade_status','6' from dual
union select '8','失效','trade_status','7' from dual
union select '10','退货','trade_status','8' from dual

--订单操作结果
union select '0','交易中','order_status','0' from dual
union select '1','交易成功','order_status','1' from dual
union select '2','交易失败','order_status','2' from dual

--订单交易结果
union select '0','处理中','order_result_status','0' from dual
union select '1','处理成功','order_result_status','1' from dual
union select '2','处理失败','order_result_status','2' from dual


-- 手机充值金额
union select '10','10元','phonerecharge','0' from dual
union select '20','20元','phonerecharge','1' from dual
union select '30','30元','phonerecharge','2' from dual
union select '50','50元','phonerecharge','3' from dual
union select '100','100元','phonerecharge','4' from dual
union select '300','300元','phonerecharge','5' from dual

-- 手机充值规则优先级别
union select '0','一般','codex_lv','0' from dual
union select '1','优先','codex_lv','1' from dual


--手机充值操作结果
union select '0','充值中','recharge','0' from dual
union select '1','充值成功','recharge','1' from dual
union select '2','充值失败','recharge','2' from dual

--手机充值支付结果
union select '0','支付成功','paystatus','0' from dual
union select '1','支付失败','paystatus','1' from dual
union select '2','退款完成','paystatus','2' from dual

--手机充值结果
union select '0','受理中','ofstatus','0' from dual
union select '1','充值成功【殴飞】','ofstatus','1' from dual
union select '2','充值失败【欧飞】','ofstatus','2' from dual

union
select '1','激活','active','' from dual  --任务激活
union
select '0','未激活','active','' from dual

union
select '0','每分钟','taskcycle','' from dual  --任务周期
union
select '1','每小时','taskcycle','' from dual
union
select '2','每天','taskcycle','' from dual
union
select '3','每周','taskcycle','' from dual
union
select '4','每月','taskcycle','' from dual
union
select '5','只一次','taskcycle','' from dual
union
select '6','立即开始','taskcycle','' from dual




--资金流
union select 'all','全部','pay_direction','0' from dual
union select 'in','收入','pay_direction','1' from dual
union select 'out','支出','pay_direction','2' from dual


--支付方式
union select '1','B2C 网银','paymentType','0' from dual
union select '2','B2B 网银','paymentType','1' from dual
union select '3','预付费卡','paymentType','2' from dual
union select '4','易生账户','paymentType','3' from dual
union select '5','现金','paymentType','4' from dual
union select '','其它','paymentType','5' from dual


-- 网络账户绑定银行名称
union select DISTINCT '1','工商银行','banktype','1'  from dual
union select DISTINCT '2','招商银行','banktype','2' from dual
union select DISTINCT '3','建设银行','banktype','3'  from dual
union select DISTINCT '4','中国银行','banktype','4' from dual
union select DISTINCT '5','交通银行','banktype','5' from dual
union select DISTINCT '6','光大银行','banktype','6' from dual
union select DISTINCT '7','杭州银行','banktype','7' from dual
union select DISTINCT '8','北京农商银行','banktype','8' from dual
union select DISTINCT '9','中国邮政储蓄银行','banktype','9' from dual
union select DISTINCT '10','宁波银行','banktype','10' from dual
union select DISTINCT '11','深圳发展银行','banktype','11' from dual

-- 用户地区
union select DISTINCT e.area,e.area,area,'' from employee e
-- 预付费卡站内消息类型
union select DISTINCT '1','充值','messagetype','0' from dual
union select DISTINCT '2','消费','messagetype','1' from dual
union select DISTINCT '3','转账','messagetype','2' from dual
union select DISTINCT '4','支付密码修改','messagetype','3' from dual
union select DISTINCT '5','活动消息','messagetype','4' from dual
union select DISTINCT '6','其他','messagetype','5' from dual
-- 预付费卡类型
union select DISTINCT c.cardtypetext,c.cardtypetext,cardtypetext,'' from card c
-- 登陆频次
union select DISTINCT '1','最近一周','ci','0' from dual
union select DISTINCT '2','最近一个月','ci','1' from dual
union select DISTINCT '3','最近三个月','ci','2' from dual
union select DISTINCT '4','最近一年','ci','3' from dual
-- 注册时间
union select DISTINCT '1','一月内','regdate','0' from dual
union select DISTINCT '2','三月内','regdate','1' from dual
union select DISTINCT '3','一年内','regdate','2' from dual
union select DISTINCT '4','一年以上','regdate','3' from dual

--支付账户交易类型
union select 'all','全部','pay_trade_type','0' from dual
union select 'payment','支付','pay_trade_type','1' from dual
union select 'refund','退款','pay_trade_type','2' from dual
union select 'transfer','转账','pay_trade_type','3' from dual
union select 'withdrawn','提现','pay_trade_type','4' from dual
union select 'charge','充值','pay_trade_type','5' from dual
union select 'withdrawnRef','提现拒绝','pay_trade_type','6' from dual
union select 'refundRef','退款拒绝','pay_trade_type','7' from dual   


--交易类型
union select 'all','全部','tradeType','0' from dual
union select 'payment','支付','tradeType','1' from dual
union select 'refund','退款','tradeType','2' from dual
union select 'transfer','转账','tradeType','3' from dual
union select 'withdrawn','提现','tradeType','4' from dual
union select 'charge','充值','tradeType','5' from dual


--交易状态
union select DISTINCT 'all','全部','tradeStatus','0' from dual
union select DISTINCT 'starting','开始','tradeStatus','1' from dual
union select DISTINCT 'processing','处理中','tradeStatus','2' from dual
union select DISTINCT 'completed','完成','tradeStatus','3' from dual
union select DISTINCT 'closed','关闭','tradeStatus','4' from dual
union select DISTINCT 'standby','待处理','tradeStatus','5' from dual

--邮轮支付状态
union select DISTINCT '','全部','cruiseStatus','0' from dual
union select DISTINCT 'waitpay','未支付','cruiseStatus','1' from dual
union select DISTINCT 'paid','已支付','cruiseStatus','2' from dual
union select DISTINCT 'audited','已审核','cruiseStatus','3' from dual
union select DISTINCT 'fail','支付失败','cruiseStatus','4' from dual

--会员中心订单类别
union select DISTINCT 'cruise','邮轮港务费支付','memType','0' from dual
union select DISTINCT 'phone','手机充值','memType','1' from dual

--通用的是否
union select DISTINCT '1','是','boolean','0' from dual
union select DISTINCT '0','否','boolean','1' from dual

--商户状态
union select DISTINCT '0','未发布','merchantStatus','0' from dual
union select DISTINCT '1','已发布','merchantStatus','1' from dual
union select DISTINCT '2','停用','merchantStatus','2' from dual
union select DISTINCT '3','删除','merchantStatus','3' from dual;


