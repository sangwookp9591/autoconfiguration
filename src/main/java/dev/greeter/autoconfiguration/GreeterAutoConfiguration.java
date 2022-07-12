package dev.greeter.autoconfiguration;

import static dev.greeter.GreeterConfigParams.AFTERNOON_MESSAGE;
import static dev.greeter.GreeterConfigParams.EVENING_MESSAGE;
import static dev.greeter.GreeterConfigParams.MORNING_MESSAGE;
import static dev.greeter.GreeterConfigParams.NIGHT_MESSAGE;
import static dev.greeter.GreeterConfigParams.USER_NAME;

import dev.greeter.Greeter;
import dev.greeter.GreetingConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(Greeter.class)
@EnableConfigurationProperties(GreeterProperties.class)
public class GreeterAutoConfiguration {

    @Autowired
    private GreeterProperties greeterProperties; //자동화로 설정된 메타데이터

    /***
     *  @ConditionalOnMissingBean
     *  이 어노테이션은 스프링 부트 프로젝트 상에서 동명의 스프링 빈이 정의되었을 시에는 쓰지 않고
     *  그 스프링 빈을 쓰며 만약 없을 시에는 자동 등록한 빈을 쓰게 끔 유도하는 용도로 쓰입니다.
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public GreetingConfig greetingConfig() {
        //자동설정
        String userName = greeterProperties.getUserName() == null
            ? System.getProperty("user.name")
            : greeterProperties.getUserName();

        String morningMessage = greeterProperties.getMorningMessage() == null
            ? System.getProperty("morning.message")
            : greeterProperties.getMorningMessage();

        String afternoonMessage = greeterProperties.getAfternoonMessage() == null
            ? System.getProperty("after.message")
            : greeterProperties.getAfternoonMessage();

        String eveningMessage = greeterProperties.getEveningMessage() == null
            ? System.getProperty("evening.message")
            : greeterProperties.getEveningMessage();

        String nightMessage = greeterProperties.getNightMessage() == null
            ? System.getProperty("night.message")
            : greeterProperties.getNightMessage();

        //greeting confing에 자동 의존성 설정.
        GreetingConfig greetingConfig = new GreetingConfig();
        greetingConfig.put(USER_NAME, userName);
        greetingConfig.put(MORNING_MESSAGE, morningMessage);
        greetingConfig.put(AFTERNOON_MESSAGE, afternoonMessage);
        greetingConfig.put(EVENING_MESSAGE, eveningMessage);
        greetingConfig.put(NIGHT_MESSAGE, nightMessage);

        return greetingConfig;
    }

    @Bean
    @ConditionalOnMissingBean
    public Greeter greeter(GreetingConfig greetingConfig) {
        return new Greeter(greetingConfig);
    }
}
