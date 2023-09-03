package aswemake.project.base.validate;


import aswemake.project.global.config.AppConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdmValidator {

    public static boolean isAdmin(String username){
        return username.equals(AppConfig.getAdminEmail());
    }
}
