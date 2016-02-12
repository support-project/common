package org.support.project.ormapping.tool;

import java.util.Collection;

import org.support.project.ormapping.entity.TableDefinition;
import org.support.project.ormapping.exception.ORMappingException;

public interface DaoClassCreator {

	
	
	/**
	 * Daoのクラスを生成する
	 * 
	 * @param tableDefinitions
	 * @param commonUpdateDateTime 
	 * @param commonUpdateUserName 
	 * @param commonInsertDateTime 
	 * @param commonInsertUserName 
	 * @param commonIgnoreTables 
	 * @param dir
	 * @param packageName
	 * @param suffix
	 * @throws ORMappingException
	 */
	void create(Collection<TableDefinition> tableDefinitions, 
			DaoGenConfig config) throws ORMappingException;

}
