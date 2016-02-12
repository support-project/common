package org.support.project.ormapping.tool;

import java.util.Collection;

import org.support.project.ormapping.entity.TableDefinition;
import org.support.project.ormapping.exception.ORMappingException;

public interface EntityClassCreator {
	
	/**
	 * Entityのクラスを生成する
	 * 
	 * @param tableDefinitions
	 * @param dir
	 * @param packageName
	 * @param suffix
	 * @throws ORMappingException
	 */
	void create(Collection<TableDefinition> tableDefinitions, String dir, String packageName, String suffix) throws ORMappingException;
	
}
