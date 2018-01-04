package pax.depot.job;

import org.apache.log4j.Logger;
import sand.depot.job.BaseJob;
import tool.dao.BizObject;
import tool.dao.QueryFactory;

import java.util.List;



/**
 * 定期查询商户，并更新本地商户信息
*/
public class SynTelJob extends BaseJob{
	static Logger logger = Logger.getLogger(SynTelJob.class);
	
	@Override
	public String run() throws Exception {
		 logger.info("同步手机任务开始");
		
		 String sql =" select * from  easypay_syntelno t where t.result='0'   order by  t.createdate "; //失败的记录
		 
		 QueryFactory qf =new  QueryFactory("easypay_syntelno");
		 
		 List <BizObject>list  =qf.executeQuerySQL(sql);
		 
		 for (int i=0;i<list.size();i++)
		 {
			BizObject obj =list.get(i);
			
			int time=obj.getInt("time", 1); 
			
			if(time<5)
			{
				String userid=obj.getString("userid");
				String telno=obj.getString("telno");
		//		NetworkService nk=getNetworkService();
//				AccountAH.SynPayTelNo(userid, telno, nk,obj.getId(),this);	
			}
		 }

		 logger.info("同步手机任务结束");
		 return "ok";
	}
	


}
