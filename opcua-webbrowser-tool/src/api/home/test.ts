import { ModellingServiceApi } from "./modellingserviceapi";
import { UaAccessLevel, UaMethodTriggerType, UaValueType } from "./modellingapitypes";
import { UaExpression, UaExpressionOperator } from "./uaexpression";

class Test
{
    public async run()
    {
        this.__testExpression();
    }

    private async __testApi()
    {
        console.log("Run test");
        
        let api = new ModellingServiceApi("http://localhost:4000");
        
        let ret = await api.setDescription({
            sid: 1,
            nodeId: "ns=7;i=3",
            desc: "cbd"
        }); 

        console.log(ret);
    }

    private __testExpression()
    {
        let expression : UaExpression = new UaExpression;

        expression.expressions.push({
            field: { 
                browsePath: ["Device1","ProcessValue","Temperature"], 
                dataType: UaValueType.Double, 
                dataTypeId: "i=11",
                name: "温度",
                path: ["设备1", "过程变量"] 
            },
            operator: UaExpressionOperator.NotBetween,
            value : [12.5, 25.6]
        });

        expression.expressions.push({
            field: { 
                browsePath: ["Device1","ProcessValue","Status"], 
                dataType: UaValueType.Enumeration, 
                dataTypeId: "ns=6;i=25",
                name: "状态",
                path: ["设备1", "过程变量"]
            },
            operator: UaExpressionOperator.InList,
            value : [1,3,4]
        });

        expression.requireAll = false;

        let json = expression.toContentFilter();

        let expressionFromJson = new UaExpression;
        expressionFromJson.fromContentFilter(json);

        console.log(expressionFromJson.expressions);
        console.log(expressionFromJson.requireAll);
    }
}

export let test = new Test;
test.run();