package com.qiaoyy.util;




public class MBResponseCode {

	public static final String SUCCESS = "0";
	public static final String ERROR = "1";
	public static final String USERNOTEXIST= "20000";
	public static final String USERNAMEEXIST="20001";
	public static final String RALLYNOTEXIST= "30001";
	public static final String RALLY_USERNOTEXIST= "30002";
	public static final String RALLY_OVERMAXMEMBERSIZE= "30003";
	public static final String RALLY_AlreadyInOtherRally= "30004";
	public static final String RALLY_NotJoined= "30005";
	public static final String RALLY_UserCountInvalidate= "30006";
	public static final String RALLY_USERNOTCREATER="30007";
	
	public static final String NEWS_LIKED="30100";
	public static final String NEWS_GETMOREERROR="30101";
	public static final String NEWS_USERHOBBYOVER="30102";//添加个人兴趣超出个人最大兴趣数
	
	public static final String SYSTEM_ERROR = "99";
	public static final String TLSSIGN_INVALIDATE= "90";
	
	public static final String BLACK_USERLIST="40000";
	
	public static final String RUN_OVERSIGNTIME="50001";//超出报名时间
	public static final String RUN_OVERMAXCOUNT="50002";//超出最大数量
	public static final String RUN_USEREXIST="50003";//用户存在
	public static final String RUN_NOTSIGN="50004";//用户不存在
	public static final String RUN_CREATERNOTQUIT="50005";//
	public static final String RUN_NOTHAVE="50006";//约跑不存在
	public static final String RUN_USERNOTCREATER="50007";//用户不是创建者
	public static final String RUN_OUTOFMASS="50008";//超出群发限制
	public static final String RUN_RUNGETMORERROR="50009";//约跑getmore错误（传递的runid不存在已经被删除）
	public static final String RUN_RUNNEEDRUNVERIFY="50010";//加入需要审核
	public static final String RUN_OVERVERIFYTIME="50011";//签到时间不符合

	public static final String GP_OVERSIGNTIME="60001";//报名时间异常（活动未开始，活动已结束）
	public static final String GP_GPIDERROR="60002";//gpid错误 不处在gp列表中
	public static final String GP_USEREXIST="60003";//用户已经报名该gp
	public static final String GP_SUCCESSBUTGPIDERROR="60004";//ridedata数据上报成功 上传的gpid 错误
	public static final String GP_SUCCESSBUTGPCOMPLETE="60005";//ridedata数据上报成功 上传的gpid 该用户之前已经完赛
	public static final String GP_SUCCESSBUTGPTIMEERROR="60006";//ridedata数据上报成功 上传的gpid 该gp活动时间错误
	
	public static final String BOX_GETMOREERROR="70001";//获取box评论加载更多 失败 传的id可能已经删除
	public static final String BOX_LIKED="70002";//已经赞过
	
	public static final String INSURANCE_CHECKFALSE="80001";//保险分不满足
	public static final String INSURANCE_AGEFALSE="80002";//被保人年龄不满足
	public static final String INSURANCE_NOUSER="80003";//被保人在摩托邦不存在
	public static final String INSURANCE_ADDERROR="80004";//悟空保返回投保失败
	public static final String INSURANCE_GETERROR="80005";//悟空保返回查询订单失败
	public static final String INSURANCE_PRICEERROR="80006";//计算价钱时小于20000
	public static final String INSURANCE_MONTHERROR="80007";//计算价钱时购买时间大于60个月
	public static final String INSURANCE_CARCHECKCODE="80008";//验车码与服务器不一致
	
	public static final String SCHOOL_GETMOREERROR="90001";//骑行学院comment getmore 失败
}
