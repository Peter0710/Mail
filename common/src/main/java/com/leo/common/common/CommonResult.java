package com.leo.common.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.extension.api.R;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Liu
 */
public class CommonResult extends HashMap<String, Object> {

    public static final int SUCCESS_CODE = 2000;

    public static final int FAILURE_CODE = 5000;

    public CommonResult() {
    }

    public static CommonResult ok() {
        CommonResult result = new CommonResult();
        result.put("code", SUCCESS_CODE);
        result.put("msg", "success");
        return result;
    }

    public static CommonResult error() {
        CommonResult result = new CommonResult();
        result.put("code", FAILURE_CODE);
        result.put("msg", "fail");
        return result;
    }

    public CommonResult set(Map<String, Object> map) {
        this.putAll(map);
        return this;
    }

    public CommonResult set(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public Integer getCode() {
        return (Integer) this.get("code");
    }

    public <T> T getData(String key, TypeReference<T> reference){
        Object o = this.get(key);
        String string = JSON.toJSONString(o);
        T t = JSON.parseObject(string, reference);
        return t;
    }
}
