package com.xmall.util;

import com.xmall.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    static{
        //对象的所有字段全部列入
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

        //取消默认转换timestamp形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,false);

        //忽略空Bean转json的错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);

        //所有的日期都统一为以下的样式,即yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));

        //忽略在json字符串中存在,但是在java对象中不存在对应属性的情况，防止错误.
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }


    public static <T> String objToString(T obj){
        if(obj == null){
            return null;
        }
        try{
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        }catch (Exception e){
            log.warn("Parse object to String error",e);
            return null;
        }
    }

    public static <T> String objToStringPretty(T obj){
        if(obj == null){
            return null;
        }
        try{
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        }catch (Exception e){
            log.warn("Parse object to String error",e);
            return null;
        }
    }

    public static <T> T stringToObj(String str,Class<T> clazz){
        if(StringUtils.isEmpty(str) || clazz == null){
            return null;
        }

        try{
            return clazz.equals(String.class)? (T)str : objectMapper.readValue(str,clazz);
        }catch (Exception e){
            log.warn("Parse String to Object error",e);
            return null;
        }
    }

    public static <T> T stringToObj(String str, TypeReference<T> typeReference){
        if(StringUtils.isEmpty(str) || typeReference == null){
            return null;
        }
        try{
            return typeReference.getType().equals(String.class) ? (T)str : objectMapper.readValue(str,typeReference);
        }catch (Exception e){
            log.warn("Parse String to Object error",e);
            return null;
        }
    }

    public static <T> T stringToObj(String str,Class<?> collectionClass,Class<?>... elementClasses){
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass,elementClasses);
        try{
            return objectMapper.readValue(str,javaType);
        }catch (Exception e){
            log.warn("Parse String to Object error",e);
            return null;
        }
    }
    public static void main(String[] args) {
        User user = new User();
        user.setId(1);
        user.setEmail("xieshang728@163.com");


        User user2 = new User();
        user2.setId(2);
        user2.setEmail("xieshang@jd.com");

        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);

        System.out.println(JsonUtil.objToString(user));
        System.out.println(JsonUtil.objToStringPretty(user));

        System.out.println(JsonUtil.stringToObj(JsonUtil.objToString(user),User.class));

        System.out.println("==========================================================");
        System.out.println(JsonUtil.objToStringPretty(users));

        System.out.println("==============================================");
        List<User> userList = JsonUtil.stringToObj(JsonUtil.objToStringPretty(users), new TypeReference<List<User>>() {
        });
        System.out.println(userList);

        System.out.println("==============================================");
        List<User> users2 = JsonUtil.stringToObj(JsonUtil.objToStringPretty(users),List.class,User.class);
        System.out.println(users2);

    }
}
