
CREATE VIEW basic.v_dictionary AS

select i.ikey AS id,i.name AS name,i.dicid AS dicid,i.odb AS odb from basic.item i where (i.disabled is null or (i.disabled <> '1'))


-- 所有用户
union select distinct e.userid, e.username, 'allUsers', e.username from basic.employee e




-- 服务商类型 --
union select DISTINCT '0','网络接入商和产权方','ter_server_type','0'
union select DISTINCT '1','网络接入商','ter_server_type','1'
union select DISTINCT '2','产权方','ter_server_type','2'

-- 服务商 --
union select DISTINCT id,server_name,'ter_server_normal','' from tms.ter_server where status = '1' -- 正常状态的服务商

union select DISTINCT id,server_name,'ter_server_net','' from tms.ter_server where status = '1' and (type='0' or type='1') -- 网络接入服务商

union select DISTINCT id,server_name,'ter_server_owner','' from tms.ter_server where status = '1' and type='2'  -- 产权方


-- 全部正常状态的终端型号
union select DISTINCT id,ter_name,'ter_type','' from tms.ter_type where status = '1' -- 正常状态型号

-- 对应厂商的终端型号
union select DISTINCT id,ter_name, 'ter_type'+ter_com_id,'' from tms.ter_type where status = '1' and ter_com_id=ter_com_id

-- 终端厂商
union select DISTINCT id,com_name,'ter_company',''  from tms.ter_company where status = '1' -- 正常状态厂商


-- 终端状态   0：未初始化；1：待初始化；2：已初始化；3：已注册；4：注销；5：待确认
union select DISTINCT '0','未初始化','ter_status','0'
union select DISTINCT '1','待初始化','ter_status','1'
union select DISTINCT '2','已初始化','ter_status','2'
union select DISTINCT '3','已注册','ter_status','3'
union select DISTINCT '4','注销','ter_status','4'
union select DISTINCT '5','待确认','ter_status','5'

-- 换机状态
union select DISTINCT '0','不允许','replace_status','0'
union select DISTINCT '1','允许','replace_status','1'

-- 通用状态
union select DISTINCT '0','失效','general_status','0'
union select DISTINCT '1','正常','general_status','1'

-- 下载任务状态
union select DISTINCT '0','删除','task_status','0'
union select DISTINCT '1','正常','task_status','1'
union select DISTINCT '2','下载完成','task_status','2'

-- 是否
union select DISTINCT '0','否','is_no','0'
union select DISTINCT '1','是','is_no','1'


-- app
union select DISTINCT id,app_name,'app',''  from tms.app where status = '1' -- 启用状态app

-- tpm
union select DISTINCT t.id,t.tpm_type,'tpm',''  from tms.tpm t where t.status = '1' -- 启用tpm

union select DISTINCT t.id,t.tpm_type,'tpm'+t.ter_com_id,'' from tms.tpm t where t.status = '1' -- 对应厂商tpm

-- 密钥类型（软加密选择，0：随机密钥；1：固定密钥）key_type
union select DISTINCT '0','随机密钥','key_type','0'
union select DISTINCT '1','固定密钥','key_type','1'

-- 加密方式（0：软加密；1：硬加密）enc_type
union select DISTINCT '0','软加密','enc_type','0'
union select DISTINCT '1','硬加密','enc_type','1'

-- 机构
union select DISTINCT id,name,'inst','' from tms.institution where status = '1'

-- 参数组类别
union select DISTINCT '0','主控参数','param_group','0'
union select DISTINCT '1','子应用参数','param_group','1'
union select DISTINCT '2','分机构个性化参数','param_group','2'

-- 参数项类型
union select DISTINCT '0','文本型','param_type','0'
union select DISTINCT '1','数字型','param_type','1'

-- 脚本程序状态
union select DISTINCT '0','文件已删除','script_status','0'
union select DISTINCT '1','正常','script_status','1'
union select DISTINCT '2','待审核','script_status','2'
union select DISTINCT '3','待完整性不通过','script_status','3'

-- 所有厂商dll路径
union select DISTINCT id,dll_path,'dll_path',''  from tms.dll_path where status = '1'

-- 某个厂商dll路径
union select DISTINCT id,dll_path, 'com_dll_path'+ter_com_id ,'' from tms.dll_path where status = '1' and ter_com_id=ter_com_id

-- 应用类别
union select DISTINCT '0','脚本解释器应用','app_type','0'
union select DISTINCT '1','传统POS应用','app_type','1'
union select DISTINCT '2','远程下载主控应用','app_type','2'
union select DISTINCT '3','第三方应用','app_type','3'

-- 参数模板
union select DISTINCT id, code+''+ name,'template','' from tms.template where status = '1'

-- 更新任务类型
union select DISTINCT '0','远程下载','task_type','0'
union select DISTINCT '1','初始化','task_type','1'

-- 任务激活
union select '1','激活','active','0'
union select '0','未激活','active','1'

 -- 任务周期
union select '0','每分钟','taskcycle','0'
union select '1','每小时','taskcycle','1'
union select '2','每天','taskcycle','2'
union select '3','每周','taskcycle','3'
union select '4','每月','taskcycle','4'
union select '5','只一次','taskcycle','5'
union select '6','立即开始','taskcycle','6'


-- 用户状态
union select '0','正常','userStatus','0'
union select '1','冻结','userStatus','1'
union select '2','删除','userStatus','2'

-- 用户类型（关系表也用）
union select '0','平台管理员','userType2','0'
union select '2','开发者','userType2','2'
union select '3','生产厂商','userType2','3'

-- 商户状态 '状态 0未启用 1冻结 2启用';
union select '0','未启用','mer_status','0'
union select '1','冻结','mer_status','1'
union select '2','启用','mer_status','2'