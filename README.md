#apifunc-manage

## 数据服务商的API接口访问管理

### API接口访问规则：?funcName=***&appId=****&accessToken=***

### funcName:接口名；appId：应用名；accessToken:MD5(appId+secretKey)32位不区分大小写

## 示例

### 访问：http://localhost:8080/funcs/api/one-month?funcName=GET_MONTH_ORDERS&appId=12142
#### {"errorCode":40001,"msg":"不合法的appId"}

### 创建APP：http://{{IP}}:8080/access/control
    =>
    {
		"refToken":"xiaolang",
		"validate":"2018-06-06",
		"appGrade":"黄金等级",
		"alltimes":100
	}
	
	<=
	{
		    "appId": "8d481623216a1c9affa76992471f67b9",
		    "refToken": "xiaolang",
		    "secretKey": "259934d00691d1aa13",
		    "createDate": 1513262553736,
		    "validDate": null,
		    "appGrade": "黄金等级",
		    "disabled": false,
		    "apisJson": "{\"GET_MONTH_ORDERS\":{\"calltimes\":0,\"alltimes\":100},\"GET_YERAR_ORDERS\":{\"calltimes\":0,\"alltimes\":100}}",
		    "apis": {
		        "GET_MONTH_ORDERS": {
		            "calltimes": 0,
		            "alltimes": 100
		        },
		        "GET_YERAR_ORDERS": {
		            "calltimes": 0,
		            "alltimes": 100
		        }
		    }
		}
	
	...
	MD5(8d481623216a1c9affa76992471f67b9259934d00691d1aa13) => 	accessToken=88BD683AD71D08B50B09AA2C1CD4FB40
	
### 访问：http://localhost:8080/funcs/api/one-month?funcName=GET_MONTH_ORDERS&appId=8d481623216a1c9affa76992471f67b9&accessToken=88BD683AD71D08B50B09AA2C1CD4FB40
    => 成功
	{
	    "msg": "you get orders of one month"
	}