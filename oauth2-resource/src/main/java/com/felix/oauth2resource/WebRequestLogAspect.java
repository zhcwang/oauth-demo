package com.felix.oauth2resource;

import com.felix.oauth2resource.utils.ClientIpUtils;
import com.felix.oauth2resource.utils.JsonUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class WebRequestLogAspect {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(public * com.felix.oauth2resource.controller.*.*(..))")
    public void wsLog() {
    }

    @Before("wsLog()")
    public void doBefore(JoinPoint joinPoint) {
        if (log.isInfoEnabled()) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                // 记录下请求内容
                Map<String, String[]> parameters = request.getParameterMap();

                try {
                    String parametersString = null;
                    String requestBody = null;
                    if (parameters != null) {
                        parametersString = JsonUtil.multiValueMapToJsonString(parameters);
                    }
                    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
                    //获取被拦截的方法
                    Method method = signature.getMethod();
                    Object object = getAnnotatedParameterValueRequestBody(method, joinPoint.getArgs());
                    if (object != null) {
                        requestBody = JsonUtil.objectToJsonString(object);
                    }
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("\n");

                    Principal principal = request.getUserPrincipal();
                    if (principal != null) {
                        stringBuffer.append("Request user ");
                        stringBuffer.append(principal.getName());
                        stringBuffer.append(";\n");
                    }
                    stringBuffer.append("Request from ");
                    stringBuffer.append(ClientIpUtils.getIpAddress(request));
                    stringBuffer.append(";\n");
                    stringBuffer.append("User-Agent = ");
                    stringBuffer.append(request.getHeader("User-Agent"));
                    stringBuffer.append(";\n");
                    stringBuffer.append("uri = ");
                    stringBuffer.append(request.getRequestURL().toString());
                    stringBuffer.append(";\n");
                    stringBuffer.append("request method = ");
                    stringBuffer.append(request.getMethod());
                    stringBuffer.append(";\n");
                    stringBuffer.append("request parameters = ");
                    stringBuffer.append(parametersString);
                    stringBuffer.append(";\n");
                    stringBuffer.append("request body = ");
                    stringBuffer.append(requestBody);
                    stringBuffer.append(";\n");

                    log.info(stringBuffer.toString());

                } catch (Exception e) {
                    log.error("log http request Exception: ", e);
                }
            }
        }

    }

    @AfterReturning(returning = "ret", pointcut = "wsLog()")
    public void doAfterReturning(Object ret) {
        // 处理完请求，返回内容
        if ((ret instanceof ResponseEntity)) {
            if (log.isInfoEnabled()) {
                try {
                    log.info("Response from server : " + ((ResponseEntity) ret).getStatusCode().value());
                } catch (Exception e) {
                    log.info("log http response Exception", e);
                }
            }
        } else {
            if (log.isInfoEnabled()) {
                try {
                    log.info("Response from server : \n" + JsonUtil.objectToJsonString(ret));
                } catch (Exception e) {
                    log.error("log http response Exception: ", e);
                }
            }
        }
    }

    private Object getAnnotatedParameterValueRequestBody(Method method, Object[] args) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
///        Parameter[] parameters = method.getParameters();

        int i = 0;
        for (Annotation[] annotations : parameterAnnotations) {
///            String name = parameters[i].getDeclaringExecutable().getName();
            for (Annotation annotation : annotations) {
                if (annotation instanceof RequestBody) {
                    return args[i];
                }
            }
            i++;
        }
        return null;
    }

    /**
     * get request headers
     *
     * @param request
     * @return
     */
    private Map<String, String> getHeadersInfo(HttpServletRequest request) {

        Map<String, String> map = new HashMap<>(16);

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }

}
