package org.support.project.ormapping.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Connection {
	public enum ConfigType { XML, Properties };
	
	/** コネクション設定の名前 */
	String name() default ORMappingParameter.DEFAULT_CONNECTION_NAME;
	/** コネクション設定ファイルのタイプ */
	ConfigType configType() default ConfigType.XML;
	/** コネクション設定ファイル名(基本は設定の名前と同じ) */
	String configFileName() default ORMappingParameter.CONNECTION_SETTING;
	
}
