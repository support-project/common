package org.support.project.ormapping.config;

import java.io.InputStream;

import org.support.project.di.DI;
import org.support.project.ormapping.config.impl.ConnectionConfigPropertiesLoader;
import org.support.project.ormapping.config.impl.ConnectionConfigXmlLoader;
import org.support.project.ormapping.exception.ORMappingException;

@DI(
keys={
		"XML",
		"Properties"
},
impls={
		ConnectionConfigXmlLoader.class,
		ConnectionConfigPropertiesLoader.class
})
public interface ConnectionConfigLoader {
	/**
	 * 設定ファイルからコネクションの設定を読み出す
	 * @param path
	 * @return
	 * @throws ORMappingException
	 */
	ConnectionConfig load(String path) throws ORMappingException;
	
	/**
	 * 設定ファイルからコネクションの設定を読み出す
	 * @param in
	 * @return
	 * @throws ORMappingException
	 */
	ConnectionConfig load(InputStream in) throws ORMappingException;
	
		
}
