# rangers-modules-apifunc

## 数据服务商的API接口访问管理

### API接口访问规则：?funcName=***&appId=****&accessToken=***

	funcName:接口名；
	appId：应用名；
	accessToken: MD5(appId+secretKey)32位不区分大小写

## 示例

	1 访问：http://localhost:8080/funcs/api/one-month?funcName=GET_MONTH_ORDERS&appId=12142
	 {"errorCode":40001,"msg":"不合法的appId"}

	2 创建APP：http://{{IP}}:8080/access/control

     =>
	   	{
		  "alltimes": 10,
		  "appGrade": "黄金级别",
		  "refToken": "DeanKano",
		  "validate": "2018-12-01"
		}
	
	<=
	{
	  "appId": "1b11edbacb2d48bb6af099512067824e",
	  "refToken": "DeanKano",
	  "secretKey": "0cbaa6029aaabcc95f",
	  "createDate": 1542332856984,
	  "validDate": 1543593600000,
	  "appGrade": "黄金级别",
	  "disabled": false,
	  "apisJson": "{\"GET_MONTH_ORDERS\":{\"calltimes\":0,\"alltimes\":10},\"GET_YERAR_ORDERS\":{\"calltimes\":0,\"alltimes\":10}}",
	  "apis": {
	    "GET_MONTH_ORDERS": {
	      "calltimes": 0,
	      "alltimes": 10
	    },
	    "GET_YERAR_ORDERS": {
	      "calltimes": 0,
	      "alltimes": 10
	    }
	  }
	}
	
	...
	MD5(1b11edbacb2d48bb6af099512067824e0cbaa6029aaabcc95f) =>  accessToken=20817a59e9a1747636aab4297b806fae
	
	3 再次访问：http://localhost:8080/funcs/api/one-month?funcName=GET_MONTH_ORDERS&appId=1b11edbacb2d48bb6af099512067824e&accessToken=20817a59e9a1747636aab4297b806fae
     => 成功
	{
	    "msg": "you get orders of one month"
	}