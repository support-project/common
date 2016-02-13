package org.support.project.ormapping.dao;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.support.project.common.log.Log;
import org.support.project.common.log.LogFactory;
import org.support.project.common.logic.H2DBServerLogic;
import org.support.project.ormapping.gen.dao.AutoNoDao;
import org.support.project.ormapping.gen.entity.AutoNoEntity;
import org.support.project.ormapping.tool.dao.InitializeDao;

public class AutoNoDaoTest {
	/** ログ */
	private static Log log = LogFactory.getLog(AutoNoDaoTest.class);

	@BeforeClass
	public static void checkDb() {
		H2DBServerLogic.get().start();
		InitializeDao dao = InitializeDao.get();
		//全テーブル削除
		dao.dropAllTable();
		// Webのデータベース登録
		dao.initializeDatabase("/ddl.sql");
	}
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		H2DBServerLogic.get().stop();
	}

	@Test
	public void testDao() {
		AutoNoDao dao = AutoNoDao.get();
		
		// 自動採番
		AutoNoEntity entity = new AutoNoEntity();
		entity.setStr("hoge");
		//log.debug(entity);
		
		entity = dao.insert(entity);
		log.debug(entity);
		Assert.assertNotNull(entity.getNo());
		
		Long max = entity.getNo();
		
		// 手入力
		entity = new AutoNoEntity();
		entity.setStr("hoge");
		entity.setNo(max + 1);
		
		entity = dao.rawPhysicalInsert(entity);
		log.debug(entity);
		Assert.assertEquals(Long.valueOf(max + 1), entity.getNo());
		
		// 再度自動採番
		
		entity = new AutoNoEntity();
		entity.setStr("hoge");
		
		entity = dao.insert(entity);
		log.debug(entity);
		Assert.assertEquals(Long.valueOf(max + 2), entity.getNo());
		
	}
	
	
	
	
	
}
