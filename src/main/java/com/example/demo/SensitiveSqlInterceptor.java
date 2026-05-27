package com.example.demo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.sql.Connection;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import com.example.demo.annotation.SensitiveSql;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@Intercepts({
    @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class SensitiveSqlInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();

        // 通过反射获取 MappedStatement
        MappedStatement mappedStatement = null;
        try {
            Field delegateField = statementHandler.getClass().getDeclaredField("delegate");
            delegateField.setAccessible(true);
            Object delegate = delegateField.get(statementHandler);
            Field msField = delegate.getClass().getDeclaredField("mappedStatement");
            msField.setAccessible(true);
            mappedStatement = (MappedStatement) msField.get(delegate);
        } catch (Exception ignored) {
            // 反射失败则跳过拦截
        }

        if (mappedStatement == null) {
            return invocation.proceed();
        }

        // 检查是否包含敏感注解
        SensitiveSql sensitiveSql = getSensitiveSqlAnnotation(mappedStatement);
        if (sensitiveSql == null) {
            return invocation.proceed();
        }

        // 获取 BoundSql 并处理参数
        BoundSql boundSql = statementHandler.getBoundSql();
        String sql = boundSql.getSql();

        // 获取参数并进行掩码处理
        Object parameterObject = boundSql.getParameterObject();
        Map<String, Object> maskedParams = new HashMap<>();

        // 处理参数
        processParameters(mappedStatement, boundSql, parameterObject, maskedParams, sensitiveSql);

        // 输出日志
//        log.info("SQL: {}", sql);
        
//        log.info("Parameters: {}", formatParameters(maskedParams));

        return invocation.proceed();
    }

    private SensitiveSql getSensitiveSqlAnnotation(MappedStatement mappedStatement) {
        // 获取 Mapper 接口方法
        String mapperInterface = mappedStatement.getId();
        String method = mapperInterface.substring(mapperInterface.lastIndexOf('.') + 1);
        String className = mapperInterface.substring(0, mapperInterface.lastIndexOf('.'));

        try {
            Class<?> mapperClass = Class.forName(className);
            for (Method m : mapperClass.getMethods()) {
                if (m.getName().equals(method)) {
                    SensitiveSql annotation = m.getAnnotation(SensitiveSql.class);
                    if (annotation != null) {
                        return annotation;
                    }
                }
            }
        } catch (ClassNotFoundException e) {
//            log.debug("Mapper class not found: {}", className);
        }
        return null;
    }

    private void processParameters(
        MappedStatement mappedStatement,
        BoundSql boundSql,
        Object parameterObject,
        Map<String, Object> maskedParams,
        SensitiveSql sensitiveSql
    ) {
        if (parameterObject == null) {
            return;
        }

        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (parameterMappings == null || parameterMappings.isEmpty()) {
            return;
        }

        int paramIndex = 0;
        for (ParameterMapping mapping : parameterMappings) {
            String propertyName = mapping.getProperty();

            // 跳过内置参数
            if (propertyName.startsWith("_")) {
                continue;
            }

            Object paramValue = getParameterValue(parameterObject, propertyName);

            // 判断是否需要掩码
            boolean needMask = shouldMask(paramIndex, sensitiveSql);

            if (needMask) {
                maskedParams.put(propertyName, maskValue(paramValue));
            } else {
                maskedParams.put(propertyName, paramValue);
            }

            paramIndex++;
        }
    }

    private Object getParameterValue(Object parameterObject, String propertyName) {
        if (parameterObject instanceof Map) {
            return ((Map<?, ?>) parameterObject).get(propertyName);
        }

        // 处理 @Param 注解包装的对象
        if (parameterObject.getClass().getName().contains("ParamMap")) {
            return ((Map<?, ?>) parameterObject).get(propertyName);
        }

        // 处理单个对象
        try {
            Field field = parameterObject.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            return field.get(parameterObject);
        } catch (Exception e) {
            return parameterObject;
        }
    }

    private boolean shouldMask(int paramIndex, SensitiveSql sensitiveSql) {
        int[] indices = sensitiveSql.value();
        if (indices.length == 0) {
            return true; // 未指定索引，全部掩码
        }
        for (int index : indices) {
            if (index == paramIndex) {
                return true;
            }
        }
        return false;
    }

    /**
     * 核心方法：对参数值进行掩码处理
     */
    private Object maskValue(Object value) {
        if (value == null) {
            return null;
        }

        // 处理 Set<String> 类型
        if (value instanceof Set) {
            Set<?> set = (Set<?>) value;
            return set.stream()
                .map(item -> maskString(String.valueOf(item)))
                .collect(Collectors.toSet());
        }

        // 处理 List<String> 类型
        if (value instanceof List) {
            List<?> list = (List<?>) value;
            return list.stream()
                .map(item -> maskString(String.valueOf(item)))
                .collect(Collectors.toList());
        }

        // 处理 String 类型
        if (value instanceof String) {
            return maskString((String) value);
        }

        // 其他类型直接返回
        return value;
    }

    /**
     * 字符串掩码规则
     */
    private String maskString(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }

        int length = value.length();
        if (length <= 2) {
            return "*".repeat(length);
        }

        // 保留前 2 位和后 2 位，中间用 * 替换
        String masked = value.substring(0, 2)
            + "*".repeat(length - 4)
            + value.substring(length - 2);

        return "***MASKED***"; // 或者完全掩码，根据需求调整
    }

    private String formatParameters(Map<String, Object> params) {
        return params.entrySet().stream()
            .map(e -> e.getKey() + ": " + e.getValue())
            .collect(Collectors.joining(", ", "{", "}"));
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 可通过配置自定义掩码规则
    }
}
